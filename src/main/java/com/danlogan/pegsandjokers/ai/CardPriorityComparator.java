package com.danlogan.pegsandjokers.ai;

import java.util.Comparator;
import java.util.List;

import com.danlogan.pegsandjokers.domain.Card;
import com.danlogan.pegsandjokers.domain.CardRank;

public class CardPriorityComparator implements Comparator<Card> {
	
	private List<CardRank> cardRankPriority;

	@Override
	public int compare(Card card1, Card card2) {
		
		return Integer.valueOf(cardRankPriority.indexOf(card1.getRank()))
				.compareTo(Integer.valueOf(cardRankPriority.indexOf(card2.getRank())));
	}
	
	public CardPriorityComparator(List<CardRank> cardRankPriority)
	{
		this.cardRankPriority = cardRankPriority;
	}

}
