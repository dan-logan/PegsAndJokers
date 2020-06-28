package com.danlogan.pegsandjokers.domain;

public class Joker extends Card {
	
	public Joker()
	{
		super(null,null);
		this.joker=true;
	}
	
	public String getName() {
		
		return "JOKER";
	}

}
