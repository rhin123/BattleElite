package net.mcnations.be.utils.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

import net.mcnations.be.utils.misc.Debug;
import net.mcnations.be.utils.particles.PlayerParticle;
import net.mcnations.core.CorePlugin;
import net.mcnations.core.common.enums.GameType;
import net.mcnations.core.database.DatabaseEngine;

public class BattlePlayer {

	private final UUID uuid;

	private int kills, deaths, wins, losses, draws;
	private List<String> unlockedKits = new ArrayList<>();
	private List<String> unlockedParticles = new ArrayList<>();
	private String selectedKit;
	
	private String selectedPlayerParticle, selectedArrowParticle;
	//private List<String> selectedParticles = new ArrayList<>();
	public BattlePlayer(UUID uuid) {
		this.uuid = uuid;

		CorePlugin.getCorePlugin().getScheduler().runTaskAsynchronously(CorePlugin.getCorePlugin(), () -> {
			DatabaseEngine databaseEngine = CorePlugin.getCorePlugin().getDatabaseEngine();
			BasicDBObject searchQuery = new BasicDBObject("uuid", uuid.toString());
			DBCursor result = databaseEngine.findQuery(GameType.BATTLES_ELITE.getDatabaseName(), searchQuery);
			if (result.hasNext()) {
				this.setKills(databaseEngine.getInteger(result, "kills"));
				this.setDeaths(databaseEngine.getInteger(result, "deaths"));
				this.setWins(databaseEngine.getInteger(result, "wins"));
				this.setLosses(databaseEngine.getInteger(result, "losses"));
				this.setDraws(databaseEngine.getInteger(result, "draws"));
				this.unlockedKits = databaseEngine.getStringList(result, "unlockedKits");
				this.unlockedParticles = databaseEngine.getStringList(result, "unlockedParticles");
				
				this.selectedKit = databaseEngine.getString(result, "selectedKit");
				
				this.selectedPlayerParticle = databaseEngine.getString(result, "selectedPlayerParticle");
				this.selectedArrowParticle = databaseEngine.getString(result, "selectedArrowParticle");
				
			} else {
				searchQuery.clear();
				searchQuery.put("uuid", uuid.toString());
				searchQuery.put("kills", 0);
				searchQuery.put("deaths", 0);
				searchQuery.put("wins", 0);
				searchQuery.put("losses", 0);
				searchQuery.put("unlockedKits", unlockedKits);
				searchQuery.put("unlockedParticles", unlockedParticles);
				
				searchQuery.put("selectedKit", selectedKit);
				
				searchQuery.put("selectedPlayerParticle", selectedPlayerParticle);
				searchQuery.put("selectedArrowParticle", selectedArrowParticle);
				
				databaseEngine.insertQuery(GameType.BATTLES_ELITE.getDatabaseName(), searchQuery);
			}
			;
			result.close();
		});

	}

	public UUID getUUID() {
		return uuid;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public int getDraws() {
		return draws;
	}

	public void setDraws(int draws) {
		this.draws = draws;
	}

	public void addKill() {
		this.kills++;
	}

	public void addDeath() {
		this.deaths++;
	}

	public void addWin() {
		this.wins++;
	}

	public void addLoss() {
		this.losses++;
	}

	public void addDraw() {
		this.draws++;
	}
	
	public String getSelectedKit()
	{
		return this.selectedKit;
	}
	
	public String getSelectedParticle(String whatUse)
	{
		if(whatUse == "player")
		return this.selectedPlayerParticle;
		
		if(whatUse == "arrow")
			return this.selectedArrowParticle;
		
		return null;
	}
	
	public void setSelectedKit(String str)
	{
		this.selectedKit = str;
	}
	
	public void setSelectedParticle(String whatUse, String particle)
	{
		
		Bukkit.broadcastMessage("Use: "+whatUse);
		Bukkit.broadcastMessage("Particle: "+particle);
		
		if(whatUse == "player")
			this.selectedPlayerParticle = particle;
		if(whatUse == "arrow")
			this.selectedArrowParticle = particle;
	}

	public void save() {
		CorePlugin.getCorePlugin().getScheduler().runTaskAsynchronously(CorePlugin.getCorePlugin(), () -> {
			DatabaseEngine databaseEngine = CorePlugin.getCorePlugin().getDatabaseEngine();
			BasicDBObject searchQuery = new BasicDBObject("uuid", uuid.toString());
			DBCursor result = databaseEngine.findQuery(GameType.BATTLES_ELITE.getDatabaseName(), searchQuery);
			if (result.hasNext()) {
				BasicDBObject updated = new BasicDBObject();
				updated.putAll(result.next());

				updated.replace("kills", kills);
				updated.replace("deaths", deaths);
				updated.replace("wins", wins);
				updated.replace("losses", losses);
				updated.replace("draws", draws);
				updated.replace("unlockedKits", unlockedKits);
				
				if(updated.containsKey("unlockedParticles"))
					updated.replace("unlockedParticles", unlockedParticles);
				updated.put("unlockedParticles", unlockedParticles);
				
				if(updated.containsKey("selectedKit"))
					updated.replace("selectedKit", selectedKit);
				updated.put("selectedKit", selectedKit);
				
				if(updated.containsKey("selectedPlayerParticle"))
					updated.replace("selectedPlayerParticle", selectedPlayerParticle);
				updated.put("selectedPlayerParticle", selectedPlayerParticle);
				
				if(updated.containsKey("selectedArrowParticle"))
					updated.replace("selectedArrowParticle", selectedArrowParticle);
				updated.put("selectedArrowParticle", selectedArrowParticle);
				
				databaseEngine.updateQuery(GameType.BATTLES_ELITE.getDatabaseName(), searchQuery, updated);
			}
			result.close();
		});
	}

	public List<String> getKits() {
		if (unlockedKits == null)
			return unlockedKits = new ArrayList<String>();

		return unlockedKits;
	}

	public List<String> getPlayerParticles() {
		if (unlockedParticles == null)
			return unlockedParticles = new ArrayList<String>();

		return unlockedParticles;
	}

	public void unlockKit(String string) {
		if (!unlockedKits.contains(string))
			unlockedKits.add(string);
	}

	public void unlockPlayerParticle(String string) {
		
		if (!unlockedParticles.contains(string))
			this.unlockedParticles.add(string);
	}

}