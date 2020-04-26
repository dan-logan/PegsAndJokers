package com.danlogan.pegsandjokers.domain;

public class PlayerTurn {
	
	private int playerNumber;
	private String cardName;
	private MoveType moveType;
	private int playerPositionNumber;
	
	public PlayerTurn() {
		super();
	}

	public int getPlayerNumber() {
		return this.playerNumber;
	}

	public String getCardName() {
		return this.cardName;
	}

	public MoveType getMoveType() {
		return this.moveType;
	}

	public int getPlayerPositionNumber() {
		return this.playerPositionNumber;
	}

}
