package com.danlogan.pegsandjokers.domain;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

public class Board {

	//Board fields
	private ConcurrentHashMap<UUID,Side> playerSides;
	
	//Board Builder
	public static class Builder {
// ArrayList<Player> players;
//		private ArrayList<Side> sides;
//		private ArrayList<BoardPosition> mainTrackPositions;
		/*
		 * private ArrayList<BoardPosition> playerStartPositions; private
		 * ArrayList<BoardPosition> playerHomePositions;
		 */		
		private ConcurrentHashMap<UUID,Side> playerSides = new ConcurrentHashMap<UUID,Side>();
		
		static Builder newInstance()
		{
			return new Builder();
		}
		public Builder withPlayers(ArrayList<Player> players)
		{
			//the board will be made up of one side for each Player
			//each side will be assigned a Color

			//will need to iterate thru colors as iterating thru players
			int nextColorIndex = 0;
			
			for (Player player : players)
			{
				Color nextColor = Color.values()[nextColorIndex];
				
				Side side = Side.Builder.newInstance().withColor(nextColor).build();
				playerSides.put(player.getId(),side);
				
				nextColorIndex++;
						
			}
			
			return this;
			
		}
		public Board build()
		{
			return new Board(this);
		}
		
		private Builder() {}
	}
	
	//Board Methods
	public Board(Builder builder) {

		this.playerSides = builder.playerSides;
	}

	public ConcurrentHashMap<UUID,Side> getPlayerSides()
	{
		return this.playerSides;
	}
}
