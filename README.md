## Modular Library Management System
# PCCCS495 – Term II Project | Student: Adrija Dutta | Roll No: 73 | Dept: CSE (IoTCSBT) | Spring 2026

## Project Title

 # Modular Library Management System with Fine Calculation and Reservation Engine

## Problem Statement

Traditional library systems rely on error-prone manual tracking that fails to enforce borrowing rules consistently across different user categories. This project implements a Java-based Library Management System that applies OOP principles to model two member types (students and faculty), each governed by distinct borrowing limits, loan durations, and fine rates. The system issues and returns books, calculates overdue fines, and persists all data across sessions — providing library administrators with a reliable, rule-enforcing platform that is easy to extend without modifying existing business logic. (≤150 words)

## Target User

Library administrators who manage book circulation for a student and faculty population.

## Core Features

- **Issue Books** — validates availability and enforces per-member borrow limits before issuing.
- **Return Books** — locates the open transaction, computes overdue fines via polymorphic dispatch, and restores availability.
- **Fine Calculation** — students: 14-day window at $1.00/day; faculty: 30-day window at $0.50/day.
- **Book Reservation Queue** — FIFO queue holds member IDs waiting for a book.
- **Data Persistence** — Java serialisation saves books, members, and transactions to disk between sessions.
- **Custom Exception Hierarchy** — `BookNotAvailableException`, `BorrowLimitExceededException`, and `InvalidReturnException` extend a common `LibraryException` base.
- **CLI Interface** — menu-driven console application for listing books/members, issuing, and returning.

## OOP Concepts Used

| Concept | Where Applied |
|---|---|
| **Abstraction** | `LibraryUser` (abstract) defines `getMaxBooks()`, `getBorrowDurationDays()`, `calculateFine()` as abstract methods |
| **Inheritance** | `StudentMember` and `FacultyMember` extend `LibraryUser` and override the three policy methods |
| **Polymorphism** | `LibraryService` calls `member.calculateFine(daysLate)` via a `LibraryUser` reference; JVM dispatches to the correct subclass |
| **Encapsulation** | All fields in `Book`, `LibraryUser`, `Transaction` are `private` with controlled getter/setter access |
| **Exception Hierarchy** | `LibraryException extends Exception` is the root; three specific subtypes allow precise error handling |

## Architecture Description

```
src/
├── Main.java                         # Entry point — CLI loop
├── model/
│   ├── Book.java                     # Book entity (Serializable)
│   ├── LibraryUser.java              # Abstract member base class
│   ├── StudentMember.java            # MAX 3 books, 14 days, $1.00/day
│   ├── FacultyMember.java            # MAX 10 books, 30 days, $0.50/day
│   └── Transaction.java              # Issue/return audit record (Serializable)
├── service/
│   └── LibraryService.java           # Core business logic, data coordination
├── exception/
│   ├── LibraryException.java         # Base checked exception
│   ├── BookNotAvailableException.java
│   ├── BorrowLimitExceededException.java
│   └── InvalidReturnException.java
└── util/
    └── StorageUtil.java              # ObjectOutputStream/InputStream helpers

data/                                 # Serialised .dat files (auto-created at runtime)
├── books.dat
├── members.dat
└── transactions.dat
```

**Flow:** `Main.java` → `LibraryService` (validates, executes) → `model` classes (state mutation) → `StorageUtil` (persistence).

## How to Run

### Prerequisites
- Java 8 or later installed ([download here](https://www.oracle.com/java/technologies/downloads/))
- A terminal / command prompt
- No external libraries or build tools required

### Step 1 — Check Java is installed

Open your terminal and run:

```bash
java -version
```

You should see something like `java version "17.0.x"`. If you get an error, install Java first.

### Step 2 — Navigate to your project folder

```bash
cd path/to/your/project
```

For example, if your project is on the Desktop:

```bash
# Windows
cd C:\Users\YourName\Desktop\LibraryProject

# macOS / Linux
cd ~/Desktop/LibraryProject
```

Your folder should look like this:

```
LibraryProject/
├── Main.java
├── model/
├── service/
├── exception/
└── util/
```

### Step 3 — Compile

**Windows (Command Prompt):**
```cmd
mkdir out
javac -d out Main.java model\*.java service\*.java exception\*.java util\*.java
```

**macOS / Linux (Terminal):**
```bash
mkdir -p out
javac -d out Main.java model/*.java service/*.java exception/*.java util/*.java
```

This compiles all source files and places the `.class` files into the `out/` folder.

### Step 4 — Run

```bash
java -cp out Main
```

### First Launch

On the first run, a `data/` folder is automatically created with two sample books (`B001`, `B002`) and two members (`S001` Alice — Student, `F001` Dr. Bob — Faculty) so you can test all features immediately.

### Example Session

```
Welcome to Modular Library Management System!
Initialized dummy data.

Options:
1. List Books
2. List Members
3. Issue Book
4. Return Book
5. Exit
Choose an option: 3
Enter Book ID: B001
Enter Member ID: S001
Book issued successfully.
```

### Troubleshooting

| Problem | Fix |
|---|---|
| `javac: command not found` | Java is not installed or not on your PATH — reinstall the JDK |
| `error: package model does not exist` | Make sure you are running the command from the project root folder, not inside a subfolder |
| `Main class not found` | Use `java -cp out Main` — do not add `.java` or `.class` |
| Data not saving between runs | Check that the `data/` folder was created next to your `out/` folder |