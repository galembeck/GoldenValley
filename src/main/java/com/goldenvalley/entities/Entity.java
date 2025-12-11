package com.goldenvalley.entities;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {

    // --- VARIÁVEIS PÚBLICAS (Para o CollisionChecker e MapManager) ---
    public int worldX, worldY;
    public int speed;
    public int size;

    // Sprites
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public int spriteCounter = 0;
    public int spriteNumber = 1;

    // --- SISTEMA DE COLISÃO ---
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;

    // --- DIREÇÃO (Corrigida para ter getValue) ---
    public Direction direction = Direction.DOWN;

    public enum Direction {
        UP("up"),
        DOWN("down"),
        LEFT("left"),
        RIGHT("right");

        private final String value;

        Direction(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}