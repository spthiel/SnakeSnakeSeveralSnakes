package me.spthiel.util.listeners;

import me.spthiel.util.data.Entry;
import net.java.games.input.Controller;
import net.java.games.input.EventQueue;

public abstract class ControllerListener extends Listener {

    private Controller controller;

    public ControllerListener(Controller controller) {
        super();
        this.controller = controller;
    }

    public Controller getController() {

        return controller;
    }

    public void poll() {
        controller.poll();
    }

    public void execute(boolean isAnalog, String keyname, float value) {
        if(isAnalog) {
            switch (keyname) {
                case "X Axis":
                    if (value > 0.5) {
                        intToFunction(2);
                    } else {
                        free(2);
                    }
                    if (value < -0.5) {
                        intToFunction(4);
                    } else {
                        free(4);
                    }
                    break;
                case "Y Axis":
                    if (value > 0.5) {
                        intToFunction(3);
                    } else {
                        free(3);
                    }
                    if (value < -0.5) {
                        intToFunction(1);
                    } else {
                        free(1);
                    }
                    break;
            }
        } else {
            boolean v = value == 1.0f;
            switch (keyname) {
                case "Button 0":
                    if(v) {
                        intToFunction(3);
                    } else {
                        free(3);
                    }
                    break;
                case "Button 1":
                    if(v) {
                        intToFunction(2);
                    } else {
                        free(2);
                    }
                    break;
                case "Button 2":
                    if(v) {
                        intToFunction(4);
                    } else {
                        free(4);
                    }
                    break;
                case "Button 3":
                    if(v) {
                        intToFunction(1);
                    } else {
                        free(1);
                    }
                    break;
                case "Button 6":
                    if(v) {
                        intToFunction(0);
                    } else {
                        free(0);
                    }
                    break;
                case "Button 7":
                    if(v) {
                        intToFunction(5);
                    } else {
                        free(5);
                    }

            }
        }
    }

    public Entry<ControllerListener, EventQueue> getEventQueue() {
        return new Entry<>(this, controller.getEventQueue());
    }
}
