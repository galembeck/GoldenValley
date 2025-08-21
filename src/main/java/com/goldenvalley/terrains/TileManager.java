package com.goldenvalley.terrains;

import com.goldenvalley.core.config.GameConfig;
import com.goldenvalley.core.panel.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class TileManager {
    private final Tile[] tiles;
    private final int[][] mapTileNumber;
    private final GamePanel gp;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tiles = new Tile[10];
        mapTileNumber = new int[GameConfig.MAX_WORLD_COL][GameConfig.MAX_WORLD_ROW];

        loadTileImage();
        loadMap(GameConfig.MAP_FILE_PATH);
    }

    public void loadTileImage() {
        try {
            tiles[0] = new Tile();
            tiles[0].image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/assets/tiles/dirt.png")
            ));

            tiles[1] = new Tile();
            tiles[1].image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/assets/tiles/grass.png")
            ));

            tiles[2] = new Tile();
            tiles[2].image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/assets/tiles/water.png")
            ));

            // Adicione mais tiles conforme necessário...

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try (InputStream is = getClass().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            for (int row = 0; row < GameConfig.MAX_WORLD_ROW; row++) {
                String line = br.readLine();

                String[] numbers = line.trim().split("\\s+");

                for (int col = 0; col < GameConfig.MAX_WORLD_COL && col < numbers.length; col++) {
                    mapTileNumber[col][row] = Integer.parseInt(numbers[col]);
                }
            }

            System.out.println("Mapa carregado com sucesso!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2D) {
        for (int worldRow = 0; worldRow < GameConfig.MAX_WORLD_ROW; worldRow++) {
            for (int worldCol = 0; worldCol < GameConfig.MAX_WORLD_COL; worldCol++) {
                int tileNumber = mapTileNumber[worldCol][worldRow];

                int worldX = worldCol * GameConfig.TILE_SIZE;
                int worldY = worldRow * GameConfig.TILE_SIZE;

                int screenX = worldX - gp.player.getX() + gp.player.screenX;
                int screenY = worldY - gp.player.getY() + gp.player.screenY;

                if (worldX + GameConfig.TILE_SIZE > gp.player.getX() - gp.player.screenX &&
                    worldX - GameConfig.TILE_SIZE < gp.player.getX() + gp.player.screenX &&
                    worldY + GameConfig.TILE_SIZE > gp.player.getY() - gp.player.screenY &&
                    worldY - GameConfig.TILE_SIZE < gp.player.getY() + gp.player.screenY) {

                    if (tileNumber >= 0 && tileNumber < tiles.length &&
                            tiles[tileNumber] != null && tiles[tileNumber].image != null) {
                        g2D.drawImage(tiles[tileNumber].image, screenX, screenY,
                                GameConfig.TILE_SIZE, GameConfig.TILE_SIZE, null);
                    }
                }
            }
        }
    }
}
