package com.danlogan.pegsandjokers.domain;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

public class Game {
	public static final String NOT_STARTED = "NOT_STARTED";
	public static final String STARTED = "STARTED";
	
	private final UUID id = java.util.UUID.randomUUID();
	private String status = Game.NOT_STARTED;	
	private ArrayList<Player> players = new ArrayList<Player>();
	private DeckOfCards drawPile;
	private ArrayBlockingQueue<Player> playerQueue;

	
	public static class Builder{
	
		private ArrayList<Player> players;
		private DeckOfCards drawPile;
		private ArrayBlockingQueue<Player> playerQueue;
		
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
			
			for(Player p : players) {
				this.playerQueue.add(p);
			}
			
			//set up draw pile with two decks of cards
			this.drawPile = new DeckOfCards();
			this.drawPile.combineDecks(new DeckOfCards());
			
			return new Game(this);
		}
		
		//default to 3 players
		private ArrayList<Player> getDefaultPlayers() {
			
			ArrayList<Player> defaultPlayers = new ArrayList<Player>();
			
			for(int i=1;i<=3;i++) {
				
				Player player = Player.Builder.newInstance()
									.withName("Player " + i)
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
	}

	public String getStatus() {
		return status;
	}
	
	//TO DO... can probably remove this start method and related exception handling
	public String start( ) throws CannotStartGameWithoutPlayersException {
		
		if (players.size() > 2) {
			this.status = Game.STARTED;
			return this.status;
		} else {
			throw new CannotStartGameWithoutPlayersException("Game requires at least 3 players. Add players before starting game");
		}
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
	
	public Player getCurrentPlayer() {
			return playerQueue.peek();
	}
}
