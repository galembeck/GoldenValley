package com.goldenvalley.entities;

import com.goldenvalley.core.config.GameConfig;
import com.goldenvalley.core.panel.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Animal extends Entity {

    protected GamePanel gp;

    // Status básicos que todo animal tem
    protected int hunger = 0;
    protected boolean isFemale = true;

    public Animal(GamePanel gp) {
        this.gp = gp;
        // Posição padrão se não for definida
        this.worldX = GameConfig.TILE_SIZE * 20;
        this.worldY = GameConfig.TILE_SIZE * 20;
        this.direction = Direction.DOWN;

        loadSprites();
    }

    // Método abstrato que Vaca e Galinha devem implementar
    protected abstract void loadSprites();
    public abstract void makeSound();
    public abstract void feed();

    public void update() {
        // Lógica simples de movimento aleatório (opcional)
        // Aqui você pode adicionar IA para o animal andar sozinho depois

        // Atualiza a animação
        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNumber = (spriteNumber == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case UP -> image = (spriteNumber == 1) ? up1 : up2;
            case DOWN -> image = (spriteNumber == 1) ? down1 : down2;
            case LEFT -> image = (spriteNumber == 1) ? left1 : left2;
            case RIGHT -> image = (spriteNumber == 1) ? right1 : right2;
        }

        // --- CORREÇÃO DO ERRO AQUI ---
        // Usamos gp.player.worldX em vez de gp.player.getX()
        // Usamos gp.player.worldY em vez de gp.player.getY()

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Otimização de Renderização (Culling)
        // Só desenha o animal se ele estiver visível na câmera
        if (worldX + GameConfig.TILE_SIZE > gp.player.worldX - gp.player.screenX &&
                worldX - GameConfig.TILE_SIZE < gp.player.worldX + gp.player.screenX &&
                worldY + GameConfig.TILE_SIZE > gp.player.worldY - gp.player.screenY &&
                worldY - GameConfig.TILE_SIZE < gp.player.worldY + gp.player.screenY) {

            if (image != null) {
                g2.drawImage(image, screenX, screenY, size, size, null);
            }
        }
    }
}