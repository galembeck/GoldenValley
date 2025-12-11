package com.goldenvalley.objects;

import com.goldenvalley.core.config.GameConfig; // Importe a configuração aqui
import com.goldenvalley.core.panel.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameObject {

    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;

    // Área sólida para colisão (Hitbox)
    // O padrão é 0,0 com tamanho de um tile
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    public void draw(Graphics2D g2, GamePanel gp) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Otimização: Só desenha se estiver visível na câmera
        // CORREÇÃO: Usar GameConfig.TILE_SIZE em vez de gp.tileSize
        if (worldX + GameConfig.TILE_SIZE > gp.player.worldX - gp.player.screenX &&
                worldX - GameConfig.TILE_SIZE < gp.player.worldX + gp.player.screenX &&
                worldY + GameConfig.TILE_SIZE > gp.player.worldY - gp.player.screenY &&
                worldY - GameConfig.TILE_SIZE < gp.player.worldY + gp.player.screenY) {

            g2.drawImage(image, screenX, screenY, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE, null);
        }
    }
}