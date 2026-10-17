// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ErroneousTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreeScanner;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.io.vision.VisionIOLimelight;
import frc.robot.observation.vision.VisionMeasurementQuality;
import frc.robot.observation.vision.VisionMeasurementQuality.Acceptance;
import frc.robot.observation.vision.VisionMeasurementQuality.RejectionReason;
import frc.robot.observation.vision.VisionMeasurementQuality.UncertaintyClass;
import frc.robot.observation.vision.VisionMeasurementQualityEvaluator;
import frc.robot.observation.vision.VisionMeasurementQualityEvaluator.Policy;
import frc.robot.observation.vision.VisionObservation.TargetObservation;
import frc.robot.observation.vision.VisionTiming;
import frc.robot.observation.vision.VisionTimingEvaluator;
import frc.robot.observation.vision.VisionTimingEvaluator.Freshness;
import frc.robot.subsystems.VisionSubsystem;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** Guards P3-H01 and CFG-H01 configuration ownership and preserved vision semantics. */
class VisionConfigurationAuthorityTest {
  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @Test
  void constantsOwnTheFourLockedFiniteOrderedDefaults() throws ReflectiveOperationException {
    double low = declaredDefault("kLowUncertaintyMaxDistanceMeters", 1.0);
    double medium = declaredDefault("kMediumUncertaintyMaxDistanceMeters", 2.0);
    double maximum = declaredDefault("kMaximumAcceptedDistanceMeters", 3.0);
    declaredDefault("kMaximumFreshAgeSeconds", 0.250);

    assertTrue(low <= medium);
    assertTrue(medium <= maximum);
  }

  @Test
  void productionConstructorUsesNamedDefaultsInTheExactArgumentPositions() throws IOException {
    CompilationUnitTree source = parseSource("frc/robot/RobotContainer.java");
    ClassTree robotContainer =
        source.getTypeDecls().stream()
            .filter(ClassTree.class::isInstance)
            .map(ClassTree.class::cast)
            .filter(type -> type.getSimpleName().contentEquals("RobotContainer"))
            .findFirst()
            .orElseThrow();
    List<MethodTree> constructors =
        robotContainer.getMembers().stream()
            .filter(MethodTree.class::isInstance)
            .map(MethodTree.class::cast)
            .filter(method -> method.getReturnType() == null)
            .toList();
    assertEquals(1, constructors.size());
    MethodTree constructor = constructors.get(0);
    assertTrue(constructor.getParameters().isEmpty());
    assertNotNull(constructor.getBody());

    // Inspect executable AST nodes; comments, text blocks, and strings cannot supply references.
    List<NewClassTree> visionCalls = new ArrayList<>();
    new TreeScanner<Void, Void>() {
      @Override
      public Void visitNewClass(NewClassTree node, Void unused) {
        if (node.getIdentifier().toString().equals("VisionSubsystem")) {
          visionCalls.add(node);
        }
        return super.visitNewClass(node, unused);
      }
    }.scan(source, null);
    assertEquals(1, visionCalls.size());

    List<AssignmentTree> assignments =
        constructor.getBody().getStatements().stream()
            .filter(ExpressionStatementTree.class::isInstance)
            .map(ExpressionStatementTree.class::cast)
            .map(ExpressionStatementTree::getExpression)
            .filter(AssignmentTree.class::isInstance)
            .map(AssignmentTree.class::cast)
            .filter(assignment -> assignment.getVariable().toString().equals("visionSubsystem"))
            .toList();
    assertEquals(1, assignments.size());
    NewClassTree visionCall =
        assertInstanceOf(NewClassTree.class, assignments.get(0).getExpression());
    assertEquals(visionCalls.get(0), visionCall);
    assertEquals("VisionSubsystem", visionCall.getIdentifier().toString());
    assertEquals(6, visionCall.getArguments().size());
    assertEquals("visionIO", visionCall.getArguments().get(0).toString());
    assertEquals("fieldLayout", visionCall.getArguments().get(1).toString());
    assertEquals(
        "Constants.VisionConstants.kRobotToCamera",
        visionCall.getArguments().get(2).toString());

    NewClassTree policyCall =
        assertInstanceOf(NewClassTree.class, visionCall.getArguments().get(3));
    assertEquals("Policy", policyCall.getIdentifier().toString());
    assertEquals(
        List.of(
            "Constants.VisionConstants.kLowUncertaintyMaxDistanceMeters",
            "Constants.VisionConstants.kMediumUncertaintyMaxDistanceMeters",
            "Constants.VisionConstants.kMaximumAcceptedDistanceMeters"),
        policyCall.getArguments().stream().map(Object::toString).toList());
    assertEquals(
        "Constants.VisionConstants.kMaximumFreshAgeSeconds",
        visionCall.getArguments().get(4).toString());
    assertEquals("Timer::getFPGATimestamp", visionCall.getArguments().get(5).toString());
    assertTrue(
        source.getImports().stream()
            .anyMatch(
                declaration ->
                    declaration.getQualifiedIdentifier().toString().equals(
                        "frc.robot.observation.vision.VisionMeasurementQualityEvaluator.Policy")));
  }

