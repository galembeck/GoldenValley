package com.goldenvalley.entities;

import com.goldenvalley.core.panel.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Animal extends Entity {
    protected boolean isFemale;
    protected float health;
    protected float hunger;
    protected GamePanel gp;

    public Animal(GamePanel gp) {
        super();
        this.gp = gp;
        this.health = 100.0f;
        this.hunger = 0.0f;
        loadSprites();
    }

    // Métodos abstratos que cada animal deve implementar
    protected abstract void loadSprites();
    public abstract void makeSound();
    public abstract void feed();

    public void update() {
        // Atualiza o estado do animal
        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNumber = (spriteNumber == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    public void render(Graphics2D g2D) {
        // Calcula a posição na tela relativa ao jogador
        int screenX = worldX - gp.player.getX() + gp.player.screenX;
        int screenY = worldY - gp.player.getY() + gp.player.screenY;

        // Seleciona o sprite correto baseado na direção e número do sprite
        BufferedImage image = switch (direction) {
            case UP -> (spriteNumber == 1) ? up1 : up2;
            case DOWN -> (spriteNumber == 1) ? down1 : down2;
            case LEFT -> (spriteNumber == 1) ? left1 : left2;
            case RIGHT -> (spriteNumber == 1) ? right1 : right2;
        };

        // Desenha o animal na tela
        if (image != null) {
            g2D.drawImage(image, screenX, screenY, size, size, null);
        }
    }

    // Getters e setters úteis
    public float getHealth() { return health; }
    public float getHunger() { return hunger; }
    public boolean isFemale() { return isFemale; }
    public void setFemale(boolean female) { isFemale = female; }
}
