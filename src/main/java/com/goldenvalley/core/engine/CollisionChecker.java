package com.goldenvalley.core.engine;

import com.goldenvalley.core.panel.GamePanel;
import com.goldenvalley.entities.Entity;

import java.awt.*;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkMap(Entity entity) {
        // Se o gerenciador de mapa ou a máscara não existirem, ignora a colisão para não travar
        if (gp.mapManager == null || gp.mapManager.collisionMask == null) {
            return;
        }

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int nextX = entity.worldX;
        int nextY = entity.worldY;

        switch (entity.direction) {
            case UP -> nextY = entityTopWorldY - entity.speed;
            case DOWN -> nextY = entityBottomWorldY + entity.speed;
            case LEFT -> nextX = entityLeftWorldX - entity.speed;
            case RIGHT -> nextX = entityRightWorldX + entity.speed;
        }

        boolean hitWall = false;

        switch (entity.direction) {
            case UP:
                hitWall = isSolid(entityLeftWorldX, nextY) || isSolid(entityRightWorldX, nextY);
                break;
            case DOWN:
                hitWall = isSolid(entityLeftWorldX, entityBottomWorldY + entity.speed) || isSolid(entityRightWorldX, entityBottomWorldY + entity.speed);
                break;
            case LEFT:
                hitWall = isSolid(nextX, entityTopWorldY) || isSolid(nextX, entityBottomWorldY);
                break;
            case RIGHT:
                hitWall = isSolid(entityRightWorldX + entity.speed, entityTopWorldY) || isSolid(entityRightWorldX + entity.speed, entityBottomWorldY);
                break;
        }

        if (hitWall) {
            entity.collisionOn = true;
        }
    }

    private boolean isSolid(int x, int y) {
        // Verificação de segurança extra
        if (gp.mapManager.collisionMask == null) return false;

        // Limites do mapa
        if (x < 0 || y < 0 || x >= gp.mapManager.getMapWidth() || y >= gp.mapManager.getMapHeight()) {
            return true;
        }

        int pixelColor = gp.mapManager.collisionMask.getRGB(x, y);
        int alpha = (pixelColor >> 24) & 0xff;

        // Se tiver cor (não for transparente), é parede
        return alpha > 0;
    }

    public int checkObject(Entity entity, boolean player) {
        int index = 999;
        // Verifica se o array de objetos foi inicializado
        if (gp.objects == null) return index;

        for (int i = 0; i < gp.objects.length; i++) {
            if (gp.objects[i] != null) {
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                gp.objects[i].solidArea.x = gp.objects[i].worldX + gp.objects[i].solidArea.x;
                gp.objects[i].solidArea.y = gp.objects[i].worldY + gp.objects[i].solidArea.y;

                switch (entity.direction) {
                    case UP: entity.solidArea.y -= entity.speed; break;
                    case DOWN: entity.solidArea.y += entity.speed; break;
                    case LEFT: entity.solidArea.x -= entity.speed; break;
                    case RIGHT: entity.solidArea.x += entity.speed; break;
                }

                if (entity.solidArea.intersects(gp.objects[i].solidArea)) {
                    if (gp.objects[i].collision) entity.collisionOn = true;
                    if (player) index = i;
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.objects[i].solidArea.x = gp.objects[i].solidAreaDefaultX;
                gp.objects[i].solidArea.y = gp.objects[i].solidAreaDefaultY;
            }
        }
        return index;
    }
    // Verifica se uma coordenada específica X,Y é sólida na máscara
    public boolean checkPosition(int x, int y) {
        // Calculamos o centro do tile para ter certeza (x + metade, y + metade)
        int checkX = x + (48/2);
        int checkY = y + (48/2);

        return isSolid(checkX, checkY);
    }
}