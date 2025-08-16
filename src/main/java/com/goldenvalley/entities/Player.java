package com.goldenvalley.entities;

import com.goldenvalley.core.config.GameConfig;
import com.goldenvalley.core.panel.GamePanel;
import com.goldenvalley.handlers.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyHandler;

    public Player(GamePanel gp, KeyHandler keyHandler) {
        this.gp = gp;
        this.keyHandler = keyHandler;

        setDefaultValues();
        getPlayerImage();

        direction = "down";
    }

    public void setDefaultValues() {
        this.x = (GameConfig.SCREEN_WIDTH - GameConfig.TILE_SIZE) / 2;
        this.y = (GameConfig.SCREEN_HEIGHT - GameConfig.TILE_SIZE) / 2;
        this.speed = GameConfig.DEFAULT_PLAYER_SPEED;
        this.size = GameConfig.TILE_SIZE;
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/player/boy_up_1.png")));
            up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/player/boy_up_2.png")));
            down1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/player/boy_down_1.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/player/boy_down_2.png")));
            left1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/player/boy_left_1.png")));
            left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/player/boy_left_2.png")));
            right1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/player/boy_right_1.png")));
            right2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/player/boy_right_2.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveUp() {
        direction = "up";
        y -= speed;
    }
    public void moveDown() {
        direction = "down";
        y += speed;
    }
    public void moveLeft() {
        direction = "left";
        x -= speed;
    }
    public void moveRight() {
        direction = "right";
        x += speed;
    }

    public void update() {
        if (keyHandler.upPressed) moveUp();
        if (keyHandler.downPressed) moveDown();
        if (keyHandler.leftPressed) moveLeft();
        if (keyHandler.rightPressed) moveRight();

        // Ensure player stays within bounds
        x = Math.max(0, Math.min(x, GameConfig.SCREEN_WIDTH - size));
        y = Math.max(0, Math.min(y, GameConfig.SCREEN_HEIGHT - size));

        spriteCounter++;

        if (spriteCounter > 12) {
            if (spriteNumber == 1) {
                spriteNumber = 2;
            } else if (spriteNumber == 2) {
                spriteNumber = 1;
            }

            spriteCounter = 0;
        }
    }

    public void render(Graphics2D g2D) {
        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNumber == 1) {
                    image = up1;
                }
                if (spriteNumber == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNumber == 1) {
                    image = down1;
                }
                if (spriteNumber == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNumber == 1) {
                    image = left1;
                }
                if (spriteNumber == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNumber == 1) {
                    image = right1;
                }
                if (spriteNumber == 2) {
                    image = right2;
                }
                break;
        }

        g2D.drawImage(image, x, y, size, size, null);
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }
}
