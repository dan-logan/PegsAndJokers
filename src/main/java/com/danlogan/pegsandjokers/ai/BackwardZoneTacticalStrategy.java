package com.danlogan.pegsandjokers.ai;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.danlogan.pegsandjokers.domain.Board;
import com.danlogan.pegsandjokers.domain.Card;
import com.danlogan.pegsandjokers.domain.CardRank;
import com.danlogan.pegsandjokers.domain.Game;
import com.danlogan.pegsandjokers.domain.MoveType;
import com.danlogan.pegsandjokers.domain.PlayerHand;
import com.danlogan.pegsandjokers.domain.PlayerNotFoundException;
import com.danlogan.pegsandjokers.domain.PlayerTurn;

import lombok.extern.java.Log;

@Log
public class BackwardZoneTacticalStrategy extends TacticalStrategy {

	public BackwardZoneTacticalStrategy(Builder builder) {
		super(builder);
	}

	//Overall strategy in BackwardZone is use an eight if you can, else try to split a 9, else move forward if possible
	@Override
	public List<MoveOption> moveOptions() {
		
		List<MoveOption> moveOptions = super.moveOptions();
		
		TacticalAI ai = this.getTacticalAI();
		Game game = ai.getGame();
		int playerNumber = ai.getPlayerNumber();
		int pegNumber = ai.getPegNumber();
		String cardToUse = null;
		Board board = game.getBoard();
		
		//If have an eight use that
		PlayerHand hand = getPlayerHand();
		
		log.info("Evaluating player hand in backward zone: " + hand.toString());
		
		Optional<Card> eight = hand.getCards().stream()
				.filter(card -> !card.getName().equals("JOKER"))  //filter out the jokers because they have no rank
				.filter(card -> card.getRank().equals(CardRank.EIGHT)).findFirst();
		
		if (eight.isPresent())
		{
			cardToUse = eight.get().getName();
			
			PlayerTurn turn = PlayerTurn.Builder.newInstance()
					.withCardName(cardToUse)
					.withMoveType(MoveType.MOVE_PEG)
					.withMoveDistance(-8)
					.withPlayerNumber(ai.getPlayerNumber())
					.withPositionNumber(ai.getPegNumber())
					.build();
			
			String currentPositionId = game.getPlayerPosition(playerNumber, pegNumber).getPlayerBoardPositionId();
			String targetPositionId = board.getBoardPositionWithOffset(board.getBoardPositionById(currentPositionId),-8).getId();
			
			MoveOption option = new MoveOption(turn, targetPositionId);
			moveOptions.add(option);
		
		}
		
		return moveOptions;
	}

}
