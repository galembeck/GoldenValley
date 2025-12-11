package com.goldenvalley.entities;

import com.goldenvalley.core.config.GameConfig;
import com.goldenvalley.core.panel.GamePanel;
import com.goldenvalley.handlers.KeyHandler;
import com.goldenvalley.objects.Crop;
import com.goldenvalley.objects.GameObject;
import com.goldenvalley.objects.Interactable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    private final GamePanel gp;
    private final KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;
    public boolean attacking;

    public Player(GamePanel gp, KeyHandler keyHandler) {
        this.gp = gp;
        this.keyHandler = keyHandler;

        this.screenX = (GameConfig.SCREEN_WIDTH - GameConfig.TILE_SIZE) / 2;
        this.screenY = (GameConfig.SCREEN_HEIGHT - GameConfig.TILE_SIZE) / 2;

        setDefaultValues();
        loadPlayerImage();
    }

    public void setDefaultValues() {
        this.worldX = GameConfig.TILE_SIZE * 11;
        this.worldY = GameConfig.TILE_SIZE * 11;
        this.speed = GameConfig.DEFAULT_PLAYER_SPEED;
        this.size = GameConfig.TILE_SIZE;
        this.direction = Direction.DOWN;

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    private void loadPlayerImage() {
        // Carrega as imagens (boy_up_1.png, etc)
        String basePath = "/assets/player/boy_";
        up1 = setup(basePath + "up_1.png");
        up2 = setup(basePath + "up_2.png");
        down1 = setup(basePath + "down_1.png");
        down2 = setup(basePath + "down_2.png");
        left1 = setup(basePath + "left_1.png");
        left2 = setup(basePath + "left_2.png");
        right1 = setup(basePath + "right_1.png");
        right2 = setup(basePath + "right_2.png");
    }

    public BufferedImage setup(String imagePath) {
        BufferedImage image = null;
        try {
            var stream = getClass().getResourceAsStream(imagePath);
            if (stream != null) image = ImageIO.read(stream);
            else System.err.println("Sprite faltando: " + imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void update() {
        if (keyHandler.upPressed || keyHandler.downPressed ||
                keyHandler.leftPressed || keyHandler.rightPressed || keyHandler.enterPressed) {

            if (keyHandler.upPressed) direction = Direction.UP;
            else if (keyHandler.downPressed) direction = Direction.DOWN;
            else if (keyHandler.leftPressed) direction = Direction.LEFT;
            else if (keyHandler.rightPressed) direction = Direction.RIGHT;

            collisionOn = false;
            gp.cChecker.checkMap(this);
            int objIndex = gp.cChecker.checkObject(this, true);

            if (keyHandler.enterPressed) {
                interact(objIndex);
                keyHandler.enterPressed = false;
            }
            else if (!collisionOn) {
                switch (direction) {
                    case UP -> worldY -= speed;
                    case DOWN -> worldY += speed;
                    case LEFT -> worldX -= speed;
                    case RIGHT -> worldX += speed;
                }
            }

            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNumber = (spriteNumber == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }

        // Limites do Mundo
        if (gp.mapManager != null) {
            int mapW = gp.mapManager.getMapWidth();
            int mapH = gp.mapManager.getMapHeight();

            if (worldX < 0) worldX = 0;
            if (worldY < 0) worldY = 0;
            if (worldX > mapW - size) worldX = mapW - size;
            if (worldY > mapH - size) worldY = mapH - size;
        }
    }

    public void interact(int index) {
        // 1. Interagir com Objeto (Quebrar/Abrir)
        if (index != 999) {
            gp.playSE(2); // Som de HIT (Index 2)

            GameObject obj = gp.objects[index];
            if (obj instanceof Interactable) {
                ((Interactable) obj).interact(gp, index);
            }
        }
        // 2. Ação no Chão (Pescar/Plantar)
        else {
            int nextX = worldX;
            int nextY = worldY;

            switch(direction) {
                case UP -> nextY -= GameConfig.TILE_SIZE;
                case DOWN -> nextY += GameConfig.TILE_SIZE;
                case LEFT -> nextX -= GameConfig.TILE_SIZE;
                case RIGHT -> nextX += GameConfig.TILE_SIZE;
            }

            // Verifica se é água/parede (Vermelho na máscara)
            boolean isSolid = gp.cChecker.checkPosition(nextX, nextY);

            if (isSolid) {
                // É água -> Pescar
                System.out.println("Pescando...");
                gp.playSE(5); // Som de FISHING (Index 5)
                gp.actionAnim.play("FISHING");
            } else {
                // É terra -> Plantar
                gp.playSE(4); // Som de PLANTING (Index 4)
                plantCrop(nextX, nextY);
                gp.actionAnim.play("PLANTING");
            }
        }
    }

    private void plantCrop(int x, int y) {
        for(int i = 0; i < gp.objects.length; i++) {
            if(gp.objects[i] == null) {
                int gridX = (x / GameConfig.TILE_SIZE) * GameConfig.TILE_SIZE;
                int gridY = (y / GameConfig.TILE_SIZE) * GameConfig.TILE_SIZE;
                gp.objects[i] = new Crop(gridX, gridY);
                break;
            }
        }
    }

    public void render(Graphics2D g2D) {
        BufferedImage image = null;
        switch (direction) {
            case UP -> image = (spriteNumber == 1) ? up1 : up2;
            case DOWN -> image = (spriteNumber == 1) ? down1 : down2;
            case LEFT -> image = (spriteNumber == 1) ? left1 : left2;
            case RIGHT -> image = (spriteNumber == 1) ? right1 : right2;
        }

        if (image != null) {
            g2D.drawImage(image, screenX, screenY, size, size, null);
        } else {
            g2D.setColor(Color.RED);
            g2D.fillRect(screenX, screenY, size, size);
        }
    }
}