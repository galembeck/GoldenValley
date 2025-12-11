package com.goldenvalley.terrains;

import com.goldenvalley.core.panel.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MapManager {

    private final GamePanel gp;
    public BufferedImage worldMap;
    public BufferedImage collisionMask;

    private int mapWidth;
    private int mapHeight;

    public MapManager(GamePanel gp) {
        this.gp = gp;
        loadMapAndMask();
    }

    public void loadMapAndMask() {
        // Nomes exatos dos arquivos que o código procura
        String mapFile = "/maps/farm_map.png";
        String maskFile = "/maps/collision_map.png";

        try {
            // 1. Carrega o Mapa Visual
            var mapStream = getClass().getResourceAsStream(mapFile);
            if (mapStream != null) {
                worldMap = ImageIO.read(mapStream);
                mapWidth = worldMap.getWidth();
                mapHeight = worldMap.getHeight();
                System.out.println("SUCESSO: Mapa carregado.");
            } else {
                System.err.println("ERRO FATAL: Não encontrei o arquivo: " + mapFile);
            }

            // 2. Carrega a Máscara
            var maskStream = getClass().getResourceAsStream(maskFile);
            if (maskStream != null) {
                collisionMask = ImageIO.read(maskStream);
                System.out.println("SUCESSO: Máscara carregada.");
            } else {
                System.err.println("ERRO: Não encontrei a máscara: " + maskFile);
                System.err.println("O jogo vai rodar sem colisão de paredes.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getMapWidth() {
        return worldMap != null ? mapWidth : 2000;
    }

    public int getMapHeight() {
        return worldMap != null ? mapHeight : 2000;
    }

    public void draw(Graphics2D g2D) {
        if (worldMap != null) {
            int x = gp.player.screenX - gp.player.worldX;
            int y = gp.player.screenY - gp.player.worldY;
            g2D.drawImage(worldMap, x, y, mapWidth, mapHeight, null);
        } else {
            // SE O MAPA FALHAR, DESENHA ISSO EM VEZ DE TELA PRETA
            g2D.setColor(new Color(0, 100, 0)); // Verde escuro
            g2D.fillRect(0, 0, gp.getWidth(), gp.getHeight());
            g2D.setColor(Color.WHITE);
            g2D.drawString("ERRO: MAPA NÃO ENCONTRADO", 50, 50);
            g2D.drawString("Verifique a pasta assets/maps/", 50, 70);
        }
    }
}