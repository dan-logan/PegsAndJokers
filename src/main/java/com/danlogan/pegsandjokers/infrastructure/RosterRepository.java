package com.danlogan.pegsandjokers.infrastructure;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.danlogan.pegsandjokers.domain.Roster;

@Service
public class RosterRepository {
	private ConcurrentHashMap<String, Roster> rosters = new ConcurrentHashMap<String, Roster>();
	
	public Roster findRosterById(String id)
	{
		return rosters.get(id);
	}

	public void save(Roster roster)
	{
		if(roster == null || roster.getGameId()==null)
		{
			throw new RuntimeException("Cannot save null Roster or Roster with null GameId");
		}
		else
		{
			rosters.put(roster.getGameId(), roster);
		}
	}
}
