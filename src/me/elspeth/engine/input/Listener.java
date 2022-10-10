package me.elspeth.engine.input;

import me.elspeth.engine.window.Window;

public abstract class Listener {

	protected Window window;
	
	public Listener(Window window) {
		this.window = window;
	}
	
}
