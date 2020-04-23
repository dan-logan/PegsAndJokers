package com.danlogan.pegsandjokers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.danlogan.pegsandjokers.domain.Board;
import com.danlogan.pegsandjokers.domain.CannotStartGameWithoutPlayersException;
import com.danlogan.pegsandjokers.domain.Game;
import com.danlogan.pegsandjokers.domain.PlayerHand;
import com.danlogan.pegsandjokers.domain.PlayerMove;
import com.danlogan.pegsandjokers.domain.PlayerNotFoundException;

import java.util.ArrayList;
import com.danlogan.pegsandjokers.infrastructure.GameNotFoundException;
import com.danlogan.pegsandjokers.infrastructure.GameRepository;


@SpringBootApplication
@RestController
public class PegsandjokersApplication {

	private static GameRepository gameRepository = new GameRepository();
//	private ArrayList<Game> games = new ArrayList<Game>();
	
	public static void main(String[] args) {
		SpringApplication.run(PegsandjokersApplication.class, args);
	}

	@GetMapping("/")
	public String root() {
		return String.format("Welcome to Pegs and Jokers! %n There are %d Games.",gameRepository.getNumberOfGames());
	}

	//Return all Games
	@GetMapping("/games")
	public ResponseEntity<ArrayList<Game>> games()
	{
		ArrayList<Game>games = gameRepository.getAllGames();
		
		return new ResponseEntity<ArrayList<Game>>(games, HttpStatus.OK);
	}


	//New Game API
	@PostMapping("/games")
	public ResponseEntity<Game> newGame() throws CannotStartGameWithoutPlayersException
	{
		
		Game game = Game.Builder.newInstance().build();
		gameRepository.addGame(game);
		game.start();
		return new ResponseEntity<Game>(game, HttpStatus.OK);
		
	}

	@GetMapping(value = "/game/{id}")
	public ResponseEntity<Game> getGameById(@PathVariable String id) throws GameNotFoundException
	{
		Game game = gameRepository.findGameById(id);
			
		return new ResponseEntity<Game>(game, HttpStatus.OK);
	}
	
	//Post a new move to the current turn
	@PostMapping("/game/{id}/moves")
	public ResponseEntity<Game> newMove(@PathVariable String id, @RequestBody PlayerMove move) throws GameNotFoundException
	{
		Game game = gameRepository.findGameById(id);
		
		game.makeMove(move);
		
		return  new ResponseEntity<Game>(game, HttpStatus.OK);
	}
	
	//Get Player Hands for a specific player number
	@GetMapping(value="/game/{id}/playerhand/{playerNumber}")
	public ResponseEntity<PlayerHand> getPlayerHand(@PathVariable String id, @PathVariable int playerNumber) throws GameNotFoundException, PlayerNotFoundException
	{
		  Game game = gameRepository.findGameById(id); 
		  
		  PlayerHand hand = game.getPlayerHand(playerNumber);
		  
		  return new ResponseEntity<PlayerHand>(hand,HttpStatus.OK);
		 	 		
	}
	
	//Get current board layout
	@GetMapping(value="/game/{id}/board")
	public ResponseEntity<Board> getBoard(@PathVariable String id) throws GameNotFoundException
	{
		Game game = gameRepository.findGameById(id);
		
		Board board = game.getBoard();
		
		return new ResponseEntity<Board>(board, HttpStatus.OK);
		
	}
	
	//probably should deprecate this action concept as it is not really RESTful
	@GetMapping(value = "/game/{id}", params = "action")
	public ResponseEntity<Game> gameAction(@PathVariable String id, @RequestParam String action) throws GameNotFoundException, CannotStartGameWithoutPlayersException {
		
		Game game = gameRepository.findGameById(id);
		
		if (game == null) {
			return new ResponseEntity<Game>(HttpStatus.NOT_FOUND);
		}

		if (action.equals("start")) {
			
				game.start();	
		}
		
		return new ResponseEntity<Game>(game, HttpStatus.OK);
		
		
	}
}
