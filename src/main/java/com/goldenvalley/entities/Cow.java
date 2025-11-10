package com.goldenvalley.entities;

import com.goldenvalley.core.config.GameConfig;
import com.goldenvalley.core.panel.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cow extends Animal {
    private float milkProduction;
    private static final Logger LOGGER = Logger.getLogger(Cow.class.getName());

    public Cow(GamePanel gp) {
        super(gp);
        this.size = GameConfig.TILE_SIZE * 2;
        this.speed = GameConfig.DEFAULT_PLAYER_SPEED / 2;
        this.milkProduction = isFemale ? 100.0f : 0.0f;
    }

    @Override
    protected void loadSprites() {
        try {
            String basePath = "/assets/animals/cow_";
            loadDirectionSprites(basePath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar sprites da vaca", e);
        }
    }

    private void loadDirectionSprites(String basePath) throws IOException {
        for (Direction dir : Direction.values()) {
            String dirStr = dir.getValue();
            BufferedImage sprite1 = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(basePath + dirStr + "_1.png")
            ));
            BufferedImage sprite2 = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream(basePath + dirStr + "_2.png")
            ));

            switch (dir) {
                case UP -> { up1 = sprite1; up2 = sprite2; }
                case DOWN -> { down1 = sprite1; down2 = sprite2; }
                case LEFT -> { left1 = sprite1; left2 = sprite2; }
                case RIGHT -> { right1 = sprite1; right2 = sprite2; }
            }
        }
    }
    @Override
    public void makeSound() {
        System.out.println("Muuu!");
    }

    @Override
    public void feed() {
        this.hunger = Math.max(0, this.hunger - 30);
        if (isFemale) {
            this.milkProduction = Math.min(100, this.milkProduction + 10);
        }
    }
}
