package com.danlogan.pegsandjokers.domain;

public class BoardPosition {
	
	private Peg peg;
	private String id;
	private boolean startPosition;
	private boolean homePosition;
	private boolean mainTrackPosition;
	private int homePositionNumber;
	private boolean hasPeg;
	private Color pegColor;
	private Integer pegNumber;

	public BoardPosition(String id) {
		this.id = id;
		this.startPosition = this.id.contains("Start");
		this.homePosition = this.id.contains("Home");
		if (this.startPosition || this.homePosition)
		{
			this.mainTrackPosition = false;
		}
		else { this.mainTrackPosition = true; }
		if (homePosition)
		{
				homePositionNumber =  Integer.parseInt(this.id.substring(this.id.indexOf("-")+1));	
		}
		else
		{
			homePositionNumber = -1;
		}
		this.hasPeg=false;
		this.pegColor=null;
		this.pegNumber=null;
	}
	
	//Constructor for start positions where Peg is inserted as board is laid out
	public BoardPosition(Peg peg, String id)
	{
		this.peg = peg;
		this.id = id;
		this.startPosition = this.id.contains("Start");
		this.homePosition = this.id.contains("Home");
		if (this.startPosition || this.homePosition)
		{
			this.mainTrackPosition = false;
		}
		else { this.mainTrackPosition = true; }
		if (homePosition)
		{
				homePositionNumber =  Integer.parseInt(this.id.substring(this.id.indexOf("-")+1));	
		}
		else
		{
			homePositionNumber = -1;
		}
		if(peg == null)
		{
			this.hasPeg = false;
			this.pegColor = null;
			this.pegNumber = null;
		}
		else {this.hasPeg = true; this.pegColor = peg.getColor(); this.pegNumber = Integer.valueOf(peg.getNumber());}
	}
	
	public boolean getHasPeg()
	{
			
		return this.hasPeg;
	}
	
	public Color getPegColor()
	{
		return this.pegColor;
	}
	
	public Peg getPeg()
	{
		return this.peg;
	}
	
	public Integer getPegNumber()
	{
		return this.pegNumber;
	}
		
	public String getId()
	{
		return this.id;
	}
	
	public boolean isStartPosition()
	{
		return this.startPosition;
	}
	
	public boolean isHomePosition()
	{
		return this.homePosition;
	}
	
	public int getHomePositionNumber()
	{
		return this.homePositionNumber;
	}
	
	public Peg removePeg()
	{
		Peg temp = this.peg;
		this.peg = null;
		this.hasPeg = false;
		this.pegColor = null;
		return temp;
	}
	
	public void addPeg(Peg pegToAdd)
	{
		this.peg = pegToAdd;
		this.hasPeg = true;
		this.pegColor = pegToAdd.getColor();
	}

	public boolean isMainTrackPosition() {

		return this.mainTrackPosition;
		
	}
	
	//Default constructor to support deserialization
	public BoardPosition()
	{
		
	}
}
