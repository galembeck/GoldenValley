package com.goldenvalley.entities;

import com.goldenvalley.core.config.GameConfig;

import java.awt.*;

public class Player {
    private int x;
    private int y;
    private final int speed;
    private final int size;

    public Player() {
        this.x = (GameConfig.SCREEN_WIDTH - GameConfig.TILE_SIZE) / 2;
        this.y = (GameConfig.SCREEN_HEIGHT - GameConfig.TILE_SIZE) / 2;
        this.speed = GameConfig.DEFAULT_PLAYER_SPEED;
        this.size = GameConfig.TILE_SIZE;
    }

    public void moveUp() { y -= speed; }
    public void moveDown() { y += speed; }
    public void moveLeft() { x -= speed; }
    public void moveRight() { x += speed; }

    public void render(Graphics2D g2D) {
        g2D.setColor(Color.WHITE);
        g2D.fillRect(x, y, size, size);
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }
}
