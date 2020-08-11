package com.danlogan.pegsandjokers.commands;

public class JoinGameCommand {
	
	@Override
	public String toString() {
		return "JoinGameCommand [gameId=" + gameId+ ", playerNumber=" + playerNumber + ", playerName=" + playerName + "]";
	}
	public JoinGameCommand(String gameId, int playerNumber, String playerName) {
		super();
		this.playerNumber = playerNumber;
		this.playerName = playerName;
		this.gameId = gameId;
	}
	
	public JoinGameCommand()
	{
		
	}
	
	public void setPlayerNumber(int playerNumber)
	{
		this.playerNumber = playerNumber;
	}
	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
	}
	public void setGameId(String gameId)
	{
		this.gameId = gameId;
	}
	
	public int getPlayerNumber() {
		return playerNumber;
	}
	public String getPlayerName() {
		return playerName;
	}
	public String getGameId()
	{
		return gameId;
	}
	
	private String gameId;
	private int playerNumber;
	private String playerName;

}
