package com.danlogan.pegsandjokers.ai;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

import com.danlogan.pegsandjokers.domain.Game;
import com.danlogan.pegsandjokers.domain.MoveType;
import com.danlogan.pegsandjokers.domain.PlayerNotFoundException;
import com.danlogan.pegsandjokers.domain.PlayerTurn;

public class StrategicAI {
	
	private Game game;
	private int playerNumber;
	
	private ArrayList<TacticalAI> tacticalAIs = new ArrayList<TacticalAI>();

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
		
		PlayerTurn turn = null;
		//to do.. prioritize cards
		try 
		{
		 turn = PlayerTurn.Builder.newInstance()
					.withMoveType(MoveType.DISCARD)
					.withCardName(this.game.getPlayerHand(this.playerNumber).getCard(1).getName())
					.withPlayerNumber(this.playerNumber)
					.build();
		}
		catch (PlayerNotFoundException ex)
		{
			throw new RuntimeException("Strategic AI failed to generate BurnACard turn due to invalid player number.");
		}
		
		return turn;
	}

}
