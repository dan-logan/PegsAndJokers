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
	
	//This get method is just for convenience and should be deprecated in favor
	//of using the /games::POST  method to create a new game
	@GetMapping("/games/new")
	public ResponseEntity<Game> newGame()
	{
		Game game = Game.Builder.newInstance().build();
		games.add(game);
		return new ResponseEntity<Game>(game, HttpStatus.OK);
	}
	private Game findGameById(String id) {
		for (Game game: games) {
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
	
	//probably should deprecate this action concept as it is not really RESTful
	@GetMapping(value = "/game/{id}", params = "action")
	public ResponseEntity<Game> gameAction(@PathVariable String id, @RequestParam String action) throws CannotStartGameWithoutPlayersException {
		
		Game game = findGameById(id);
		
		if (game == null) {
			return new ResponseEntity<Game>(HttpStatus.NOT_FOUND);
		}

		if (action.equals("start")) {
			
				game.start();	
		}
		
		return new ResponseEntity<Game>(game, HttpStatus.OK);
		
		
	}
}
