package com.danlogan.pegsandjokers.domain;

import java.util.ArrayList;

public class Side {
	
	//Side Fields
	private Color color;
	private ArrayList<BoardPosition> homePositions;
	private ArrayList<BoardPosition> startPositions;
	private ArrayList<BoardPosition> mainTrackPositions;
	private BoardPosition comeOutPosition;
	private BoardPosition readyToGoHomePosition;
	

	//Side Builder
	public static class Builder {
		
		//Builder fields
		Color sideColor;
		private ArrayList<BoardPosition> homePositions = new ArrayList<BoardPosition>();
		private ArrayList<BoardPosition> startPositions = new ArrayList<BoardPosition>();
		private ArrayList<BoardPosition> mainTrackPositions = new ArrayList<BoardPosition>();
		private BoardPosition comeOutPosition;
		private BoardPosition readyToGoHomePosition;
		
		public static Builder newInstance()
		{
			return new Builder();
		}
		
		public Builder withColor(Color color)
		{
			this.sideColor = color;
			return this;
		}
		
		public Side build()
		{
			//add 18 main track positions per side
			for(int i=1;i<19;i++)
			{
				BoardPosition boardPosition = new BoardPosition();
				mainTrackPositions.add(boardPosition);
			}
			
			//add 5 home positions per side
			for(int i=1;i<5;i++)
			{
				BoardPosition boardPosition = new BoardPosition();
				homePositions.add(boardPosition);
			}
			
			//add 5 start positions per side
			for(int i=1;i<5;i++)
			{
				BoardPosition boardPosition = new BoardPosition();
				startPositions.add(boardPosition);
			}

			return new Side(this);
		}
		
		private Builder() {}
		
	}
	
	
	//Side Methods
	public Side(Builder builder)
	{
		this.color = builder.sideColor;
		this.homePositions = builder.homePositions;
		this.startPositions = builder.startPositions;
		this.mainTrackPositions = builder.mainTrackPositions;
		this.comeOutPosition = builder.comeOutPosition;
		this.readyToGoHomePosition = builder.readyToGoHomePosition;
	
	}
	
	public Color getColor()
	{
		return this.color;
	}
	


}