  @Test
  void actualRobotContainerInjectsTheRegisteredPolicyAndFreshness()
      throws ReflectiveOperationException {
    Configuration configuration = injectedConfiguration();

    assertEquals(1.0, configuration.policy().lowMaxMeters());
    assertEquals(2.0, configuration.policy().mediumMaxMeters());
    assertEquals(3.0, configuration.policy().maximumAcceptedMeters());
    assertEquals(0.250, configuration.maximumFreshAgeSeconds());
    assertEquals(
        Constants.VisionConstants.kLowUncertaintyMaxDistanceMeters,
        configuration.policy().lowMaxMeters());
    assertEquals(
        Constants.VisionConstants.kMediumUncertaintyMaxDistanceMeters,
        configuration.policy().mediumMaxMeters());
    assertEquals(
        Constants.VisionConstants.kMaximumAcceptedDistanceMeters,
        configuration.policy().maximumAcceptedMeters());
    assertEquals(
        Constants.VisionConstants.kMaximumFreshAgeSeconds,
        configuration.maximumFreshAgeSeconds());
  }

  @Test
  void injectedDistancePolicyRetainsEveryInclusiveBoundary()
      throws ReflectiveOperationException {
    Policy policy = injectedConfiguration().policy();

    assertQuality(Math.nextDown(1.0), policy, UncertaintyClass.LOW);
    assertQuality(1.0, policy, UncertaintyClass.LOW);
    assertQuality(Math.nextUp(1.0), policy, UncertaintyClass.MEDIUM);
    assertQuality(Math.nextDown(2.0), policy, UncertaintyClass.MEDIUM);
    assertQuality(2.0, policy, UncertaintyClass.MEDIUM);
    assertQuality(Math.nextUp(2.0), policy, UncertaintyClass.HIGH);
    assertQuality(Math.nextDown(3.0), policy, UncertaintyClass.HIGH);
    assertQuality(3.0, policy, UncertaintyClass.HIGH);
    assertQuality(Math.nextUp(3.0), policy, UncertaintyClass.UNUSABLE);
  }

  @Test
  void injectedFreshnessRetainsTheInclusiveLimitWithoutClockReads()
      throws ReflectiveOperationException {
    double maximumFreshAgeSeconds = injectedConfiguration().maximumFreshAgeSeconds();
    // A zero measurement timestamp avoids cancellation when testing adjacent doubles.
    VisionTiming timing = new VisionTiming(0.0, 0.0);

    assertEquals(
        Freshness.FRESH,
        VisionTimingEvaluator.classifyFreshness(
            timing, Math.nextDown(0.250), maximumFreshAgeSeconds));
    assertEquals(
        Freshness.FRESH,
        VisionTimingEvaluator.classifyFreshness(timing, 0.250, maximumFreshAgeSeconds));
    assertEquals(
        Freshness.STALE,
        VisionTimingEvaluator.classifyFreshness(
            timing, Math.nextUp(0.250), maximumFreshAgeSeconds));
  }

  @Test
  void evaluatorsHonorExplicitPoliciesRatherThanHiddenDefaults() {
    TargetObservation target = targetAt(1.0);
    Policy alternatePolicy = new Policy(0.250, 0.500, 0.750);
    VisionMeasurementQuality expected =
        new VisionMeasurementQuality(
            Acceptance.REJECTED, UncertaintyClass.UNUSABLE, RejectionReason.TARGET_TOO_FAR);

    assertEquals(expected, VisionMeasurementQualityEvaluator.evaluate(target, alternatePolicy));
    assertEquals(expected, VisionMeasurementQualityEvaluator.evaluate(target, alternatePolicy));
    VisionTiming timing = new VisionTiming(0.0, 0.0);
    assertEquals(
        Freshness.FRESH, VisionTimingEvaluator.classifyFreshness(timing, 0.375, 0.500));
    assertEquals(
        Freshness.STALE, VisionTimingEvaluator.classifyFreshness(timing, 0.375, 0.250));
  }

