# Homework 7

This project is configured to work offline using local libraries in the `lib/` directory. All code resides in the default package.

## 📂 Project Structure

```
hwa7/
├── src/main/java/       # Application source code
│   └── Puzzle.java      # Main class with lab exercises
├── src/test/java/       # Test code
│   ├── PuzzleTest.java  # JUnit tests
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
java -cp bin Puzzle

# 4. Run JUnit tests
java -cp "bin;lib/*" org.junit.runner.JUnitCore PuzzleTest
```

### Linux and macOS

```bash
# 1. Compile main code
javac -d bin src/main/java/*.java

# 2. Compile tests (requires lib folder and main code in bin)
javac -d bin -cp "lib/*:bin" src/test/java/*.java

# 3. Run application
java -cp bin Puzzle

# 4. Run JUnit tests
java -cp "bin:lib/*" org.junit.runner.JUnitCore PuzzleTest
```

---


## 📋 Task Description

Write a Java program to solve word puzzles where you have to guess which digit corresponds to which letter to make a given equality valid. Each letter must correspond to a different decimal digit, and leading zeros are not allowed in the numbers.

For example, the puzzle SEND+MORE=MONEY has exactly one solution: S=9, E=5, N=6, D=7, M=1, O=0, R=8, Y=2, giving 9567+1085=10652.

Input data from command line (parameter of the main method) contains three words separated by single spaces: first addend, second addend and sum. Words consist of uppercase letters of the Latin alphabet; no word is longer than 18 letters. Output has to contain the initial text of the puzzle, at least one solution (if there are any solutions) and total number of solutions. 


---

## ⚙️ Requirements

- **Java 8** or higher
