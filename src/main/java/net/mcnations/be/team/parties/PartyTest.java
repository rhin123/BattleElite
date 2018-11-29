package net.mcnations.be.team.parties;

import org.bukkit.Bukkit;

public class PartyTest {
	
	public static Team[] Teams = new Team[4];
	private static DoubleLinkedPartyList header;
	
	public static void main(String[] args) {
		
		System.out.println("Running");
		
		Teams[0] = new Team(2);
		Teams[1] = new Team(2);
		Teams[2] = new Team(2);
		Teams[3] = new Team(2);
		
		addNewParty("Craftiii4","rhin");
		addNewParty("player1");
		addNewParty("player2");
		addNewParty("player3");
		addNewParty("mak","jonas");
		addNewParty("player4");
		
		System.out.println("Ending");
		
	}
	
	public static void addToTeams() {
		
		DoubleLinkedPartyList pointer = header;
		
		while (pointer != null) {
			
			Team team = getTeamWithMostOpenings();
			
			team.addParty(pointer.getCurrent());
			pointer = pointer.getFooter();
			
		}
		
		int i = 1;
		for (Team team : Teams)
			Bukkit.broadcastMessage("Balancing: "+"Team" + i++ + " (" + team.getSize() + ") : " + team.getShowPlayers());
		
	}
	
	public static void removePlayer(String player)
	{
		for(int i = 0; i < 4; i++)
		if(Teams[i].getPlayers().contains(player))
			Teams[i].getPlayers().remove(player);
	}
	
	private static Team getTeamWithMostOpenings() {
		
		int teamN = 0;
		int smallest = Teams[0].getSize() - Teams[0].getTaken();
		
		for (int i=1; i<Teams.length; i++) {
			
			int open = Teams[i].getSize() - Teams[i].getTaken();
			
			if (smallest < open) {
				smallest = open;
				teamN = i;
			}
			
		}
		
		return Teams[teamN];
		
	}
	
	public static void addNewParty(String... strings) {
		
		System.out.println("Adding new party of size: " + strings.length);
		for (Team team : Teams)
			team.reset();
		
		TeamParty party = new TeamParty(strings);
		
		if (header == null)
			header = new DoubleLinkedPartyList(party);
		else {
			
			DoubleLinkedPartyList pointer = header;
			boolean done = false;
			
			while (party.getSize() <= pointer.getCurrent().getSize()) {
				
				if (pointer.getFooter() == null) {
					DoubleLinkedPartyList newPointer = new DoubleLinkedPartyList(party);
					pointer.setFooter(newPointer);
					newPointer.setHeader(pointer);
					done = true;
					break;
				}
				
				pointer = pointer.getFooter();
				
			}
			
			if (!done) {
				
				DoubleLinkedPartyList newPointer = new DoubleLinkedPartyList(party, pointer.getHeader(), pointer);
				
				if (pointer.getHeader() != null)
					pointer.getHeader().setFooter(newPointer);
				else
					header = newPointer;
				
				pointer.setHeader(newPointer);
				
			}
			
		}
		
		addToTeams();
		
	}
	
}
