package com.danlogan.pegsandjokers.ai;

import java.util.List;

import com.danlogan.pegsandjokers.domain.Game;

public class TacticalAI {

	private Game game;
	private int playerNumber;
	private int pegNumber;
	
	public TacticalAI(Game game, int playerNumber, int pegNumber) {
		
		this.game = game;
		this.playerNumber = playerNumber;
		this.pegNumber = pegNumber;
	}

	public List<MoveOption> moveOptions() {

		TacticalStrategy strategy = TacticalStrategy.Builder.newInstance().forTacticalAI(this).build();
		
		return strategy.moveOptions();
		
	}

	public Game getGame() {
		return this.game;
	}

	public int getPlayerNumber() {
	
		return this.playerNumber;
	}

	public int getPegNumber() {
	
		return this.pegNumber;
	}

}
