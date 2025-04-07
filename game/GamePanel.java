package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.plaf.DimensionUIResource;

public class GamePanel extends JPanel implements Runnable{
    // Screen settings
    final int tileSize = 32;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    long lastBattleTime = 0;
    final long battleCooldown = 1000; 

    // Game loop
    Thread gameThread;
    final int FPS = 60;

    // Game elements
    TileMap tileMap = new TileMap();
    Player player = new Player(tileSize, tileSize, tileMap);
    BufferedImage playerSprite;


    // Battle UI
    String[] battleMenu = { "Fight", "Magic", "Run" };
    int selectedOption = 0;

    // Enemy UI
    Enemy enemy = new Enemy();
    int playerHP = 30;
    boolean playerTurn = true;
    String battleMessage = "";
    boolean inAction = false;

    // Game state
    GameState gameState = GameState.OVERWORLD;

    public GamePanel() {
        this.setPreferredSize(new DimensionUIResource(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(new InputHandler());
        this.setFocusable(true);

        try {
            playerSprite = ImageIO.read(getClass().getResourceAsStream("/assets/player/dark_soldier-dragonrider.png"));
        } catch (IOException e) {}
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void sleep(int ms) {
        try { 
            Thread.sleep(ms); 
        } catch (InterruptedException e) {}
    }    

    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {
        if (gameState == GameState.OVERWORLD) {
            player.update();
        
            int currentTileX = player.getTileX();
            int currentTileY = player.getTileY();
        
            // Check for encounter if player changed tile
            if (player.lastTileX != currentTileX || player.lastTileY != currentTileY) {
                player.lastTileX = currentTileX;
                player.lastTileY = currentTileY;
        
                int tileIndex = tileMap.getTileIndexAt(player.getCenterX(), player.getCenterY());
        
                if (tileMap.isBattleZone(tileIndex)) {
                    long now = System.currentTimeMillis();
                    if (now - lastBattleTime > battleCooldown) {
                        // 10% chance
                        if (Math.random() < 0.10) { 
                            lastBattleTime = now;
                            gameState = GameState.BATTLE;
                            battleMessage = "";
                            enemy = new Enemy();
                            selectedOption = 0;
                            playerTurn = true;
                            inAction = false;
                            System.out.println("Random battle triggered!");
                        }
                    }
                }
            }
        }        

        if (gameState == GameState.BATTLE) {
            if (!inAction && playerTurn) {
                if (InputHandler.isKeyDown(java.awt.event.KeyEvent.VK_UP)) {
                    selectedOption = (selectedOption - 1 + battleMenu.length) % battleMenu.length;
                    sleep(150);
                }
                if (InputHandler.isKeyDown(java.awt.event.KeyEvent.VK_DOWN)) {
                    selectedOption = (selectedOption + 1) % battleMenu.length;
                    sleep(150);
                }
                if (InputHandler.isKeyDown(java.awt.event.KeyEvent.VK_Z)) {
                    if (battleMenu[selectedOption].equals("Fight")) {
                        int damage = (int)(Math.random() * 6 + 5); 
                        enemy.takeDamage(damage);
                        battleMessage = "You hit " + enemy.name + " for " + damage + "!";
                        inAction = true;
                        sleep(500);
                    } else if (battleMenu[selectedOption].equals("Run")) {
                        gameState = GameState.OVERWORLD;
                        battleMessage = "";
                        enemy = new Enemy(); 
                        playerHP = 30;
                        selectedOption = 0;
                        return;
                    }
                }
            }
            
            // Enemy turn after action
            if (inAction && !enemy.isDefeated()) {
                sleep(1000);
                int enemyDamage = (int)(Math.random() * 4 + 3); 
                playerHP -= enemyDamage;
                battleMessage += "\n" + enemy.name + " hits you for " + enemyDamage + "!";
                inAction = false;
                playerTurn = true;
            }
            
            // End battle
            if (enemy.isDefeated()) {
                battleMessage = "You defeated the " + enemy.name + "!";
                if (InputHandler.isKeyDown(java.awt.event.KeyEvent.VK_Z)) {
                    gameState = GameState.OVERWORLD;
                    enemy = new Enemy();
                    playerHP = 30;
                    selectedOption = 0;
                    battleMessage = "";
                }
            } else if (playerHP <= 0) {
                battleMessage = "You were defeated...";
                if (InputHandler.isKeyDown(java.awt.event.KeyEvent.VK_Z)) {
                    gameState = GameState.OVERWORLD;
                    enemy = new Enemy();
                    playerHP = 30;
                    selectedOption = 0;
                    battleMessage = "";
                }
            }            
        }        
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == GameState.OVERWORLD) {
            tileMap.draw(g2);
            player.draw(g2);
        } else if (gameState == GameState.BATTLE) {
            g2.setColor(Color.black);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(Color.white);
            g2.setFont(new Font("Arial", Font.BOLD, 28));
            g2.drawString("BATTLE!", 150, 40);

            // Draw enemy sprite
            if (enemy.sprite != null) {
                int spriteSize = 64;
                int col = 0; 
                int row = 2; 
                BufferedImage enemyFrame = enemy.sprite.getSubimage(col * spriteSize, row * spriteSize, spriteSize, spriteSize); 
                g2.drawImage(enemyFrame, 250, 60, 128, 128, null);

            }

            // Draw player sprite 
            if (playerSprite != null) {
                int frameWidth = 48;
                int frameHeight = 64;
                int col = 1; 
                int row = 0; 
                BufferedImage playerFrame = playerSprite.getSubimage(col * frameWidth, row * frameHeight, frameWidth, frameHeight);

                g2.drawImage(playerFrame, 80, 250, frameWidth * 2, frameHeight * 2, null); 
            }

            // Player HP
            g2.drawString("Player HP: " + playerHP, 50, 100);

            // Battle menu 
            for (int i = 0; i < battleMenu.length; i++) {
                if (i == selectedOption) {
                    g2.setColor(Color.YELLOW);
                    g2.drawString("> " + battleMenu[i], 100, 180 + i * 30);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.drawString(battleMenu[i], 100, 180 + i * 30);
                }
            }

            // Battle message
            g2.setColor(Color.CYAN);
            g2.setFont(new Font("Arial", Font.PLAIN, 16));

            String[] lines = battleMessage.split("\n");
            for (int i = 0; i < lines.length; i++) {
                g2.drawString(lines[i], 50, 300 + i * 20);
            }
        }

        g2.dispose();
    }
}
