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
	
	public boolean isStartPosition(String positionId)
	{
		return this.getBoardPositionById(positionId).isStartPosition();
	}
	
	public BoardPosition getBoardPositionById(String positionId)
	{
		for (Side side : this.getPlayerSides())
		{
			for (BoardPosition position : side.getAllPositions())
			{
				if (position.getId().equals(positionId))
				{
					return position;
				}
			}
			
		}
		
		return null;
	}

	public BoardPosition getBoardPositionWithOffset(BoardPosition playerBoardPosition, int step) 
	{

		int stepDistance = 	(step >=0) ? 1 : -1;
		
		Side boardPositionSide = this.getBoardPositionSide(playerBoardPosition);
		int boardSideIndex = this.getSideIndex(boardPositionSide);
		int sidePositionIndex = this.getBoardPositionSideIndex(boardPositionSide, playerBoardPosition);

		int numberOfSides = this.getPlayerSides().size();
		Side nextSide = boardPositionSide;

		//Change Board position side if move wraps around to a different side
		if (sidePositionIndex+step > 17 || sidePositionIndex+step < 0)
		{
			nextSide = this.getPlayerSides().get((numberOfSides + stepDistance + boardSideIndex)%numberOfSides);
		}

		BoardPosition newBoardPosition = nextSide.getPosition((18+step+sidePositionIndex)%18);


		return newBoardPosition;
	}
	
	public ArrayList<BoardPosition> getStartPositionsForPlayerNumber(int playerNumber)
	{
		Side playerSide = this.playerSides.get(playerNumber -1);
		
		return playerSide.getStartPositions();
		
	}
	
	public String getReadyToGoHomePositionIdForPlayerNumber(int playerNumber)
	{
		Side playerSide = this.playerSides.get(playerNumber - 1);
		
		return playerSide.getReadyToGoHomePositionId();
	}

	public ArrayList<BoardPosition> getHomePositionsForPlayerNumber(int playerNumber) {

		Side playerSide = this.playerSides.get(playerNumber -1);
		
		return playerSide.getHomePositions();
	}
	
	//Default constructor to support deserialization
	public Board()
	{
		
	}

	public BoardPosition getComeOutPositionForPlayerNumber(int playerNumber) {
		
		Side playerSide = this.playerSides.get(playerNumber - 1);
		
		return playerSide.comeOutPosition();
	}

}
