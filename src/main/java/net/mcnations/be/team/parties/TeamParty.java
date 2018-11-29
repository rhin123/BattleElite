package net.mcnations.be.team.parties;

import java.util.ArrayList;
import java.util.List;

public class TeamParty {
	
	private int size;
	private List<String> players = new ArrayList<String>();
	
	public TeamParty(String... strings) { 
		
		this.size = strings.length;
		
		for (String s : strings)
			players.add(s);
		
	}
	
	public int getSize() { return size; }
	public List<String> getPlayers() { return players; }

}
