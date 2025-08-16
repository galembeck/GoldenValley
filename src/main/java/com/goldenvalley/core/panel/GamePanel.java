package com.goldenvalley.core.panel;

import com.goldenvalley.core.config.GameConfig;
import com.goldenvalley.core.engine.GameLoop;
import com.goldenvalley.core.engine.GameRenderer;
import com.goldenvalley.core.engine.GameUpdater;
import com.goldenvalley.handlers.KeyHandler;
import com.goldenvalley.entities.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements GameUpdater, GameRenderer {

    private final KeyHandler keyHandler;
    private final Player player;
    private final GameLoop gameLoop;

    public GamePanel() {
        this.keyHandler = new KeyHandler();
        this.player = new Player();
        this.gameLoop = new GameLoop(this, this);

        setupPanel();
    }

    private void setupPanel() {
        this.setPreferredSize(new Dimension(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameLoop.start();
    }

    @Override
    public void update() {
        if (keyHandler.upPressed) player.moveUp();
        if (keyHandler.downPressed) player.moveDown();
        if (keyHandler.leftPressed) player.moveLeft();
        if (keyHandler.rightPressed) player.moveRight();
    }

    @Override
    public void render() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        player.render(g2D);

        g2D.dispose();
    }
}