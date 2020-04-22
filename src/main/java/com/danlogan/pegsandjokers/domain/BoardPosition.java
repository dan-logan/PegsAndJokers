package com.danlogan.pegsandjokers.domain;

public class BoardPosition {
	
	Peg peg;

	public BoardPosition() {
		// TODO Auto-generated constructor stub
	}
	
	//Constructor for start positions where Peg is inserted as board is laid out
	public BoardPosition(Peg peg)
	{
		this.peg = peg;
	}
	
	public boolean getHasPeg()
	{
		if(peg == null)
		{
			return false;
		}
		
		return true;
	}
	
	public Peg getPeg()
	{
		return peg;
	}

}
