
# 🐍 Neon Snake Modern - Level Edition

A dynamic, neon-styled arcade Snake game built with Java Swing and custom 2D graphics. Features automatic level progression, speed acceleration, custom obstacle layouts, and visual alerts.

---

## 🌟 Key Features

* **📈 Progressive Level System:** The game level automatically increases for every **5 apples eaten**, continuously boosting the speed and challenge.
* **🧱 Dynamic Obstacles:** 
  * **Level 1:** Clean play area with no wall obstacles.
  * **Level 2:** Introduces dual center neon-pink barrier walls.
  * **Level 3+:** Adds complex wall patterns for high-level survival strategy.
* **✨ "LEVEL UP!" Visual Alert:** Dynamic gold animated text appears on-screen whenever you reach a new level.
* **🍎 Smart Food Spawning:** Apples strictly spawn in open spaces—never on top of the snake's body or wall obstacles.
* **🎨 Modern Neon Aesthetic:** Visuals crafted using `Graphics2D` featuring smooth rounded rectangles, glow effects, and a dark gradient backdrop.

---

## 🎮 How to Play

### Controls
* **`↑` / `↓` / `←` / `→` (Arrow Keys):** Change the snake's direction.
* **`SPACE`:** Restart the game immediately after a Game Over.

### Rules
1. Eat red apples to grow longer and score points.
2. Every **5 apples** advances you to the next level.
3. Avoid colliding with:
   * The outer screen borders
   * Your own tail/body
   * The pink obstacle walls (Level 2 and above)

---

## 🛠️ Tech Stack & Requirements

* **Language:** Java (JDK 8 or higher)
* **GUI Framework:** Java Swing (`JFrame`, `JPanel`)
* **Graphics Engine:** `java.awt.Graphics2D` (Rendering Hints: Anti-aliasing enabled)

---

## 🚀 How to Run Locally

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/YOUR_USERNAME/java-neon-snake-game.git](https://github.com/YOUR_USERNAME/java-neon-snake-game.git)

```
```
2. **Navigate to the project folder:**
   ```bash
   cd java-neon-snake-game



3. **Compile the Java source file:**
```bash
javac SnakeGame.java

```


4. **Launch the game:**
```bash
java SnakeGame

```



*(Note: Replace `YOUR_USERNAME` with your actual GitHub username!)*

---

## 📝 License

This project is open-source and available under the [MIT License](https://www.google.com/search?q=LICENSE). Feel free to modify, distribute, or use this project for your own learning!

```