  @Test
  void evaluatorSourcesDoNotReadCompositionOrRuntimeConfiguration() throws IOException {
    Set<String> forbidden =
        Set.of(
            "Constants", "RobotContainer", "Timer", "DriverStation", "CommandScheduler",
            "NetworkTableInstance", "System");
    for (String relativePath :
        List.of(
            "frc/robot/observation/vision/VisionMeasurementQualityEvaluator.java",
            "frc/robot/observation/vision/VisionTimingEvaluator.java")) {
      CompilationUnitTree source = parseSource(relativePath);
      List<String> dependencies = new ArrayList<>();
      new TreeScanner<Void, Void>() {
        @Override
        public Void visitIdentifier(IdentifierTree node, Void unused) {
          if (forbidden.contains(node.getName().toString())) {
            dependencies.add(node.getName().toString());
          }
          return super.visitIdentifier(node, unused);
        }

        @Override
        public Void visitMemberSelect(MemberSelectTree node, Void unused) {
          if (forbidden.contains(node.getIdentifier().toString())) {
            dependencies.add(node.getIdentifier().toString());
          }
          return super.visitMemberSelect(node, unused);
        }
      }.scan(source, null);
      assertTrue(dependencies.isEmpty(), relativePath + ": " + dependencies);
    }
  }

  @Test
  void constantsOwnTheLockedLimelightTableName() throws ReflectiveOperationException {
    Field declaration = Constants.VisionConstants.class.getDeclaredField("kLimelightTableName");
    assertEquals(String.class, declaration.getType());
    assertTrue(Modifier.isPublic(declaration.getModifiers()));
    assertTrue(Modifier.isStatic(declaration.getModifiers()));
    assertTrue(Modifier.isFinal(declaration.getModifiers()));
    assertEquals("limelight", declaration.get(null));
  }

  @Test
  void defaultLimelightConstructorConsumesTheNamedTableConfiguration() throws IOException {
    CompilationUnitTree source = parseSource("frc/robot/io/vision/VisionIOLimelight.java");
    ClassTree adapter =
        source.getTypeDecls().stream()
            .filter(ClassTree.class::isInstance)
            .map(ClassTree.class::cast)
            .filter(type -> type.getSimpleName().contentEquals("VisionIOLimelight"))
            .findFirst()
            .orElseThrow();
    List<VariableTree> fields =
        adapter.getMembers().stream()
            .filter(VariableTree.class::isInstance)
            .map(VariableTree.class::cast)
            .toList();
    assertFalse(
        fields.stream()
            .anyMatch(declaration -> declaration.getName().contentEquals("kLimelightTableName")));
    assertFalse(
        fields.stream()
            .anyMatch(
                declaration ->
                    declaration.getModifiers().getFlags()
                            .contains(javax.lang.model.element.Modifier.PRIVATE)
                        && declaration.getInitializer() instanceof LiteralTree literal
                        && "limelight".equals(literal.getValue())));

    List<MethodTree> defaultConstructors =
        adapter.getMembers().stream()
            .filter(MethodTree.class::isInstance)
            .map(MethodTree.class::cast)
            .filter(method -> method.getReturnType() == null && method.getParameters().isEmpty())
            .toList();
    assertEquals(1, defaultConstructors.size());
    MethodTree constructor = defaultConstructors.get(0);
    assertTrue(
        constructor.getModifiers().getFlags().contains(javax.lang.model.element.Modifier.PUBLIC));
    assertNotNull(constructor.getBody());
    assertEquals(1, constructor.getBody().getStatements().size());

    ExpressionStatementTree delegation =
        assertInstanceOf(
            ExpressionStatementTree.class, constructor.getBody().getStatements().get(0));
    MethodInvocationTree constructorCall =
        assertInstanceOf(MethodInvocationTree.class, delegation.getExpression());
    IdentifierTree constructorName =
        assertInstanceOf(IdentifierTree.class, constructorCall.getMethodSelect());
    assertEquals("this", constructorName.getName().toString());
    assertEquals(1, constructorCall.getArguments().size());

    MethodInvocationTree tableCall =
        assertInstanceOf(MethodInvocationTree.class, constructorCall.getArguments().get(0));
    MemberSelectTree tableSelection =
        assertInstanceOf(MemberSelectTree.class, tableCall.getMethodSelect());
    assertEquals("getTable", tableSelection.getIdentifier().toString());
    MethodInvocationTree instanceCall =
        assertInstanceOf(MethodInvocationTree.class, tableSelection.getExpression());
    assertEquals("NetworkTableInstance.getDefault", instanceCall.getMethodSelect().toString());
    assertTrue(instanceCall.getArguments().isEmpty());
    assertEquals(1, tableCall.getArguments().size());
    MemberSelectTree configuredName =
        assertInstanceOf(MemberSelectTree.class, tableCall.getArguments().get(0));
    assertEquals("Constants.VisionConstants.kLimelightTableName", configuredName.toString());
  }

