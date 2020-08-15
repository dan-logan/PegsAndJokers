package com.danlogan.pegsandjokers;

import java.util.UUID;

import org.springframework.context.ApplicationEvent;

public class TurnTakenEvent extends ApplicationEvent {
	
	private String gameId;
	private int takenByPlayerNumber;
	private int nextPlayerNumber;

	@Override
	public String toString() {
		return "TurnTakenEvent [gameId=" + gameId + ", takenByPlayerNumber=" + takenByPlayerNumber
				+ ", nextPlayerNumber=" + nextPlayerNumber + "]";
	}

	public String getGameId() {
		return gameId;
	}

	public int getTakenByPlayerNumber() {
		return takenByPlayerNumber;
	}

	public int getNextPlayerNumber() {
		return nextPlayerNumber;
	}

	public TurnTakenEvent(String gameId, int takenByPlayerNumber, int nextPlayerNumber) {
		super(gameId);
		this.gameId = gameId;
		this.takenByPlayerNumber = takenByPlayerNumber;
		this.nextPlayerNumber = nextPlayerNumber;
	}

}
