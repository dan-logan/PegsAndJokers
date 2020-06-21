package com.danlogan.pegsandjokers.domain.events;

import java.util.UUID;

public class GameEvent {
	
	private long eventTimeStampMillis;
	private UUID gameId;
	
	public GameEvent()
	{
		
	}
	
	public GameEvent(UUID gameId, long currentTimeMillis)
	{
		this.eventTimeStampMillis = currentTimeMillis;
	}

}
