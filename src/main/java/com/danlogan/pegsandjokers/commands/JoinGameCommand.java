package com.danlogan.pegsandjokers.commands;

public class JoinGameCommand {
	
	@Override
	public String toString() {
		return "JoinGameCommand [playerNumber=" + playerNumber + ", playerName=" + playerName + "]";
	}
	public JoinGameCommand(int playerNumber, String playerName) {
		super();
		this.playerNumber = playerNumber;
		this.playerName = playerName;
	}
	public int getPlayerNumber() {
		return playerNumber;
	}
	public String getPlayerName() {
		return playerName;
	}
	private int playerNumber;
	private String playerName;

}
