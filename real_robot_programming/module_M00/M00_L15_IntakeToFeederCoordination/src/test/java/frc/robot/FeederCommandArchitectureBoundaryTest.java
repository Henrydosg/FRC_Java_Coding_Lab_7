// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ErroneousTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreeScanner;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.RunFeederCommand;
import frc.robot.subsystems.FeederSubsystem;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.junit.jupiter.api.Test;

/** Locks scheduler ownership and dependency boundaries for the M00_L06 Feeder command. */
class FeederCommandArchitectureBoundaryTest {
  @Test
  void commandExistsInCommandsAndOwnsOnlyOneInjectedFeederSubsystem() {
    assertEquals("frc.robot.commands", RunFeederCommand.class.getPackageName());
    assertEquals(Command.class, RunFeederCommand.class.getSuperclass());

    List<Field> semanticFields =
        Arrays.stream(RunFeederCommand.class.getDeclaredFields())
            .filter(field -> !field.isSynthetic())
            .filter(field -> !Modifier.isStatic(field.getModifiers()))
            .toList();
    assertEquals(1, semanticFields.size());
    assertEquals(FeederSubsystem.class, semanticFields.get(0).getType());

    Constructor<?>[] constructors = RunFeederCommand.class.getDeclaredConstructors();
    assertEquals(1, constructors.length);
    assertEquals(List.of(FeederSubsystem.class), List.of(constructors[0].getParameterTypes()));
    assertThrows(
        NoSuchMethodException.class,
        () -> RunFeederCommand.class.getDeclaredMethod("runsWhenDisabled"));
  }

  @Test
  void commandImportsOnlySchedulerSubsystemAndLanguageDependencies() throws IOException {
    Set<String> imports = imports(source("frc/robot/commands/RunFeederCommand.java"));

    assertTrue(imports.contains("edu.wpi.first.wpilibj2.command.Command"));
    assertTrue(imports.contains("frc.robot.subsystems.FeederSubsystem"));
    assertTrue(imports.contains("java.util.Objects"));
    assertFalse(imports.stream().anyMatch(name -> name.startsWith("frc.robot.io")));
    assertFalse(imports.stream().anyMatch(name -> name.startsWith("frc.robot.telemetry")));
    assertFalse(imports.stream().anyMatch(name -> name.startsWith("edu.wpi.first.networktables")));
    assertFalse(imports.stream().anyMatch(name -> name.startsWith("com.ctre")));
    assertFalse(imports.stream().anyMatch(name -> name.startsWith("com.revrobotics")));
    assertFalse(imports.stream().anyMatch(name -> name.equals("frc.robot.Constants")));
  }

  @Test
  void commandHasNoCoordinationOrAutonomousDependencies() throws IOException {
    Set<String> imports = imports(source("frc/robot/commands/RunFeederCommand.java"));

    assertFalse(
        imports.stream()
            .map(name -> name.toLowerCase(Locale.ROOT))
            .anyMatch(
                name ->
                    name.contains("intake")
                        || name.contains("flywheel")
                        || name.contains("shooter")
                        || name.contains("autonomous")));
    assertFalse(imports.stream().anyMatch(name -> name.contains("NamedCommands")));
  }

  @Test
  void feederIoScopeRemainsNoopOnlyWithoutSimulationOrRealAdapter() throws IOException {
    Path feederIoDirectory = Path.of("src", "main", "java", "frc", "robot", "io", "feeder");
    try (Stream<Path> feederFiles = Files.list(feederIoDirectory)) {
      assertEquals(
          Set.of("FeederIO.java", "FeederIONoop.java"),
          feederFiles.map(path -> path.getFileName().toString()).collect(Collectors.toSet()));
    }

    assertFalse(Files.exists(feederIoDirectory.resolve("FeederIOSim.java")));
    assertFalse(Files.exists(feederIoDirectory.resolve("FeederIOCTRE.java")));
    assertFalse(Files.exists(feederIoDirectory.resolve("FeederIOReal.java")));
  }

