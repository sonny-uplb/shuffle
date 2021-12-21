package com.sheenergizer.games.shuffle;

import android.app.Application;


public class ShuffleGame extends Application{
	private GameData gd;
	
	public void setCurrentGame(GameData gd) {
		this.gd = gd;
	}
	
	public GameData getCurrentGame() {
		return gd;
	}
}
