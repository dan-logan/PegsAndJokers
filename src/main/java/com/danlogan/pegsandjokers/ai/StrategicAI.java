package com.danlogan.pegsandjokers.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.danlogan.pegsandjokers.domain.Card;
import com.danlogan.pegsandjokers.domain.CardRank;
import com.danlogan.pegsandjokers.domain.Game;
import com.danlogan.pegsandjokers.domain.MoveType;
import com.danlogan.pegsandjokers.domain.PlayerHand;
import com.danlogan.pegsandjokers.domain.PlayerNotFoundException;
import com.danlogan.pegsandjokers.domain.PlayerTurn;

import jdk.internal.org.jline.utils.Log;

public class StrategicAI {
	
	private Game game;
	private int playerNumber;
	
	private ArrayList<TacticalAI> tacticalAIs = new ArrayList<TacticalAI>();
	private List<CardRank> burnCardPriority = List.of(CardRank.SIX, CardRank.SEVEN, CardRank.FIVE, CardRank.FOUR,
			CardRank.THREE, CardRank.TWO, CardRank.NINE, CardRank.EIGHT, CardRank.JACK, CardRank.QUEEN, CardRank.KING, 
			CardRank.ACE);
	

	public StrategicAI(Game game, int playerNumber) {
		
		this.game = game;
		this.playerNumber = playerNumber;
		
		for (int i=0;i<5;i++) //create a tactical AI for each peg
		{
			TacticalAI ai = new TacticalAI(game,playerNumber, i+1);
			tacticalAIs.add(ai);
		}
		
	}

	public PlayerTurn getNextTurn() {
		
		//start simple and return tactical AIs that have at least one option found
		Stream<TacticalAI> tacticalAIStream = this.tacticalAIs.stream()
					.filter(ai -> ai.moveOptions().size()>0);
		
		Optional<TacticalAI> tacticalAItoUse = tacticalAIStream.findFirst();
		if (tacticalAItoUse.isPresent())
			return tacticalAItoUse.get().moveOptions().get(0).getPlayerTurn();
		else
			//when all else fails.. burn a card
			return getBurnACardTurn();
	}

	private PlayerTurn getBurnACardTurn() {
		
		PlayerHand hand=null;
		try {
			hand = this.game.getPlayerHand(this.playerNumber);
		} catch (PlayerNotFoundException e) {
			Log.warn("AI could not get player hand for player number: " + playerNumber);
			e.printStackTrace();
		}
		
		//prioritize cards to burn
		Stream<Card> playableCards = hand.getCards().stream()
				.filter(card -> !card.getName().equals("JOKER")) // don't burn jokers
				.sorted(new CardPriorityComparator(burnCardPriority));
		Card burnCard = playableCards.findFirst().get();
		
		PlayerTurn turn = null;
		 turn = PlayerTurn.Builder.newInstance()
					.withMoveType(MoveType.DISCARD)
					.withCardName(burnCard.getName())
					.withPlayerNumber(this.playerNumber)
					.build();

		return turn;
	}

}
