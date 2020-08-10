package com.danlogan.pegsandjokers;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


public class RosterEventSseEmitter extends SseEmitter {

	private String gameId;
	
	public RosterEventSseEmitter() {
		super();
	}

	public RosterEventSseEmitter(Long timeout, String gameId) {
		super(timeout);
		this.gameId = gameId;
	}

	public String getGameId()
	{
		return this.gameId;
	}
}
