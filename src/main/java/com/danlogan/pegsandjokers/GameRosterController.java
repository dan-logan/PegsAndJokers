package com.danlogan.pegsandjokers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.danlogan.pegsandjokers.domain.Roster;
import com.danlogan.pegsandjokers.infrastructure.RosterRepository;

@Controller
public class GameRosterController {
	
	@Autowired
	private RosterRepository rosterRespository;
	
	@GetMapping("/roster")
	@ResponseBody
	public ResponseEntity<Roster> roster()
	{
		Roster roster = rosterRespository.getRoster();
		return new ResponseEntity<Roster>(roster, HttpStatus.OK);
	}
	

}
