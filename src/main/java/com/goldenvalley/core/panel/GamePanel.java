package com.goldenvalley.core.panel;

import com.goldenvalley.core.config.GameConfig;
import com.goldenvalley.core.engine.*;
import com.goldenvalley.entities.Player;
import com.goldenvalley.handlers.KeyHandler;
import com.goldenvalley.objects.GameObject;
import com.goldenvalley.terrains.MapManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements GameUpdater, GameRenderer {

    // --- SISTEMA ---
    private final GameLoop gameLoop;
    private final KeyHandler keyHandler;

    // --- ENTIDADES ---
    public final Player player;
    public final MapManager mapManager;

    // --- LÓGICA DO MUNDO ---
    public CollisionChecker cChecker;
    public AssetSetter aSetter;
    public GameObject[] objects = new GameObject[200];

    // --- NOVO: ANIMAÇÃO DE AÇÃO ---
    public ActionAnimation actionAnim;

    // --- DEBUG ---
    public boolean debugMode = false;

    public GamePanel() {
        this.setPreferredSize(new Dimension(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        // Inicializações
        this.keyHandler = new KeyHandler(this);
        this.addKeyListener(keyHandler);

        this.gameLoop = new GameLoop(this, this);
        this.player = new Player(this, keyHandler);
        this.mapManager = new MapManager(this);
        this.cChecker = new CollisionChecker(this);
        this.aSetter = new AssetSetter(this);

        // Inicializa o sistema de animação
        this.actionAnim = new ActionAnimation(this);

        setupGame();
    }

    public void setupGame() {
        aSetter.setObject();
    }

    public void startGameThread() {
        gameLoop.start();
    }

    @Override
    public void update() {
        // --- TRAVAMENTO PARA CUTSCENE ---
        // Se a animação estiver ativa, atualizamos ELA e NÃO o jogador
        if (actionAnim.active) {
            actionAnim.update();
        } else {
            // Jogo normal: Jogador se move
            player.update();
        }
    }

    @Override
    public void render() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        // 1. Mapa
        if (mapManager != null) mapManager.draw(g2D);

        // 2. Objetos
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null) objects[i].draw(g2D, this);
        }

        // 3. Jogador
        if (player != null) player.render(g2D);

        // 4. EFEITOS ESPECIAIS (Por cima de tudo)
        if (actionAnim != null) actionAnim.draw(g2D);

        // 5. Debug UI
        if (debugMode) {
            g2D.setFont(new Font("Arial", Font.BOLD, 20));
            g2D.setColor(Color.WHITE);
            g2D.drawString("X: " + player.worldX + " Y: " + player.worldY, 10, 40);
            g2D.setColor(Color.RED);
            g2D.drawRect(player.screenX + player.solidArea.x,
                    player.screenY + player.solidArea.y,
                    player.solidArea.width, player.solidArea.height);
        }

        g2D.dispose();
    }
}