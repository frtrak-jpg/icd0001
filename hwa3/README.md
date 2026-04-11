# Homework 3

This project is configured to work offline using local libraries in the `lib/` directory. All code resides in the default package.

## 📂 Project Structure

```
hwa3/
├── src/main/java/       # Application source code
│   └── DoubleStack.java      # Main class with lab exercises
├── src/test/java/       # Test code
│   ├── DoubleStackTest.java  # JUnit tests
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
java -cp bin DoubleStack

# 4. Run JUnit tests
java -cp "bin;lib/*" org.junit.runner.JUnitCore DoubleStackTest
```

### Linux and macOS

```bash
# 1. Compile main code
javac -d bin src/main/java/*.java

# 2. Compile tests (requires lib folder and main code in bin)
javac -d bin -cp "lib/*:bin" src/test/java/*.java

# 3. Run application
java -cp bin DoubleStack

# 4. Run JUnit tests
java -cp "bin:lib/*" org.junit.runner.JUnitCore DoubleStackTest
```

---


## 📋 Task Description

Implement an abstract data type "Stack of double numbers" (LIFO) using linkedlists. String representation of a stack (provided by toString method) must be ordered from bottom to top (tos is the last element).
<br>
List of compulsory operations:<br>
Constructor for a new stack: DoubleStack() <br>
Copy of the stack: Object clone() <br>
Check whether the stack is empty: boolean stEmpty() <br>
Adding an element to the stack: void push (double a) <br>
Removing an element from the stack: double pop() <br>
Arithmetic operation s ( + - * / ) between two topmost elements of the stack (result is left on top): void op (String s) <br>
Reading the top without removing it: double tos() <br>
Check whether two stacks are equal: boolean equals (Object o) <br>
Conversion of the stack to string (top last): String toString() <br>
</br>
Write a method <br>
public static double interpret (String pol) <br>
to calculate the value of an arithmetic expression pol in RPN (Reverse Polish Notation) using this stack type. Expression is a string which contains double numbers (including negative and multi-digit numbers) and arithmetic operations + - * / separated by whitespace symbols. The result must be double value of the expression or throwing a RuntimeException in case the expression is not correct. Expression is not correct if it is empty, contains illegal symbols, leaves redundant elements on top of stack or causes stack underflow.
All error messages must contain full context (e.g. illegal string that caused the error) and an explanation of the situation in user terms.
<br></br>
Example. DoubleStack.interpret ("2. 15. -") should return -13. .


---

## ⚙️ Requirements

- **Java 8** or higher
