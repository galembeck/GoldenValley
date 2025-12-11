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

    // --- CONFIGURAÇÕES DE TELA ---
    public final int tileSize = GameConfig.TILE_SIZE;
    public final int screenWidth = GameConfig.SCREEN_WIDTH;
    public final int screenHeight = GameConfig.SCREEN_HEIGHT;

    // --- SISTEMAS DO MOTOR ---
    // KeyHandler recebe 'this' para acessar variáveis como debugMode
    public KeyHandler keyHandler = new KeyHandler(this);
    public Sound music = new Sound();
    public Sound se = new Sound();
    public GameLoop gameLoop = new GameLoop(this, this);

    // --- GERENCIADORES E ENTIDADES ---
    public Player player = new Player(this, keyHandler);
    public MapManager mapManager = new MapManager(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);

    // Sistema de Cutscene (Pescar/Plantar)
    public ActionAnimation actionAnim = new ActionAnimation(this);

    // --- OBJETOS DO JOGO ---
    public GameObject[] objects = new GameObject[200];

    // --- DEBUG ---
    public boolean debugMode = false;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void setupGame() {
        // Coloca os objetos (pedras, etc) no mapa
        aSetter.setObject();

        // Toca a música de fundo do jogo (Index 0)
        // O index 1 era a do menu, que o Swing já tocou/parou se você configurou lá
        playMusic(0);
    }

    public void startGameThread() {
        gameLoop.start();
    }

    @Override
    public void update() {
        // Se uma animação de ação (pescar/plantar) estiver rodando, o jogo "congela"
        if (actionAnim.active) {
            actionAnim.update();
        } else {
            // Jogo normal fluindo
            player.update();
        }
    }

    // Método obrigatório da interface GameRenderer
    @Override
    public void render() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        // 1. DESENHA O MAPA (Fundo)
        if (mapManager != null) {
            mapManager.draw(g2D);
        }

        // 2. DESENHA OS OBJETOS (Pedras, Plantações)
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null) {
                objects[i].draw(g2D, this);
            }
        }

        // 3. DESENHA O JOGADOR
        if (player != null) {
            player.render(g2D);
        }

        // 4. DESENHA ANIMAÇÕES DE AÇÃO (Texto "Pescando...", Barra, etc)
        if (actionAnim != null) {
            actionAnim.draw(g2D);
        }

        // 5. DEBUG UI (Se apertar T)
        if (debugMode) {
            g2D.setColor(Color.WHITE);
            g2D.setFont(new Font("Arial", Font.BOLD, 18));
            g2D.drawString("X: " + player.worldX + " Y: " + player.worldY, 10, 30);

            // Desenha a Hitbox do player
            g2D.setColor(Color.RED);
            g2D.drawRect(player.screenX + player.solidArea.x,
                    player.screenY + player.solidArea.y,
                    player.solidArea.width, player.solidArea.height);
        }

        g2D.dispose();
    }

    // --- SISTEMA DE SOM ---
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }
}