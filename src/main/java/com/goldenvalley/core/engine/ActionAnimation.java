package com.goldenvalley.core.engine;

import com.goldenvalley.core.panel.GamePanel;
import java.awt.*;

public class ActionAnimation {

    GamePanel gp;
    public boolean active = false;
    private int counter = 0;
    private String currentAction = "";

    // Configuração visual
    private final int DURATION = 60; // Duração em frames (60 = 1 segundo)

    public ActionAnimation(GamePanel gp) {
        this.gp = gp;
    }

    public void play(String action) {
        this.active = true;
        this.currentAction = action;
        this.counter = 0;

        // Opcional: Tocar som aqui
    }

    public void update() {
        if (!active) return;

        counter++;
        if (counter > DURATION) {
            active = false;
            counter = 0;
            // Ao terminar, libera o jogador
            gp.player.attacking = false;
        }
    }

    public void draw(Graphics2D g2) {
        if (!active) return;

        // Pega a posição do jogador na tela
        int x = gp.player.screenX;
        int y = gp.player.screenY;

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));

        if (currentAction.equals("FISHING")) {
            // Desenha uma "barra de progresso" ou texto sobre a cabeça
            g2.drawString("Pescando...", x - 10, y - 20);

            // Desenha um quadrado azul simples representando a boia
            g2.setColor(Color.CYAN);
            // Faz a boia subir e descer (animação simples com matemática)
            int bobY = (int) (Math.sin(counter * 0.2) * 5);
            g2.fillOval(x + 16, y - 30 + bobY, 16, 16);
        }
        else if (currentAction.equals("PLANTING")) {
            g2.drawString("Plantando...", x - 10, y - 20);

            // Efeito visual de "poeira" ou terra
            g2.setColor(new Color(139, 69, 19)); // Marrom
            int radius = counter / 2; // Círculo cresce
            if(radius < 24) {
                g2.fillOval(x + 24 - radius/2, y + 40 - radius/2, radius, radius);
            }
        }
    }
}