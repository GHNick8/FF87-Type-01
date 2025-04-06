package game;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {
    int x, y, speed = 4;
    // final int tileSize = 64;
    final int frameWidth = 48;
    final int frameHeight = 64;
    BufferedImage spriteSheet;
    BufferedImage currentFrame;
    int frame = 0;
    int frameCounter = 0;

    enum Direction {
        DOWN, LEFT, RIGHT, UP
    } 

    Direction direction = Direction.DOWN;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        // this.tileSize = 32;
        loadSprites();
    }

    public void loadSprites() {
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream("/assets/player/dark_soldier-dragonrider.png"));
            // DEBUG sprite 
            // spriteSheet = ImageIO.read(new File("/assets/player/dark_soldier-dragonrider.png"));
            currentFrame = spriteSheet.getSubimage(0, 0, frameWidth, frameHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        boolean moved = false;

        if (InputHandler.isKeyDown(KeyEvent.VK_LEFT)) {
            x -= speed;
            direction = Direction.LEFT;
            moved = true;
        }

        if (InputHandler.isKeyDown(KeyEvent.VK_RIGHT)) {
            x += speed;
            direction = Direction.RIGHT;
            moved = true;
        }
        if (InputHandler.isKeyDown(KeyEvent.VK_UP)) {
            y -= speed;
            direction = Direction.UP;
            moved = true;
        }
        if (InputHandler.isKeyDown(KeyEvent.VK_DOWN)) {
            y += speed;
            direction = Direction.DOWN;
            moved = true;
        }

        if (moved) {
            animate();
        } else {
            // Idle pose 
            frame = 0; 
        }
    }

    public void animate() {
        frameCounter++;
        if (frameCounter >= 10) {
            frame = (frame + 1) % 3;
            frameCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        int row = switch (direction) {
            case DOWN -> 2;
            case LEFT -> 3;
            case RIGHT -> 1;
            case UP -> 0;
        };
        currentFrame = spriteSheet.getSubimage(frame * frameWidth, row * frameHeight, frameWidth, frameHeight);
        g2.drawImage(currentFrame, x, y, frameWidth, frameHeight, null);
    }
}
