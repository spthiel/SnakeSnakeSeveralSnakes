package me.spthiel.util;

import me.spthiel.Config;
import me.spthiel.util.listeners.ControllerListener;
import me.spthiel.util.listeners.KeyboardListener;
import me.spthiel.util.listeners.Listener;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import me.spthiel.display.Window;

public class InputListener implements KeyListener {


    private boolean[] isPlaying = new boolean[4];
    private boolean[] pressed = new boolean[4];
    private LinkedList<Controller> pressedControllers = new LinkedList<>();

    private LinkedList<KeyboardListener> keyboardListeners = new LinkedList<>();
    private LinkedList<ControllerListener> controllerListeners = new LinkedList<>();
    private LinkedList<KeyboardListener> keyboardsToRemove = new LinkedList<>();
    private LinkedList<ControllerListener> controllersToRemove = new LinkedList<>();
    private Window window;

    public InputListener(Window window) {
        this.window = window;
        window.addKeyListener(this);
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ignored) {
                }
                Arrays.stream(ControllerEnvironment.getDefaultEnvironment().getControllers())
                    .filter(controller -> controller.getType() == Controller.Type.GAMEPAD)
                    .forEach(
                        controller -> {
                            List<ControllerListener> listener = controllerListeners.stream()
                                .filter(l -> l.getController().equals(controller))
                                .collect(Collectors.toList());
                            if(listener.size() > 0) {
                                listener.stream()
                                    .peek(ControllerListener::poll)
                                    .map(ControllerListener::getEventQueue)
                                    .forEach(entry -> {
                                        Event event = new Event();

                                        while(entry.getValue().getNextEvent(event)) {
                                            Component c = event.getComponent();
                                            entry.getKey().execute(c.isAnalog(), c.getName(), event.getValue());
                                        }
                                    });
                            } else {
                                controller.poll();
                                EventQueue eq = controller.getEventQueue();
                                Event event = new Event();
                                while(eq.getNextEvent(event)) {
                                    Component c = event.getComponent();
                                    if(c.getName().equalsIgnoreCase("Button 7")) {
                                        boolean value = event.getValue() == 1.0f;
                                        if(value) {
                                            if(!pressedControllers.contains(controller)) {
                                                window.newPlayer(controller);
                                            }
                                        } else {
                                            pressedControllers.remove(controller);
                                        }
                                    }
                                }
                            }
                        }
                    );
                controllersToRemove.forEach(listener -> controllerListeners.remove(listener));
            }
        });
        t.start();
    }
    
    public void registerListener(Listener listener) {
        if(listener instanceof KeyboardListener) {
            keyboardListeners.add((KeyboardListener)listener);
        } else if(listener instanceof ControllerListener) {
            controllerListeners.add((ControllerListener)listener);
        }
    }
    
    public void removeListener(Listener listener) {
        if(listener instanceof KeyboardListener) {
            keyboardsToRemove.add((KeyboardListener)listener);
            KeyboardListener l = (KeyboardListener)listener;
            int keyCode = l.getKeycodes()[5];
            for (int i = 0 ; i < Config.INPUTS.length; i++) {
                int n = Config.INPUTS[i][5];
                if (n == keyCode) {
                    isPlaying[i] = false;
                    pressed[i] = true;
                    break;
                }
            }
        } else if(listener instanceof ControllerListener) {
            controllersToRemove.add((ControllerListener)listener);
            pressedControllers.add(((ControllerListener)listener).getController());
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    
    }
    
    @Override
    public void keyPressed(KeyEvent e) {

        for (int i = 0; i < Config.INPUTS.length; i++) {
            int n = Config.INPUTS[i][5];
            if (n == e.getKeyCode()) {
                if (!isPlaying[i] && !pressed[i]) {
                    window.newPlayer(Config.INPUTS[i]);
                    isPlaying[i] = true;
                }
                break;
            }
        }

        keyboardListeners.stream()
                .filter(keyboardListener -> keyboardListener.acceptsKey(e.getKeyCode()))
                .forEach(keyboardListener -> keyboardListener.execute(e.getKeyCode()));

        keyboardsToRemove.forEach(listener -> keyboardListeners.remove(listener));
        keyboardsToRemove.clear();

    }

    @Override
    public void keyReleased(KeyEvent e) {

        for (int i = 0; i < Config.INPUTS.length; i++) {
            int n = Config.INPUTS[i][5];
            if (n == e.getKeyCode()) {
                pressed[i] = false;
                break;
            }
        }
        keyboardListeners.stream()
                .filter(listener -> listener.acceptsKey(e.getKeyCode()))
                .forEach(listener -> listener.freeKey(e.getKeyCode()));
    }
}
