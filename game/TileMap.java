package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TileMap {
    final int tileSize = 32;
    Tile[] tiles;
    int[][] map = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    };

    public TileMap() {
        loadTiles();
    }

    public void loadTiles() {
        try {
            BufferedImage tileSheet = ImageIO.read(getClass().getResourceAsStream("/assets/tiles/town01/Basic Overworld A5.png"));
            // DEBUG tiles
            // BufferedImage tileSheet = ImageIO.read(new File("/assets/tiles/town01/Basic Overworld A5.png"));

            tiles = new Tile[20]; // Expand later 

            for (int i = 0; i < 20; i++) {
                int col = i % 5;
                int row = i / 5;

                BufferedImage tileImg = tileSheet.getSubimage(col * tileSize, row * tileSize, tileSize, tileSize);
                tiles[i] = new Tile(tileImg, false); // Set true for walls later
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                int tileIndex = map[row][col];
                if (tileIndex < tiles.length) {
                    g2.drawImage(tiles[tileIndex].image, col * tileSize, row * tileSize, tileSize, tileSize, null);
                }
            }
        }
    }

    /* Use for extra area later
    public void draw(Graphics2D g2) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                int tile = map[row][col];

                switch (tile) {
                    case 0 -> g2.setColor(Color.gray);  // Floor
                    case 1 -> g2.setColor(Color.darkGray);  // Wall
                    case 2 -> g2.setColor(Color.red);  // Battle Trigger
                }

                g2.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
            }
        }
    }
    */
}
