# 🎲 Box Puzzle Game

A console-based puzzle game where players strategically roll and manipulate boxes on an 8×8 grid to match their top sides to a randomly selected target letter.

![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk)
![Status](https://img.shields.io/badge/status-complete-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)
<small>We got 97 from this though :(</small>

## 📋 Overview

**Box Puzzle Game** is an Object-Oriented Programming project developed for **CENG211 - Fall 2025**. The game challenges players through 5 turns, each with two strategic stages:

1. **Rolling Stage**: Select an edge box to roll, triggering a domino effect across the row/column
2. **Tool Stage**: Open any box to acquire and immediately use a special tool

The goal is to maximize the number of boxes showing the target letter on their top side by the end of the game.

## ✨ Features

### Core Mechanics
- **8×8 Dynamic Grid** with 64 interactable boxes
- **Domino Effect Rolling** - boxes cascade when pushed from edges
- **6-Sided Box Simulation** - realistic rolling physics with proper surface rotation
- **5 Unique Tools** with different strategic applications

### Box Types
| Type | Symbol | Behavior |
|------|--------|----------|
| Regular | `R` | Can be rolled and stamped |
| Unchanging | `U` | Can be rolled, cannot be stamped |
| Fixed | `X` | Immovable obstacle, blocks rolling path |

### Special Tools
| Tool | Effect |
|------|--------|
| **BoxFixer** | Converts any box into an immovable FixedBox |
| **BoxFlipper** | Flips a box upside down (swaps top/bottom) |
| **MassRowStamp** | Stamps all boxes in a row with target letter |
| **MassColumnStamp** | Stamps all boxes in a column with target letter |
| **PlusShapeStamp** | Stamps 5 boxes in a plus (+) pattern |

## 🏗️ Architecture

```
src/
├── BoxPuzzleApp.java           # Application entry point
└── game/
    ├── core/
    │   ├── BoxPuzzle.java      # Game controller & MenuHandler (inner class)
    │   └── BoxGrid.java        # Grid data structure & operations
    ├── boxes/
    │   ├── Box.java            # Abstract base class
    │   ├── RegularBox.java     # Standard box implementation
    │   ├── UnchangingBox.java  # Non-stampable variant
    │   └── FixedBox.java       # Immovable obstacle
    ├── tools/
    │   ├── SpecialTool.java    # Abstract tool base class
    │   ├── BoxFixer.java       # Converts box to fixed
    │   ├── BoxFlipper.java     # Flips box upside down
    │   ├── MassRowStamp.java   # Row stamping tool
    │   ├── MassColumnStamp.java# Column stamping tool
    │   └── PlusShapeStamp.java # Plus pattern stamping
    ├── interfaces/
    │   ├── Rollable.java       # Rolling behavior contract
    │   ├── Stampable.java      # Stamping behavior contract
    │   └── Openable.java       # Opening behavior contract
    ├── exceptions/
    │   ├── EmptyBoxException.java
    │   ├── InvalidPositionException.java
    │   ├── BoxAlreadyFixedException.java
    │   └── UnmovableFixedBoxException.java
    ├── menu/
    │   ├── IMenuDisplay.java   # Display interface
    │   ├── IValidator.java     # Input validation interface
    │   └── InputValidator.java # User input validation
    └── util/
        ├── Direction.java      # Direction enum (UP/DOWN/LEFT/RIGHT)
        └── RandUtil.java       # Random generation utilities
```

## 🎯 OOP Concepts Demonstrated

| Concept | Implementation |
|---------|----------------|
| **Inheritance** | `Box` hierarchy (RegularBox, UnchangingBox, FixedBox) |
| **Abstraction** | Abstract `Box` and `SpecialTool` classes |
| **Polymorphism** | Different box behaviors via method overriding |
| **Encapsulation** | Private fields with controlled access |
| **Interfaces** | `Rollable`, `Stampable`, `Openable` contracts |
| **Generics** | Generic `useTool<T>` and `openBox<T>` methods |
| **Enums** | Type-safe `Direction` with movement deltas |
| **Inner Classes** | `MenuHandler` for UI separation |
| **Custom Exceptions** | Domain-specific error handling |
| **Collections** | `List<List<Box>>` for grid representation |

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Any Java IDE (IntelliJ IDEA, Eclipse, VS Code)

### Running the Game

```bash
# Clone the repository
git clone https://github.com/yourusername/G14_CENG211_HW4.git

# Navigate to project directory
cd G14_CENG211_HW4

# Compile
javac -d out src/*.java src/game/**/*.java

# Run
java -cp out BoxPuzzleApp
```

Or simply open in your IDE and run `BoxPuzzleApp.java`.

## 🎮 How to Play

1. **Game Start**: A random target letter (A-H) is selected
2. **Stage 1 - Rolling**: 
   - Enter coordinates of an edge box (e.g., `R1-C4`)
   - If corner box, choose direction (e.g., `down` or `right`)
   - All boxes in that path roll, creating a domino effect
3. **Stage 2 - Tool**:
   - Enter coordinates of any box to open it
   - If it contains a tool, you must use it immediately
   - Apply the tool to your chosen target
4. **End Game**: After 5 turns (or if no moves possible), score is calculated

### Example Gameplay
```
Target Letter: E
Turn 1/5 - Stage 1
Current Grid:
    C1  C2  C3  C4  C5  C6  C7  C8
R1  RM  RM  UM  RM  RM  XM  RM  RM
R2  RM  RM  RM  RM  RM  RM  RM  RM
...

Please enter the location of the edge box you want to roll: R1-C1
```

## 📊 Grid Legend

- **Type Indicators**: `R` = Regular, `U` = Unchanging, `X` = Fixed
- **Status Indicators**: `M` = Mystery (closed), `O` = Opened
- **Top Letter**: A-H (shown after box type)

Example: `RB-M` = Regular box, B on top, closed

## 🧪 Design Decisions

### Why `List<List<Box>>` over `Box[][]`?
- Type-safe generics without casting
- Interface-based programming flexibility
- Cleaner iteration with enhanced for-loops
- ArrayList provides O(1) access with better cache utilization

### Why Abstract Classes for Box and SpecialTool?
- Objects that can be instantiated, stored, and manipulated
- Shared state and behavior among subclasses
- Clear "is-a" relationship hierarchy

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **Group 14** - CENG211 Fall 2025

---

<p align="center">
  <i>Developed as part of Izmir Institute of Technology - Computer Engineering curriculum</i>
</p>
