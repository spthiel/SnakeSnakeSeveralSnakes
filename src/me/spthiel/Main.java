package me.spthiel;

import me.spthiel.main.Game;

public class Main {
    
    public static void main(String[] args) {
        
        Game game = new Game(Config.MAPSIZE);
    
        Thread updateLoop = new Thread(() -> {
        
            long current;
            long last = System.currentTimeMillis();
            float delta;
            float timeperframe = 1000.0f/Config.MAXFPS;
            float timer = 0;
        
            while(true) {
            
                current = System.currentTimeMillis();
                delta = current - last;
                last = current;
                timer += delta;
            
                if(timer >= timeperframe) {
                    game.update();
                    timer -= timeperframe;
                }
            }
        
        });
        updateLoop.start();
        
    }
}
