# Homework 5

This project is configured to work offline using local libraries in the `lib/` directory. All code resides in the default package.

## 📂 Project Structure

```
hwa5/
├── src/main/java/       # Application source code
│   └── Tnode.java      # Main class with lab exercises
├── src/test/java/       # Test code
│   ├── TnodeTest.java  # JUnit tests
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
java -cp bin Tnode

# 4. Run JUnit tests
java -cp "bin;lib/*" org.junit.runner.JUnitCore TnodeTest
```

### Linux and macOS

```bash
# 1. Compile main code
javac -d bin src/main/java/*.java

# 2. Compile tests (requires lib folder and main code in bin)
javac -d bin -cp "lib/*:bin" src/test/java/*.java

# 3. Run application
java -cp bin Tnode

# 4. Run JUnit tests
java -cp "bin:lib/*" org.junit.runner.JUnitCore TnodeTest
```

---


## 📋 Task Description

Write a method buildFromRPN to build a tree from the Reverse Polish Notation of a given integer arithmetic expression (only numbers, '+', '-', '\*' and '/' are allowed in the expression), return the root node of the tree.

Also write the toString method for a tree as the left parenthetic string representation (with round brackets and commas, without spaces). Tree is represented by the root node (this). Tree is a pointer structure with two pointers to link nodes of type Tnode - a pointer to the first child and a pointer to the next sibling.

Build your test trees and print the results in main-method, do not forget to test a tree that consists of one node only.
Node name must be non-empty and must not contain round brackets, commas or whitespace symbols.
In case of an invalid input string the buildFromRPN method must throw a RuntimeException with a meaningful error message (provide full context and cause of an error in user terms).

---

## ⚙️ Requirements

- **Java 8** or higher
