package com.goldenvalley.core.engine;

import com.goldenvalley.core.config.GameConfig;

public class GameLoop implements Runnable {
    private Thread gameThread;
    private final GameUpdater gameUpdater;
    private final GameRenderer gameRenderer;

    public GameLoop(GameUpdater gameUpdater, GameRenderer gameRenderer) {
        this.gameUpdater = gameUpdater;
        this.gameRenderer = gameRenderer;
    }

    public void start() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stop() {
        if (gameThread != null) {
            gameThread.interrupt();
            gameThread = null;
        }
    }

    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / GameConfig.FPS;
        double delta = 0;
        int drawCount = 0;
        long timer = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        try {
            while (gameThread != null && !Thread.currentThread().isInterrupted()) {
                currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / drawInterval;
                timer += (currentTime - lastTime);
                lastTime = currentTime;

                if (delta >= 1) {
                    gameUpdater.update();
                    gameRenderer.render();
                    delta--;
                    drawCount++;
                }

                if (timer >= 1000000000) {
                    System.out.println("FPS: " + drawCount);
                    drawCount = 0;
                    timer = 0;
                }

                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
