package me.spthiel.util.listeners;

public abstract class Listener {
    
    private static int nextid;
    
    private int id;
    private boolean[] pressed;
    
    Listener() {
        id = nextid++;
        this.pressed = new boolean[]{false, false, false, false, false, true};
    }

    void free(int idx) {
        pressed[idx] = false;
    }

    void intToFunction(int i) {
        if(pressed[i]) {
            return;
        }
        pressed[i] = true;
        switch(i) {
            case 0:
                trigger();
                break;
            case 1:
                up();
                break;
            case 2:
                right();
                break;
            case 3:
                down();
                break;
            case 4:
                left();
                break;
            case 5:
                remove();
        }
    }

    public abstract void up();
    public abstract void down();
    public abstract void left();
    public abstract void right();
    public abstract void trigger();
    public abstract void remove();
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(!(obj instanceof Listener)) {
            return false;
        }
        return ((Listener)obj).id == id;
    }
}
