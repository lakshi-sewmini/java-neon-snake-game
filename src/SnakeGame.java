import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame {

    public SnakeGame() {
        this.add(new GamePanel());
        this.setTitle("Neon Snake Modern - Level Edition");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SnakeGame());
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener {
    static final int SCREEN_WIDTH = 500;
    static final int SCREEN_HEIGHT = 650; // Dynamic height with score panel
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * (SCREEN_HEIGHT - 50)) / UNIT_SIZE;
    static final int INITIAL_DELAY = 130;

    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 5;
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    // Level System Variables
    int level = 1;
    int levelUpTimer = 0; // "LEVEL UP!"
    ArrayList<Point> obstacles = new ArrayList<>();

    // Colors - Neon Theme
    Color colorBgStart = new Color(10, 10, 25);
    Color colorBgEnd = new Color(0, 0, 0);
    Color colorSnakeHead = new Color(0, 255, 100);
    Color colorSnakeBody = new Color(0, 200, 80);
    Color colorApple = new Color(255, 50, 50);
    Color colorObstacle = new Color(255, 0, 128); // Neon Pink Blocker
    Color colorScoreTxt = Color.WHITE;

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(this);
        startGame();
    }

    public void startGame() {
        bodyParts = 5;
        applesEaten = 0;
        level = 1;
        direction = 'R';
        obstacles.clear();

        // initial position - be in the middle
        for (int i = 0; i < bodyParts; i++) {
            x[i] = (SCREEN_WIDTH / 2) - (i * UNIT_SIZE);
            y[i] = (SCREEN_HEIGHT / 2);
        }

        newApple();
        running = true;

        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(INITIAL_DELAY, this);
        timer.start();
    }

    // method creating obstacles of according to the level
    public void setupObstacles() {
        obstacles.clear();

        // Level 2: 2 middle walls
        if (level >= 2) {
            for (int i = 5; i < 15; i++) {
                obstacles.add(new Point(5 * UNIT_SIZE, i * UNIT_SIZE));
                obstacles.add(new Point(14 * UNIT_SIZE, i * UNIT_SIZE));
            }
        }
        // Level 3 or more : Adding another batch
        if (level >= 3) {
            for (int i = 7; i < 13; i++) {
                obstacles.add(new Point(i * UNIT_SIZE, 6 * UNIT_SIZE));
                obstacles.add(new Point(i * UNIT_SIZE, 18 * UNIT_SIZE));
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        draw(g2d);
    }

    public void draw(Graphics2D g2d) {
        // 1. Background Gradient
        GradientPaint gp = new GradientPaint(0, 0, colorBgStart, 0, SCREEN_HEIGHT, colorBgEnd);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // 2. Top Header Bar (Score & Level)
        g2d.setColor(new Color(255, 255, 255, 20));
        g2d.fillRect(0, 0, SCREEN_WIDTH, 50);

        g2d.setColor(colorScoreTxt);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 18));
        g2d.drawString("SCORE: " + applesEaten, 20, 32);
        g2d.drawString("LEVEL: " + level, SCREEN_WIDTH - 130, 32);

        // Border for game area
        g2d.setColor(new Color(255, 255, 255, 50));
        g2d.drawRect(0, 50, SCREEN_WIDTH - 1, SCREEN_HEIGHT - 51);

        if (running) {
            // 3. Draw Obstacles (බාධක)
            g2d.setColor(colorObstacle);
            for (Point p : obstacles) {
                g2d.fill(new RoundRectangle2D.Double(p.x + 1, p.y + 1, UNIT_SIZE - 2, UNIT_SIZE - 2, 6, 6));
            }

            // 4. Draw Apple with Glow
            g2d.setColor(new Color(colorApple.getRed(), colorApple.getGreen(), colorApple.getBlue(), 80));
            g2d.fillOval(appleX - 4, appleY - 4, UNIT_SIZE + 8, UNIT_SIZE + 8);
            g2d.setColor(colorApple);
            g2d.fillOval(appleX + 2, appleY + 2, UNIT_SIZE - 4, UNIT_SIZE - 4);

            // 5. Draw Snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g2d.setColor(new Color(colorSnakeHead.getRed(), colorSnakeHead.getGreen(), colorSnakeHead.getBlue(), 100));
                    g2d.fill(new RoundRectangle2D.Double(x[i] - 2, y[i] - 2, UNIT_SIZE + 4, UNIT_SIZE + 4, 10, 10));
                    g2d.setColor(colorSnakeHead);
                    g2d.fill(new RoundRectangle2D.Double(x[i] + 1, y[i] + 1, UNIT_SIZE - 2, UNIT_SIZE - 2, 8, 8));
                } else {
                    g2d.setColor(colorSnakeBody);
                    if (i > bodyParts - 3) {
                        g2d.setColor(new Color(colorSnakeBody.getRed(), colorSnakeBody.getGreen(), colorSnakeBody.getBlue(), 150));
                    }
                    g2d.fill(new RoundRectangle2D.Double(x[i] + 2, y[i] + 2, UNIT_SIZE - 4, UNIT_SIZE - 4, 8, 8));
                }
            }

            // 6. Draw "LEVEL UP!" Text Animation
            if (levelUpTimer > 0) {
                g2d.setColor(new Color(255, 215, 0)); // Gold Color
                g2d.setFont(new Font("Monospaced", Font.BOLD, 45));
                FontMetrics m = getFontMetrics(g2d.getFont());
                g2d.drawString("LEVEL UP!", (SCREEN_WIDTH - m.stringWidth("LEVEL UP!")) / 2, SCREEN_HEIGHT / 2);
                levelUpTimer--;
            }

        } else {
            gameOver(g2d);
        }
    }

    public void newApple() {
        boolean validPosition = false;

        while (!validPosition) {
            appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            int minY = 50 / UNIT_SIZE;
            int maxY = SCREEN_HEIGHT / UNIT_SIZE;
            appleY = (random.nextInt(maxY - minY) + minY) * UNIT_SIZE;

            validPosition = true;

            // Preventing the formation of apples on the snake's body
            for (int i = 0; i < bodyParts; i++) {
                if (x[i] == appleX && y[i] == appleY) {
                    validPosition = false;
                    break;
                }
            }

            // Preventing apples from forming on obstacles
            for (Point p : obstacles) {
                if (p.x == appleX && p.y == appleY) {
                    validPosition = false;
                    break;
                }
            }
        }
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U': y[0] -= UNIT_SIZE; break;
            case 'D': y[0] += UNIT_SIZE; break;
            case 'L': x[0] -= UNIT_SIZE; break;
            case 'R': x[0] += UNIT_SIZE; break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;

            // apple 5 out of 5 level increase
            if (applesEaten % 5 == 0) {
                level++;
                levelUpTimer = 15; // To display the message for 15 ticks

                // Increasing the speed (Reducing the timer delay )
                int newDelay = Math.max(40, INITIAL_DELAY - (level * 12));
                timer.setDelay(newDelay);

                // adjusting obstacles according to the new level
                setupObstacles();
            }

            newApple();
        }
    }

    public void checkCollisions() {
        // The snake bites its own tail
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        // Bounding Walls
        if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 50 || y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        //Crashing into obstacles
        for (Point p : obstacles) {
            if (x[0] == p.x && y[0] == p.y) {
                running = false;
                break;
            }
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        g2d.setColor(colorApple);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 60));
        FontMetrics metrics1 = getFontMetrics(g2d.getFont());
        g2d.drawString("GAME OVER", (SCREEN_WIDTH - metrics1.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT / 2 - 40);

        g2d.setColor(colorScoreTxt);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 25));
        String finalScore = "Final Score: " + applesEaten;
        FontMetrics metrics2 = getFontMetrics(g2d.getFont());
        g2d.drawString(finalScore, (SCREEN_WIDTH - metrics2.stringWidth(finalScore)) / 2, SCREEN_HEIGHT / 2 + 20);

        String reachedLevel = "Reached Level: " + level;
        FontMetrics metrics3 = getFontMetrics(g2d.getFont());
        g2d.drawString(reachedLevel, (SCREEN_WIDTH - metrics3.stringWidth(reachedLevel)) / 2, SCREEN_HEIGHT / 2 + 60);

        g2d.setFont(new Font("Monospaced", Font.PLAIN, 18));
        g2d.drawString("Press SPACE to Restart", (SCREEN_WIDTH - getFontMetrics(g2d.getFont()).stringWidth("Press SPACE to Restart")) / 2, SCREEN_HEIGHT - 80);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!running && e.getKeyCode() == KeyEvent.VK_SPACE) {
            startGame();
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R';
                break;
            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D';
                break;
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}