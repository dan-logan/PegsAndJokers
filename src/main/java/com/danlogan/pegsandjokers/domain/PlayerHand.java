package com.danlogan.pegsandjokers.domain;

import java.util.ArrayList;

public class PlayerHand {

	private int playerNumber;
	private ArrayList<Card> cards = new ArrayList<Card>();
	
	//Builder Class
	public static class Builder {
		
		private int playerNumber;
		
		public static Builder newInstance(int playerNumber) {
			return new Builder(playerNumber);
		}
		
		private Builder(int playerNumber) {
			
			this.playerNumber = playerNumber;
		}
		
		public PlayerHand build() {
			
			return new PlayerHand(this);
		}
		
	}
	
	//Player Hand Methods
	public PlayerHand(Builder builder) {
		
		this.playerNumber = builder.playerNumber;
	}
	
	public void drawCard(DeckOfCards deck)
	{
		cards.add(deck.draw());
	}

	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public boolean hasCard(String cardName) {
		
		for (Card card : this.cards)
		{
			 if(card.getName().equals(cardName))
			 {
				 return true;
			 }
		}
		
		return false;
		
	}
	
	public void discardCard(ArrayList<Card> discardPile, String cardName)
	{
		Card cardToDiscard=null;
		
		for (Card card : this.cards)
		{
			if(card.getName().equals(cardName))
			{
				cardToDiscard = card;
			}
		}
		
		if (cardToDiscard != null)
		{
			discardPile.add(cardToDiscard);
			this.cards.remove(cardToDiscard);
		}
	}
}
