package com.goldenvalley;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Menu extends JFrame {

    private Image buttonNormalImg;
    private Image buttonHoverImg;
    private Font pixelFont;

    public Menu() {
        setTitle("Golden Valley");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 540);
        setLocationRelativeTo(null);
        setResizable(false);

        pixelFont = loadCustomFont("src/main/resources/assets/Menu/Stardew_Valley.ttf", 23f);

        ImageIcon normalIcon = loadScaledIcon("src/main/resources/assets/Menu/button_normal.png", 380, 75);
        if (normalIcon != null) buttonNormalImg = normalIcon.getImage();

        ImageIcon hoverIcon = loadScaledIcon("src/main/resources/assets/Menu/button_hover.png", 380, 75);
        if (hoverIcon != null) buttonHoverImg = hoverIcon.getImage();

        BackgroundPanel backgroundPanel = new BackgroundPanel("src/main/resources/assets/Menu/background.png");
        backgroundPanel.setLayout(new GridBagLayout());
        setContentPane(backgroundPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(5, 0, 10, 0);

        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = loadScaledIcon("src/main/resources/assets/Menu/logo.png", 550, 280);

        if (logoIcon != null) {
            logoLabel.setIcon(logoIcon);
        } else {
            logoLabel.setText("GOLDEN VALLEY");
            logoLabel.setForeground(Color.WHITE);
            logoLabel.setFont(pixelFont.deriveFont(40f));
        }

        gbc.gridy = 0;
        backgroundPanel.add(logoLabel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        buttonPanel.add(createStyledButton("NOVO JOGO", e -> iniciarJogo()));
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
        versionLabel.setFont(pixelFont.deriveFont(18f));

        JLabel copyLabel = new JLabel("© 2024 Golden Pixel Studio");
        copyLabel.setForeground(Color.WHITE);
        copyLabel.setFont(pixelFont.deriveFont(18f));

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
    }

    private void iniciarJogo() {
        dispose();
        new GoldenValley();
    }

    private JButton createStyledButton(String text, ActionListener action) {
        JButton button = new JButton(text);

        button.setFont(pixelFont);
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
            File fontFile = new File(path);
            if (fontFile.exists()) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(font);
                return font.deriveFont(size);
            } else {
                System.err.println("Fonte não encontrada em: " + path);
            }
        } catch (FontFormatException | IOException e) {
            System.err.println("Erro ao carregar fonte: " + e.getMessage());
        }
        return new Font("Serif", Font.BOLD, (int) size);
    }

    private ImageIcon loadScaledIcon(String path, int width, int height) {
        if (new File(path).exists()) {
            ImageIcon originalIcon = new ImageIcon(path);
            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Menu::new);
    }

    private class BackgroundPanel extends JPanel {
        private final Image backgroundImage;

        BackgroundPanel(String imagePath) {
            ImageIcon icon = null;
            if (new File(imagePath).exists()) {
                icon = new ImageIcon(imagePath);
            }
            backgroundImage = icon != null ? icon.getImage() : null;
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