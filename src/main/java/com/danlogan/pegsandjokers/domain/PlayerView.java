package com.danlogan.pegsandjokers.domain;

public class PlayerView {
	
	private Game game;
	private int playerNumber;
	private PlayerHand playerHand;
	
	public PlayerView(Game game, int playerNumber) throws PlayerNotFoundException
	{
		this.game = game;
		this.playerNumber = playerNumber;
		
		this.playerHand = this.game.getPlayerHand(playerNumber);
	}
	
	public PlayerHand getPlayerHand()
	{
		return this.playerHand;
	}
	
	public Color getPlayerColor()
	{
		int playerNumber = this.game.getCurrentPlayer().getNumber();
		return Color.values()[playerNumber];
	}
	
	public Board getBoard()
	{
		return this.game.getBoard();
	}
	
	public String getPlayerMessage()
	{
		String message;
		if (this.game.getCurrentPlayer().getNumber() == this.playerNumber)
		{
			message =  "It's your turn";
		}
		else
		{
			message = "It's not your turn";
		}
		
		return message;
	}

}
