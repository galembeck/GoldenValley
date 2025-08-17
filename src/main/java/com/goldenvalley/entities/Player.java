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
    private final KeyHandler keyHandler;

    public final int screenX, screenY;

    public Player(GamePanel gp, KeyHandler keyHandler) {
        this.keyHandler = keyHandler;

        screenX = (GameConfig.SCREEN_WIDTH - GameConfig.TILE_SIZE) / 2;
        screenY = (GameConfig.SCREEN_HEIGHT - GameConfig.TILE_SIZE) / 2;

        setDefaultValues();
        loadPlayerImage();

        direction = Direction.DOWN;
    }

    public void setDefaultValues() {
        this.worldX = GameConfig.TILE_SIZE * 23;
        this.worldY = GameConfig.TILE_SIZE * 21;
        this.speed = GameConfig.DEFAULT_PLAYER_SPEED;
        this.size = GameConfig.TILE_SIZE;
    }

    private void loadPlayerImage() {
        String basePath = "/assets/player/boy_";
        String[] directions = {"up", "down", "left", "right"};

        for (String dir : directions) {
            loadDirectionSprites(dir, basePath + dir + "_");
        }
    }

    private void loadDirectionSprites(String directionStr, String basePath) {
        try {
            BufferedImage sprite1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(basePath + "1.png")));
            BufferedImage sprite2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(basePath + "2.png")));

            switch (directionStr) {
                case "up" -> { up1 = sprite1; up2 = sprite2; }
                case "down" -> { down1 = sprite1; down2 = sprite2; }
                case "left" -> { left1 = sprite1; left2 = sprite2; }
                case "right" -> { right1 = sprite1; right2 = sprite2; }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveUp() {
        direction = Direction.UP;
        worldY -= speed;
    }

    public void moveDown() {
        direction = Direction.DOWN;
        worldY += speed;
    }

    public void moveLeft() {
        direction = Direction.LEFT;
        worldX -= speed;
    }

    public void moveRight() {
        direction = Direction.RIGHT;
        worldX += speed;
    }

    public void update() {
        boolean isMoving = false;

        if (keyHandler.upPressed) {
            moveUp();
            isMoving = true;
        }
        if (keyHandler.downPressed) {
            moveDown();
            isMoving = true;
        }
        if (keyHandler.leftPressed) {
            moveLeft();
            isMoving = true;
        }
        if (keyHandler.rightPressed) {
            moveRight();
            isMoving = true;
        }

//        worldX = Math.max(0, Math.min(worldX, GameConfig.SCREEN_WIDTH - size));
//        worldY = Math.max(0, Math.min(worldY, GameConfig.SCREEN_HEIGHT - size));

        if (isMoving) {
            spriteCounter++;
            if (spriteCounter > 10) {
                spriteNumber = (spriteNumber == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    public void render(Graphics2D g2D) {
        BufferedImage image = switch (direction) {
            case UP -> (spriteNumber == 1) ? up1 : up2;
            case DOWN -> (spriteNumber == 1) ? down1 : down2;
            case LEFT -> (spriteNumber == 1) ? left1 : left2;
            case RIGHT -> (spriteNumber == 1) ? right1 : right2;
        };

        if (image != null) {
            g2D.drawImage(image, screenX, screenY, size, size, null);
        }
    }
}