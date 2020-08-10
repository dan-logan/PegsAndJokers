package com.danlogan.pegsandjokers;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.danlogan.pegsandjokers.commands.JoinGameCommand;
import com.danlogan.pegsandjokers.domain.Roster;
import com.danlogan.pegsandjokers.domain.events.PlayerJoinedGameEvent;
import com.danlogan.pegsandjokers.domain.events.RosterCreatedEvent;
import com.danlogan.pegsandjokers.infrastructure.RosterRepository;

@Controller
public class GameRosterController {
	
	private static final Logger LOG = Logger.getLogger(GameRosterController.class.getName());

	@Autowired
	private RosterRepository rosterRespository;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	@GetMapping("/api/roster/{id}")
	@ResponseBody
	public ResponseEntity<Roster> getRosterById(@PathVariable String id)
	{
		LOG.info("Recieve find Roster request for gameID: " + id);
		Roster roster = rosterRespository.findRosterById(id);
	
		HttpStatus status = roster != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
			
		return new ResponseEntity<Roster>(roster, status);
	}
	
	@PostMapping("/api/rosters")
	@ResponseBody
	public ResponseEntity<Roster> createRoster(@RequestBody CreateRosterCommand cmd)
	{
		LOG.info("Received create Roster Request: " + cmd.toString());
		
		Roster roster = Roster.Builder.newInstance()
						.withGameId(cmd.getGameID())
						.withNumberOfPlayers(cmd.getNumberOfPlayers())
						.build();
		
		rosterRespository.save(roster);
		
		applicationEventPublisher.publishEvent(new RosterCreatedEvent(roster));
		
		return new ResponseEntity<Roster>(roster, HttpStatus.CREATED);
		
	}
	
	@PostMapping("/api/roster/{id}/player/")
	@ResponseBody
	public ResponseEntity<Roster> joinGame(@PathVariable String id, @RequestBody JoinGameCommand cmd)
	{
		LOG.info(String.format("Received joinGameRequest with command: %s", cmd.toString()));
		
		Roster roster = rosterRespository.findRosterById(id);
		
		if (roster == null)
		{
			throw new RuntimeException(String.format("Roster with gameID %s is not found.", id));
		}
		
		roster.assignSeat(cmd.getPlayerNumber(), cmd.getPlayerName());
		
		applicationEventPublisher.publishEvent(new PlayerJoinedGameEvent(roster, cmd.getPlayerNumber(), cmd.getPlayerName()));
		
		return new ResponseEntity<Roster>(roster, HttpStatus.OK);
	}
	
}

class CreateRosterCommand
{
	private String gameID;
	private int numberOfPlayers;
	
	@Override
	public String toString() {
		return "CreateRosterCommand [gameID=" + gameID + ", numberOfPlayers=" + numberOfPlayers + "]";
	}

	public CreateRosterCommand(String gameID, int numberOfPlayers) {
		super();
		this.gameID = gameID;
		this.numberOfPlayers = numberOfPlayers;
	}

	public String getGameID() {
		return gameID;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public CreateRosterCommand()
	{
		
	}
}
