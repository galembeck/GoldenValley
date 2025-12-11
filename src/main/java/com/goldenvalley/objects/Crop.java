package com.goldenvalley.objects;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Crop extends GameObject {

    public Crop(int x, int y) {
        this.name = "Turnip"; // Exemplo: Nabo
        this.worldX = x;
        this.worldY = y;
        this.collision = false; // Pode andar por cima da plantação

        try {
            // Use o sprite da erva daninha temporariamente se não tiver um de planta
            // Ou crie um 'crop.png'
            image = ImageIO.read(getClass().getResourceAsStream("/assets/objects/crop.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}