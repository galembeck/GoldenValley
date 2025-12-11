package com.goldenvalley.core.engine;

import com.goldenvalley.core.config.GameConfig;
import com.goldenvalley.core.panel.GamePanel;
import com.goldenvalley.objects.Rock;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        int index = 0;
        int TILE = GameConfig.TILE_SIZE;

        // Apenas objetos interativos (Pedras, Troncos)
        // Não precisamos mais definir paredes invisíveis aqui!

        gp.objects[index] = new Rock();
        gp.objects[index].worldX = 23 * TILE;
        gp.objects[index].worldY = 25 * TILE;
        index++;

        gp.objects[index] = new Rock();
        gp.objects[index].worldX = 24 * TILE;
        gp.objects[index].worldY = 25 * TILE;
        index++;
    }
}