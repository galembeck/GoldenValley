package com.goldenvalley.entities;

import com.goldenvalley.core.config.GameConfig;
import com.goldenvalley.core.panel.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Chicken extends Animal {
    private boolean canLayEggs;
    private static final Logger LOGGER = Logger.getLogger(Chicken.class.getName());

    public Chicken(GamePanel gp) {
        super(gp);
        this.size = GameConfig.TILE_SIZE;
        this.speed = GameConfig.DEFAULT_PLAYER_SPEED * 3/4;
        this.canLayEggs = isFemale;
    }

    @Override
    protected void loadSprites() {
        try {
            String basePath = "/assets/animals/chicken_";
            loadDirectionSprites(basePath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar sprites da galinha", e);
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
        System.out.println("Có có!");
    }

    @Override
    public void feed() {
        this.hunger = Math.max(0, this.hunger - 20);
    }

    public boolean layEgg() {
        if (canLayEggs && hunger < 50) {
            hunger += 30;
            return true;
        }
        return false;
    }
}
