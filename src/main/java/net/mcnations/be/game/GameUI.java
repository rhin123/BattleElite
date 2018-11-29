package net.mcnations.be.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import net.mcnations.be.BattleElite;
import net.mcnations.be.classes.ClassSelectUI;
import net.mcnations.be.classes.SetClass;
import net.mcnations.be.team.TeamHelper;
import net.mcnations.be.team.TeamQue;
import net.mcnations.be.utils.item.Item;
import net.mcnations.be.world.WorldHelper;

public class GameUI {

	public static Inventory teamUIInv;
	public static ItemStack teamSelector = new ItemStack(
			new ItemStack(Material.INK_SACK, 1, (short) 0, DyeColor.RED.getData()));
	public static ItemStack classSelect = new ItemStack(Material.NETHER_STAR);
	public static ItemStack teamColor = new ItemStack(Material.WOOL);
	public static ItemStack teamRed = new ItemStack(Material.WOOL, 1, DyeColor.RED.getData());
	public static ItemStack teamBlue = new ItemStack(Material.WOOL, 1, DyeColor.BLUE.getData());
	public static ItemStack teamGreen = new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getData());
	public static ItemStack teamYellow = new ItemStack(Material.WOOL, 1, DyeColor.YELLOW.getData());
	public static ItemStack cancel = new ItemStack(Material.BARRIER);
	public static String teamName = "No team";

	public static ItemMeta imTeamColor;

	public static int getTeamDiff(Player player) {
		// We want to remove num if the player is already on a team.
		if (TeamHelper.getPlayerTeam(player) != "none")
			return -1;
		return 0;
	}

	public static void setTeamRed(final Player player, boolean override) {

		// We use this because the plugin thinks there is 1 more player than
		// there is for some reason if the player is on a diff team.

		if (TeamHelper.getPlayerTeam(player) == "red") {
			if (!override)
				player.sendMessage(ChatColor.GOLD + "You are already in this team");
			return;
		}

		if (!TeamHelper.isTeamBalanced("red") & !override) {

			TeamQue.addPlayerToQue(player, "red");

			return;
		}

		if (TeamHelper.isTeamBalanced("red") || override) {
			TeamHelper.addTeam("red", player);

			if (!override)
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You choose &cTeam Red"));

			Item.setDescription(GameUI.teamRed, player.getInventory(), ChatColor.DARK_RED + "Team Red", 8);
		}

	}

	public static void setTeam(String name, final Player player) {
		switch(name)
		{
		case "blue":
			setTeamBlue(player, true);
			break;
			
		case "red":
			setTeamRed(player, true);
			break;
			
		case "green":
			setTeamGreen(player, true);
			break;
			
		case "yellow":
			setTeamYellow(player, true);
			break;
		}
	}

	public static void setTeamBlue(final Player player, boolean override) {

		if (TeamHelper.getPlayerTeam(player) == "blue") {
			if (!override)
				player.sendMessage(ChatColor.GOLD + "You are already in this team");
			return;
		}

		if (!TeamHelper.isTeamBalanced("blue") & !override) {

			TeamQue.addPlayerToQue(player, "blue");

			return;
		}

		if (TeamHelper.isTeamBalanced("blue") || override) {

			TeamHelper.addTeam("blue", player);

			if (!override)
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You choose &9Team Blue"));

			Item.setDescription(GameUI.teamBlue, player.getInventory(), ChatColor.BLUE + "Team Blue", 8);
		}

	}

	public static void setTeamGreen(final Player player, boolean override) {

		if (TeamHelper.getPlayerTeam(player) == "green") {
			if (!override)
				player.sendMessage(ChatColor.GOLD + "You are already in this team");
			return;
		}

		if (!TeamHelper.isTeamBalanced("green") & !override) {

			TeamQue.addPlayerToQue(player, "green");
			return;
		}

		if (TeamHelper.isTeamBalanced("green") || override) {

			TeamHelper.addTeam("green", player);

			if (!override)
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You choose &2Team Green"));

			Item.setDescription(GameUI.teamGreen, player.getInventory(), ChatColor.DARK_GREEN + "Team Green", 8);
		}

	}

	public static void setTeamYellow(final Player player, boolean override) {

		if (TeamHelper.getPlayerTeam(player) == "yellow") {
			if (!override)
				player.sendMessage(ChatColor.GOLD + "You are already in this team");
			return;
		}

		if (!TeamHelper.isTeamBalanced("yellow") & !override) {

			TeamQue.addPlayerToQue(player, "yellow");

			return;
		}

		if (TeamHelper.isTeamBalanced("yellow") || override) {

			TeamHelper.addTeam("yellow", player);

			if (!override)
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You choose &eTeam Yellow"));

			Item.setDescription(GameUI.teamYellow, player.getInventory(), ChatColor.YELLOW + "Team Yellow", 8);
		}

	}

	public static void giveHubItems(final Player player) {
		player.getInventory().clear();
		removeArmor(player);
		player.setFireTicks(0);
		player.setMaxHealth(20);
		player.setHealth(20);
		player.setFoodLevel(20);

		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());

		// Teleports Player to Hub
		final Location loc = new Location(WorldHelper.getHubWorld(),
				BattleElite.getCorePlugin().getConfig().getInt("Hub.x"),
				BattleElite.getCorePlugin().getConfig().getInt("Hub.y"),
				BattleElite.getCorePlugin().getConfig().getInt("Hub.z"));
		player.teleport(loc);

		// Hub Items
		imTeamColor = teamColor.getItemMeta();
		imTeamColor.setDisplayName(teamName);

		teamColor.setItemMeta(imTeamColor);
		Item.setDescription(GameUI.classSelect, player.getInventory(),
				ChatColor.translateAlternateColorCodes('&', "&b&lSelect Class"), 1);

		Item.setDescription(GameUI.teamSelector, player.getInventory(),
				ChatColor.translateAlternateColorCodes('&', "&4&lSelect Team"), 0);

		SetClass.initalizeClass(player, SetClass.getPlayerClass(player));

		// add classSelects
		player.getInventory().setItem(8, teamColor);
		player.getInventory().setItem(7, backToHubItem());
		player.setGameMode(GameMode.ADVENTURE);

		TeamHelper.removePlayerFromTeam(player);
	}

	public static void removeArmor(Player player) {
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
	}

	private static ItemStack backToHubItem() {
		ItemStack item = new ItemStack(Material.ENDER_PEARL);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lReturn to Hub"));
		item.setItemMeta(itemMeta);

		return item;

	}

	public static Inventory customizationInv() {
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Player Customization");

		Item.setDescription(new ItemStack(Material.WOOD_SWORD), inv, ChatColor.GOLD + "Kits", 2);
		Item.setDescription(new ItemStack(Material.BLAZE_POWDER), inv, ChatColor.GOLD + "Particles", 6);

		return inv;
	}

}