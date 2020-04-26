package com.danlogan.pegsandjokers.domain;

public class Card {
	private final CardRank rank;
	private final String name;
	private final Suit suit;

	public Card(CardRank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
		this.name = String.format("%s of %s",rank,suit);
	}

	public CardRank getRank() {
		return rank;
	}

	public String getName() {
		return name;
	}

	public Suit getSuit() {
		return suit;
	}
	
	public boolean isJoker() {
		return false;
	}
	
	public boolean canBeUsedToStart()
	{
		boolean canBeUsedToStart = false;
		
		switch(this.rank)
		{
		case ACE:
			canBeUsedToStart = true;
			break;
		
		case JACK:
			canBeUsedToStart = true;
			break;
		
		case QUEEN:
			canBeUsedToStart = true;
			break;
			
		case KING:
			canBeUsedToStart = true;
			break;
		
		default:
			return false;
		
		}
		
		return canBeUsedToStart;
	}
	
	public boolean canBeUsedToMoveForward()
	{
		if (this.name.equals("JOKER") || this.rank == CardRank.EIGHT)
		{
			return false;
		}
		else {return true;}
	}

}