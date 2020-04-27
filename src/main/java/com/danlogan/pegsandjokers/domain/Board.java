package com.danlogan.pegsandjokers.domain;

import java.util.ArrayList;

public class Board {

	//Board fields
	private ArrayList<Side> playerSides;
	
	//Board Builder
	public static class Builder {

		private ArrayList<Side> playerSides = new ArrayList<Side>();
		
		static Builder newInstance()
		{
			return new Builder();
		}
		public Builder withNumberOfPlayers(int players)
		{
			//the board will be made up of one side for each Player
			//each side will be assigned a Color
		
			for (int player=0;player<players;player++)
			{
				Color nextColor = Color.values()[player];
				
				Side side = Side.Builder.newInstance().withColor(nextColor).build();
				playerSides.add(side);
							
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

	public ArrayList<Side> getPlayerSides()
	{
		return this.playerSides;
	}
	
	public int getBoardPositionSideIndex(Side side, BoardPosition boardPosition)
	{
		return side.getMainTrackPositions().indexOf(boardPosition);
	}
	
	public Side getBoardPositionSide(BoardPosition boardPosition)
	{
		Side returnSide = null;
		for (Side side : this.playerSides )
		{
			if(side.getMainTrackPositions().contains(boardPosition))
			{
				returnSide = side;
			}
		}

		return returnSide;
	}
	
	public int getSideIndex(Side sideToFind)
	{
		return this.playerSides.indexOf(sideToFind);

	}

}
