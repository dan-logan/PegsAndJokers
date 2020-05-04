package com.danlogan.pegsandjokers.domain;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

public class Game {
	public static final String NOT_STARTED = "NOT_STARTED";
	public static final String STARTED = "STARTED";
	
	private final UUID id = java.util.UUID.randomUUID();
	private String status = Game.NOT_STARTED;	
	private ArrayList<Player> players;
	private DeckOfCards drawPile;
	private ArrayList<Card> discardPile;
	private ArrayBlockingQueue<Player> playerQueue;
	private ArrayList<PlayerHand> playerHands;
	private ArrayList<ArrayList<PlayerPosition>> playerPositions;
	private Board board;

	
	public static class Builder{
	
		private ArrayList<Player> players = new ArrayList<Player>();
		private DeckOfCards drawPile;
		private ArrayList<Card> discardPile = new ArrayList<Card>();
		private ArrayBlockingQueue<Player> playerQueue;
		private ArrayList<PlayerHand> playerHands = new ArrayList<PlayerHand>();
		private ArrayList<ArrayList<PlayerPosition>> playerPositions = new ArrayList<ArrayList<PlayerPosition>>();
		private Board board;
		
		private ArrayList<ArrayList<String>> initialPlayerPositionIds = new ArrayList<ArrayList<String>>();
		
		public static Builder newInstance() {
			
			Builder builder = new Builder();
			//Intialize initialPlayerPostions to an 8 player by 5 position two-dimensional array list
			
			 for (int player=0;player<5;player++)
			 {
				 builder.initialPlayerPositionIds.add(new ArrayList<String>());
				 
				 for (int pos=0;pos<5;pos++)
				 {
					builder.initialPlayerPositionIds.get(player).add(null); 
				 }
			 }
			
			
			return builder;
		}
		
		private Builder() {}
		
		//setter methods to configure builder
		public Builder withPlayerNamed(String playerName)
		{
			int playerNumber = players.size() + 1;
			Player player = Player.Builder.newInstance()
					.withName("Player " + playerNumber)
					.withNumber(playerNumber)
					.build();
			
			players.add(player);
			
			return this;
		}
		
		public Builder withPlayerHand(PlayerHand hand)
		{
			this.playerHands.add(hand);
			return this;
		}
		
