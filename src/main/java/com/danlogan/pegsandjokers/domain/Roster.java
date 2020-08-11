package com.danlogan.pegsandjokers.domain;

import java.util.ArrayList;

public class Roster {
	
	//Roster Properties
	private String gameId;
	private int numberOfPlayers;
	private ArrayList<String> playerNames;
	
	//Roster Builder
	public static class Builder {
		String gameId;
		int numberOfPlayers;
		ArrayList<String> playerNames = new ArrayList<String>();
		
		public static Builder newInstance()
		{
			return new Builder();
		}
		
		public Roster build()
		{			
			return new Roster(this);	
		}
		
		public Builder withGameId(String gameId)
		{
			this.gameId = gameId;
			return this;
		}
		
		public Builder withNumberOfPlayers(int number)
		{
			this.numberOfPlayers = number;
			for(int i = 0; i<number; i++)
			{
				this.playerNames.add("");
			}
			return this;
		}
	}
	
	//Roster Methods
	public Roster(Builder builder)
	{
		//set Roster from builder
		this.gameId = builder.gameId;
		this.numberOfPlayers = builder.numberOfPlayers;
		this.playerNames = builder.playerNames;
	}
	
	public String getGameId()
	{
		return this.gameId;
	}
	
	public int getNumberOfPlayers()
	{
		return this.numberOfPlayers;
	}
	
	public ArrayList<String> getPlayerNames()
	{
		return this.playerNames;
	}
	
	public String getPlayerName(int playerNumber)
	{
		if (playerNumber > 0 && playerNumber <= playerNames.size())
		{
			return playerNames.get(playerNumber-1);
		}
		else
		{
			throw new RuntimeException(String.format("%d is not a valid player Number for this roster", playerNumber));
		}
	}
	
	public void assignSeat(int playerNumber, String playerName)
	{
		if (playerName == null || playerName.equals(""))
		{
			throw new RuntimeException("Player name cannot be blank");
		}

		if (playerNumber > 0 && playerNumber <= playerNames.size())
		{
			if(playerNames.get(playerNumber-1).equals(""))
			{
				playerNames.set(playerNumber-1, playerName);
			}
			else
			{
				throw new RuntimeException("That seat is taken");
			}
		}
		else
		{
			throw new RuntimeException(String.format("%d is not a valid player Number for this roster", playerNumber));
		}
	
	}

	public int assignNextAvailableSeat(String playerName) {
		
		for(int i=0;i<this.playerNames.size();i++)
		{
			if (this.playerNames.get(i).equals(""))
			{
				this.playerNames.set(i, playerName);
				return i+1;
			}
		}
		
		return -1;
	}

}
