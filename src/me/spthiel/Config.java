package me.spthiel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Config {
    
    public static final int
            MAPSIZE = 75,
            MAXFPS = 15,
            LENGTHFROMCHERRY = 100000,
            INVINCIBILITYSTEPS = 30,
            INITIALLENGTH = 3,
            CHERRYCOUNT = 3
            ;
    
    
    public static final boolean
            OBSTACLES = false
            ;
    
    public static final NumberFormat NUMBERFORMAT      = new DecimalFormat("000,000");
    
    //region public static final int[][] INPUTS
    public static final int[][] INPUTS = {
            {
                    KeyEvent.VK_Q,
                    KeyEvent.VK_W,
                    KeyEvent.VK_D,
                    KeyEvent.VK_S,
                    KeyEvent.VK_A,
                    KeyEvent.VK_E
            },
            {
                    KeyEvent.VK_CONTROL,
                    KeyEvent.VK_UP,
                    KeyEvent.VK_RIGHT,
                    KeyEvent.VK_DOWN,
                    KeyEvent.VK_LEFT,
                    KeyEvent.VK_NUMPAD0
            },
            {
                    KeyEvent.VK_NUMPAD7,
                    KeyEvent.VK_NUMPAD8,
                    KeyEvent.VK_NUMPAD6,
                    KeyEvent.VK_NUMPAD5,
                    KeyEvent.VK_NUMPAD4,
                    KeyEvent.VK_NUMPAD9
            },
            {
                    KeyEvent.VK_U,
                    KeyEvent.VK_I,
                    KeyEvent.VK_L,
                    KeyEvent.VK_K,
                    KeyEvent.VK_J,
                    KeyEvent.VK_O
            }
    };
    //endregion
    //region public static final Color[] SNAKECOLORS
    public static final Color[] SNAKECOLORS = {
            new Color(  0, 255,   0),               //  0
            new Color(255,  52,  98),               //  1
            new Color(255, 255,   0),               //  2
            new Color(  0, 132, 255),               //  3
            new Color(255, 175, 175),               //  4
            new Color(255, 255, 255),               //  5
            new Color(255,   0, 255),               //  6
            new Color(221, 255, 130),               //  7
            new Color(  0, 255, 129),               //  8
            new Color(230, 195, 255),               //  9
            new Color(255, 200,   0),               // 10
            new Color(255, 117, 206),               // 11
    };
    //endregion
}
