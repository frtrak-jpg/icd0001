# Homework 2

This project is configured to work offline using local libraries in the `lib/` directory. All code resides in the default package.

## 📂 Project Structure

```
hwa2/
├── src/main/java/       # Application source code
│   └── DoubleSorting.java      # Main class with lab exercises
├── src/test/java/       # Test code
│   ├── DoubleSortingTest.java  # JUnit tests
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
java -cp bin DoubleSorting

# 4. Run JUnit tests
java -cp "bin;lib/*" org.junit.runner.JUnitCore DoubleSortingTest
```

### Linux and macOS

```bash
# 1. Compile main code
javac -d bin src/main/java/*.java

# 2. Compile tests (requires lib folder and main code in bin)
javac -d bin -cp "lib/*:bin" src/test/java/*.java

# 3. Run application
java -cp bin DoubleSorting

# 4. Run JUnit tests
java -cp "bin:lib/*" org.junit.runner.JUnitCore DoubleSortingTest
```

---


## 📋 Task Description

Write a Java program to sort an array of double numbers using binary insertion sort method. Binary insertion method is a modified insertion method that uses binary search to find the insertion point.

---

## ⚙️ Requirements

- **Java 8** or higher
