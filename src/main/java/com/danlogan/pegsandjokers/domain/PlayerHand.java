package com.danlogan.pegsandjokers.domain;

import java.util.ArrayList;

public class PlayerHand {

	private int playerNumber;
	private ArrayList<Card> cards;
	
	//Builder Class
	public static class Builder {
		
		private int playerNumber;
		private ArrayList<Card> cards;
		
		public static Builder newInstance(int playerNumber, ArrayList<Card> cards) {
			return new Builder(playerNumber, cards);
		}
		
		private Builder(int playerNumber, ArrayList<Card> cards) {
			
			this.playerNumber = playerNumber;
			this.cards = cards;
		}
		
	}
	
	//Player Hand Methods
	public PlayerHand(Builder builder) {
		
		this.playerNumber = builder.playerNumber;
		this.cards = builder.cards;
	}

}
