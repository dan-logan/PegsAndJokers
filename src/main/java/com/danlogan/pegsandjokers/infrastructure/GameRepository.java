package com.danlogan.pegsandjokers.infrastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import com.danlogan.pegsandjokers.domain.Game;

public class GameRepository {
	
	private static ConcurrentHashMap<String,Game> games = new ConcurrentHashMap<String,Game>();
	
	public static Game findGameById(String id) throws GameNotFoundException {
		
		Game game = games.get(id);
		if (game == null) {
			throw new GameNotFoundException("Game not found for id = "+id);
		}
		
		return game;
	}
	
	public static void addGame(Game game) {
		games.put(game.getId().toString(), game);
	}
	
	public static int getNumberOfGames() {
		return games.size();
	}
	
	public static ArrayList<Game> getAllGames(){
	
		Collection<Game> values = games.values();
		
		ArrayList<Game> list = new ArrayList<Game>(values);
				
		return list;
	}

}
