package net.mcnations.be.team;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.mcnations.be.game.GameUI;
import net.mcnations.core.common.general.cache.MCNPlayerCache;
import net.mcnations.core.common.general.gameplayers.MCNPlayer;

public class TeamQue {

	public static List<Player> blueQue = new ArrayList<Player>();
	public static List<Player> redQue = new ArrayList<Player>();
	public static List<Player> greenQue = new ArrayList<Player>();
	public static List<Player> yellowQue = new ArrayList<Player>();

	public static void addPlayerToQue(Player player, String team) {
		//
		if (team == "blue" && !blueQue.contains(player)) {
			blueQue.add(player);
			player.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&6You have been added to the que for &9Team Blue"));
		}

		if (team == "red" && !redQue.contains(player)) {
			redQue.add(player);
			player.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&6You have been added to the que for &4Team Red"));
		}

		if (team == "green" && !greenQue.contains(player)) {
			greenQue.add(player);
			player.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&6You have been added to the que for &2Team Green"));
		}

		if (team == "yellow" && !yellowQue.contains(player)) {
			yellowQue.add(player);
			player.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&6You have been added to the que for &eTeam Yellow"));
		}

	}

	public static void checkQueStatus() {
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			MCNPlayer corePlayer = MCNPlayerCache.getCache(player.getUniqueId());
			if (redQue.contains(player)) {
				corePlayer.sendActionBar(ChatColor.translateAlternateColorCodes('&', ChatColor.GOLD + "In red que: &r&l"
						+ (TeamQue.redQue.indexOf(player) + 1) + "&6/&r&l" + TeamQue.redQue.size()));
				if (TeamHelper.getTeamDifferece("red") < GameUI.getTeamDiff(player)
						|| TeamHelper.getTeamDifferece("red") == 0 && (redQue.indexOf(player) == 0)
								&& !checkQue("red", player)) {
					GameUI.setTeamRed(player, false);
					redQue.remove(player);
					return;
				}
			}

			if (blueQue.contains(player)) {
				corePlayer.sendActionBar(ChatColor.translateAlternateColorCodes('&', ChatColor.GOLD + "In blue que: &r&l"
						+ (TeamQue.blueQue.indexOf(player) + 1) + "&6/&r&l" + TeamQue.blueQue.size()));
				if (TeamHelper.getTeamDifferece("blue") < GameUI.getTeamDiff(player)
						|| TeamHelper.getTeamDifferece("blue") == 0 && (blueQue.indexOf(player) == 0)
								&& !checkQue("blue", player)) {
					GameUI.setTeamBlue(player, false);
					blueQue.remove(player);
					return;
				}
			}

			if (greenQue.contains(player)) {
				corePlayer.sendActionBar(ChatColor.translateAlternateColorCodes('&', ChatColor.GOLD + "In green que: &r&l"
						+ (TeamQue.greenQue.indexOf(player) + 1) + "&6/&r&l" + TeamQue.greenQue.size()));
				if (TeamHelper.getTeamDifferece("green") < GameUI.getTeamDiff(player)
						|| TeamHelper.getTeamDifferece("green") == 0 && (greenQue.indexOf(player) == 0)
								&& !checkQue("green", player)) {
					GameUI.setTeamGreen(player, false);
					greenQue.remove(player);
					return;
				}

			}

			if (yellowQue.contains(player)) {
				corePlayer.sendActionBar(ChatColor.translateAlternateColorCodes('&', ChatColor.GOLD + "In yellow que: &r&l"
						+ (TeamQue.yellowQue.indexOf(player) + 1) + "&6/&r&l" + TeamQue.yellowQue.size()));
				if (TeamHelper.getTeamDifferece("yellow") < GameUI.getTeamDiff(player)
						|| TeamHelper.getTeamDifferece("yellow") == 0 && (yellowQue.indexOf(player) == 0)
								&& !checkQue("yellow", player)) {
					GameUI.setTeamYellow(player, false);
					yellowQue.remove(player);
					return;
				}
			}

		}

	}

	public static void removePlayer(Player player) {
		if (blueQue.contains(player))
			blueQue.remove(player);
		if (redQue.contains(player))
			redQue.remove(player);
		if (greenQue.contains(player))
			greenQue.remove(player);
		if (yellowQue.contains(player))
			yellowQue.remove(player);

	}

	public static boolean checkQue(String team, Player player) {
		switch (team) {

		case "blue":
			if (blueQue.contains(player))
				return true;
			break;
		case "red":
			if (redQue.contains(player))
				return true;
			break;
		case "green":
			if (greenQue.contains(player))
				return true;
			break;
		case "yellow":
			if (yellowQue.contains(player))
				return true;
			break;
		}

		return false;
	}

	public static boolean checkQue(Player player) {
		if (blueQue.contains(player))
			return true;
		if (redQue.contains(player))
			return true;
		if (greenQue.contains(player))
			return true;
		if (yellowQue.contains(player))
			return true;

		return false;
	}

	public static void removeQues() {
		blueQue.clear();
		redQue.clear();
		greenQue.clear();
		yellowQue.clear();
	}

}