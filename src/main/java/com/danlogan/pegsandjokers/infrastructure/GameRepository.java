package com.danlogan.pegsandjokers.infrastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.danlogan.pegsandjokers.domain.Game;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GameRepository {
	
	private ConcurrentHashMap<String,String> gamesAsStrings = new ConcurrentHashMap<String,String>();
	
	public GameRepository() {
		super();
	}
	
	public Game findGameById(String id) throws GameNotFoundException, JsonMappingException, JsonProcessingException {
		
		String gameAsString = gamesAsStrings.get(id);
		if (gameAsString == null) {
			throw new GameNotFoundException("Game not found for id = "+id);
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		Game game = objectMapper.readValue(gameAsString, Game.class);
		
		return game;
	}
	
	public void addGame(Game game) throws JsonProcessingException {
		
		String gameId = game.getId().toString();
		ObjectMapper objectMapper = new ObjectMapper();
		String gameAsString = objectMapper.writeValueAsString(game);
		
		this.gamesAsStrings.put(gameId, gameAsString);
		
	}
	
	public void saveGame(Game game) throws JsonProcessingException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		String gameAsString = objectMapper.writeValueAsString(game);
		gamesAsStrings.put(game.getId().toString(),gameAsString);
	}
	
	public int getNumberOfGames() {
		return gamesAsStrings.size();
	}
	
	public ArrayList<Game> getAllGames() throws JsonMappingException, JsonProcessingException{
	
		Collection<String> values = gamesAsStrings.values();
		ObjectMapper objectMapper = new ObjectMapper();
		
		ArrayList<Game> list = new ArrayList<Game>();
		
		for (String gameAsString : values)
		{
			Game game = objectMapper.readValue(gameAsString, Game.class);
			list.add(game);
		}
				
		return list;
	}

}
