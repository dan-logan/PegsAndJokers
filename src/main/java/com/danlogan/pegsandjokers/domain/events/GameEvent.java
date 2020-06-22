package com.danlogan.pegsandjokers.domain.events;

import java.sql.Timestamp;
import java.util.UUID;

public class GameEvent {
	
	private long eventTimeStampMillis;
	private UUID gameId;
	
	public GameEvent()
	{
		
	}
	
	public GameEvent(UUID gameId, long currentTimeMillis)
	{
		this.gameId = gameId;
		this.eventTimeStampMillis = currentTimeMillis;
	}

	public Timestamp getTimeStamp()
	{
		return new Timestamp(eventTimeStampMillis);
	}
	
	public String getGameId()
	{
		return gameId.toString();
	}
	
	public String getDetails()
	{
		return this.toString();
	}
}
