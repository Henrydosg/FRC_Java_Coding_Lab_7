# Copy Instructions

Copy the extracted files into:

```text
C:\Users\xps7350i7\Desktop\FRC_Java_Coding_Lab_7
```

Allow matching files to be replaced.

Then run:

```powershell
cd C:\Users\xps7350i7\Desktop\FRC_Java_Coding_Lab_7
git status --short
git diff --check
git diff --name-only | Select-String "\.java$"
```

Expected:

- governance and Markdown files only;
- no Java result from the final command;
- LF/CRLF warnings are acceptable;
- no whitespace error from `git diff --check`.
