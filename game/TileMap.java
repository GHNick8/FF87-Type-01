package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

public final class TileMap {
    final int tileSize = 32;
    Tile[] tiles;
    int[][] map;

    public TileMap() {
        loadTiles();
        loadMapFromCSV("/assets/maps/map.csv");
    }

    public void loadTiles() {
        try {
            BufferedImage tileSheet = ImageIO.read(getClass().getResourceAsStream("/assets/tiles/town01/Basic Overworld A5.png"));
            // DEBUG tiles
            // BufferedImage tileSheet = ImageIO.read(new File("/assets/tiles/town01/Basic Overworld A5.png"));

            tiles = new Tile[64]; 

            /*for (int i = 0; i < 20; i++) {
                int col = i % 5;
                int row = i / 5;*/

            for (int i = 0; i < tiles.length; i++) {
                int col = i % 8;
                int row = i / 8;

                BufferedImage tileImg = tileSheet.getSubimage(col * tileSize, row * tileSize, tileSize, tileSize);

                // Solid tiles
                boolean isSolid = switch (i) {
                    case 2, 43 -> true; 
                    default -> false;
                };

                tiles[i] = new Tile(tileImg, isSolid); 
            }

        } catch (IOException e) {}
    }

    public void loadMapFromCSV(String path) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                java.util.List<int[]> rows = new java.util.ArrayList<>();
                String line;
                
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    int[] row = new int[values.length];
                    for (int i = 0; i < values.length; i++) {
                        row[i] = Integer.parseInt(values[i].trim());
                    }
                    rows.add(row);
                }

                // Convert to 2D array
                map = new int[rows.size()][];
                for (int i = 0; i < rows.size(); i++) {
                    map[i] = rows.get(i);
                }
            }
        } catch (IOException | NumberFormatException e) {}
    }

    public boolean isTileBlocked(int x, int y) {
        int col = x / tileSize;
        int row = y / tileSize;
    
        // Prevent out-of-bounds crash
        if (row < 0 || col < 0 || row >= map.length || col >= map[0].length) {
            return true;
        }
    
        int tileIndex = map[row][col];
        return tiles[tileIndex].collision;
    }    

    public int getTileIndexAt(int x, int y) {
        int col = x / tileSize;
        int row = y / tileSize;
        if (row < 0 || col < 0 || row >= map.length || col >= map[0].length) return -1;
        return map[row][col];
    }    

    public boolean isBattleZone(int tileIndex) {
        return tileIndex == 1 || tileIndex == 50; 
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
