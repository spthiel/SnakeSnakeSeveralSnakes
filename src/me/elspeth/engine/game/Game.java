package me.elspeth.engine.game;

import me.elspeth.engine.game.gamestates.GameState;
import me.elspeth.impl.gamestates.menus.MainMenuState;

public class Game {

	private GameState gamestate = new MainMenuState(this);
	
	public Game() {
	
	}
	
	public void setGameState(GameState gamestate) {
		if (this.gamestate != null) {
			this.gamestate.onExit();
		}
		this.gamestate = gamestate;
		gamestate.onEnter(this.gamestate);
	}
	
}
