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

}