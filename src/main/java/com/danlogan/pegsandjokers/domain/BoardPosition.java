package com.danlogan.pegsandjokers.domain;

public class BoardPosition {
	
	Peg peg;
	String id;

	public BoardPosition(String id) {
		this.id = id;
	}
	
	//Constructor for start positions where Peg is inserted as board is laid out
	public BoardPosition(Peg peg, String id)
	{
		this.peg = peg;
		this.id = id;
	}
	
	public boolean getHasPeg()
	{
		if(peg == null)
		{
			return false;
		}
		
		return true;
	}
	
	public Color getPegColor()
	{
		return getHasPeg() ? peg.getColor() : null;
	}
	
	public String getId()
	{
		return this.id;
	}
}
