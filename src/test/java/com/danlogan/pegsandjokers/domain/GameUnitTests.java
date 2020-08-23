package com.danlogan.pegsandjokers.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.danlogan.pegsandjokers.infrastructure.GameEventListener;
import com.danlogan.pegsandjokers.infrastructure.GameNotFoundException;
import com.danlogan.pegsandjokers.infrastructure.GameRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		
		assertThat(game.getPlayers().size()).isEqualTo(4);
		assertThat(game.getPlayers().get(0).getName()).isEqualTo("Player 1");
		assertThat(game.getPlayers().get(1).getName()).isEqualTo("Player 2");
		assertThat(game.getPlayers().get(2).getName()).isEqualTo("Player 3");
		assertThat(game.getPlayers().get(3).getName()).isEqualTo("Player 4");
		
		assertThat(game.getBoard().getPlayerSides().size()).isEqualTo(4);
		
		assertThat(game.getPlayerPositions(1).size()).isEqualTo(5);
		assertThat(game.getBoard().isStartPosition(game.getPlayerPositions(1).get(0).getPlayerBoardPositionId()));
		assertThat(game.getBoard().isStartPosition(game.getPlayerPositions(1).get(1).getPlayerBoardPositionId()));
		assertThat(game.getBoard().isStartPosition(game.getPlayerPositions(1).get(2).getPlayerBoardPositionId()));
		assertThat(game.getBoard().isStartPosition(game.getPlayerPositions(1).get(3).getPlayerBoardPositionId()));
		assertThat(game.getBoard().isStartPosition(game.getPlayerPositions(1).get(4).getPlayerBoardPositionId()));
		assertThat(game.getCardsRemaining()).isEqualTo(162);
		
	}
	
	@Test
	void testBuildGameWithSpecificPlayerNames()
	{
		Game game = Game.Builder.newInstance()
				.withPlayerNamed("Dan")
				.withPlayerNamed("Topher")
				.build();

		assertThat(game.getPlayers().get(0).getName()).isEqualTo("Dan");
		assertThat(game.getPlayers().get(0).getNumber()).isEqualTo(1);
		assertThat(game.getPlayers().get(1).getName()).isEqualTo("Topher");
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
	void testDiscardTurn() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
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
	public void testStartAPegTurn() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
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

		assertThat(game.getPlayerPosition(1,1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
		assertThat(game.getBoard().getBoardPositionById("Tomato-8").getPegColor()).isEqualTo(Color.Tomato);
		
	}
	
	@Test
	public void testMovePegForwardTurn() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
	Card cardToPlay = new Card(CardRank.THREE, Suit.CLUBS);
		
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"Tomato-8")
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.MOVE_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.withMoveDistance(3)
				.build();
		
		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");

		game.takeTurn(turn);

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-11");
		
		assertThat(game.getBoard().getBoardPositionById("Tomato-8").getHasPeg()).isFalse();
		

	}
	
	@Test
	public void testCannotMovePastYourOwnPeg() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
	Card cardToPlay = new Card(CardRank.THREE, Suit.CLUBS);
		
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"Tomato-8")
				.withPlayerPosition(1, 2, "Tomato-9")
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.MOVE_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.withMoveDistance(3)
				.build();
		
		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");

		try {
			game.takeTurn(turn);
			assert(false);
		}
		catch (CannotMoveToAPositionYouOccupyException e)
		{
			assertThat(e.getMessage()).contains("cannot move over a position with one of your own pegs in it");
		}

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
		
	}
	
	@Test
	public void testUseEightFromReadyToGoHomeSpot() throws PlayerNotFoundException, InvalidMoveException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
		Card cardToPlay = new Card(CardRank.EIGHT, Suit.CLUBS);
		
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"TomatoHome-5")
				.withPlayerPosition(1, 2, "TomatoHome-4")
				.withPlayerPosition(1, 3, "Tomato-3")
				.withPlayerPosition(1, 4, "TomatoHome-3")
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.MOVE_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(3)
				.withMoveDistance(-8)
				.build();
		
		game.takeTurn(turn);
		assertThat(game.getPlayerPosition(1, 3).getPlayerBoardPositionId()).isEqualTo("PINK-13");


	}

	@Test
	public void testCantLandOnOwnPeg() throws PlayerNotFoundException, InvalidMoveException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
	Card cardToPlay = new Card(CardRank.ACE, Suit.CLUBS);
		
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"Tomato-8")
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
	
	@Test
	public void testCannotMoveBackward() throws PlayerNotFoundException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
		Card cardToPlay = new Card(CardRank.THREE, Suit.CLUBS);

		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"Tomato-8")
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.MOVE_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.withMoveDistance(-3)
				.build();

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");

		try {
			game.takeTurn(turn);
			assert(false);
		}
		catch (InvalidMoveException e)
		{
			assertThat(e.getMessage()).contains("backward");
		}

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");

	}

	@Test
	public void testCannotMoveForward() throws PlayerNotFoundException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
		Card cardToPlay = new Card(CardRank.EIGHT, Suit.CLUBS);

		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"Tomato-8")
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.MOVE_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.withMoveDistance(8)
				.build();

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");

		try {
			game.takeTurn(turn);
			assert(false);
		}
		catch (InvalidMoveException e)
		{
			assertThat(e.getMessage()).contains("forward");
		}

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");

	}

	@Test
	public void testMoveEightBackwards() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
		Card cardToPlay = new Card(CardRank.EIGHT, Suit.CLUBS);

		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"Tomato-7")
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.MOVE_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.withMoveDistance(-8)
				.build();

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-7");

		game.takeTurn(turn);

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("PINK-17");

	}

	@Test
	public void testCannotMoveWrongDistance() throws PlayerNotFoundException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
		Card cardToPlay = new Card(CardRank.EIGHT, Suit.CLUBS);

		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"Tomato-8")
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.MOVE_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.withMoveDistance(-4)
				.build();

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");

		try {
			game.takeTurn(turn);
			assert(false);
		}
		catch (InvalidMoveException e)
		{
			assertThat(e.getMessage()).contains("invalid distance");
		}

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");

	}
	
	@Test
	public void testSendOpponentBackToStart() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
	Card cardToPlay = new Card(CardRank.QUEEN, Suit.CLUBS);
		
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(2,1,"Tomato-8")
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.START_A_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.build();
		
		assertThat(game.getPlayerPosition(2, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");

		game.takeTurn(turn);

		assertThat(game.getBoard().isStartPosition(game.getPlayerPosition(2, 1).getPlayerBoardPositionId())).isTrue();
		
	}

	@Test
	public void testSplitASeven() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
		Card cardToPlay = new Card(CardRank.SEVEN, Suit.CLUBS);

		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"Tomato-8")
				.withPlayerPosition(1,2,"LightBlue-1")
				.build();

		int[] splitMoveArray = {1,3,2,4};
		
		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.SPLIT_MOVE)
				.withPlayerNumber(1)
				.withSplitMoveArray(splitMoveArray)
				.build();

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
		assertThat(game.getPlayerPosition(1, 2).getPlayerBoardPositionId()).isEqualTo("LightBlue-1");

		game.takeTurn(turn);

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-11");
		assertThat(game.getPlayerPosition(1, 2).getPlayerBoardPositionId()).isEqualTo("LightBlue-5");

	}

	@Test
	public void testSevenSplitMustAddToSeven() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
		Card cardToPlay = new Card(CardRank.SEVEN, Suit.CLUBS);

		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"Tomato-8")
				.withPlayerPosition(1,2,"LightBlue-1")
				.build();

		int[] splitMoveArray = {1,3,2,3};
		
		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.SPLIT_MOVE)
				.withPlayerNumber(1)
				.withSplitMoveArray(splitMoveArray)
				.build();

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
		assertThat(game.getPlayerPosition(1, 2).getPlayerBoardPositionId()).isEqualTo("LightBlue-1");

		try {
			game.takeTurn(turn);
			assert(false);
		}
		catch (InvalidMoveException e)
		{
			assertThat(e.getMessage()).contains("cannot split a SEVEN");
		}

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
		assertThat(game.getPlayerPosition(1, 2).getPlayerBoardPositionId()).isEqualTo("LightBlue-1");

	}
	

	@Test
	public void testRollbackSplitMoveOnSecondPegError() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
		Card cardToPlay = new Card(CardRank.SEVEN, Suit.CLUBS);

		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"Tomato-9")
				.withPlayerPosition(1,2,"Tomato-8")
				.build();

		int[] splitMoveArray = {1,3,2,4};
		
		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.SPLIT_MOVE)
				.withPlayerNumber(1)
				.withSplitMoveArray(splitMoveArray)
				.build();

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-9");
		assertThat(game.getPlayerPosition(1, 2).getPlayerBoardPositionId()).isEqualTo("Tomato-8");

		try {
			game.takeTurn(turn);
			assert(false);
		}
		catch (CannotMoveToAPositionYouOccupyException e)
		{
			assertThat(e.getMessage()).contains("with one of your own pegs in it");
		}

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-9");
		assertThat(game.getPlayerPosition(1, 2).getPlayerBoardPositionId()).isEqualTo("Tomato-8");

	}

	@Test
	public void testCannotSplitAnEight() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
		Card cardToPlay = new Card(CardRank.EIGHT, Suit.CLUBS);

		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"Tomato-8")
				.withPlayerPosition(1,2,"LightBlue-1")
				.build();

		int[] splitMoveArray = {1,3,2,3};
		
		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.SPLIT_MOVE)
				.withPlayerNumber(1)
				.withSplitMoveArray(splitMoveArray)
				.build();

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
		assertThat(game.getPlayerPosition(1, 2).getPlayerBoardPositionId()).isEqualTo("LightBlue-1");

		try {
			game.takeTurn(turn);
			assert(false);
		}
		catch (InvalidMoveException e)
		{
			assertThat(e.getMessage()).contains("cannot split");
		}

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
		assertThat(game.getPlayerPosition(1, 2).getPlayerBoardPositionId()).isEqualTo("LightBlue-1");

	}

	@Test
	public void testMustSplitANine() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
		Card cardToPlay = new Card(CardRank.NINE, Suit.CLUBS);

		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"Tomato-8")
				.build();

	
		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.MOVE_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.withMoveDistance(9)
				.build();

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
	
		try {
			game.takeTurn(turn);
			assert(false);
		}
		catch (InvalidMoveException e)
		{
			assertThat(e.getMessage()).contains("9 is an invalid distance");
		}

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");

	}
	
	@Test
	public void testNineMustMoveOneForwardOneBackward() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
		Card cardToPlay = new Card(CardRank.NINE, Suit.CLUBS);

		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"Tomato-8")
				.withPlayerPosition(1,2,"Tomato-9")
				.build();

		int[] splitMoveArray = {2,6,1,3};
		
		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.SPLIT_MOVE)
				.withPlayerNumber(1)
				.withSplitMoveArray(splitMoveArray)
				.build();

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
		assertThat(game.getPlayerPosition(1, 2).getPlayerBoardPositionId()).isEqualTo("Tomato-9");
	
		try {
			game.takeTurn(turn);
			assert(false);
		}
		catch (InvalidMoveException e)
		{
			assertThat(e.getMessage()).contains("cannot split");
		}

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
		assertThat(game.getPlayerPosition(1, 2).getPlayerBoardPositionId()).isEqualTo("Tomato-9");

	}

	@Test
	public void testSplitNineForwardOneBackward() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
		Card cardToPlay = new Card(CardRank.NINE, Suit.CLUBS);

		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		GameEventListener listener = new GameEventListener();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"Tomato-8")
				.withPlayerPosition(1,2,"Tomato-9")
				.withEventListener(listener)
				.build();
		
		game.start();

		int[] splitMoveArray = {2,3,1,-6};
		
		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.SPLIT_MOVE)
				.withPlayerNumber(1)
				.withSplitMoveArray(splitMoveArray)
				.build();

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
		assertThat(game.getPlayerPosition(1, 2).getPlayerBoardPositionId()).isEqualTo("Tomato-9");
	
		game.takeTurn(turn);

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-2");
		assertThat(game.getPlayerPosition(1, 2).getPlayerBoardPositionId()).isEqualTo("Tomato-12");

	}

	@Test
	public void testSplitNineToGoHome() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException, JsonProcessingException, GameNotFoundException
	{
		Card cardToPlay = new Card(CardRank.NINE, Suit.CLUBS);

		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"TomatoHome-5")
				.withPlayerPosition(1,2,"TomatoHome-4")
				.withPlayerPosition(1,3,"LightBlue-4")
				.withPlayerPosition(1,4,"Tomato-3")
				.build();

		game.start();
		String gameId = game.getId().toString();
		
		GameRepository repository = new GameRepository();
		
		repository.addGame(game);
		
		game = repository.findGameById(gameId);
		
		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("TomatoHome-5");
		assertThat(game.getPlayerPosition(1, 2).getPlayerBoardPositionId()).isEqualTo("TomatoHome-4");
		assertThat(game.getPlayerPosition(1, 4).getPlayerBoardPositionId()).isEqualTo("Tomato-3");
		assertThat(game.getPlayerPosition(1, 3).getPlayerBoardPositionId()).isEqualTo("LightBlue-4");
	
		int[] splitMoveArray = {4,3,3,-6};

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.SPLIT_MOVE)
				.withPlayerNumber(1)
				.withSplitMoveArray(splitMoveArray)
				.build();

		game.takeTurn(turn);

		assertThat(game.getPlayerPosition(1, 4).getPlayerBoardPositionId()).isEqualTo("TomatoHome-3");
		assertThat(game.getPlayerPosition(1, 3).getPlayerBoardPositionId()).isEqualTo("Tomato-16");

	}

	@Test
	public void testUseJokerToReplaceOpponentPeg() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
		Card cardToPlay = new Joker();

		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(2,1,"Tomato-8")
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.USE_JOKER)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.withTargetBoardPositionId("Tomato-8")
				.build();

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("TomatoStart-1");
		assertThat(game.getPlayerPosition(2, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
	
		game.takeTurn(turn);

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
		assertThat(game.getPlayerPosition(2, 1).getPlayerBoardPositionId()).isEqualTo("LightBlueStart-1");

	}

	@Test
	public void testCannotUseAnotherCardAsJoker() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
		Card cardToPlay = new Card(CardRank.NINE, Suit.CLUBS);

		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(2,1,"LightBlue-9")
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.USE_JOKER)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.withTargetBoardPositionId("LightBlue-9")
				.build();

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("TomatoStart-1");
		assertThat(game.getPlayerPosition(2, 1).getPlayerBoardPositionId()).isEqualTo("LightBlue-9");
	
		try {
			game.takeTurn(turn);
			assert(false);
		}
		catch (InvalidMoveException e)
		{
			assertThat(e.getMessage()).contains("is not a joker");
		}

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("TomatoStart-1");
		assertThat(game.getPlayerPosition(2, 1).getPlayerBoardPositionId()).isEqualTo("LightBlue-9");

	}

	@Test
	public void testCannotMoveJokerToEmptyPosition() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
		Card cardToPlay = new Joker();

		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.USE_JOKER)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.withTargetBoardPositionId("LightBlue-9")
				.build();

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("TomatoStart-1");
	
		try {
			game.takeTurn(turn);
			assert(false);
		}
		catch (InvalidMoveException e)
		{
			assertThat(e.getMessage()).contains("There is not a peg in position");
		}

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("TomatoStart-1");
	
	}

	@Test
	public void testCannotMoveJokerToStartPosition() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
		Card cardToPlay = new Joker();

		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withNumberOfPlayers(3)
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.USE_JOKER)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.withTargetBoardPositionId("LightBlueStart-1")
				.build();

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("TomatoStart-1");
	
		try {
			game.takeTurn(turn);
			assert(false);
		}
		catch (InvalidMoveException e)
		{
			assertThat(e.getMessage()).contains("Cannot replace opponent peg if it is not on main track");
		}

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("TomatoStart-1");
	
	}

	@Test
	public void testCannotUseJokerToReplaceYourOwnPeg() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
		Card cardToPlay = new Joker();

		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,2,"LightBlue-9")
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.USE_JOKER)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.withTargetBoardPositionId("LightBlue-9")
				.build();

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("TomatoStart-1");
		assertThat(game.getPlayerPosition(1, 2).getPlayerBoardPositionId()).isEqualTo("LightBlue-9");
	
		try {
			game.takeTurn(turn);
			assert(false);
		}
		catch (InvalidMoveException e)
		{
			assertThat(e.getMessage()).contains("replace your own peg");
		}

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("TomatoStart-1");
		assertThat(game.getPlayerPosition(1, 2).getPlayerBoardPositionId()).isEqualTo("LightBlue-9");

	}

	@Test
	public void testMovePlayerIntoHome() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
	Card cardToPlay = new Card(CardRank.THREE, Suit.CLUBS);
		
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"Tomato-3")
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.MOVE_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.withMoveDistance(3)
				.build();
		
		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-3");
	
		game.takeTurn(turn);

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("TomatoHome-3");
		assertThat(game.getBoard().getBoardPositionById(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isHomePosition()).isTrue();
		
	}

	
	@Test
	public void testMovePastHomeWhenMovingToFar() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
	Card cardToPlay = new Card(CardRank.TEN, Suit.CLUBS);
		
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"Tomato-2")
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.MOVE_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.withMoveDistance(10)
				.build();
		
		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-2");
	
		game.takeTurn(turn);

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-12");
		
	}

	@Test
	public void testMoveWithinHomeTrack() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
	Card cardToPlay = new Card(CardRank.TWO, Suit.CLUBS);
		
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"TomatoHome-2")
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.MOVE_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.withMoveDistance(2)
				.build();
		
		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("TomatoHome-2");
	
		game.takeTurn(turn);

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("TomatoHome-4");
		
	}

	@Test
	public void testPegWithinHomeTrackCannotMovePastEnd() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
	Card cardToPlay = new Card(CardRank.SIX, Suit.CLUBS);
		
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"TomatoHome-2")
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.MOVE_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.withMoveDistance(6)
				.build();
		
		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("TomatoHome-2");
	
		System.out.println("Testing cannot move past end of home when starting from within home");
		try
		{
			game.takeTurn(turn);
			assert(false);
		}
		catch (InvalidMoveException ex)
		{
			assertThat(ex.getMessage()).contains("past end of home");
		}

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("TomatoHome-2");
		
	}
	

	@Test
	public void testCannotPassYourselfInHomeWhenFirstHomeFull() throws PlayerNotFoundException, InvalidMoveException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException,
	PlayerPositionNotFoundException
	{
	Card cardToPlay = new Card(CardRank.THREE, Suit.CLUBS);
		
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1,1,"Tomato-3")
				.withPlayerPosition(1, 2, "TomatoHome-1")
				.build();

		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.MOVE_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.withMoveDistance(3)
				.build();
		
		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-3");
	
		game.takeTurn(turn);

		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-6");
			
	}

	@Test
	public void testGamesWithDifferentNumberOfPlayers()
	{
	
//		Game gameWith4Players = Game.Builder.newInstance().withNumberOfPlayers(4).build();
	//	assertThat(gameWith4Players.getPlayers().size()).isEqualTo(4);
		
		Game gameWith8Players = Game.Builder.newInstance()
				.withNumberOfPlayers(8)
				.build();
		assertThat(gameWith8Players.getPlayers().size()).isEqualTo(8);

	}
	
	@Test
	public void testUseTheRightNumberOfDecks()
	{
		Game gameWith4Players = Game.Builder.newInstance()
				.withNumberOfPlayers(4)
				.build();
		
		assertThat(gameWith4Players.getCardsRemaining()).isEqualTo(162);
	
		Game gameWith6Players = Game.Builder.newInstance()
				.withNumberOfPlayers(6)
				.build();
		
		assertThat(gameWith6Players.getCardsRemaining()).isEqualTo(162);


		Game gameWith8Players = Game.Builder.newInstance()
				.withNumberOfPlayers(8)
				.build();
		
		assertThat(gameWith8Players.getCardsRemaining()).isEqualTo(216);

		
	}
	
	@Test
	public void testPlayerHandDiscard() throws InvalidMoveException
	{
		PlayerHand hand1 = PlayerHand.Builder.newInstance(1)
		.withCard(new Card(CardRank.ACE, Suit.CLUBS))
		.withCard(new Card(CardRank.TWO, Suit.CLUBS))
		.build();
		
		ArrayList<Card> discardPile = new ArrayList<Card>();
		
		hand1.discardCard(discardPile, "ACE of CLUBS");
		
		assertThat(hand1.getCards().size()).isEqualTo(1);
		assertThat(hand1.getCard("ACE of CLUBS")).isNull();
		
		//Now make sure error handling is correct
		try
		{
			hand1.discardCard(discardPile, null);
			assert(false);
		}
		catch (InvalidMoveException ex)
		{
			assertThat(ex.getMessage()).contains("null cannot be discarded");
		}
		
	}
	
	@Test
	public void testReshuffleFromDiscardPileWhenDrawPileDepleted() throws PlayerNotFoundException, InvalidGameStateException, InvalidMoveException, PlayerPositionNotFoundException, CannotMoveToAPositionYouOccupyException
	{
		ArrayList<Card> drawPile = new ArrayList<Card>();
		
		for (int i=1;i<23;i++)
		{
			drawPile.add(new Card(CardRank.ACE,Suit.CLUBS));
		}
		
		DeckOfCards drawPileDeck = new DeckOfCards(drawPile);
		
		Game game = Game.Builder.newInstance()
				.withDrawPile(drawPileDeck)
				.build();
		
		assertThat(game.getCardsRemaining()).isEqualTo(22);
		assertThat(game.getDiscardPileCount()).isEqualTo(0);
		
		game.deal();
		
		assertThat(game.getCardsRemaining()).isEqualTo(2);
		assertThat(game.getDiscardPileCount()).isEqualTo(0);
		
		//Now each player has 5 cards, the drawPile has 2 cards and nothing in the discard pile
		//when we have a player discard the drawPile should go to 1 and the discard pile should go to 1
		
		PlayerTurn turn = PlayerTurn.Builder.newInstance()
							.withPlayerNumber(1)
							.withCardName("ACE of CLUBS")
							.withMoveType(MoveType.DISCARD)
							.build();
		
		game.takeTurn(turn);
		
		assertThat(game.getCardsRemaining()).isEqualTo(1);
		assertThat(game.getDiscardPileCount()).isEqualTo(1);
	
		//Now each player has 5 cards, the drawPile has 1 cards and discard pile has 1 card
		//when we have a player discard the last card from the  drawPile,
		//we want the discardPile to be put back in drawPile so drawpile should go to 2 and the discard pile should go to 0

		PlayerTurn turn2 = PlayerTurn.Builder.newInstance()
				.withPlayerNumber(2)
				.withCardName("ACE of CLUBS")
				.withMoveType(MoveType.DISCARD)
				.build();

		game.takeTurn(turn2);

		assertThat(game.getCardsRemaining()).isEqualTo(2);
		assertThat(game.getDiscardPileCount()).isEqualTo(0);

		
	}
	
	
	@Test
	public void testTrackFirstBurnedCard() throws PlayerNotFoundException, InvalidGameStateException, InvalidMoveException, PlayerPositionNotFoundException, CannotMoveToAPositionYouOccupyException
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

		assertThat(game.getPlayerHand(1).getBurnedCardCount()).isEqualTo(0);
		
		game.takeTurn(turn);
		
		assertThat(game.getPlayerHand(1).getCard(cardToDiscard.getName())).isNull();
		assertThat(deckSizeBeforeTurn - game.getCardsRemaining()).isEqualTo(1);
		assertThat(game.getPlayerHand(1).getBurnedCardCount()).isEqualTo(1);
		
	}
	
	@Test
	public void testTrackSecondBurnedCard() throws PlayerNotFoundException, InvalidGameStateException, InvalidMoveException, PlayerPositionNotFoundException, CannotMoveToAPositionYouOccupyException
	{
		PlayerHand hand = PlayerHand.Builder.newInstance(1)
							.withBurnedCardCount(1)
							.withCard(new Card(CardRank.ACE, Suit.CLUBS))
							.build();
		
		Game game = Game.Builder.newInstance()
				.withPlayerHand(hand)
				.build();
		
		game.deal();
		int deckSizeBeforeTurn = game.getCardsRemaining();
		
		Card cardToDiscard = game.getPlayerHand(1).getCard(1);
	
		PlayerTurn turn = PlayerTurn.Builder.newInstance()
								.withCardName(cardToDiscard.getName())
								.withMoveType(MoveType.DISCARD)
								.withPlayerNumber(1)
								.build();

		assertThat(game.getPlayerHand(1).getBurnedCardCount()).isEqualTo(1);
		
		game.takeTurn(turn);
		
		assertThat(game.getPlayerHand(1).getCard(cardToDiscard.getName())).isNull();
		assertThat(deckSizeBeforeTurn - game.getCardsRemaining()).isEqualTo(1);
		assertThat(game.getPlayerHand(1).getBurnedCardCount()).isEqualTo(2);
		
	}
	
	@Test
	public void testUseFreeStartWith2BurnedCards() throws PlayerNotFoundException, InvalidGameStateException, InvalidMoveException, PlayerPositionNotFoundException, CannotMoveToAPositionYouOccupyException
	{
		PlayerHand hand = PlayerHand.Builder.newInstance(1)
				.withBurnedCardCount(2)
				.withCard(new Card(CardRank.ACE, Suit.CLUBS))
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(hand)
				.build();

		game.deal();
	
		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withMoveType(MoveType.FREE_START)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.withCardName("ACE of CLUBS")
				.build();

		game.takeTurn(turn);
		
		assertThat(game.getPlayerPosition(1,1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
		assertThat(game.getPlayerView(1).getLastCardPlayed()).isNull();
		assertThat(game.getPlayerHand(1).getCard("ACE of CLUBS")).isNotNull();

		
	}
	
	@Test
	public void testGameWith2Players()
	{
		
		Game game = Game.Builder.newInstance()
				.withNumberOfPlayers(2)
				.build();

		game.deal();
	
		assertThat(game.getPlayers().size()).isEqualTo(2);
		
	}
	
	@Test
	public void testMovePastHomeWhenHomePartiallyFull() throws PlayerNotFoundException, InvalidGameStateException, InvalidMoveException, PlayerPositionNotFoundException, CannotMoveToAPositionYouOccupyException
	{
		PlayerHand hand = PlayerHand.Builder.newInstance(1)
							.withCard(new Card(CardRank.SIX, Suit.HEARTS))
							.build();
		
		Game game = Game.Builder.newInstance()
					.withNumberOfPlayers(2)
					.withPlayerPosition(1, 1, "TomatoHome-2")
					.withPlayerPosition(1, 2, "TomatoHome-5")
					.withPlayerPosition(1, 3, "TomatoHome-3")
					.withPlayerPosition(1, 4, "TomatoHome-1")
					.withPlayerPosition(1, 5, "LightBlue-18")
					.withPlayerHand(hand)
					.build();
		
		game.deal();
		
		PlayerTurn turn = PlayerTurn.Builder.newInstance()
							.withCardName("SIX of HEARTS")
							.withMoveType(MoveType.MOVE_PEG)
							.withMoveDistance(6)
							.withPlayerNumber(1)
							.withPositionNumber(5)
							.build();
		
		game.takeTurn(turn);
		
		assertThat(game.getPlayerPosition(1, 5).getPlayerBoardPositionId()).isEqualTo("Tomato-6");
		
	}
	
	@Test
	public void testMovePastHomeWhenHomePartiallyFull2() throws PlayerNotFoundException, InvalidGameStateException, InvalidMoveException, PlayerPositionNotFoundException, CannotMoveToAPositionYouOccupyException
	{
		PlayerHand hand = PlayerHand.Builder.newInstance(1)
							.withCard(new Card(CardRank.JACK, Suit.HEARTS))
							.build();
		
		Game game = Game.Builder.newInstance()
					.withNumberOfPlayers(2)
					.withPlayerPosition(1, 2, "TomatoHome-5")
					.withPlayerPosition(1, 3, "TomatoHome-4")
					.withPlayerPosition(1, 5, "LightBlue-14")
					.withPlayerHand(hand)
					.build();
		
		game.deal();
		
		PlayerTurn turn = PlayerTurn.Builder.newInstance()
							.withCardName("JACK of HEARTS")
							.withMoveType(MoveType.MOVE_PEG)
							.withMoveDistance(11)
							.withPlayerNumber(1)
							.withPositionNumber(5)
							.build();
		
		game.takeTurn(turn);
		
		assertThat(game.getPlayerPosition(1, 5).getPlayerBoardPositionId()).isEqualTo("Tomato-7");
		
	}
	
	@Test
	public void testBasicGameSerializeAndDeserialize() throws JsonProcessingException, PlayerNotFoundException, CannotStartGameWithoutPlayersException
	{

		GameEventListener listener = new GameEventListener();

		Game game = Game.Builder.newInstance()
						.withEventListener(listener).build();
		
		game.start();
		
		PlayerHand hand1 = game.getPlayerHand(1);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		String gameAsString = objectMapper.writeValueAsString(game);
		
		Game restoredGame = objectMapper.readValue(gameAsString, Game.class);
		
		assertThat(restoredGame.getPlayerHand(1).getCards().get(0)).usingRecursiveComparison().isEqualTo(hand1.getCards().get(0));

		assertThat(restoredGame).usingRecursiveComparison().ignoringFields("eventListener").isEqualTo(game);
		
		assertThat(restoredGame.getPlayerHand(1).getCards().size()).isEqualTo(5);
	}
	
	@Test
	public void testStartAPegWithRepositorySaveRetrieve() throws JsonProcessingException, PlayerNotFoundException, InvalidGameStateException, InvalidMoveException, PlayerPositionNotFoundException, CannotMoveToAPositionYouOccupyException, GameNotFoundException
	{
		
		Card cardToPlay = new Card(CardRank.ACE, Suit.CLUBS);
		
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.build();

		String gameId = game.getId().toString();
		
		GameRepository gameRepository = new GameRepository();
		
		gameRepository.addGame(game);
		
		game = gameRepository.findGameById(gameId);
		
		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.START_A_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.build();

		game.takeTurn(turn);
		assertThat(game.getBoard().getBoardPositionById("Tomato-8").getPegColor()).isEqualTo(Color.Tomato);
		
		gameRepository.saveGame(game);
		
		game = gameRepository.findGameById(gameId);
		
		assertThat(game.getPlayerPosition(1,1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
		assertThat(game.getBoard().getBoardPositionById("Tomato-8").getPegColor()).isEqualTo(Color.Tomato);
	
		PlayerView playerView = game.getPlayerView(1);
		
		assertThat(playerView.getBoard().getBoardPositionById("Tomato-8").getPegColor()).isEqualTo(Color.Tomato);

	}

	@Test
	public void testCannotStartAPegWithJoker() throws JsonProcessingException, PlayerNotFoundException, InvalidGameStateException, InvalidMoveException, PlayerPositionNotFoundException, CannotMoveToAPositionYouOccupyException, GameNotFoundException
	{
		
		Card cardToPlay = new Joker();
		
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.build();

		String gameId = game.getId().toString();
		
		GameRepository gameRepository = new GameRepository();
		
		gameRepository.addGame(game);
		
		game = gameRepository.findGameById(gameId);
		
		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.START_A_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(1)
				.build();

		try
		{
			game.takeTurn(turn);
			assert(false);
		}
		catch (InvalidGameStateException ex)
		{
			assertThat(ex.getMessage()).contains("JOKER to start a peg");
		}
	
	}
	
	@Test 
	public void testGameStartEventEmitted() throws CannotStartGameWithoutPlayersException
	{
		GameEventListener listener = new GameEventListener();
		
		Game game = Game.Builder.newInstance()
					.withEventListener(listener)
					.build();
		
		game.start();
		
		assertThat(listener.getEventCount()).isEqualTo(1);
		assertThat(listener.getEvents().get(listener.getEventCount()-1).getClass()).isEqualTo(com.danlogan.pegsandjokers.domain.events.GameStartedEvent.class);
		
		
	}

	@Test
	public void testWinningAfterAllPegsGoHome() throws JsonProcessingException, PlayerNotFoundException, InvalidGameStateException, InvalidMoveException, PlayerPositionNotFoundException, CannotMoveToAPositionYouOccupyException, GameNotFoundException
	{
		
		Card cardToPlay = new Card(CardRank.ACE, Suit.CLUBS);
		
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(cardToPlay)
				.build();

		Game game = Game.Builder.newInstance()
				.withPlayerHand(playerHand)
				.withPlayerPosition(1, 1, "TomatoHome-5")
				.withPlayerPosition(1,2, "TomatoHome-4")
				.withPlayerPosition(1,3, "TomatoHome-3")
				.withPlayerPosition(1, 4, "TomatoHome-2")
				.withPlayerPosition(1, 5, "Tomato-3")
				.build();

		String gameId = game.getId().toString();
		
		GameRepository gameRepository = new GameRepository();
		
		gameRepository.addGame(game);
		
		game = gameRepository.findGameById(gameId);
		
		PlayerTurn turn = PlayerTurn.Builder.newInstance()
				.withCardName(cardToPlay.getName())
				.withMoveType(MoveType.MOVE_PEG)
				.withPlayerNumber(1)
				.withPositionNumber(5)
				.withMoveDistance(1)
				.build();

		assertThat(game.getStatus()).isEqualTo("NOT_STARTED");
		game.start();
		assertThat(game.getStatus()).isEqualTo("STARTED");
		
		game.takeTurn(turn);
		
		
		assertThat(game.getPlayerPosition(1, 5).getPlayerBoardPositionId()).isEqualTo("TomatoHome-1");
		assertThat(game.getStatus()).isEqualTo("OVER");
		PlayerView playerView1 = new PlayerView(game,1);
		PlayerView playerView2 = new PlayerView(game, 2);
		assertThat(playerView1.getPlayerMessage()).isEqualTo("You win!");
		assertThat(playerView2.getPlayerMessage()).isEqualTo("Player 1 wins!");		
	}

}
