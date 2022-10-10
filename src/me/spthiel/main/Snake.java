package me.spthiel.main;

import java.awt.*;
import java.util.*;
import java.util.List;

import me.spthiel.Config;
import me.spthiel.display.Canvas;
import me.spthiel.util.listeners.ControllerListener;
import me.spthiel.util.listeners.KeyboardListener;
import me.spthiel.util.listeners.Listener;
import me.spthiel.util.data.Vec2;

import net.java.games.input.Controller;

public class Snake {
    
    private int              x;
    private int              y;
    private int              snakeInvincibleSteps = Config.INVINCIBILITYSTEPS;
    private int              direction;
    private int              length;
    private Game             game;
    private Canvas           canvas;
    private Listener         listener;
    private LinkedList<Vec2> tail;
    private Color            color;
    private Color            invincible;
    private boolean          turned;
    private boolean          isDead;
    
    Snake(Game game, Color color, Canvas canvas) {
        
        invincible = color.darker().darker();
        this.game = game;
        this.canvas = canvas;
        x = game.getSize() / 2;
        y = game.getSize() / 2;
        length = Config.INITIALLENGTH;
        tail = new LinkedList<>();
        tail.add(new Vec2(x, y));
        turned = false;
        game.registerSnake(this);
        this.color = color;
    }
    
    public Snake(Game game, Color color, Canvas canvas, int[] keyListener) {
        
        this(game, color, canvas);
        listener = new KeyboardListener(keyListener) {
            @Override
            public void up() {
                
                turn(1);
            }
            
            @Override
            public void down() {
                
                turn(3);
            }
            
            @Override
            public void left() {
                
                turn(4);
            }
            
            @Override
            public void right() {
                
                turn(2);
            }
            
            @Override
            public void trigger() {
                
                canvas.toggleBehavior();
            }
            
            @Override
            public void remove() {
                removeThis(this);
            }
        };
        game.getListener().registerListener(listener);
    }
    
    public Snake(Game game, Color color, Canvas canvas, Controller controller) {
        
        this(game, color, canvas);
        listener = new ControllerListener(controller) {
            @Override
            public void up() {
                
                turn(1);
            }
            
            @Override
            public void down() {
                
                turn(3);
            }
            
            @Override
            public void left() {
                
                turn(4);
            }
            
            @Override
            public void right() {
                
                turn(2);
            }
            
            @Override
            public void trigger() {
                
                canvas.toggleBehavior();
            }
            
            @Override
            public void remove() {
                removeThis(this);
            }
        };
        game.getListener().registerListener(listener);
    }
    
    private void removeThis(Listener listener) {
        if(!isDead) {
            game.getListener().removeListener(listener);
            game.getWindow().removePlayer(canvas);
            game.removeSnake(this);
        } else {
            respawn();
        }
    }
    
    public void walk() {
        
        if (direction != 0) {
            turned = false;
            if (snakeInvincibleSteps > 0) {
                snakeInvincibleSteps--;
            }
            switch (direction) {
                case 1:
                    y--;
                    break;
                case 2:
                    x++;
                    break;
                case 3:
                    y++;
                    break;
                case 4:
                    x--;
                    break;
            }
            if (x < 0) {
                x = game.getSize() - 1;
            }
            if (x >= game.getSize()) {
                x = 0;
            }
            if (y < 0) {
                y = game.getSize() - 1;
            }
            if (y >= game.getSize()) {
                y = 0;
            }
            if (length > tail.size()) {
                tail.add(new Vec2(-1, -1));
            }
            
            checkField();
            ListIterator<Vec2> it      = tail.listIterator(tail.size());
            Vec2               current = it.previous();
            while (it.hasPrevious()) {
                Vec2 previous = it.previous();
                current.setKey(previous.getKey());
                current.setValue(previous.getValue());
                current = previous;
            }
            current.setKey(x);
            current.setValue(y);
        }
    }
    
    private void checkField() {
        
        if (canvas.cherryContains(x, y)) {
            canvas.eat();
            length += Config.LENGTHFROMCHERRY;
            canvas.setText(length);
        } else if (game.getWall()[x][y]) {
            isDead = true;
        } else if(game.isSnake(x,y)) {
            if(snakeInvincibleSteps <= 0) {
                isDead = true;
            }
        }
        
    }
    
    public void update() {
        
        walk();
        if (isDead) {
            return;
        }
        tail.stream()
            .filter(pos -> pos.getKey() >= 0 && pos.getValue() >= 0)
            .forEach(pos -> game
                    .getWindow()
                    .setColor(pos.getKey(), pos.getValue(), (snakeInvincibleSteps%2 == 1 ? invincible : color)));
        
    }
    
    public boolean isAlive() {
        
        return !isDead;
    }
    
    public void setCanvas(Canvas canvas) {
        
        this.canvas = canvas;
    }
    
    private boolean containsPair(int x, int y, List<Vec2> list) {
        
        return list.stream().anyMatch(e -> e.getKey() == x && e.getValue() == y);
    }
    
    public boolean turn(int newdirection) {
        
        if (!turned && (newdirection + 1) % 4 != direction - 1) {
            turned = true;
            direction = newdirection;
            return true;
        }
        return false;
    }
    
    public LinkedList<Vec2> getTail() {
        
        return tail;
    }
    
    public void respawn() {
        snakeInvincibleSteps = Config.INVINCIBILITYSTEPS;
        x = game.getSize() / 2;
        y = game.getSize() / 2;
        length = Config.INITIALLENGTH;
        tail = new LinkedList<>();
        tail.add(new Vec2(x, y));
        turned = false;
        direction = 0;
        canvas.eat();
        isDead = false;
    }
}
