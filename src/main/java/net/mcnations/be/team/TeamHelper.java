package net.mcnations.be.team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import ca.wacos.nametagedit.NametagAPI;
import net.mcnations.be.BattleElite;
import net.mcnations.be.game.GameUI;
import net.mcnations.be.game.states.InGame;
import net.mcnations.be.utils.RandomNumber;
import net.mcnations.be.world.WorldHelper;
import net.mcnations.core.common.features.party.Party;
import net.mcnations.core.common.features.party.PartyAPI;

public class TeamHelper {

	public static List<Player> blueplayer = new ArrayList<Player>();
	public static List<Player> redplayer = new ArrayList<Player>();
	public static List<Player> greenplayer = new ArrayList<Player>();
	public static List<Player> yellowplayer = new ArrayList<Player>();
	public static List<Location> savedLocations = new ArrayList<Location>();
	public static HashMap<Player, String> oldTeam = new HashMap<>();
	
	public static int getTeamDifferece(String team) {
		if (team == "blue")
			return blueplayer.size() - (redplayer.size() + greenplayer.size() + yellowplayer.size());
		if (team == "red")
			return redplayer.size() - (blueplayer.size() + greenplayer.size() + yellowplayer.size());
		if (team == "green")
			return greenplayer.size() - (redplayer.size() + blueplayer.size() + yellowplayer.size());
		if (team == "yellow")
			return yellowplayer.size() - (redplayer.size() + greenplayer.size() + blueplayer.size());

		return -1;
	}

	public static boolean isAllEqual(int[] a) {
		for (int i = 1; i < a.length; i++) {
			if (a[0] != a[i]) {
				return false;
			}
		}

		return true;
	}

	public static int getMax(int[] array) {
		int largest = Collections.max(Arrays.asList(array[0], array[1], array[2], array[3]));

		return largest;
	}

	public static int getMin(int[] array) {
		int largest = Collections.min(Arrays.asList(array[0], array[1], array[2], array[3]));

		return largest;
	}

	public static boolean isTeamBalanced(String team) {
		int b = blueplayer.size();
		int r = redplayer.size();
		int g = greenplayer.size();
		int y = yellowplayer.size();

		int[] teamSizes = { b, r, g, y };

		int minnum = getMin(teamSizes);
		// BattleElite.getPlugin().getLogger().info("B: "+b+" R: "+r+" G: "+g+"
		// Y: "+y);
		// BattleElite.getPlugin().getLogger().info("Maxnum: "+maxnum);

		// if ((b < maxnum && b != minnum) || isAllEqual(teamSizes))
		switch (team) {
		case "blue":
			if (isAllEqual(teamSizes) || b == minnum)
				return true;
			break;
		case "red":
			if (isAllEqual(teamSizes) || r == minnum)
				return true;
			break;
		case "green":
			if (isAllEqual(teamSizes) || g == minnum)
				return true;
			break;
		case "yellow":
			if (isAllEqual(teamSizes) || y == minnum)
				return true;
			break;
		}

		return false;
	}

	public static void clearTeams() {
		blueplayer.clear();
		redplayer.clear();
		greenplayer.clear();
		yellowplayer.clear();
		savedLocations.clear();
		oldTeam.clear();
		for (Player player : Bukkit.getOnlinePlayers())
			NametagAPI.setPrefix(player.getName(), ChatColor.WHITE + "");
	}

	public static void addTeam(String team, Player player) {
		if (team == "blue") {
			removePlayerFromTeam(player);
			TeamQue.removePlayer(player);
			blueplayer.add(player);
		} else if (team == "red") {
			removePlayerFromTeam(player);
			TeamQue.removePlayer(player);
			redplayer.add(player);
		} else if (team == "green") {
			removePlayerFromTeam(player);
			TeamQue.removePlayer(player);
			greenplayer.add(player);
		} else if (team == "yellow") {
			removePlayerFromTeam(player);
			TeamQue.removePlayer(player);
			yellowplayer.add(player);
			// Nametag color moved to playerSpawn in game class
		}
	}

	// I know I know, i could of used blueplayer.contains, but im autistic.
	public static void removePlayerFromTeam(Player player) {
		NametagAPI.setPrefix(player.getName(), ChatColor.WHITE + "");
		for (int i = 0; i < TeamHelper.blueplayer.size(); i++)
			if (player == TeamHelper.blueplayer.get(i))
				TeamHelper.blueplayer.remove(i);
		for (int i = 0; i < TeamHelper.redplayer.size(); i++)
			if (player == TeamHelper.redplayer.get(i))
				TeamHelper.redplayer.remove(i);
		for (int i = 0; i < TeamHelper.greenplayer.size(); i++)
			if (player == TeamHelper.greenplayer.get(i))
				TeamHelper.greenplayer.remove(i);
		for (int i = 0; i < TeamHelper.yellowplayer.size(); i++)
			if (player == TeamHelper.yellowplayer.get(i))
				TeamHelper.yellowplayer.remove(i);
	}

