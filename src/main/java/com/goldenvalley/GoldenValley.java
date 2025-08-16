package com.goldenvalley;

import com.goldenvalley.core.panel.GamePanel;

import javax.swing.*;

public class GoldenValley {

    public static void main(String[] args) {

        JFrame window = new JFrame(); // Cria uma nova janela chamada "window" do tipo JFrame (janela de aplicativo Swing)
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define a operação padrão de fechamento da janela para encerrar o aplicativo quando a janela for fechada
        window.setResizable(false); // Impede que a janela seja redimensionada pelo usuário
        window.setTitle("Golden Valley"); // Define o título da janela como "Golden Valley"

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel); // Adiciona o painel do jogo à janela

        window.pack(); // Ajusta o tamanho da janela para caber no conteúdo do painel

        window.setLocationRelativeTo(null); // Centraliza a janela na tela
        window.setVisible(true); // Torna a janela visível

        gamePanel.startGameThread();
    }
}