  @Test
  void publicDefaultLimelightConstructionUsesTheLockedJsonEndpoint()
      throws ReflectiveOperationException {
    VisionIOLimelight adapter = new VisionIOLimelight();
    NetworkTableEntry entry =
        assertInstanceOf(
            NetworkTableEntry.class, field(VisionIOLimelight.class, "jsonEntry").get(adapter));
    assertEquals("/limelight/json", entry.getName());
  }

  private static double declaredDefault(String name, double expected)
      throws ReflectiveOperationException {
    Field field = Constants.VisionConstants.class.getDeclaredField(name);
    assertEquals(double.class, field.getType());
    assertTrue(Modifier.isPublic(field.getModifiers()));
    assertTrue(Modifier.isStatic(field.getModifiers()));
    assertTrue(Modifier.isFinal(field.getModifiers()));
    double value = field.getDouble(null);
    assertEquals(expected, value);
    assertTrue(Double.isFinite(value));
    assertTrue(value >= 0.0);
    return value;
  }

  private static Configuration injectedConfiguration() throws ReflectiveOperationException {
    clearRuntimeRegistration();
    try {
      RobotContainer container = new RobotContainer();
      VisionSubsystem subsystem =
          (VisionSubsystem) field(RobotContainer.class, "visionSubsystem").get(container);
      Policy policy = (Policy) field(VisionSubsystem.class, "qualityPolicy").get(subsystem);
      double maximumFreshAgeSeconds =
          field(VisionSubsystem.class, "maximumFreshAgeSeconds").getDouble(subsystem);
      assertNotNull(policy);
      return new Configuration(policy, maximumFreshAgeSeconds);
    } finally {
      clearRuntimeRegistration();
    }
  }

  private static void clearRuntimeRegistration() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.cancelAll();
    scheduler.unregisterAllSubsystems();
    scheduler.getDefaultButtonLoop().clear();
    NamedCommands.clearAll();
    AutoBuilder.resetForTesting();
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(false);
    DriverStationSim.notifyNewData();
  }

  private static Field field(Class<?> owner, String name) throws NoSuchFieldException {
    Field field = owner.getDeclaredField(name);
    field.setAccessible(true);
    return field;
  }

  private static TargetObservation targetAt(double distanceMeters) {
    return new TargetObservation(
        7, new Transform3d(distanceMeters, 0.0, 0.0, new Rotation3d()));
  }

  private static void assertQuality(
      double distanceMeters, Policy policy, UncertaintyClass expectedClass) {
    boolean accepted = expectedClass != UncertaintyClass.UNUSABLE;
    VisionMeasurementQuality expected =
        new VisionMeasurementQuality(
            accepted ? Acceptance.ACCEPTED : Acceptance.REJECTED,
            expectedClass,
            accepted ? RejectionReason.NONE : RejectionReason.TARGET_TOO_FAR);
    assertEquals(
        expected,
        VisionMeasurementQualityEvaluator.evaluate(targetAt(distanceMeters), policy),
        "distanceMeters=" + distanceMeters);
  }

  /** Uses the existing JDK parser convention without analysis, compilation, or class output. */
  private static CompilationUnitTree parseSource(String relativePath) throws IOException {
    Path sourceFile = Path.of("src", "main", "java").resolve(relativePath);
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    assertNotNull(compiler, "A JDK is required for the architecture source guard");
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    try (StandardJavaFileManager fileManager =
        compiler.getStandardFileManager(diagnostics, Locale.ROOT, StandardCharsets.UTF_8)) {
      JavaCompiler.CompilationTask task =
          compiler.getTask(
              null, fileManager, diagnostics, List.of("-proc:none"), null,
              fileManager.getJavaFileObjects(sourceFile.toFile()));
      JavacTask parser = assertInstanceOf(JavacTask.class, task);
      List<CompilationUnitTree> units = new ArrayList<>();
      for (CompilationUnitTree unit : parser.parse()) {
        units.add(unit);
      }
      assertFalse(
          diagnostics.getDiagnostics().stream()
              .anyMatch(diagnostic -> diagnostic.getKind() == Diagnostic.Kind.ERROR),
          () -> "Java parse failure: " + sourceFile + ": " + diagnostics.getDiagnostics());
      assertEquals(1, units.size());
      new TreeScanner<Void, Void>() {
        @Override
        public Void visitErroneous(ErroneousTree node, Void unused) {
          throw new IllegalStateException("Erroneous Java source tree: " + sourceFile);
        }
      }.scan(units.get(0), null);
      return units.get(0);
    }
  }

  private record Configuration(Policy policy, double maximumFreshAgeSeconds) {}
}
