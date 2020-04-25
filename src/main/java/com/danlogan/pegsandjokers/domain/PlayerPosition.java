package com.danlogan.pegsandjokers.domain;

public class PlayerPosition {
	
	private BoardPosition boardPosition;
	
	public PlayerPosition(BoardPosition bp)
	{
		this.boardPosition = bp;
	}
	
	public BoardPosition getPlayerBoardPosition()
	{
		return  this.boardPosition;
	}

}
