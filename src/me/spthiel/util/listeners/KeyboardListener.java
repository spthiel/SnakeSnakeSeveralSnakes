package me.spthiel.util.listeners;

import java.util.Arrays;
import java.util.stream.IntStream;

public abstract class KeyboardListener extends Listener {

    private int[] keycodes;

    public KeyboardListener(int[] keycodes) {
        super();
        this.keycodes = keycodes;
    }

    public boolean acceptsKey(int key) {
        return containsKey(key);
    }

    private boolean containsKey(int key) {
        return Arrays.stream(keycodes).anyMatch(n -> n == key);
    }

    public int[] getKeycodes() {

        return keycodes;
    }

    public void freeKey(int key) {
    
        IntStream.range(0, keycodes.length)
                .filter(i -> keycodes[i] == key)
                .forEach(this::free);
    }

    public void execute(int key) {
        IntStream.range(0, keycodes.length)
                .filter(i -> keycodes[i] == key)
                .forEach(this::intToFunction);
    }

}
