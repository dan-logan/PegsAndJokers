package com.danlogan.pegsandjokers.ai;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.danlogan.pegsandjokers.domain.CannotMoveToAPositionYouOccupyException;
import com.danlogan.pegsandjokers.domain.Card;
import com.danlogan.pegsandjokers.domain.CardRank;
import com.danlogan.pegsandjokers.domain.Game;
import com.danlogan.pegsandjokers.domain.InvalidGameStateException;
import com.danlogan.pegsandjokers.domain.InvalidMoveException;
import com.danlogan.pegsandjokers.domain.MoveType;
import com.danlogan.pegsandjokers.domain.PlayerHand;
import com.danlogan.pegsandjokers.domain.PlayerNotFoundException;
import com.danlogan.pegsandjokers.domain.PlayerPositionNotFoundException;
import com.danlogan.pegsandjokers.domain.PlayerTurn;
import com.danlogan.pegsandjokers.domain.Suit;

public class AITests {

	@Test
	public void testStartZoneOptionsAtBeginningofGameWithOnlyOneStartableCard() throws PlayerNotFoundException, InvalidGameStateException, InvalidMoveException, PlayerPositionNotFoundException, CannotMoveToAPositionYouOccupyException
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
		
		//make sure suggested turn is playable
		PlayerTurn turn = ai.moveOptions().get(0).getPlayerTurn();
		
		game.takeTurn(turn);
		
		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");

	}

	@Test
	public void testStartZoneOptionsAtBeginningofGameWithPrioritizedCards() throws PlayerNotFoundException, InvalidGameStateException, InvalidMoveException, PlayerPositionNotFoundException, CannotMoveToAPositionYouOccupyException
	{
		//create default game (default is 4 players)
		//use all startable cards to makes sure they are ranked in priority order
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(new Card(CardRank.QUEEN, Suit.HEARTS))
				.withCard(new Card(CardRank.ACE, Suit.HEARTS))
				.withCard(new Card(CardRank.JACK, Suit.HEARTS))
				.withCard(new Card(CardRank.KING, Suit.HEARTS))
				.withCard(new Card(CardRank.FIVE, Suit.HEARTS))
				.build();

		
		Game game = Game.Builder.newInstance()
					.withPlayerHand(playerHand)
					.build();
		
		TacticalAI ai = new TacticalAI(game,1,1); //create tactical AI for player 1, peg 1
		
		assertThat(ai.moveOptions().size()).isEqualTo(4);
		
		assertThat(ai.moveOptions().get(0).getResultingBoardPositionID()).isEqualTo("Tomato-8");
		assertThat(ai.moveOptions().get(0).getSuggestedCard()).isEqualTo("JACK of HEARTS");
		assertThat(ai.moveOptions().get(1).getResultingBoardPositionID()).isEqualTo("Tomato-8");
		assertThat(ai.moveOptions().get(1).getSuggestedCard()).isEqualTo("QUEEN of HEARTS");
		assertThat(ai.moveOptions().get(2).getResultingBoardPositionID()).isEqualTo("Tomato-8");
		assertThat(ai.moveOptions().get(2).getSuggestedCard()).isEqualTo("KING of HEARTS");
		assertThat(ai.moveOptions().get(3).getResultingBoardPositionID()).isEqualTo("Tomato-8");
		assertThat(ai.moveOptions().get(3).getSuggestedCard()).isEqualTo("ACE of HEARTS");
		
		//make sure suggested turn is playable
		PlayerTurn turn = ai.moveOptions().get(0).getPlayerTurn();
		
		game.takeTurn(turn);
		
		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
		assertThat(game.getLastCardPlayed().getName()).isEqualTo("JACK of HEARTS");

	}

	@Test
	public void testStartZoneOptionsWithOwnPegInComeOutSpot() throws PlayerNotFoundException, InvalidGameStateException, InvalidMoveException, PlayerPositionNotFoundException, CannotMoveToAPositionYouOccupyException
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
					.withPlayerPosition(1, 1, "Tomato-8")
					.build();
		
		TacticalAI ai = new TacticalAI(game,1,1); //create tactical AI for player 1, peg 1
		
		assertThat(ai.moveOptions().size()).isEqualTo(0);  //starting is not an option
		
	}

	@Test
	public void testStartZoneOptionsWithOpponentInComeOutSpot() throws PlayerNotFoundException, InvalidGameStateException, InvalidMoveException, PlayerPositionNotFoundException, CannotMoveToAPositionYouOccupyException
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
					.withPlayerPosition(2, 1, "Tomato-8")
					.build();
		
		TacticalAI ai = new TacticalAI(game,1,1); //create tactical AI for player 1, peg 1
		
		assertThat(ai.moveOptions().size()).isEqualTo(1);  
		
		assertThat(game.getPlayerPosition(2, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
		
		game.takeTurn(ai.moveOptions().get(0).getPlayerTurn());
		
		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
		
	}
	
	@Test
	public void testStrategicAIStartsAPegAtBeginningOfGame() throws PlayerNotFoundException, InvalidGameStateException, InvalidMoveException, PlayerPositionNotFoundException, CannotMoveToAPositionYouOccupyException
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
		
		StrategicAI ai = new StrategicAI(game, 1); //create strategic AI for player 1
		
		PlayerTurn turn = ai.getNextTurn();
		assertThat(turn.getCardName()).isEqualTo("QUEEN of HEARTS");
		assertThat(turn.getMoveType()).isEqualTo(MoveType.START_A_PEG);
		
		//make sure turn can actually be taken
		game.takeTurn(turn);
		
		assertThat(game.getPlayerPosition(1, 1).getPlayerBoardPositionId()).isEqualTo("Tomato-8");
		
	}

	@Test
	public void testStrategicAIBurnsACardIfCantStartAtBeginningOfGame() throws PlayerNotFoundException, InvalidGameStateException, InvalidMoveException, PlayerPositionNotFoundException, CannotMoveToAPositionYouOccupyException
	{
		//create default game (default is 4 players)
		//use a hand that has only one startable card
		PlayerHand playerHand = PlayerHand.Builder.newInstance(1)
				.withCard(new Card(CardRank.EIGHT, Suit.HEARTS))
				.withCard(new Card(CardRank.FOUR, Suit.DIAMONDS))
				.withCard(new Card(CardRank.THREE, Suit.HEARTS))
				.withCard(new Card(CardRank.FIVE, Suit.HEARTS))
				.withCard(new Card(CardRank.TWO, Suit.HEARTS))
				.build();

		
		Game game = Game.Builder.newInstance()
					.withPlayerHand(playerHand)
					.build();
		
		StrategicAI ai = new StrategicAI(game, 1); //create strategic AI for player 1
		
		PlayerTurn turn = ai.getNextTurn();
		assertThat(turn.getCardName()).isEqualTo("FIVE of HEARTS");
		assertThat(turn.getMoveType()).isEqualTo(MoveType.DISCARD);
		
		//make sure turn can actually be taken
		game.takeTurn(turn);
		
		assertThat(game.getLastCardPlayed().getName()).isEqualTo("FIVE of HEARTS");
		
	}

}
