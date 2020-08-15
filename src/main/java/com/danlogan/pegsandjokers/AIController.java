package com.danlogan.pegsandjokers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import com.danlogan.pegsandjokers.ai.StrategicAI;
import com.danlogan.pegsandjokers.domain.CannotMoveToAPositionYouOccupyException;
import com.danlogan.pegsandjokers.domain.Game;
import com.danlogan.pegsandjokers.domain.InvalidGameStateException;
import com.danlogan.pegsandjokers.domain.InvalidMoveException;
import com.danlogan.pegsandjokers.domain.PlayerNotFoundException;
import com.danlogan.pegsandjokers.domain.PlayerPositionNotFoundException;
import com.danlogan.pegsandjokers.domain.PlayerTurn;
import com.danlogan.pegsandjokers.domain.PlayerView;
import com.danlogan.pegsandjokers.domain.Roster;
import com.danlogan.pegsandjokers.infrastructure.GameNotFoundException;
import com.danlogan.pegsandjokers.infrastructure.GameRepository;
import com.danlogan.pegsandjokers.infrastructure.RosterRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.java.Log;

@Log
@Controller
public class AIController {
	
	@Autowired
	RosterRepository rosterRepository;
	
	@Autowired
	GameRepository gameRepository;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Async
	@EventListener
	public void onTurnTakenEvent(TurnTakenEvent turnTakenEvent)
	{
		log.info("TurnTaken event being handled by AIController: " + turnTakenEvent.toString());
		
		//wait 5 seconds to simulate computer think time
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String gameId = turnTakenEvent.getGameId();
		int nextPlayerNumber = turnTakenEvent.getNextPlayerNumber();
		
		Roster roster = rosterRepository.findRosterById(gameId);
				
		if (roster != null && roster.getPlayerName(nextPlayerNumber).equals(""))
		{
			//next player has not joined so have the AI take the turn
			Game game=null;
			try {
				game = gameRepository.findGameById(gameId);
			} catch (Exception e) {
				e.printStackTrace();
				log.warning("Failed to find game with game id: " + gameId);
			}
		
			if (game != null)
			{
				StrategicAI ai = new StrategicAI(game, nextPlayerNumber);
				PlayerTurn turn = ai.getNextTurn();
				log.info("AI took a turn: " + turn.toString());

				try {
					game.takeTurn(turn);
				} catch (PlayerNotFoundException | InvalidGameStateException | InvalidMoveException
						| PlayerPositionNotFoundException | CannotMoveToAPositionYouOccupyException e) {
					e.printStackTrace();
					log.warning("AI recommended invalid move");
				}	
				
				try {
					gameRepository.saveGame(game);
				} catch (JsonProcessingException e) {
					log.warning("Could not save game with game id: " + gameId);
					e.printStackTrace();
				}
				
				PlayerView playerView=null;
				try {
					playerView = new PlayerView(game, nextPlayerNumber);
				} catch (PlayerNotFoundException e) {
					log.warning("Could not generate player view for playerNumber: " + nextPlayerNumber);
					e.printStackTrace();
				}
				
				applicationEventPublisher.publishEvent(new TurnTakenEvent(game.getId().toString(), turn.getPlayerNumber(), playerView.getCurrentPlayerNumber()));

			}
			
		}
				
	}
	

}
