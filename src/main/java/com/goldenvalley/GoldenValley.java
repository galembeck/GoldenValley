package com.goldenvalley;

import com.goldenvalley.core.panel.GamePanel;

import javax.swing.*;

public class GoldenValley extends JFrame {
    private GamePanel gamePanel;

    public GoldenValley() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Golden Valley");

        gamePanel = new GamePanel();
        add(gamePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        gamePanel.startGameThread();
    }
}
