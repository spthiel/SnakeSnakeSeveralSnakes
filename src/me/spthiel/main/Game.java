package me.spthiel.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

import me.spthiel.Config;
import me.spthiel.display.Window;
import me.spthiel.util.InputListener;
import me.spthiel.util.Noise;
import me.spthiel.util.data.Vec2;

public class Game {

    private final boolean[][] wall;
    private final Window window;
    private LinkedList<Snake> snakes;
    private LinkedList<Snake> toRemove;
    private LinkedList<Snake> toRegister;
    private int size;
    private List<Vec2> reachable;
    private Random random;
    private InputListener listener;

    public boolean[][] getWall() {

        return wall;
    }

    public Window getWindow() {

        return window;
    }

    public int getSize() {

        return size;
    }

    public InputListener getListener() {
        return listener;
    }

    public LinkedList<Snake> getSnake() {

        return snakes;
    }

    public void registerSnake(Snake snake) {
        toRegister.add(snake);
    }

    public void removeSnake(Snake snake) {
        toRemove.add(snake);
    }

    public Game(int size) {

        this.size = size;
        this.snakes = new LinkedList<>();
        this.toRemove = new LinkedList<>();
        this.toRegister = new LinkedList<>();
        wall = new boolean[size][size];
        random = new Random();

        regenerate();
        window = new Window(this, wall);
        listener = new InputListener(window);

        window.addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    renew();
                }
            }
        });

    }
    
    public boolean isSnake(final int x, final int y) {
        //return snakes.stream().anyMatch(snake -> snake.getTail().stream().anyMatch(vec -> vec.getKey() == x && vec.getValue() == y));
        return snakes.stream().filter(Snake::isAlive).flatMap(snake -> snake.getTail().stream()).anyMatch(vec -> vec.getKey() == x && vec.getValue() == y);
    }

    private void renew() {
        regenerate();
        window.setWall(wall);
        snakes.forEach(Snake:: respawn);
    }
    
    public void regenerate() {

        if(!Config.OBSTACLES) {
            setAll(false);
            return;
        }
        
        Noise n = new Noise(null, 1000.0f, size, size);

        n.initialise();
        boolean[][] noise = n.toBooleans();

        int cx = size / 2;
        int cy = size / 2;

        for (int x = 0; x < size; x++) {
            for (int i = -2; i <= 2; i++) {
                noise[x][i + cy] = false;
            }
        }

        for (int y = 0; y < size; y++) {
            for (int i = -2; i <= 2; i++) {
                noise[i + cx][y] = false;
            }
        }

        reachable = generateReachable(noise);

        setAll(true);

        reachable.forEach(e -> wall[e.getKey()][e.getValue()] = false);

    }
    
    private void setAll(boolean value) {
        for (int x = 0; x < wall.length; x++) {
            for (int y = 0; y < wall.length; y++) {
                wall[x][y] = value;
            }
        }
    }
    
    private List<Vec2> generateReachable(boolean[][] wall) {

        LinkedList<Vec2> out = new LinkedList<>();

        LinkedList<Vec2> toCheck = new LinkedList<>();
        toCheck.add(new Vec2(size / 2, size / 2));

        while (toCheck.size() > 0) {
            Vec2 entry = toCheck.pop();
            if (out.contains(entry)) {
                continue;
            }
            if (wall[entry.getKey()][entry.getValue()]) {
                continue;
            }
            if (testForLines(entry.getKey(), entry.getValue(), wall)) {
                continue;
            }
            out.add(entry);
            toCheck.add(new Vec2(entry.getKey() == 0 ? size - 1 : entry.getKey() - 1, entry.getValue()));
            toCheck.add(new Vec2(entry.getKey() == size - 1 ? 0 : entry.getKey() + 1, entry.getValue()));
            toCheck.add(new Vec2(entry.getKey(), entry.getValue() == 0 ? size - 1 : entry.getValue() - 1));
            toCheck.add(new Vec2(entry.getKey(), entry.getValue() == size - 1 ? 0 : entry.getValue() + 1));
        }

        return out;
    }

    private static boolean testForLines(int x, int y, boolean[][] wall) {

        int[] dir = {x - 1, x + 1, y - 1, y + 1};
        for (int i = 0; i < dir.length; i++) {
            if (dir[i] < 0) {
                dir[i] = wall.length - 1;
            } else if (dir[i] >= wall.length) {
                dir[i] = 0;
            }
        }
        boolean hori = wall[dir[0]][y] && wall[dir[1]][y];
        boolean vert = wall[x][dir[2]] && wall[x][dir[3]];
        return (hori) ||
                (vert);
    }

    private boolean containsPair(int x, int y, List<Vec2> list) {

        return list.stream().anyMatch(e -> e.getKey() == x && e.getValue() == y);
    }

    public Vec2 getCherryPosition() {

        while (true) {
            int idx = random.nextInt(reachable.size());
            Vec2 e = reachable.get(idx);

            if (snakes.stream().anyMatch(snake -> snake.getTail().contains(e))) {
                continue;
            }
            return reachable.get(idx);
        }
    }

    public void update() {
        snakes.addAll(toRegister);
        toRegister.clear();
        window.setWall(wall);
        snakes.stream()
                .filter(Snake::isAlive)
                .forEach(Snake::update);
        toRemove.forEach(snake -> snakes.remove(snake));
        toRemove.clear();
        window.repaint();
    }

}
