package net.mcnations.be.team.parties;

public class DoubleLinkedPartyList {
	
	private DoubleLinkedPartyList head, footer;
	private TeamParty current;
	
	public DoubleLinkedPartyList(TeamParty party) {
		current = party;
		head = null;
		footer = null;
	}
	
	public DoubleLinkedPartyList(TeamParty party, DoubleLinkedPartyList head, DoubleLinkedPartyList footer) {
		current = party;
		this.head = head;
		this.footer = footer;
	}
	
	public DoubleLinkedPartyList getHeader() { return head; }
	public DoubleLinkedPartyList getFooter() { return footer; }
	public TeamParty getCurrent() { return current; }
	
	public void setHeader(DoubleLinkedPartyList p) { head = p; }
	public void setFooter(DoubleLinkedPartyList p) { footer = p; }

}
