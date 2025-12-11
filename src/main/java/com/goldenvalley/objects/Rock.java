package com.goldenvalley.objects;

import com.goldenvalley.core.panel.GamePanel;
import javax.imageio.ImageIO;
import java.io.IOException;

// 1. Adicione "implements Interactable"
public class Rock extends GameObject implements Interactable {

    public Rock() {
        this.name = "Rock";
        int x = 0,y=0;
        this.worldX = x;
        this.worldY = y;
        this.collision = true;

        try {
            // Ajuste o caminho conforme necessário
            image = ImageIO.read(getClass().getResourceAsStream("/assets/objects/Rock.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 2. Implemente o método da interface
    @Override
    public void interact(GamePanel gp, int index) {
        // A lógica de quebrar a pedra fica AQUI, e não no Player
        System.out.println("CRACK! Você quebrou a pedra.");

        // Remove a pedra do jogo (se torna null no array)
        gp.objects[index] = null;

        // Aqui você poderia adicionar sons ou dropar itens (pedras no inventário)
    }
}