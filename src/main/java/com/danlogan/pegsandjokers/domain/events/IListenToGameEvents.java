package com.danlogan.pegsandjokers.domain.events;

import java.util.ArrayList;

public interface IListenToGameEvents {

	
	public void eventEmitted(GameEvent event);
	
	public int getEventCount();
	
	public ArrayList<GameEvent> getEvents();
	
}
