package com.danlogan.pegsandjokers.ai;

import java.util.ArrayList;
import java.util.List;

import com.danlogan.pegsandjokers.domain.Game;
import com.danlogan.pegsandjokers.domain.PlayerHand;
import com.danlogan.pegsandjokers.domain.PlayerNotFoundException;
import com.danlogan.pegsandjokers.domain.PlayerPosition;

import jdk.internal.org.jline.utils.Log;

public class TacticalStrategy {

	private TacticalAI tacticalAI;

	public TacticalStrategy(Builder builder) {
		this.tacticalAI = builder.tacticalAI;
	}

	public static class Builder {

		private TacticalAI tacticalAI;
		private TacticalZone tacticalZone;

		public Builder forTacticalAI(TacticalAI tacticalAI) {

			this.tacticalAI = tacticalAI;
			
			return this;
		}

		public static Builder newInstance() {

			return new Builder();
		}

		public TacticalStrategy build() {
			
			TacticalStrategy strategy=null;
			this.tacticalZone = this.determineTacticalZone();
			
			switch(this.tacticalZone)
			{
			case START_ZONE:
				
				strategy = new StartZoneTacticalStrategy(this);
				break;
				
			case BACKWARD_ZONE:
				strategy = new BackwardZoneTacticalStrategy(this);
				break;
			}
			
			return strategy;
		}

		private TacticalZone determineTacticalZone() {
			
			TacticalZone zone=null;
			
			Game game = this.tacticalAI.getGame();
			
			PlayerPosition playerPosition = game.getPlayerPosition(this.tacticalAI.getPlayerNumber(), this.tacticalAI.getPegNumber());
			
			String boardPositionId = playerPosition.getPlayerBoardPositionId();
			
			if (game.getBoard().getBoardPositionById(boardPositionId).isStartPosition())
				{zone = TacticalZone.START_ZONE;}
			else if (game.getBoard().getBoardPositionById(boardPositionId).isMainTrackPosition())
				{zone = TacticalZone.BACKWARD_ZONE;}//fix this code... need to examine where it is on main track
					
			return zone;
		}
		
	}

	protected TacticalAI getTacticalAI()
	{
		return this.tacticalAI;
	}
	
	//Default logic is to return an empty list of move Options
	//Child strategies should override this method and provide options based on strategy
	public List<MoveOption> moveOptions() {
		
		ArrayList<MoveOption> moveOptions = new ArrayList<MoveOption>();
		
		return moveOptions;
	}
	
	protected PlayerHand getPlayerHand()
	{
		PlayerHand playerHand = null;
		
		try {
			playerHand = this.tacticalAI.getGame().getPlayerHand(this.tacticalAI.getPlayerNumber());
		} catch (PlayerNotFoundException e) {
			Log.warn("AI could not get player hand for playerNumber: " + this.tacticalAI.getPlayerNumber());
			e.printStackTrace();
		}
		
		return playerHand;
	}

}
