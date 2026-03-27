# Homework 1

This project is configured to work offline using local libraries in the `lib/` directory. All code resides in the default package.

## 📂 Project Structure

```
hwa1/
├── src/main/java/       # Application source code
│   └── ColorSort.java      # Main class with lab exercises
├── src/test/java/       # Test code
│   ├── ColorSortTest.java  # JUnit tests
│   └── Aout.java        # Test helper utilities
├── lib/                 # Local libraries
│   ├── junit-4.13.2.jar
│   └── hamcrest-core-1.3.jar
├── bin/                 # Compiled class files
└──
```

## 🛠️ Command Line Instructions

Since the project uses the default package and local JAR files, commands differ by operating system (path separator: Windows `;` vs Linux/Mac `:`).

### Windows (Command Prompt / PowerShell)

```bash
# 1. Compile main code
javac -d bin src/main/java/*.java

# 2. Compile tests (requires lib folder and main code in bin)
javac -d bin -cp "lib/*;bin" src/test/java/*.java

# 3. Run application
java -cp bin ColorSort

# 4. Run JUnit tests
java -cp "bin;lib/*" org.junit.runner.JUnitCore ColorSortTest
```

### Linux and macOS

```bash
# 1. Compile main code
javac -d bin src/main/java/*.java

# 2. Compile tests (requires lib folder and main code in bin)
javac -d bin -cp "lib/*:bin" src/test/java/*.java

# 3. Run application
java -cp bin ColorSort

# 4. Run JUnit tests
java -cp "bin:lib/*" org.junit.runner.JUnitCore ColorSortTest
```

---


## 📋 Task Description

An array contains red, green and blue balls in random order. Write a possibly fast method to rearrange the array, so that all red balls are at the beginning and all blue balls are at the end of the array (green balls in the middle).

---

## ⚙️ Requirements

- **Java 8** or higher
