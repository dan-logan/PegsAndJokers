package com.danlogan.pegsandjokers.ai;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import com.danlogan.pegsandjokers.domain.BoardPosition;
import com.danlogan.pegsandjokers.domain.Card;
import com.danlogan.pegsandjokers.domain.CardRank;
import com.danlogan.pegsandjokers.domain.MoveType;
import com.danlogan.pegsandjokers.domain.PlayerHand;
import com.danlogan.pegsandjokers.domain.PlayerNotFoundException;
import com.danlogan.pegsandjokers.domain.PlayerPosition;
import com.danlogan.pegsandjokers.domain.PlayerTurn;

public class StartZoneTacticalStrategy extends TacticalStrategy {

	public StartZoneTacticalStrategy(Builder builder) {
		super(builder);
	}

	@Override
	public List<MoveOption> moveOptions() {
		List<MoveOption> moveOptions = super.moveOptions();
		
		TacticalAI ai = this.getTacticalAI();
	
		BoardPosition comeOutPosition = ai.getGame().getBoard().getComeOutPositionForPlayerNumber(ai.getPlayerNumber());
		
		if (! comeOutPosition.getHasPeg()) //if come out spot available check cards to see if startable card available
		{
			PlayerHand hand = null;
			try {
				hand = ai.getGame().getPlayerHand(ai.getPlayerNumber());
			}
			catch (PlayerNotFoundException ex)
			{
				throw new RuntimeException("Cannot get moveOptions since Tactical AI has invalid player");
			}
			
			Stream<Card> playableCards = hand.getCards().stream().filter(card -> card.canBeUsedToStart()).sorted(new StartZoneCardPriorityComparator());
			
			playableCards.forEachOrdered(card ->{
			
				PlayerTurn turn = PlayerTurn.Builder.newInstance()
						.withCardName(card.getName())
						.withMoveType(MoveType.START_A_PEG)
						.withPlayerNumber(ai.getPegNumber())
						.withPositionNumber(ai.getPegNumber())
						.build();
			
				MoveOption option = new MoveOption(turn, comeOutPosition.getId());
				moveOptions.add(option);
				
			});
			
		}
		
		return moveOptions;
	}

	static class StartZoneCardPriorityComparator implements Comparator<Card> {
		static List<CardRank> cardRankPriority = List.of(CardRank.JACK, CardRank.QUEEN, CardRank.KING, CardRank.ACE);

		@Override
		public int compare(Card card1, Card card2) {
			
			return Integer.valueOf(cardRankPriority.indexOf(card1.getRank()))
					.compareTo(Integer.valueOf(cardRankPriority.indexOf(card2.getRank())));
		}}
}