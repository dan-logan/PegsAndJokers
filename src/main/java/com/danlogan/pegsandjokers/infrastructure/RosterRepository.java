package com.danlogan.pegsandjokers.infrastructure;

import org.springframework.stereotype.Service;

import com.danlogan.pegsandjokers.domain.Roster;

@Service
public class RosterRepository {
	
	public Roster getRoster()
	{
		return Roster.Builder.newInstance().build();
	}

}
