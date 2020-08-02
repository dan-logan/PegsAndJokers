package com.danlogan.pegsandjokers.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.danlogan.pegsandjokers.infrastructure.RosterRepository;

public class RosterUnitTests {
	
	@Test
	public void testCreatingRoster()
	{
		Roster roster = Roster.Builder.newInstance().build();
		assertThat(roster).isNotNull();
	}
	
	@Test
	public void testCreateRosterForGameId()
	{
		Roster roster = Roster.Builder.newInstance()
				.withGameId("myGameID")
				.build();
		
		assertThat(roster.getGameId()).isEqualTo("myGameID");
	}
	
	@Test
	public void testCreateRosterForTwoPlayers()
	{
		Roster roster = Roster.Builder.newInstance()
				.withGameId("myGameID")
				.withNumberOfPlayers(2)
				.build();
		
		assertThat(roster.getNumberOfPlayers()).isEqualTo(2);
	}
	
	@Test
	public void testListOfPlayersInitiallyBlank()
	{
		Roster roster = Roster.Builder.newInstance()
				.withGameId("myGameID")
				.withNumberOfPlayers(2)
				.build();
		
		assertThat(roster.getPlayerNames().size()).isEqualTo(2);
		assertThat(roster.getPlayerNames().stream().allMatch(name -> name=="")).isTrue();
	}
	
	@Test
	public void testJoinRosterWithSpecificSeat()
	{
		Roster roster = Roster.Builder.newInstance()
				.withGameId("myGameID")
				.withNumberOfPlayers(3)
				.build();
		
		assertThat(roster.getPlayerNames().size()).isEqualTo(3);
	
		roster.assignSeat(2,"Bubba");
		assertThat(roster.getPlayerName(1)).isEqualTo("");
		assertThat(roster.getPlayerName(2)).isEqualTo("Bubba");
		assertThat(roster.getPlayerName(3)).isEqualTo("");
	}
	
	@Test
	public void testCannotJoinIfSeatIsTaken()
	{
		Roster roster = Roster.Builder.newInstance()
				.withGameId("myGameID")
				.withNumberOfPlayers(3)
				.build();
		
		assertThat(roster.getPlayerNames().size()).isEqualTo(3);
	
		roster.assignSeat(2,"Bubba");
		
		try {
			roster.assignSeat(2,"Gump");
			assert(false);
		}
		catch (Throwable e)
		{
			assertThat(e.getMessage()).isEqualTo("That seat is taken");
		}
	}
	
	@Test 
	public void testPlayerNameCannotBeBlank()
	{
		Roster roster = Roster.Builder.newInstance()
				.withGameId("myGameID")
				.withNumberOfPlayers(3)
				.build();
		
		assertThat(roster.getPlayerNames().size()).isEqualTo(3);
	
		try {
			roster.assignSeat(2,"");
			assert(false);
		}
		catch (Throwable e)
		{
			assertThat(e.getMessage()).isEqualTo("Player name cannot be blank");
		}

		
	}
	
	@Test
	public void testRosterRespositorySaveAndFind()
	{
		RosterRepository repo = new RosterRepository();
		
		Roster roster = Roster.Builder.newInstance()
				.withGameId("myGameID")
				.withNumberOfPlayers(3)
				.build();

		repo.save(roster);
		
		Roster roster2 = repo.findRosterById("myGameID");
		
		assertThat(roster2).isNotNull();

		assertThat(roster).isEqualToComparingFieldByField(roster2);
		
	}

}
