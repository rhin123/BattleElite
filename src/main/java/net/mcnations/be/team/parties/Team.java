package net.mcnations.be.team.parties;

import java.util.ArrayList;
import java.util.List;

public class Team {
	
	private final int Size;
	private List<TeamParty> Parties;
	
	public Team(int size) { Size = size; Parties = new ArrayList<TeamParty>(); }
	
	public int getSize() { return Size; }
	public int getTaken() {
		int total = 0;
		for (TeamParty p : Parties)
			total+= p.getSize();
		return total;
	}
	public void addParty(TeamParty p) { Parties.add(p); }
	public List<TeamParty> getParties() { return Parties; }
	public void reset() { Parties.clear(); }
	public List<String> getPlayers()
	{
		List<String> players = new ArrayList<String>();
		for (TeamParty p : Parties) 
		for (String playername : p.getPlayers()) {
			players.add(playername);
		}
		return players;
	}
	
	public String getShowPlayers() {
		String players = "";
		
		for (TeamParty p : Parties) {
			
			players+= "[";
			boolean first = true;
			
			for (String playername : p.getPlayers()) {
				players+= first ? playername : "," + playername;
				first = false;
			}
			
			players+= "]";
			
		}
		
		return players;
	}

}
