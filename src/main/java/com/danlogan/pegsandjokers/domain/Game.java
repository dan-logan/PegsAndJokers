package com.danlogan.pegsandjokers.domain;
import java.util.ArrayList;
import java.util.UUID;

public class Game {
	public static final String NOT_STARTED = "NOT_STARTED";
	public static final String STARTED = "STARTED";
	
	private final UUID id = java.util.UUID.randomUUID();
	private String status = Game.NOT_STARTED;	
	private ArrayList players = new ArrayList();

	
	public Game(Builder builder)
	{
		//set all properties from the builder
	}
	
	public static class Builder{
		//instance fields
		
		public static Builder newInstance() {
			return new Builder();
		}
		
		private Builder() {}
		
		//setter methods to configure builder
		
		//build method to return a new instance from Builder
		public Game build() {
			return new Game(this);
		}
	}

	public UUID getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}
	
	public String start( ) throws CannotStartGameWithoutPlayersException {
		
		if (players.size() > 0) {
			this.status = Game.STARTED;
			return this.status;
		} else {
			throw new CannotStartGameWithoutPlayersException("No players exist. Add players before starting game");
		}
	}

}
