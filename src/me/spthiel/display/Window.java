package me.spthiel.display;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

import me.spthiel.main.Game;
import me.spthiel.util.data.Vec2;
import net.java.games.input.Controller;

public class Window extends JFrame {
    
    private ArrayList<Canvas> panels;
    private Color[][]         colors;
    private int               blocksize;
    private Game              game;
    private int               playercount;
    
    private final int MAXHEIGHT;
    private final int MAXWIDTH;
    
    public Window(Game game, boolean[][] wall) {
    
        super("SnakeSnakeSeveralSnakes");

        Rectangle rect = getScreensize();

        MAXHEIGHT = rect.height-50;
        MAXWIDTH = rect.width-50;

        setLayout(new GridLayout(1, 1));
        
        int heightsize = MAXHEIGHT/wall.length;
        int widthsize = MAXWIDTH/wall.length;
        setBounds(100,100,700,700);
        
        this.blocksize = Math.min(heightsize,widthsize);
        this.game = game;
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    
        colors = new Color[wall.length][wall.length];
        panels = new ArrayList<>();
    
        for(int x = 0; x < wall.length; x++) {
            for(int y = 0; y < wall.length; y++) {
                if(wall[x][y]) {
                    colors[x][y] = Color.CYAN;
                } else {
                    colors[x][y] = Color.BLACK;
                }
            }
        }
        setVisible(true);
        setBackground(Color.BLACK);
        updateLocation();
    }
    
    public void setColor(int x, int y, Color c) {
        colors[x][y] = c;
    }
    
    public Color[][] getColors() {
        
        return colors;
    }
    
    public void setWall(boolean[][] wall) {
        
        for(int x = 0; x < wall.length; x++) {
            for (int y = 0 ; y < wall.length ; y++) {
                if (wall[x][y]) {
                    colors[x][y] = Color.CYAN;
                } else {
                    colors[x][y] = Color.BLACK;
                }
            }
        }
        repaint();
    }
    
    public Vec2 getGridLayout(int amount) {
        
        switch(amount) {
            case 0:
                return new Vec2(1, 1);
            case 1:
                return new Vec2(1, 1);
            case 2:
                return new Vec2(2, 1);
            case 3:
            case 4:
                return new Vec2(2, 2);
            case 5:
            case 6:
                return new Vec2(3, 2);
            case 7:
            case 8:
                return new Vec2(4, 2);
            case 9:
                return new Vec2(3, 3);
            case 10:
            case 11:
            case 12:
                return new Vec2(4, 3);
        }
        return null;
    }
    
    private static Border border = BorderFactory.createLineBorder(Color.WHITE, 2);
    
    public void newPlayer(int[] keylistener) {

        playercount++;
        reevaluateSpace();
        addCanvas(new Canvas(game, blocksize, playercount, keylistener));
        updateCanvases();
    }

    public void newPlayer(Controller controller) {
        playercount++;
        reevaluateSpace();
        addCanvas(new Canvas(game, blocksize, playercount, controller));
        updateCanvases();

    }

    private void reevaluateSpace() {
        Vec2 layout = getGridLayout(playercount);

        setLayout(new GridLayout(layout.getValue(), layout.getKey()));

        int heightsize = MAXHEIGHT/layout.getValue()/colors.length;
        int widthsize = MAXWIDTH/layout.getKey()/colors.length;

        this.blocksize = Math.min(heightsize,widthsize);
    }

    private void updateCanvases() {
        Dimension dim = new Dimension(blocksize * colors.length, blocksize * colors.length);
        panels.forEach(p -> {
            p.setPreferredSize(dim);
            p.setBlocksize(blocksize);
        });
        pack();
        updateLocation();
    }

    private void addCanvas(Canvas c) {
        c.setBorder(border);
        panels.add(c);
        add(c);
        c.setVisible(true);
    }

    public void removePlayer(Canvas canvas) {
        playercount--;
        canvas.setVisible(false);
        remove(canvas);
        reevaluateSpace();
        updateCanvases();
    }

    private void updateLocation() {
        int width = getWidth();
        int height = getHeight();

        Rectangle rect = getScreensize();

        int x = rect.width/2-width/2;
        int y = rect.height/2-height/2;

        setLocation(x,y);
    }

    private static Rectangle stored = null;

    public Rectangle getScreensize() {
        if(stored != null) {
            return stored;
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        int top = scnMax.top;
        int right = scnMax.right;
        int bottom = scnMax.bottom;
        int left = scnMax.left;

        return (stored = new Rectangle(left, top, screenSize.width - right - left, screenSize.height - top - bottom));
    }

}
