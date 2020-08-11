package com.danlogan.pegsandjokers.commands;

public class JoinGameNextAvailableSeatCommand {
	
	@Override
	public String toString() {
		return "JoinGameNextAvailableSeatCommand [gameId=" + gameId+ ", playerName=" + playerName + "]";
	}
	public JoinGameNextAvailableSeatCommand(String gameId, String playerName) {
		super();
		this.playerName = playerName;
		this.gameId = gameId;
	}
	
	public JoinGameNextAvailableSeatCommand()
	{
		
	}
	
	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
	}
	public void setGameId(String gameId)
	{
		this.gameId = gameId;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	public String getGameId()
	{
		return gameId;
	}
	
	private String gameId;
	private String playerName;

}
