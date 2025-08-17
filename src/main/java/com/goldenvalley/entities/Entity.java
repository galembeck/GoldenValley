package com.goldenvalley.entities;

import java.awt.image.BufferedImage;

public class Entity {

    protected int worldX, worldY;
    protected int speed;
    protected int size;

    protected Direction direction = Direction.DOWN;
    protected BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;

    protected int spriteCounter = 0;
    protected int spriteNumber = 1;

    public enum Direction {
        UP("up"), DOWN("down"), LEFT("left"), RIGHT("right");

        private final String value;

        Direction(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public int getX() { return worldX; }
    public int getY() { return worldY; }
    public int getSpeed() { return speed; }
    public int getSize() { return size; }
    public Direction getDirection() { return direction; }
}