	public static String getPlayerTeam(Player player) {
		for (int i = 0; i < TeamHelper.blueplayer.size(); i++)
			if (player == TeamHelper.blueplayer.get(i))
				return "blue";
		for (int i = 0; i < TeamHelper.redplayer.size(); i++)
			if (player == TeamHelper.redplayer.get(i))
				return "red";
		for (int i = 0; i < TeamHelper.greenplayer.size(); i++)
			if (player == TeamHelper.greenplayer.get(i))
				return "green";
		for (int i = 0; i < TeamHelper.yellowplayer.size(); i++)
			if (player == TeamHelper.yellowplayer.get(i))
				return "yellow";
		return "none";
	}

	public static String getTeamColor(Player player) {
		String playerTeam = getPlayerTeam(player);
		switch (playerTeam) {
		case "blue":
			return ChatColor.translateAlternateColorCodes('&', "&3");
		case "red":
			return ChatColor.translateAlternateColorCodes('&', "&4");
		case "green":
			return ChatColor.translateAlternateColorCodes('&', "&2");
		case "yellow":
			return ChatColor.translateAlternateColorCodes('&', "&e");
		default:
			return ChatColor.BLACK + "**Dead** " + ChatColor.GRAY;
		}

	}
	public static void setRandomTeam(Player player) {
		
		if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_LOBBY")) {
			
			
			Random rnd = new Random();

			int teamNum = rnd.nextInt(4);
			while (TeamHelper.getPlayerTeam(player) == "none") {
				switch (teamNum) {
				case 0:
					if (TeamHelper.isTeamBalanced("blue"))
						GameUI.setTeamBlue(player, false);
					break;
				case 1:
					if (TeamHelper.isTeamBalanced("red"))
						GameUI.setTeamRed(player, false);
					break;
				case 2:
					if (TeamHelper.isTeamBalanced("green"))
						GameUI.setTeamGreen(player, false);
					break;
				case 3:
					if (TeamHelper.isTeamBalanced("yellow"))
						GameUI.setTeamYellow(player, false);
					break;

				}
				teamNum = rnd.nextInt(4);
			}
		} else if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME"))
			player.sendMessage(ChatColor.GOLD + "Game is currently in progress");

	}

	public static int playersReady() {
		return Bukkit.getOnlinePlayers().size();
	}

	public static int getEnemyTeamSize(String team) {

		switch (team) {
		case "blue":
			return redplayer.size() + greenplayer.size() + yellowplayer.size();

		case "red":
			return blueplayer.size() + greenplayer.size() + yellowplayer.size();

		case "green":
			return redplayer.size() + blueplayer.size() + yellowplayer.size();

		case "yellow":
			return redplayer.size() + greenplayer.size() + blueplayer.size();
		}

		return 0;

	}

	public static int getTeamSize(String team) {

		switch (team) {

		case "blue":
			return blueplayer.size();

		case "red":
			return redplayer.size();

		case "green":
			return greenplayer.size();

		case "yellow":
			return yellowplayer.size();

		case "none":
			return Bukkit.getOnlinePlayers().size()
					- (blueplayer.size() + redplayer.size() + greenplayer.size() + yellowplayer.size());

		}

		return 0;

	}

	//
	public static void teleportToTeam(Player player, String team) {
		Location loc;
		int x, z;

		switch (team) {
		case "blue":
			x = BattleElite.getCorePlugin().getConfig().getInt(WorldHelper.getPlayWorldName() + ".Blue.x");
			z = BattleElite.getCorePlugin().getConfig().getInt(WorldHelper.getPlayWorldName() + ".Blue.z");

			x = x + RandomNumber.getRandomNumber(3);
			z = z + RandomNumber.getRandomNumber(3);

			loc = new Location(WorldHelper.getPlayWorld(), x,
					BattleElite.getCorePlugin().getConfig().getInt(WorldHelper.getPlayWorldName() + ".Blue.y"), z);

			if (!savedLocations.contains(loc)) {
				player.teleport(loc);
				savedLocations.add(loc);
			} else
				teleportToTeam(player, TeamHelper.getPlayerTeam(player));

			break;

		case "red":
			x = BattleElite.getCorePlugin().getConfig().getInt(WorldHelper.getPlayWorldName() + ".Red.x");
			z = BattleElite.getCorePlugin().getConfig().getInt(WorldHelper.getPlayWorldName() + ".Red.z");

			x = x + RandomNumber.getRandomNumber(3);
			z = z + RandomNumber.getRandomNumber(3);

			loc = new Location(WorldHelper.getPlayWorld(), x,
					BattleElite.getCorePlugin().getConfig().getInt(WorldHelper.getPlayWorldName() + ".Red.y"), z);

			if (!savedLocations.contains(loc)) {
				player.teleport(loc);
				savedLocations.add(loc);
			} else
				teleportToTeam(player, TeamHelper.getPlayerTeam(player));

			break;
		case "green":
			x = BattleElite.getCorePlugin().getConfig().getInt(WorldHelper.getPlayWorldName() + ".Green.x");
			z = BattleElite.getCorePlugin().getConfig().getInt(WorldHelper.getPlayWorldName() + ".Green.z");

			x = x + RandomNumber.getRandomNumber(3);
			z = z + RandomNumber.getRandomNumber(3);

			loc = new Location(WorldHelper.getPlayWorld(), x,
					BattleElite.getCorePlugin().getConfig().getInt(WorldHelper.getPlayWorldName() + ".Green.y"), z);

			if (!savedLocations.contains(loc)) {
				player.teleport(loc);
				savedLocations.add(loc);
			} else
				teleportToTeam(player, TeamHelper.getPlayerTeam(player));

			break;
		case "yellow":
			x = BattleElite.getCorePlugin().getConfig().getInt(WorldHelper.getPlayWorldName() + ".Yellow.x");
			z = BattleElite.getCorePlugin().getConfig().getInt(WorldHelper.getPlayWorldName() + ".Yellow.z");

			x = x + RandomNumber.getRandomNumber(3);
			z = z + RandomNumber.getRandomNumber(3);

			loc = new Location(WorldHelper.getPlayWorld(), x,
					BattleElite.getCorePlugin().getConfig().getInt(WorldHelper.getPlayWorldName() + ".Yellow.y"), z);

			if (!savedLocations.contains(loc)) {
				player.teleport(loc);
				savedLocations.add(loc);
			} else
				teleportToTeam(player, TeamHelper.getPlayerTeam(player));

			break;
		}

	}

	public static List<Player> getTeam(String team) {
		switch (team) {
		case "blue":
			return blueplayer;
		case "red":
			return redplayer;
		case "green":
			return greenplayer;
		case "yellow":
			return yellowplayer;

		default:
			return null;
		}
	}

	public static void addOldTeam(Player player) {
		String oldteam = TeamHelper.getPlayerTeam(player);
		oldTeam.put(player, oldteam);
	}

	public static String getOldTeam(Player player) {
		return oldTeam.get(player);
	}

	private static boolean isPlayerInTeam(Player player) {
		if (blueplayer.contains(player) || redplayer.contains(player) || greenplayer.contains(player)
				|| yellowplayer.contains(player))
			return true;
		return false;
	}

	private static String getSmallestTeam(int point) {
		int blue, red, green, yellow;

		blue = 5;
		red = 3;
		green = 6;
		yellow = 6;

		if(blue == getSmallestTeamSize(point))
			return "blue";
		if(red == getSmallestTeamSize(point))
			return "red";
		if(green == getSmallestTeamSize(point))
			return "green";
		if(yellow == getSmallestTeamSize(point))
			return "yellow";
		
		return "none";
	}
	
	private static int getSmallestTeamSize(int point) {
		int blue, red, green, yellow;

		blue = blueplayer.size();
		red = redplayer.size();
		green = greenplayer.size();
		yellow = yellowplayer.size();

		List<Integer> sizes = Arrays.asList(blue, red, green, yellow);
		
		Collections.sort(sizes);
		
		return sizes.get(point);
	}
	
	private static int getBiggestTeamSize() {
		int blue, red, green, yellow;

		blue = blueplayer.size();
		red = redplayer.size();
		green = greenplayer.size();
		yellow = yellowplayer.size();

		List<Integer> sizes = Arrays.asList(blue, red, green, yellow);
		
		return Collections.max(sizes);
	}

	public static boolean isPlayerOnWinningTeam(Player player) {
		if(TeamHelper.getPlayerTeam(player) == InGame.getTeamNameRaw())
			return true;
		
		if(getOldTeam(player) == InGame.getTeamNameRaw())
			return true;
		
		return false;
	}

	public static void setPartyTeam(Player player, Party party) {
		player.sendMessage(ChatColor.GOLD+"Setting your team to your specified party...");
		removePlayerFromTeam(player);
		
		int blueSize = blueplayer.size();
		int redSize = redplayer.size();
		int greenSize = greenplayer.size();
		int yellowSize = yellowplayer.size();
		
		
		int partySize = party.getCurrentOnline().size();
		
		
		if((getBiggestTeamSize() - getSmallestTeamSize(0)) <= partySize)
		{
			for(UUID partyPlayer : party.getCurrentOnline())
			TeamHelper.addTeam(getSmallestTeam(0), Bukkit.getPlayer(partyPlayer));
			
			Bukkit.broadcastMessage("A team fits for the party!");
		}else if((getBiggestTeamSize() - getSmallestTeamSize(0)) > partySize) //ELSE IF THE SMALLEST TEAM IS FULL
		{
			Bukkit.broadcastMessage("Moving players to fit the party!");
			int requiredPlayersToMove = getSmallestTeamSize(0) - party.getCurrentOnline().size();
			
			int moved = 0;
			
			for(Player teamPlayer : TeamHelper.getTeam(getSmallestTeam(0)))
			{
				if(PartyAPI.getAssociatedParty(teamPlayer.getUniqueId()) == null)
				{
					if(moved < requiredPlayersToMove)
					{
					TeamHelper.removePlayerFromTeam(teamPlayer);
					moved++;
					GameUI.setTeam(getSmallestTeam(1), teamPlayer);
					}
				}
			}
			
		}
		
	}

}
