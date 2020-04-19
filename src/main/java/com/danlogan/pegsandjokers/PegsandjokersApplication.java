package com.danlogan.pegsandjokers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.danlogan.pegsandjokers.domain.CannotStartGameWithoutPlayersException;
import com.danlogan.pegsandjokers.domain.Game;
import com.sun.tools.javac.util.List;

import java.util.ArrayList;


@SpringBootApplication
@RestController
public class PegsandjokersApplication {

	private ArrayList<Game> games = new ArrayList<Game>();
	
	public static void main(String[] args) {
		SpringApplication.run(PegsandjokersApplication.class, args);
	}

	@GetMapping("/")
	public String root() {
		return String.format("Welcome to Pegs and Jokers! %n There are %d Games.",games.size());
	}
	
	//Return all Games
	@GetMapping("/games")
	public ResponseEntity<ArrayList<Game>> games()
	{
			return new ResponseEntity<ArrayList<Game>>(games, HttpStatus.OK);
	}
		
	@GetMapping("/games/new")
	public ResponseEntity<Game> newGame()
	{
		Game game = Game.Builder.newInstance().build();
		games.add(game);
		return new ResponseEntity<Game>(game, HttpStatus.OK);
	}
	private Game findGameById(String id) {
		for (Game game: games) {
			System.out.print(String.format("Checking Game with id %s%n", game.getId().toString()));
			System.out.print(String.format("Comparing to id %s%n", id));
			if(game.getId().toString().equals(id)) {
				return game;
			}
		}
		
		return null;
		
	}
	
	@GetMapping(value = "/game/{id}")
	public ResponseEntity<Game> getGameById(@PathVariable String id)
	{
		Game game = findGameById(id);
			
		if (game == null) {
			return new ResponseEntity<Game>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Game>(game, HttpStatus.OK);
	}
	
	@GetMapping(value = "/game/{id}", params = "action")
	public ResponseEntity<Game> gameAction(@PathVariable String id, @RequestParam String action) throws CannotStartGameWithoutPlayersException {
		
		Game game = findGameById(id);
		
		if (game == null) {
			return new ResponseEntity<Game>(HttpStatus.NOT_FOUND);
		}

		if (action.equals("start")) {
//			try {
				game.start();	
//			}
//			catch(CannotStartGameWithoutPlayersException e) {
				
	//			return new ResponseEntity<Game>(HttpStatus.CONFLICT);
	//			return ResponseEntity.badRequest().body(e.getMessage());

		//	}
		}
		
		return new ResponseEntity<Game>(game, HttpStatus.OK);
		
		
	}
}
