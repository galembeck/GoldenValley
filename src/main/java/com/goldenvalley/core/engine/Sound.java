package com.goldenvalley.core.engine;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {

    Clip clip;
    URL[] soundURL = new URL[30];

    public Sound() {
        // --- TENTATIVA DE CARREGAR OS SONS ---
        // Certifique-se que os arquivos estão em: src/main/resources/assets/sounds/


        loadSound(1, "/assets/music/Menu.wav");
        loadSound(4, "/assets/music/Planting.wav");
        loadSound(5, "/assets/music/Fishing.wav");
    }
    private void loadSound(int index, String path) {
        soundURL[index] = getClass().getResource(path);

        if (soundURL[index] == null) {
            System.err.println("❌ ERRO GRAVE: Não encontrei o som: " + path);
            System.err.println("Verifique se o nome está exato (maiúsculas/minúsculas importam!)");
        } else {
            System.out.println("✅ Som carregado: " + path);
        }
    }

    public void setFile(int i) {
        try {
            if (soundURL[i] != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
                clip = AudioSystem.getClip();
                clip.open(ais);
            } else {
                System.err.println("Tentando tocar som NULO no index: " + i);
            }
        } catch (Exception e) {
            System.err.println("Erro ao abrir arquivo de áudio. Ele está corrompido ou é um MP3 renomeado?");
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // Reinicia o som do começo
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip != null) clip.stop();
    }
}