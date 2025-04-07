package game;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public final class Player {
    int x, y, speed = 4;
    final int frameWidth = 48;
    final int frameHeight = 64;
    int lastTileX = -1;
    int lastTileY = -1;
    BufferedImage spriteSheet;
    BufferedImage currentFrame;
    int frame = 0;
    int frameCounter = 0;

    TileMap tileMap;

    enum Direction {
        DOWN, LEFT, RIGHT, UP
    } 

    Direction direction = Direction.DOWN;

    public int getCenterX() {
        return x + frameWidth / 2;
    }
    
    public int getCenterY() {
        return y + frameHeight / 2;
    }    

    public int getTileX() {
        return (x + frameWidth / 2) / tileMap.tileSize;
    }
    
    public int getTileY() {
        return (y + frameHeight / 2) / tileMap.tileSize;
    }

    public Player(int x, int y, TileMap tileMap) {
        this.x = x;
        this.y = y;
        // this.tileSize = 32;
        this.tileMap = tileMap;
        loadSprites();

        // Initialize last tile based on starting position
        this.lastTileX = getTileX();
        this.lastTileY = getTileY();
    }

    public void loadSprites() {
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream("/assets/player/dark_soldier-dragonrider.png"));
            // DEBUG sprite 
            // spriteSheet = ImageIO.read(new File("/assets/player/dark_soldier-dragonrider.png"));
            currentFrame = spriteSheet.getSubimage(0, 0, frameWidth, frameHeight);
        } catch (IOException e) {}
    }

    public void update() {
        boolean moved = false;
        
        int nextX = x;
        int nextY = y;

        if (InputHandler.isKeyDown(KeyEvent.VK_LEFT)) {
            nextX -= speed;
            direction = Direction.LEFT;
            moved = true;
        }

        if (InputHandler.isKeyDown(KeyEvent.VK_RIGHT)) {
            nextX += speed;
            direction = Direction.RIGHT;
            moved = true;
        }
        if (InputHandler.isKeyDown(KeyEvent.VK_UP)) {
            nextY -= speed;
            direction = Direction.UP;
            moved = true;
        }
        if (InputHandler.isKeyDown(KeyEvent.VK_DOWN)) {
            nextY += speed;
            direction = Direction.DOWN;
            moved = true;
        }

        if (moved) {
            int centerX = nextX + frameWidth / 2;
            int centerY = nextY + frameHeight / 2;
    
            if (!tileMap.isTileBlocked(centerX, centerY)) {
                x = nextX;
                y = nextY;
            }
    
            animate(); 
        } else {
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
