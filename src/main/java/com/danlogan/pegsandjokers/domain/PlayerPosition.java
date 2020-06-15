package com.danlogan.pegsandjokers.domain;

public class PlayerPosition {
	
	private BoardPosition playerBoardPosition;
	private int playerNumber;
	private Color pegColor;
	
	public PlayerPosition(int playerNumber, BoardPosition bp)
	{
		this.playerNumber = playerNumber;
		this.playerBoardPosition = bp;
		this.pegColor = bp.getPegColor();
	}
	
	public int getPlayerNumber()
	{
		return this.playerNumber;
	}
	
	public BoardPosition getPlayerBoardPosition()
	{
		return  this.playerBoardPosition;
	}
	
	public void moveTo(BoardPosition newPosition) throws CannotMoveToAPositionYouOccupyException
	{
		//Make sure the new position is not occupied already by another one of the player's pegs
		if (this.getPlayerBoardPosition().getPegColor() == newPosition.getPegColor())
		{
			throw new CannotMoveToAPositionYouOccupyException("You cannot move to a position with one of your own pegs in it.");
		}
		
		Peg peg = playerBoardPosition.removePeg();
		System.out.println("moving peg from: " + playerBoardPosition.getId());
		
		newPosition.addPeg(peg);
		
		System.out.println("moved pegged to: " + newPosition.getId());
		
		this.playerBoardPosition = newPosition;
		this.pegColor = peg.getColor();
	}
	
	public Color getPegColor()
	{
		return this.pegColor;
	}

	//Default Constructor for deserialiation
	public PlayerPosition()
	{
		
	}
}
