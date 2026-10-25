# ⛏ Game Character Manager - Minecraft / Pixel Edition (Java Swing)

An upgraded version of the **java-game-character** abstraction lesson. It is a desktop app where you can create, view, search, update, remove, and battle with game characters, all in a pixelated Minecraft-style interface.

## Concepts Demonstrated

| Concept | Where |
|---|---|
| **Abstraction** | `GameCharacter` is an abstract class with abstract methods `attack()`, `specialMove()`, `attackDamage()`, `specialDamage()`, `getType()`, `getImageFile()`, `getColor()`, plus shared concrete methods (`takeDamage`, `heal`, `showName`, ...). |
| **Abstract Data Type** | `CharacterList` is a hand-written **singly linked list**. A `Deque` is used as a **stack** for *Undo Remove*. |
| **GUI** | Java Swing (`GameWindow.java`) with a custom pixel theme. |
| **User Input** | Text fields, combo box, spinner, search box, buttons, confirmation dialogs. |

## Character Types

| Class | Picture | HP | Special Move |
|---|---|---|---|
| **Warrior** | `images/warrior.png` | High (120 + 10/lvl) | *Shield Bash*: 1.5x damage and heals 10% max HP |
| **Mage** | `images/mage.png` | Low (70 + 5/lvl) | *Lightning Storm*: 2x damage |
| **Archer** | `images/archer.png` | Medium (90 + 7/lvl) | *Triple Shot*: three arrows (2.1x total); normal shots have a 25% crit chance |

## Theme Features

- **Dirt-block tiled background** generated in code
- **Glass panels** (translucent tinted panes with bevelled edges and pixel glints) for the form, table, and battle log
- **Stone-style pixel buttons** with hover / pressed states and text shadows
- **Pixel scene** (sky, clouds, grass blocks) showing the selected character's picture, drawn with nearest-neighbour scaling so the pixels stay sharp
- **Minecraft-style hearts** instead of a plain HP bar; they animate smoothly as HP changes
- Idle bobbing animation and a "DEFEATED" banner

## Minecraft Font

The program loads a Minecraft-style font if you place one in the `fonts/` folder, named **`minecraft.ttf`**. Free options include "Minecraft" or "Minecraftia" from dafont.com or fontspace.com. Without it, the app falls back to a bold monospaced font and still works.

## Features

- **Create**, **view**, **search** (live, by name or class), **update**, and **remove** characters
- **Undo Remove** restores the last removed character (stack)
- **Battle**: select a character, then *Attack* or *Special Move* against a random living opponent; *Heal All* restores everyone

## Input Validation

- Name cannot be empty, max 15 characters, letters/numbers/spaces only
- Names must be unique (case-insensitive)
- Level limited to 1-99
- Update/Remove/Attack require a selection; battles require a living opponent

## How to Run

Requires **JDK 8 or newer**. Run from inside the project folder so the `images/` and `fonts/` folders are found.

```bash
cd GameCharacterManager
javac *.java
java Main
```

## Project Layout

```
GameCharacter.java   abstract base class
Warrior.java  Mage.java  Archer.java   concrete types
CharacterList.java   linked-list ADT
Main.java            tiny entry point (uses GameCharacter references)
GameWindow.java      Swing GUI (pixel theme)
images/              archer.png, warrior.png, mage.png
fonts/               put minecraft.ttf here (optional)
README.md
```