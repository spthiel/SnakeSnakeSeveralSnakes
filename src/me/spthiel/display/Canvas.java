package me.spthiel.display;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import me.spthiel.Config;
import me.spthiel.main.Game;
import me.spthiel.main.Snake;
import me.spthiel.util.data.Vec2;
import me.spthiel.util.data.Vec2O;

import net.java.games.input.Controller;

public class Canvas extends JPanel {
    
    private Vec2[] cherries;
    private Game             game;
    private Snake            snake;
    private int              blocksize;
    private boolean          scrolling;
    private JTextField       field;
    private int              highscore;
    private JTextField       highscoreField;
    
    Canvas(Game game, int blocksize) {
        
        this.game = game;
        this.blocksize = blocksize;
        this.scrolling = true;
        cherries = new Vec2[Config.CHERRYCOUNT];
        generateCherries();
        field = new JTextField();
        field.setBounds(0, 0, 100, 20);
        field.setBackground(new Color(0, 0, 0, .5f));
        field.setFocusable(false);
        field.setBorder(null);
        field.setForeground(Color.WHITE);
        field.setText(Config.NUMBERFORMAT.format(Config.INITIALLENGTH));
        field.setVisible(true);
        add(field);
    
        highscoreField = new JTextField();
        highscoreField.setBounds(0, 0, 100, 20);
        highscoreField.setBackground(new Color(0, 0, 0, .5f));
        highscoreField.setFocusable(false);
        highscoreField.setBorder(null);
        highscoreField.setForeground(Color.WHITE);
        highscoreField.setText(Config.NUMBERFORMAT.format(Config.INITIALLENGTH));
        highscoreField.setVisible(true);
        add(highscoreField);
    }
    
    public Canvas(Game game, int blocksize, int playercount, int[] keylistener) {
        
        this(game, blocksize);
        this.snake = new Snake(game, Config.SNAKECOLORS[playercount - 1], this, keylistener);
    }
    
    public Canvas(Game game, int blocksize, int playercount, Controller controller) {
        
        this(game, blocksize);
        this.snake = new Snake(game, Config.SNAKECOLORS[playercount - 1], this, controller);
    }
    
    public void setBlocksize(int blocksize) {
        
        this.blocksize = blocksize;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        Vec2 snake = this.snake.getTail().getFirst();
        
        boolean[][] wall   = game.getWall();
        Color[][]   colors = game.getWindow().getColors();
        
        for (int x = 0 ; x < colors.length ; x++) {
            for (int y = 0 ; y < colors.length ; y++) {
                int dx = x;
                int dy = y;
                if (scrolling) {
                    dx -= (wall.length / 2 - snake.getKey());
                    dy -= (wall.length / 2 - snake.getValue());
                    if (dx < 0) {
                        dx += colors.length;
                    }
                    if (dy < 0) {
                        dy += colors.length;
                    }
                    if (dx >= colors.length) {
                        dx -= colors.length;
                    }
                    if (dy >= colors.length) {
                        dy -= colors.length;
                    }
                }
                if (cherryContains(dx, dy)) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(colors[dx][dy]);
                }
                g.fillRect(x * blocksize, y * blocksize, blocksize, blocksize);
            }
        }
    }
    
    public boolean cherryContains(int x, int y) {
        
        return Arrays.stream(cherries).anyMatch(cherry -> cherry.getKey() == x && cherry.getValue() == y);
    }
    
    public void eat() {
        
        generateCherries();
    }
    
    private void generateCherries() {
    
        for(int i = 0; i < cherries.length; i++) {
            cherries[i] = game.getCherryPosition();
        }
    }
    
    public void setText(int number) {
        
        String num = Config.NUMBERFORMAT.format(number);
        
        if(number > highscore) {
            highscoreField.setText(num);
            highscore = number;
        }
        
        field.setText(num);
    }
    
    public void toggleBehavior() {
        
        scrolling = !scrolling;
    }
    
}
