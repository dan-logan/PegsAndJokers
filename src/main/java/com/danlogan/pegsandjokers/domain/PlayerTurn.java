package com.danlogan.pegsandjokers.domain;

public class PlayerTurn {
	
	private int playerNumber;
	private String cardName;
	private MoveType moveType;
	private int playerPositionNumber;
	
	public static class Builder
	{
		//Builder fields
		private int playerNumber;
		private String cardName;
		private MoveType moveType;
		private int playerPositionNumber;
		
		public static Builder newInstance()
		{
			return new Builder();
		}
		
		private Builder()
		{
			
		}
		
		public Builder withPlayerNumber(int number)
		{
			this.playerNumber = number;
			return this;
		}
		
		public Builder withCardName(String cardName)
		{
			this.cardName = cardName;
			return this;
		}
		
		public Builder withMoveType(MoveType moveType)
		{
			this.moveType = moveType;
			return this;
		}
		
		public Builder withPlayerPosition(int playerPositionNumber)
		{
			this.playerPositionNumber = playerPositionNumber;
			return this;
		}
		
		public PlayerTurn build()
		{
			return new PlayerTurn(this);
		}

		public Builder withPositionNumber(int positionNumber) {

			this.playerPositionNumber = positionNumber;
			
			return this;
		}
	}
	
	public PlayerTurn(Builder builder) {
		this.cardName = builder.cardName;
		this.moveType = builder.moveType;
		this.playerNumber = builder.playerNumber;
		this.playerPositionNumber = builder.playerPositionNumber;
	}

	public int getPlayerNumber() {
		return this.playerNumber;
	}

	public String getCardName() {
		return this.cardName;
	}

	public MoveType getMoveType() {
		return this.moveType;
	}

	public int getPlayerPositionNumber() {
		return this.playerPositionNumber;
	}

}
