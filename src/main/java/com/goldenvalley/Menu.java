package com.goldenvalley;

import com.goldenvalley.core.engine.Sound;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

public class Menu extends JFrame {

    private Image buttonNormalImg;
    private Image buttonHoverImg;
    private Font pixelFont;

    // --- 2. SISTEMA DE SOM ---
    private Sound sound = new Sound();

    public Menu() {
        setTitle("Golden Valley");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 540);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- CARREGAMENTO DE ASSETS VISUAIS ---
        pixelFont = loadCustomFont("/assets/Menu/Stardew_Valley.ttf", 23f);

        ImageIcon normalIcon = loadScaledIcon("/assets/Menu/button_normal.png", 380, 75);
        if (normalIcon != null) buttonNormalImg = normalIcon.getImage();

        ImageIcon hoverIcon = loadScaledIcon("/assets/Menu/button_hover.png", 380, 75);
        if (hoverIcon != null) buttonHoverImg = hoverIcon.getImage();

        BackgroundPanel backgroundPanel = new BackgroundPanel("/assets/Menu/background.png");
        backgroundPanel.setLayout(new GridBagLayout());
        setContentPane(backgroundPanel);

        // ... (resto da configuração visual dos componentes) ...
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(5, 0, 10, 0);

        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = loadScaledIcon("/assets/Menu/logo.png", 550, 280);

        if (logoIcon != null) {
            logoLabel.setIcon(logoIcon);
        } else {
            logoLabel.setText("GOLDEN VALLEY");
            logoLabel.setForeground(Color.WHITE);
            logoLabel.setFont(pixelFont != null ? pixelFont.deriveFont(40f) : new Font("Arial", Font.BOLD, 40));
        }

        gbc.gridy = 0;
        backgroundPanel.add(logoLabel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        buttonPanel.add(createStyledButton("NOVO JOGO", e -> acaoIniciarJogo()));
        buttonPanel.add(Box.createVerticalStrut(8));
        buttonPanel.add(createStyledButton("CARREGAR", e -> System.out.println("Carregar...")));
        buttonPanel.add(Box.createVerticalStrut(8));
        buttonPanel.add(createStyledButton("OPCOES", e -> System.out.println("Opções...")));
        buttonPanel.add(Box.createVerticalStrut(8));
        buttonPanel.add(createStyledButton("SAIR", e -> System.exit(0)));

        gbc.gridy = 1;
        backgroundPanel.add(buttonPanel, gbc);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        JLabel versionLabel = new JLabel("v. 0.1.5 Alpha");
        versionLabel.setForeground(Color.WHITE);
        if (pixelFont != null) versionLabel.setFont(pixelFont.deriveFont(18f));

        JLabel copyLabel = new JLabel("© 2024 Golden Pixel Studio");
        copyLabel.setForeground(Color.WHITE);
        if (pixelFont != null) copyLabel.setFont(pixelFont.deriveFont(18f));

        footerPanel.add(versionLabel, BorderLayout.WEST);
        footerPanel.add(copyLabel, BorderLayout.EAST);

        GridBagConstraints footerGbc = new GridBagConstraints();
        footerGbc.gridx = 0;
        footerGbc.gridy = 2;
        footerGbc.weighty = 1.0;
        footerGbc.anchor = GridBagConstraints.PAGE_END;
        footerGbc.fill = GridBagConstraints.HORIZONTAL;
        backgroundPanel.add(footerPanel, footerGbc);

        setVisible(true);

        // --- 3. TOCAR MÚSICA AO ABRIR O MENU ---
        // O index 1 é o Menu.wav (definido na classe Sound)
        playMusic(1);
    }

    private void acaoIniciarJogo() {
        // --- 4. PARAR MÚSICA ANTES DE ENTRAR NO JOGO ---
        stopMusic();

        dispose();
        GoldenValley.iniciarJogo();
    }

    // --- MÉTODOS AUXILIARES DE SOM ---
    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void stopMusic() {
        sound.stop();
    }

    // ... (o resto do código de botões, fontes e imagens continua igual) ...

    private JButton createStyledButton(String text, ActionListener action) {
        JButton button = new JButton(text);

        if (pixelFont != null) button.setFont(pixelFont);
        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Dimension btnSize = new Dimension(380, 75);
        button.setPreferredSize(btnSize);
        button.setMaximumSize(btnSize);
        button.setMinimumSize(btnSize);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addActionListener(action);

        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                AbstractButton b = (AbstractButton) c;
                ButtonModel model = b.getModel();
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                Image imgToDraw = model.isRollover() ? buttonHoverImg : buttonNormalImg;

                if (imgToDraw != null) {
                    g2.drawImage(imgToDraw, 0, 0, c.getWidth(), c.getHeight(), null);
                } else {
                    g2.setColor(new Color(139, 69, 19));
                    g2.fillRect(0, 0, c.getWidth(), c.getHeight());
                }

                String text = b.getText();
                g2.setFont(b.getFont());
                FontMetrics fm = g2.getFontMetrics();

                int x = (c.getWidth() - fm.stringWidth(text)) / 2;
                int y = (c.getHeight() - fm.getHeight()) / 2 + fm.getAscent() - 4;

                g2.setColor(new Color(60, 30, 0));
                g2.drawString(text, x + 3, y + 3);

                if (model.isPressed()) {
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.drawString(text, x + 2, y + 2);
                } else if (model.isRollover()) {
                    g2.setColor(new Color(255, 255, 200));
                    g2.drawString(text, x, y);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.drawString(text, x, y);
                }

                g2.dispose();
            }
        });

        return button;
    }

    private Font loadCustomFont(String path, float size) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            if (is != null) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, is);
                return font.deriveFont(size);
            } else {
                System.err.println("Fonte não encontrada: " + path);
            }
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return new Font("Serif", Font.BOLD, (int) size);
    }

    private ImageIcon loadScaledIcon(String path, int width, int height) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon originalIcon = new ImageIcon(imgURL);
            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } else {
            System.err.println("Imagem não encontrada: " + path);
            return null;
        }
    }

    private class BackgroundPanel extends JPanel {
        private final Image backgroundImage;

        BackgroundPanel(String imagePath) {
            java.net.URL imgURL = getClass().getResource(imagePath);
            if (imgURL != null) {
                backgroundImage = new ImageIcon(imgURL).getImage();
            } else {
                backgroundImage = null;
                System.err.println("Background não encontrado: " + imagePath);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}