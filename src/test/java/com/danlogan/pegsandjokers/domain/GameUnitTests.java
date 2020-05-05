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
	
	@Test
	public void testStartAPegTurn() throws PlayerNotFoundException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
		Card cardToPlay = new Card(CardRank.ACE, Suit.CLUBS);
		
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.START_A_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.build();

		game.takeTurn(turn);
		
		assertThat(game.getPlayerPosition(1,1).getPlayerBoardPosition().getId()).isEqualTo("RED-8");
	}
	
	@Test
	public void testMovePegForwardTurn() throws PlayerNotFoundException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
	Card cardToPlay = new Card(CardRank.THREE, Suit.CLUBS);
		
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"RED-8")
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.MOVE_PEG_FORWARD)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.build();
		
		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPosition().getId()).isEqualTo("RED-8");

		game.takeTurn(turn);

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPosition().getId()).isEqualTo("RED-11");
		
		assertThat(game.getBoard().getBoardPositionById("RED-8").getHasPeg()).isFalse();
		

	}
	
	@Test
	public void testCantLandOnOwnPeg() throws PlayerNotFoundException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
	Card cardToPlay = new Card(CardRank.ACE, Suit.CLUBS);
		
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"RED-8")
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.START_A_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(2)
				.build();
		
		try {
			game.takeTurn(turn);
			assert(false);
		}
		catch (CannotMoveToAPositionYouOccupyException e)
		{
			assertThat(e.getMessage()).isNotBlank();
		}

	}
}
