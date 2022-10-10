package me.elspeth.engine.game.gamestates;

import me.elspeth.engine.game.Game;

public abstract class GameState {

	private Game game;
	
	public GameState(Game game) {
		this.game = game;
	}
	
	public void onEnter(GameState lastState) {}
	public void onExit() {}
	
}
