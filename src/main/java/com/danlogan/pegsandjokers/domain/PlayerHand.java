package com.danlogan.pegsandjokers.domain;

import java.util.ArrayList;

public class PlayerHand {

	@Override
	public String toString() {
		return "PlayerHand [playerNumber=" + playerNumber + ", cards=" + cards + ", burnedCardCount=" + burnedCardCount
				+ "]";
	}

	private int playerNumber;
	private ArrayList<Card> cards;
	private int burnedCardCount;
	
	//Builder Class
	public static class Builder {
		
		private int playerNumber;
		private ArrayList<Card> cards = new ArrayList<Card>();
		private int burnedCardCount=0;
		
		public static Builder newInstance(int playerNumber) {
			return new Builder(playerNumber);
		}
		
		private Builder(int playerNumber) {
			
			this.playerNumber = playerNumber;
		}
		
		public PlayerHand build() {
			
			return new PlayerHand(this);
		}

		public Builder withCard(Card card) {
			
			this.cards.add(card);
			
			return this;
		}
		
		public Builder withBurnedCardCount(int count)
		{
			this.burnedCardCount = count;
			return this;
		}
		
	}
	
	//Player Hand Methods
	public PlayerHand(Builder builder) {
		
		this.playerNumber = builder.playerNumber;
		this.cards = builder.cards;
		this.burnedCardCount = builder.burnedCardCount;
	}
	
	public void drawCard(DeckOfCards deck)
	{
		cards.add(deck.draw());
	}

	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public Card getCard(int cardNumber)
	{
		return cards.get(cardNumber-1);
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
	
	public Card getCard(String cardName) {
		
		Card resultCard = null;
		
		for (Card card : this.cards)
		{
			 if(card.getName().equals(cardName))
			 {
				 resultCard = card;
			 }
		}
		
		return resultCard;
		
	}
	
	public void discardCard(ArrayList<Card> discardPile, String cardName) throws InvalidMoveException
	{
		Card cardToDiscard=this.getCard(cardName);
		
		if (cardToDiscard != null)
		{
			discardPile.add(cardToDiscard);
			this.cards.remove(cardToDiscard);
		}
		else
		{
			throw new InvalidMoveException(String.format("%s cannot be discarded because it is not in your hand.",cardName));
		}
	}
	
	public void burnCard(ArrayList<Card> discardPile, String cardName) throws InvalidMoveException
	{
		this.discardCard(discardPile, cardName);
		this.burnedCardCount++;
	}
	
	public int getBurnedCardCount()
	{
		return this.burnedCardCount;
	}
	
	public void resetBurnedCards()
	{
		this.burnedCardCount = 0;
	}
	
	public int getPlayerNumber()
	{
		return this.playerNumber;
	}
	
	//Default Contstructor for deserialization
	public PlayerHand()
	{
		
	}
}
