package com.danlogan.pegsandjokers.ai;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.danlogan.pegsandjokers.domain.Card;
import com.danlogan.pegsandjokers.domain.CardRank;
import com.danlogan.pegsandjokers.domain.Game;
import com.danlogan.pegsandjokers.domain.PlayerHand;
import com.danlogan.pegsandjokers.domain.Suit;

public class AITests {

	@Test
	public void testStartZoneOptionsAtBeginningofGameWithOnlyOneStartableCard()
	{
		//create default game (default is 4 players)
		//use a hand that has only one startable card
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(new Card(CardRank.QUEEN, Suit.HEARTS))
				.withCard(new Card(CardRank.TWO, Suit.HEARTS))
				.withCard(new Card(CardRank.THREE, Suit.HEARTS))
				.withCard(new Card(CardRank.FOUR, Suit.HEARTS))
				.withCard(new Card(CardRank.FIVE, Suit.HEARTS))
				.build();

		
		Game game = Game.Builder.newInstance()
					.withPlayerHand(playerHand)
					.build();
		
		TacticalAI ai = new TacticalAI(game,1,1); //create tactical AI for player 1, peg 1
		
		assertThat(ai.moveOptions().size()).isEqualTo(1);
		
		assertThat(ai.moveOptions().get(0).getResultingBoardPositionID()).isEqualTo("Tomato-8");
		assertThat(ai.moveOptions().get(0).getSuggestedCard()).isEqualTo("QUEEN of HEARTS");

	}
}