		public Builder withPlayerPosition(int playerNumber, int positionNumber, String boardPositionId)
		{
			this.initialPlayerPositionIds.get(playerNumber-1).add(positionNumber-1,boardPositionId);
			return this;
		}

		
		//build method to return a new instance from Builder
		public Game build() {
			
			//if less than 3 players have been provided to the builder use default players
			if (this.players.size() < 3) {
				for(Player defaultPlayer : getDefaultPlayers())
				{
					this.players.add(defaultPlayer);
				}
			}
			
			//Build a queue for players to take turns
			this.playerQueue = new ArrayBlockingQueue<Player>(this.players.size());
			
	
			//Create the game board
			this.board = Board.Builder.newInstance().withNumberOfPlayers(players.size()).build();
			
			//for each player add to player queue, create any missing PlayerHands or PlayerPositions as defaults
			int playerNumber = 0;

			for(Player p : players) {
				this.playerQueue.add(p);

				if (this.playerHands.size()==playerNumber)
				{
					this.playerHands.add(PlayerHand.Builder.newInstance(playerNumber).build());
				}
				
				ArrayList<BoardPosition> playerStartPositions = this.board.getPlayerSides().get(playerNumber).getStartPositions();

				ArrayList<PlayerPosition> playerInitialPositions = new ArrayList<PlayerPosition>();
				
				for (BoardPosition bp : playerStartPositions)
				{
					playerInitialPositions.add(new PlayerPosition(bp));
				}
				
				this.playerPositions.add(playerInitialPositions);
				
				//for any initialized player positions... move update the default start positions
				ArrayList<String> thisPlayersInitialPositionIds = null;
				
				if (this.initialPlayerPositionIds.size() > playerNumber)
				{
					thisPlayersInitialPositionIds = this.initialPlayerPositionIds.get(playerNumber);
				}

				int initialPositionNumber = 0;
				for(String positionId : thisPlayersInitialPositionIds)
				{
					if (positionId != null)
					{
						PlayerPosition positionToInitialize = this.playerPositions.get(playerNumber).get(initialPositionNumber);

						try {
							positionToInitialize.moveTo(this.board.getBoardPositionById(positionId));
						}catch(CannotMoveToAPositionYouOccupyException ex)
						{
							throw new RuntimeException("Cannot intialize with board with given initial postions. Duplicate Position found.");
						}
					}

					initialPositionNumber++;
				}
				
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
			
			for(int i=players.size()+1;i<=3;i++) {
				
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
		this.discardPile = builder.discardPile;
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
	
	public void takeTurn(PlayerTurn turn) throws PlayerNotFoundException, InvalidGameStateException, PlayerPositionNotFoundException, CannotMoveToAPositionYouOccupyException {

		//Get the PlayerHand for the player taking a turn
		PlayerHand playerHand = this.getPlayerHand(turn.getPlayerNumber());
		
		//Make sure player taking turn is the current player
		int currentPlayerNumber = this.getCurrentPlayer().getNumber();
		
		if (currentPlayerNumber != turn.getPlayerNumber())
		{
			throw new InvalidGameStateException(String.format("It's not your turn.  It is player %d's turn.", currentPlayerNumber));
		}
			
		//Make sure the current player has the card being played in their hand
		if (!playerHand.hasCard(turn.getCardName()))
		{
			throw new InvalidGameStateException("You cannot play a card that is not in your hand.");
		}
		
		//Make the requested move
		switch(turn.getMoveType())
		{
			case START_A_PEG: 
				
				this.handleStartAPegRequest(turn, playerHand);
				break;

			case DISCARD:
				
				this.handleDiscardRequest(turn, playerHand);
				break;
				
			case MOVE_PEG_FORWARD:
				this.handleMovePegForwardRequest(turn, playerHand);
				break;
					
		}
		
		
		//At end of turn, discard the card played, draw a new card, and move player to back of the queue
		playerHand.discardCard(this.discardPile, turn.getCardName());
		playerHand.drawCard(this.drawPile);
		
		Player tempPlayer = playerQueue.remove();
		playerQueue.add(tempPlayer);
	}
	
	private void validatePosition(PlayerTurn turn) throws PlayerPositionNotFoundException
	{
		//Make sure they are requesting a valid position number
		int playerPositionNumber = turn.getPlayerPositionNumber();
		if (playerPositionNumber <1 || playerPositionNumber > 5)
		{
			throw new PlayerPositionNotFoundException(String.format("Position number %d is not a valid position.", playerPositionNumber));
		}
		
	}
	
	private void handleStartAPegRequest(PlayerTurn turn, PlayerHand playerHand) throws InvalidGameStateException, CannotMoveToAPositionYouOccupyException, PlayerPositionNotFoundException

	{
		//Make sure a valid position is referenced in the request
		this.validatePosition(turn);

		//First verify that the card being used for the turn can be used to start a peg
		Card cardBeingPlayed = playerHand.getCard(turn.getCardName());
		
		if(!cardBeingPlayed.canBeUsedToStart())
		{
			throw new InvalidGameStateException(String.format("Cannot use a %s to start a peg.", turn.getCardName()));
		}
		
		//Then verify that the peg being requested to move is in a start position
		int playerPositionNumber = turn.getPlayerPositionNumber();
		PlayerPosition playerPosition = this.getPlayerPositions(turn.getPlayerNumber()).get(playerPositionNumber-1);
		
		if (!playerPosition.getPlayerBoardPosition().isStartPosition())
		{
			throw new InvalidGameStateException("Can only start pegs that are in the start position.");
		}
		
		//Lastly make sure there is actually a peg in that start position
		if (!playerPosition.getPlayerBoardPosition().getHasPeg())
		{
			throw new InvalidGameStateException(String.format("Player Postion %d does not have a peg in it.", playerPositionNumber));
		}
		
		//Then update the PlayerPosition to be in the come out position
		BoardPosition comeOutPosition = this.board.getPlayerSides().get(turn.getPlayerNumber()-1).getComeOutPosition();
		
		playerPosition.moveTo(comeOutPosition);
		
		return;
	}
	
	private void handleDiscardRequest(PlayerTurn turn, PlayerHand playerHand)
	{
		
		playerHand.discardCard(this.discardPile, turn.getCardName());
		
	}
	
	private void handleMovePegForwardRequest(PlayerTurn turn, PlayerHand playerHand) throws PlayerPositionNotFoundException, InvalidGameStateException, CannotMoveToAPositionYouOccupyException
	{
		//Make sure a valid position is requested
		this.validatePosition(turn);
		
		//Verify that the card being used for the turn can be used to move forward
		Card cardBeingPlayed = playerHand.getCard(turn.getCardName());
		
		if(!cardBeingPlayed.canBeUsedToMoveForward())
		{
			throw new InvalidGameStateException(String.format("Cannot use a %s to move forward.", turn.getCardName()));
		}
		
		//Verify that the position referenced in the turn is not a start position
		int playerPositionNumber = turn.getPlayerPositionNumber();
		PlayerPosition playerPosition = this.getPlayerPositions(turn.getPlayerNumber()).get(playerPositionNumber-1);
		
		if (playerPosition.getPlayerBoardPosition().isStartPosition())
		{
			throw new InvalidGameStateException("Pegs in start position cannot move forward.");
		}
		
		//Determine how many spaces forward to move
		int spacesToMove = cardBeingPlayed.getDistanceForMoves();
		System.out.println("Going to move this many spaces" + spacesToMove);
		
		//If on main track move forward accounting for 18 spaces per side
		BoardPosition playerBoardPosition = playerPosition.getPlayerBoardPosition();
		
		if (playerBoardPosition.isMainTrackPosition())
		{
			Side boardPositionSide = board.getBoardPositionSide(playerBoardPosition);
			int sidePositionIndex = board.getBoardPositionSideIndex(boardPositionSide, playerBoardPosition);
			
			System.out.println("Board position side index is " + board.getSideIndex(boardPositionSide));
			System.out.println("Side position index of playerBoardPostion is " + sidePositionIndex);

			if(sidePositionIndex + spacesToMove < 18)
			{
				BoardPosition newBoardPosition = boardPositionSide.getPosition(sidePositionIndex + spacesToMove);
				playerPosition.moveTo(newBoardPosition);
			}
			else
			{
				//wrap around to first side when on the last side
				int boardSideIndex = board.getSideIndex(boardPositionSide);
				Side nextSide = null;
				
				if (boardSideIndex < board.getPlayerSides().size()-1)
				{
					nextSide = this.board.getPlayerSides().get(board.getSideIndex(boardPositionSide)+1);
				}
				else
				{
					nextSide = this.board.getPlayerSides().get(0);
				}
				
				BoardPosition newBoardPosition = nextSide.getPosition(spacesToMove - (18 - sidePositionIndex));
				playerPosition.moveTo(newBoardPosition);

			}
		}
	
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
	
	public Player getCurrentPlayer() 
	{
			Player currentPlayer=playerQueue.peek();

			return currentPlayer;
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
	
	public PlayerPosition getPlayerPosition(int playerNumber, int positionNumber)
	{
		return this.playerPositions.get(playerNumber - 1).get(positionNumber -1);
	}
	
	
	public ArrayList<Move> getAllowedMoves() 
	{ 
		ArrayList<Move> allowedMoves = new ArrayList<Move>();

		//for each card in the current player's hand, look at each of the player's positions to 
		//determine which moves would be allowed

/*		int currentPlayerNumber = this.getCurrentPlayer().getNumber();

		for (Card card : this.playerHands.get(currentPlayerNumber-1).getCards()) 
			{ 
				for (PlayerPosition currentPosition : this.getPlayerPositions(currentPlayerNumber)) 
				{ 
					//determine the moves this card will allow the player's peg at this position to do
					addMovesBasedOnCardAndPosition(allowedMoves,card,currentPosition);
				} 
			}
*/
		return allowedMoves; 
	}

	
	private void addMovesBasedOnCardAndPosition(ArrayList<Move> allowedMoves, Card card, PlayerPosition currentPosition) 
	{
//		System.out.println("Checking a card " + card.getName());
		
		switch (card.getRank()) {
		case ACE:
//			System.out.println("Found an ACE");
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
