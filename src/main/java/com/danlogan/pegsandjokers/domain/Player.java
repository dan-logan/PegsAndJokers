package com.danlogan.pegsandjokers.domain;


public class Player {

	//Player properties
	private final String name;

	//Player Class Builder
	public static class Builder{

		private String name;

		public static Builder newInstance() {
			return new Builder();
		}

		private Builder() {}

		//setter methods to configure builder

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		//build method to return a new instance from Builder
		public Player build() {
			return new Player(this);
		}
	}

	//Player Class Methods
	public Player(Builder builder)
	{
		//set all properties from the builder
		this.name = builder.name;
	}

	public String getName() {
		return name;
	}
}

