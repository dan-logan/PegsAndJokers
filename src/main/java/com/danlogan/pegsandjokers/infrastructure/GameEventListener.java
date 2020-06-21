package com.danlogan.pegsandjokers.infrastructure;

import java.util.ArrayList;

import com.danlogan.pegsandjokers.domain.events.GameEvent;
import com.danlogan.pegsandjokers.domain.events.IListenToGameEvents;

public class GameEventListener implements IListenToGameEvents {

	private ArrayList<GameEvent> gameEvents = new ArrayList<GameEvent>();
	
	@Override
	public void eventEmitted(GameEvent event) {
		
		gameEvents.add(event);

	}

	@Override
	public int getEventCount() {
		 
		return gameEvents.size();
	}

	@Override
	public ArrayList<GameEvent> getEvents() {
	 
		return gameEvents;
	}

}
