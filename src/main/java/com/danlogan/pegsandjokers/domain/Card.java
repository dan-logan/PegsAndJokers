package com.danlogan.pegsandjokers.domain;

public class Card {
	@Override
	public String toString() {
		return "Card [name=" + name + "]";
	}

	private CardRank rank;
	private String name;
	private Suit suit;
	boolean joker=false;
	String nameAndRankAsJSON;
	private int pegMovingDistance;

	public Card(CardRank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
		this.name = String.format("%s of %s",rank,suit);
		this.nameAndRankAsJSON = "{\"fullname\": \""+this.name+"\", \"rank\": "+this.rank+"}";
		if (rank == null)
		{
			//set move distance to -99 for jokers
			this.pegMovingDistance = -99;
		}
		else
		{
			if(rank.equals(CardRank.EIGHT))
			{
				this.pegMovingDistance = -8;
			}
			else {
				this.pegMovingDistance = rank.ordinal() + 1;
			}
		}
	}

	public CardRank getRank() {
		return rank;
	}

	public int getPegMovingDistance()
	{
		return this.pegMovingDistance;
	}
	
	public String getNameAndRankAsJSON()
	{
		return nameAndRankAsJSON; 
	}
	
	public String getName() {
		return name;
	}

	public Suit getSuit() {
		return suit;
	}
	
	public boolean isJoker() {
		return joker;
	}
	
	public boolean canBeUsedToStart()
	{
		boolean canBeUsedToStart = false;
		
		if (this.isJoker()) {return false;}
		
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
	
	public boolean canBeUsedToMoveBackward()
	{
		if (this.rank.equals(CardRank.EIGHT) || this.rank.equals(CardRank.NINE))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean canBeSplit()
	{
		if (this.rank.equals(CardRank.SEVEN) || this.rank.equals(CardRank.NINE))
		{
			return true;
		}
		
		return false;
	}
	
	public boolean isValidSplit(int moveDistance1, int moveDistance2)
	{
		if (!this.canBeSplit()) {return false;}
		
		switch(this.rank)
		{
		case SEVEN:
			if (moveDistance1 + moveDistance2 != 7)
			{
				return false;
			}
			break;
		
		case NINE:
			if ( Math.abs(moveDistance1) + Math.abs(moveDistance2) != 9)
			{
				return false;
			}
			else
			{
				if (moveDistance1 * moveDistance2 > 0)
				{
					return false;
				}
			}
			break;
			
		default: return false;
		}
		
		return true;
	}

	public boolean canMoveDistanceOf(int spacesToMove) {
		
		//Jokers can move any distance
		if(this.isJoker())
		{
			return true;
		}
		
		//Eights can only move backwards 8 spaces
		if(this.rank.equals(CardRank.EIGHT))
		{
			if (spacesToMove != -8)
			{
				return false;
			}
			else {return true;}
		}
		
		//Nines must be split forwards and backwards and so can move between -8 and 8 spaces
		if(this.rank.equals(CardRank.NINE))
		{
			if (java.lang.Math.abs(spacesToMove) > 8)
			{
				return false;
			}
			else {return true;}
		}
		
		//Sevens can be split into two smaller moves
		if(this.rank.equals(CardRank.SEVEN))
		{
			if ((spacesToMove < 1) || (spacesToMove > 7))
			{
				return false;
			}
			else {return true;}
		}
	
		
		//All other cards move according to their rank
		if((this.rank.ordinal() +1) != spacesToMove)
		{
			return false;
		}
		
		return true;
	}
	
	//default constructor for deserialization
	public Card()
	{
		
	}

}