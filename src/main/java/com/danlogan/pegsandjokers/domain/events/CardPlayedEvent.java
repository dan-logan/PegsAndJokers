package com.danlogan.pegsandjokers.domain.events;

import java.util.UUID;

public class CardPlayedEvent extends GameEvent {
	
	private int playerNumber;
	private String cardName;

	@Override
	public String toString() {
		return "CardPlayedEvent [playerNumber=" + playerNumber + ", cardName=" + cardName + "]";
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public String getCardName() {
		return cardName;
	}

	public CardPlayedEvent(UUID gameId, long currentTimeMillis, int playerNumber, String cardName) {

		super(gameId, currentTimeMillis);
		
		this.playerNumber = playerNumber;
		this.cardName = cardName;

	}
	
	

}
