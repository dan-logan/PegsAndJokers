package com.danlogan.pegsandjokers.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class GameUnitTests {
	
	@Test
	void aSimpleTestShouldWork()
	{
		boolean  aBool = true;
		assert(aBool);
	}
	
	@Test
	void testDefaultGameSetup() throws PlayerNotFoundException
	{
		Game game = Game.Builder.newInstance().build();
		
		assertThat(game.getPlayers().size()).isEqualTo(3);
		assertThat(game.getPlayers().get(0).getName()).isEqualTo("Player 1");
		assertThat(game.getPlayers().get(1).getName()).isEqualTo("Player 2");
		assertThat(game.getPlayers().get(2).getName()).isEqualTo("Player 3");
		
		assertThat(game.getBoard().getPlayerSides().size()).isEqualTo(3);
		
		assertThat(game.getPlayerPositions(1).size()).isEqualTo(5);
		assertThat(game.getPlayerPositions(1).get(0).getPlayerBoardPosition().isStartPosition());
		assertThat(game.getPlayerPositions(1).get(1).getPlayerBoardPosition().isStartPosition());
		assertThat(game.getPlayerPositions(1).get(2).getPlayerBoardPosition().isStartPosition());
		assertThat(game.getPlayerPositions(1).get(3).getPlayerBoardPosition().isStartPosition());
		assertThat(game.getPlayerPositions(1).get(4).getPlayerBoardPosition().isStartPosition());

		assertThat(game.getCardsRemaining()).isEqualTo(108);
		
	}
	
	@Test
	void testBuildGameWithSpecificPlayerNames()
	{
		Game game = Game.Builder.newInstance()
				.withPlayerNamed("Player 1")
				.withPlayerNamed("Player 2")
				.build();

		assertThat(game.getPlayers().get(0).getName()).isEqualTo("Player 1");
		assertThat(game.getPlayers().get(0).getNumber()).isEqualTo(1);
		assertThat(game.getPlayers().get(1).getName()).isEqualTo("Player 2");
		assertThat(game.getPlayers().get(1).getNumber()).isEqualTo(2);

	}

	@Test
	void testBuildGameWithSpecificPlayerHands() throws PlayerNotFoundException
	{
		
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
												.withCard(new Card(CardRank.ACE, Suit.CLUBS))
												.build();

		Game game = Game.Builder.newInstance()
						.withPlayerHand(playerHand)
						.build();
		
		assertThat(game.getPlayerHand(1).getCards().size()).isEqualTo(1);
		assertThat(game.getPlayerHand(1).getCard("ACE of CLUBS")).isNotNull();
	}
	
	@Test
	void testDiscardTurn() throws PlayerNotFoundException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
									PlayerPositionNotFoundException
	{
		Game game = Game.Builder.newInstance().build();
		
		game.deal();
		int deckSizeBeforeTurn = game.getCardsRemaining();
		
		Card cardToDiscard = game.getPlayerHand(1).getCard(1);
	
		PlayerTurn turn = PlayerTurn.Builder.newInstance()
								.withCardName(cardToDiscard.getName())
								.withMoveType(MoveType.DISCARD)
								.withPlayerNumber(1)
								.build();

		game.takeTurn(turn);
		
		assertThat(game.getPlayerHand(1).getCard(cardToDiscard.getName())).isNull();
		assertThat(deckSizeBeforeTurn - game.getCardsRemaining()).isEqualTo(1);
		
	}
}
