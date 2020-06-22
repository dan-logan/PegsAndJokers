package com.danlogan.pegsandjokers.domain.events;

import java.util.UUID;

public class GameStartedEvent extends GameEvent {
	
	public GameStartedEvent ()
	{
		
	}
	
	public GameStartedEvent(UUID gameId, long eventTimeMillis)
	{
		super(gameId, eventTimeMillis);
	}
	
	public String toString()
	{
		return "GameStartedEvent[]";
	}

}
