package com.danlogan.pegsandjokers.domain;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.tomcat.util.collections.CaseInsensitiveKeyMap;

public class Game {
	public static final String NOT_STARTED = "NOT_STARTED";
	public static final String STARTED = "STARTED";
	
	private final UUID id = java.util.UUID.randomUUID();
	private String status = Game.NOT_STARTED;	
	private ArrayList<Player> players;
	private DeckOfCards drawPile;
	private ArrayBlockingQueue<Player> playerQueue;
	private ArrayList<PlayerHand> playerHands;
	private ArrayList<ArrayList<PlayerPosition>> playerPositions;
	private Board board;

	
	public static class Builder{
	
		private ArrayList<Player> players;
		private DeckOfCards drawPile;
		private ArrayBlockingQueue<Player> playerQueue;
		private ArrayList<PlayerHand> playerHands = new ArrayList<PlayerHand>();
		private ArrayList<ArrayList<PlayerPosition>> playerPositions = new ArrayList<ArrayList<PlayerPosition>>();
		private Board board;
		
		public static Builder newInstance() {
			return new Builder();
		}
		
		private Builder() {}
		
		//setter methods to configure builder
		
		//build method to return a new instance from Builder
		public Game build() {
			
			//if no players have been provided to the builder use the default
			if (this.players == null) {
				this.players = getDefaultPlayers();	
			}
			
			//Build a queue for players to take turns
			this.playerQueue = new ArrayBlockingQueue<Player>(this.players.size());
			
	
			//Create the game board
			this.board = Board.Builder.newInstance().withNumberOfPlayers(players.size()).build();
			
			//for each player add to player queue, create a Player Hand and initialize player positions
			int playerNumber = 0;

			for(Player p : players) {
				this.playerQueue.add(p);
				this.playerHands.add(PlayerHand.Builder.newInstance(playerNumber).build());
				ArrayList<BoardPosition> playerStartPositions = this.board.getPlayerSides().get(playerNumber).getStartPositions();

				ArrayList<PlayerPosition> playerInitialPositions = new ArrayList<PlayerPosition>();
				
				for (BoardPosition bp : playerStartPositions)
				{
					playerInitialPositions.add(new PlayerPosition(bp));
				}
				
				this.playerPositions.add(playerInitialPositions);
				
				playerNumber++;
			}
			
			
			//set up draw pile with two decks of cards
			this.drawPile = new DeckOfCards();
			this.drawPile.combineDecks(new DeckOfCards());
			
			Game game = new Game(this);
			
					
			return game;
		}
		
		//default to 3 players
		private ArrayList<Player> getDefaultPlayers() {
			
			ArrayList<Player> defaultPlayers = new ArrayList<Player>();
			
			for(int i=1;i<=3;i++) {
				
				Player player = Player.Builder.newInstance()
									.withName("Player " + i)
									.withNumber(i)
									.build();
				
				defaultPlayers.add(player);
				
			}
			
			return defaultPlayers;
			
		}
	}

	public UUID getId() {
		return id;
	}

	//Game Class Methods
	public Game(Builder builder)
	{
		//set all properties from the builder
		players = builder.players;
		drawPile = builder.drawPile;
		playerQueue = builder.playerQueue;
		playerHands = builder.playerHands;
		board = builder.board;
		playerPositions = builder.playerPositions;
	}

	public String getStatus() {
		return status;
	}
	
	public String start( ) throws CannotStartGameWithoutPlayersException {
		
		if (players.size() > 2) {
			this.status = Game.STARTED;
		} else {
			throw new CannotStartGameWithoutPlayersException("Game requires at least 3 players. Add players before starting game");
		}
		
		//after game is started shuffle the draw pile and deal the cards
		this.drawPile.shuffle();
		this.deal();
				
		return this.status;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public int getCardsRemaining() {
		return drawPile.cardsRemaining();
	}
	
	public void makeMove(PlayerMove move) {
		//TO DO:  put in move logic
		
		
		//At end of move, Players turn is over so move them to the back of the queue
		Player currentPlayer = playerQueue.remove();
		playerQueue.add(currentPlayer);
	}
	
	public void deal( )
	{
		//Deal 5 cards to each player
		for(int i=1;i<6;i++)
		{
			for (PlayerHand hand : this.playerHands) {
				hand.drawCard(this.drawPile);
			}
		}
		
	}
	
	public Player getCurrentPlayer() {
			return playerQueue.peek();
	}
	
	public PlayerHand getPlayerHand(int playerNumber) throws PlayerNotFoundException {
		
		int numberPlaying = this.playerHands.size();
		
		if( (playerNumber<1) || (playerNumber>numberPlaying) )
		{
			throw new PlayerNotFoundException(String.format("Player number %d does not exist. There are %d players in this game.", playerNumber, numberPlaying));
		}

		return this.playerHands.get(playerNumber-1);
	}
	
	public Board getBoard()
	{
		return this.board;
	}
	
	public PlayerView getPlayerView(int playerNumber) throws PlayerNotFoundException
	{
		return new PlayerView(this, playerNumber);
	}
	
	public ArrayList<PlayerPosition> getPlayerPositions(int playerNumber)
	{
		return this.playerPositions.get(playerNumber-1);

	}
	
	
	public ArrayList<Move> getAllowedMoves() 
	{ 
		ArrayList<Move> allowedMoves = new ArrayList<Move>();

		//for each card in the current player's hand, look at each of the player's positions to 
		//determine which moves would be allowed

		int currentPlayerNumber = this.getCurrentPlayer().getNumber();

		for (Card card : this.playerHands.get(currentPlayerNumber).getCards()) 
			{ 
				for (PlayerPosition currentPosition : this.getPlayerPositions(currentPlayerNumber)) 
				{ 
					//determine the moves this card will allow the player's peg at this position to do
					addMovesBasedOnCardAndPosition(allowedMoves,card,currentPosition);
				} 
			}

		return allowedMoves; 
	}

	
	private void addMovesBasedOnCardAndPosition(ArrayList<Move> allowedMoves, Card card, PlayerPosition currentPosition) 
	{
		System.out.println("Checking a card " + card.getName());
		
		switch (card.getRank()) {
		case ACE:
			System.out.println("Found an ACE");
			addMovesForAce(allowedMoves, currentPosition);

			break;
		}

		return;

	}

	private void addMovesForAce(ArrayList<Move> allowedMoves, PlayerPosition currentPosition) 
	{ 
		//If current position is a start position and the comeOutPostion is empty then 
		//add a move from currentPostion to comeOutPosition

		//TO DO.... currentPosition.getPlayerBoardPosition().isStartPosition();

		return;

	}

}
