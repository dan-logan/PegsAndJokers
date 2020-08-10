package com.danlogan.pegsandjokers.domain.events;

import com.danlogan.pegsandjokers.domain.Roster;

public class RosterCreatedEvent extends RosterEvent {

	public RosterCreatedEvent(Roster roster) {
		super(roster);
		
	}

	@Override
	public String toString() {
		return "RosterCreatedEvent [gameId=" + gameId + ", numberOfPlayers=" + numberOfPlayers + ", playerNames="
				+ playerNames + "]";
	}

}
