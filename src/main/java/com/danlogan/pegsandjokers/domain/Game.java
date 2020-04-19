package com.danlogan.pegsandjokers.domain;
import java.util.ArrayList;
import java.util.UUID;

public class Game {
	public static final String NOT_STARTED = "NOT_STARTED";
	public static final String STARTED = "STARTED";
	
	private final UUID id = java.util.UUID.randomUUID();
	private String status = Game.NOT_STARTED;	
	private ArrayList<Player> players = new ArrayList<Player>();
	private DeckOfCards drawPile;

	
	public static class Builder{
	
		private ArrayList<Player> players;
		private DeckOfCards drawPile;
		
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
			
			//set up draw pile
			this.drawPile = new DeckOfCards();
			
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
	}

	public String getStatus() {
		return status;
	}
	
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

}
