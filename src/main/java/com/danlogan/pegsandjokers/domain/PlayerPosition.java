package com.danlogan.pegsandjokers.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "atHome"})
public class PlayerPosition {
	
	private String playerBoardPositionId;
	private int playerNumber;
	private Color pegColor;
	private int pegNumber;
	
	public PlayerPosition(int playerNumber, BoardPosition bp)
	{
		this.playerNumber = playerNumber;
		this.playerBoardPositionId = bp.getId();
		this.pegColor = bp.getPegColor();
		this.pegNumber = bp.getPegNumber();
	}
	
	public int getPlayerNumber()
	{
		return this.playerNumber;
	}
	
	public String getPlayerBoardPositionId()
	{
		return  this.playerBoardPositionId;
	}
	
	public void moveTo(String newPositionId)
	{
		this.playerBoardPositionId = newPositionId;
	}
	
/* OLD Version
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
*/
	public Color getPegColor()
	{
		return this.pegColor;
	}
	
	public int getPegNumber()
	{
		return this.pegNumber;
	}

	//Default Constructor for deserialiation
	public PlayerPosition()
	{
		
	}

	public boolean isAtHome() {
		
		return this.getPlayerBoardPositionId().contains("Home");
	}
}
