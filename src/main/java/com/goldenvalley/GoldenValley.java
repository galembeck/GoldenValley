package com.goldenvalley;

import com.goldenvalley.core.panel.GamePanel;

import javax.swing.*;

public class GoldenValley {

    public static void main(String[] args) {
        // Ao rodar o programa, abrimos o MENU primeiro
        SwingUtilities.invokeLater(() -> new Menu());
    }

    // Este método será chamado pelo botão do Menu
    public static void iniciarJogo() {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Golden Valley RPG");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}