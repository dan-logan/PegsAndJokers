package com.danlogan.pegsandjokers.domain;

import java.util.ArrayList;

public class DeckOfCards {
	
	ArrayList<Card> cards = new ArrayList<Card>();

	public DeckOfCards() {
		
		for(Suit suit : Suit.values())
		{
			for(CardRank rank : CardRank.values()) {
				Card card = new Card(rank, suit);
				cards.add(card);
			}
		}
		
		cards.add(new Joker());
		cards.add(new Joker());
	}
	
	public int cardsRemaining() {
		return cards.size();
	}

}
