package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    public static final int APP_SCREEN_WIDTH = 800;
    public static final int APP_SCREEN_HEIGHT = 600;
    public static final int UNIT_SIZE = 25;
    public static final int UNITS_COUNT = (APP_SCREEN_HEIGHT / UNIT_SIZE) + (APP_SCREEN_WIDTH / UNIT_SIZE);
    public static final int DELAY = 110;
    int[] rows = new int[UNITS_COUNT];
    int[] cols = new int[UNITS_COUNT];

    Random random;
    boolean running = false;
    Timer timer;
    int appleX;
    int appleY;
    int bodyCounts = 4;
    char direction = 'R';
    private int score;

    public GamePanel() {
        this.random = new Random();
        this.setPreferredSize(new Dimension(APP_SCREEN_WIDTH, APP_SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        this.running = true;
        this.timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics) {

        if (running) {
            graphics.setColor(Color.RED);
            graphics.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i< bodyCounts;i++) {
                if(i == 0) {
                    graphics.setColor(Color.yellow);
                    graphics.fillRect(rows[i], cols[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    graphics.setColor(Color.green);
                    graphics.fillRect(rows[i], cols[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        } else {
            gameOver(graphics);
        }
    }

    private void gameOver(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Kabel", Font.BOLD, 75));
        FontMetrics metricsGameOver = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (APP_SCREEN_WIDTH - metricsGameOver.stringWidth("Game Over"))/2, APP_SCREEN_HEIGHT / 2);

        graphics.setFont(new Font("Ink Free", Font.PLAIN, 50));
        FontMetrics metricsScore = getFontMetrics(graphics.getFont());
        graphics.drawString(String.format("Score: %d", score),
                                (APP_SCREEN_WIDTH - metricsScore.stringWidth("Score: %d"))/2, APP_SCREEN_HEIGHT - 525);
    }

    public void newApple(){
        appleX = random.nextInt(APP_SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt(APP_SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyCounts; i > 0; i--) {
            rows[i] = rows[i-1];
            cols[i] = cols[i-1];
        }

        switch(direction) {
            case 'U':
                cols[0] = cols[0] - UNIT_SIZE;
                break;
            case 'D':
                cols[0] = cols[0] + UNIT_SIZE;
                break;
            case 'L':
                rows[0] = rows[0] - UNIT_SIZE;
                break;
            case 'R':
                rows[0] = rows[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if (rows[0] == appleX && cols[0] == appleY) {
            newApple();
            bodyCounts++;
            score++;
        }
    }

    public void checkCollision(){

        for (int i = bodyCounts; i > 0; i--) {
            if ((rows[0] == rows[i]) && (cols[0] == cols[i])) {
                running = false;
                break;
            }
        }

        if (!running) {
            timer.stop();
        }

        if (rows[0] < 0) {
            rows[0] = APP_SCREEN_WIDTH;
        }

        if (rows[0] > APP_SCREEN_WIDTH) {
            rows[0] = 0;
        }

        if (cols[0] < 0) {
            cols[0] = APP_SCREEN_HEIGHT;
        }

        if (cols[0] > APP_SCREEN_HEIGHT) {
            cols[0] = 0;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
            checkApple();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{

        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
