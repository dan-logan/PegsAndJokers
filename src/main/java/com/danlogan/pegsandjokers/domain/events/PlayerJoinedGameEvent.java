package com.danlogan.pegsandjokers.domain.events;

import com.danlogan.pegsandjokers.domain.Roster;

public class PlayerJoinedGameEvent extends RosterEvent {
	
	private int playerNumber;
	private String playerName; 

	public int getPlayerNumber() {
		return playerNumber;
	}

	public String getPlayerName() {
		return playerName;
	}

	public PlayerJoinedGameEvent(Roster roster, int playerNumber, String playerName) {
		super(roster);
		this.playerNumber = playerNumber;
		this.playerName = playerName;
	}

	@Override
	public String toString() {
		return "PlayerJoinedGameEvent [playerNumber=" + playerNumber + ", playerName=" + playerName + ", gameId="
				+ gameId + ", numberOfPlayers=" + numberOfPlayers + ", playerNames=" + playerNames + "]";
	}

}