  @Test
  void feederCommandScopeContainsOnlyAuthorizedCommands() throws IOException {
    Path commands = Path.of("src", "main", "java", "frc", "robot", "commands");
    List<Path> commandJavaPaths;
    try (Stream<Path> commandFiles = Files.walk(commands)) {
      commandJavaPaths =
          commandFiles
              .filter(Files::isRegularFile)
              .filter(path -> path.getFileName().toString().endsWith(".java"))
              .toList();
    }

    Set<String> feederNamedCommandFiles =
        commandJavaPaths.stream()
            .filter(path -> path.getFileName().toString().contains("Feeder"))
            .map(path -> normalizedRelativePath(commands, path))
            .collect(Collectors.toSet());
    assertEquals(
        Set.of("RunFeederCommand.java", "IntakeToFeederCommand.java"),
        feederNamedCommandFiles);

    Set<String> feederSubsystemCommandFiles = new HashSet<>();
    for (Path commandPath : commandJavaPaths) {
      if (referencesJavaIdentifier(commandPath, "FeederSubsystem")) {
        feederSubsystemCommandFiles.add(normalizedRelativePath(commands, commandPath));
      }
    }
    assertEquals(
        Set.of("RunFeederCommand.java", "IntakeToFeederCommand.java", "ShootCommand.java"),
        feederSubsystemCommandFiles);
  }

  private static String normalizedRelativePath(Path root, Path path) {
    return root.relativize(path).toString().replace('\\', '/');
  }

  private static boolean referencesJavaIdentifier(Path sourceFile, String identifier)
      throws IOException {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    if (compiler == null) {
      throw new IllegalStateException(
          "System Java compiler is unavailable while parsing " + sourceFile);
    }

    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    try (StandardJavaFileManager fileManager =
        compiler.getStandardFileManager(diagnostics, Locale.ROOT, StandardCharsets.UTF_8)) {
      Iterable<? extends JavaFileObject> sourceFiles =
          fileManager.getJavaFileObjects(sourceFile.toFile());
      JavaCompiler.CompilationTask compilerTask =
          compiler.getTask(
              null, fileManager, diagnostics, List.of("-proc:none"), null, sourceFiles);
      if (!(compilerTask instanceof JavacTask javacTask)) {
        throw new IllegalStateException(
            "System Java compiler does not expose JavacTask while parsing " + sourceFile);
      }

      List<CompilationUnitTree> compilationUnits = new ArrayList<>();
      try {
        for (CompilationUnitTree compilationUnit : javacTask.parse()) {
          compilationUnits.add(compilationUnit);
        }
      } catch (IOException parseFailure) {
        throw new IOException(
            "Unable to parse Java source for Feeder ownership: " + sourceFile, parseFailure);
      }

      Diagnostic<? extends JavaFileObject> parseError =
          diagnostics.getDiagnostics().stream()
              .filter(diagnostic -> diagnostic.getKind() == Diagnostic.Kind.ERROR)
              .findFirst()
              .orElse(null);
      if (parseError != null) {
        throw new IllegalStateException(
            "Unable to parse Java source for Feeder ownership at "
                + sourceFile
                + ":"
                + parseError.getLineNumber()
                + ": "
                + parseError.getMessage(Locale.ROOT));
      }

      boolean[] found = {false};
      TreeScanner<Void, Void> identifierScanner =
          new TreeScanner<>() {
            @Override
            public Void visitIdentifier(IdentifierTree node, Void unused) {
              if (identifier.equals(node.getName().toString())) {
                found[0] = true;
              }
              return super.visitIdentifier(node, unused);
            }

            @Override
            public Void visitMemberSelect(MemberSelectTree node, Void unused) {
              if (identifier.equals(node.getIdentifier().toString())) {
                found[0] = true;
              }
              return super.visitMemberSelect(node, unused);
            }

            @Override
            public Void visitErroneous(ErroneousTree node, Void unused) {
              throw new IllegalStateException(
                  "Java parser produced an erroneous syntax tree for Feeder ownership: "
                      + sourceFile);
            }
          };
      for (CompilationUnitTree compilationUnit : compilationUnits) {
        identifierScanner.scan(compilationUnit, null);
      }
      return found[0];
    }
  }

  private static Set<String> imports(String source) {
    return source
        .lines()
        .map(String::trim)
        .filter(line -> line.startsWith("import "))
        .map(line -> line.substring("import ".length(), line.length() - 1))
        .collect(Collectors.toSet());
  }

  private static String source(String relativePath) throws IOException {
    return Files.readString(Path.of("src", "main", "java").resolve(relativePath));
  }
}
