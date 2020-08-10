package com.danlogan.pegsandjokers.domain.events;

import java.util.List;

import com.danlogan.pegsandjokers.domain.Roster;

public class RosterEvent {
	
	protected String gameId;
	protected int numberOfPlayers;
	protected List<String> playerNames;
	
	public RosterEvent(Roster roster)
	{
		this.gameId = roster.getGameId();
		this.numberOfPlayers = roster.getNumberOfPlayers();
		this.playerNames = roster.getPlayerNames();
	}

	public String getGameId() {
		return gameId;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public List<String> getPlayerNames() {
		return playerNames;
	}

}
