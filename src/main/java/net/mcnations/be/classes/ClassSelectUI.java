package net.mcnations.be.classes;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.mcnations.be.utils.player.BattlePlayer;
import net.mcnations.be.utils.player.PlayerCache;

public class ClassSelectUI {
	public static ItemStack f = new ItemStack(Material.STONE_SWORD);
	public static ItemStack m = new ItemStack(Material.BOW);
	public static ItemStack al = new ItemStack(Material.POTION);
	public static ItemStack en = new ItemStack(Material.STONE_PICKAXE);
	public static ItemStack ac = new ItemStack(Material.FEATHER);
	public static ItemStack v = new ItemStack(Material.REDSTONE);
	public static ItemStack ma = new ItemStack(Material.STONE_AXE);
	public static ItemStack s = new ItemStack(Material.LEATHER_CHESTPLATE);
	public static ItemStack t = new ItemStack(Material.IRON_CHESTPLATE);
	Player player;

	public static ItemStack teamSelector = new ItemStack(Material.STICK);
	public static ItemStack teamColor = new ItemStack(Material.WOOL);
	public static String teamName = "Team: None";

	public static ItemMeta im;

	public static List<String> getLore(String name, Player player) {

		switch (name) {

		case "Warrior":

			return Arrays.asList(ChatColor.GRAY + "4 Apples", ChatColor.GRAY + "Wooden Sword",
					checkIfClassUnlocked("warrior", player).replace("%price", "0XP"));

		case "Marksman":

			return Arrays.asList(ChatColor.GRAY + "3 Arrows ", ChatColor.GRAY + "Bow", ChatColor.GRAY + "Arrow Regen",
					checkIfClassUnlocked("marksman", player).replace("%price", "0XP"));

		case "Alchemist":

			return Arrays.asList(ChatColor.GRAY + "Speed Potion", ChatColor.GRAY + "Fire Resistance Potion",
					ChatColor.GRAY + "Regeneration I Potion", ChatColor.GRAY + "Wooden Sword",
					checkIfClassUnlocked("alchemist", player).replace("%price", "5000XP"));

		case "Engineer":

			return Arrays.asList(ChatColor.GRAY + "5 Apples", ChatColor.GRAY + "Iron Pickaxe",
					ChatColor.GRAY + "Able to Craft Weapons", ChatColor.GRAY + "Can throw TNT",
					checkIfClassUnlocked("engineer", player).replace("%price", "5000XP"));

		case "Acrobat":

			return Arrays.asList(ChatColor.GRAY + "4 Apples", ChatColor.GRAY + "Leather Armor",
					ChatColor.GRAY + "Double Jump Every 15 sec.",
					checkIfClassUnlocked("acrobat", player).replace("%price", "5000XP"));

		case "Vampire":

			return Arrays.asList(ChatColor.GRAY + "4 Apples", ChatColor.GRAY + "Leather Armor",
					ChatColor.GRAY + "Gets Regeneration II Each Kill",
					checkIfClassUnlocked("vampire", player).replace("%price", "10000XP"));

		case "Marauder":

			return Arrays.asList(ChatColor.GRAY + "4 Apples", ChatColor.GRAY + "Slowness II",
					ChatColor.GRAY + "Posion Blade",
					checkIfClassUnlocked("marauder", player).replace("%price", "10000XP"));

		case "Scout":

			return Arrays.asList(ChatColor.GRAY + "4 Apples", ChatColor.GRAY + "Speed II",
					ChatColor.GRAY + "Leather Armor Only",
					checkIfClassUnlocked("scout", player).replace("%price", "7000XP"));

		case "Tank":

			return Arrays.asList(ChatColor.GRAY + "5 Apples", ChatColor.GRAY + "Slowness II",
					ChatColor.GRAY + "Iron Armor", checkIfClassUnlocked("tank", player).replace("%price", "10000XP"));

		default:
			return null;

		}
	}

	public static void setDescription(ItemStack item, Inventory inv, String name, List<String> lore, int location) {
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + name);
		im.setLore(lore);
		item.setItemMeta(im);
		inv.setItem(location, item);
	}

	public static void setDescription(ItemStack item, Inventory inv, String name, int location) {
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + name);
		item.setItemMeta(im);
		inv.setItem(location, item);
	}

	public static String checkIfClassUnlocked(String clazz, Player player) {
		BattlePlayer battlePlayer = PlayerCache.getPlayerCache(player.getUniqueId());
		List<String> unlockedKits = battlePlayer.getKits();

		String locked = ChatColor.RED + "Locked " + ChatColor.GRAY + "(%price)";
		String unlocked = ChatColor.GREEN + "Unlocked";

		switch (clazz) {
		case "warrior":
			return unlocked;

		case "marksman":
			return unlocked;

		case "alchemist":
			if (unlockedKits.contains("ALCHEMIST"))
				return unlocked;
			else
				return locked;

		case "engineer":
			if (unlockedKits.contains("ENGINEER"))
				return unlocked;
			else
				return locked;

		case "acrobat":
			if (unlockedKits.contains("ACROBAT"))
				return unlocked;
			else
				return locked;

		case "vampire":
			if (unlockedKits.contains("VAMPIRE"))
				return unlocked;
			else
				return locked;

		case "marauder":
			if (unlockedKits.contains("MARAUDER"))
				return unlocked;
			else
				return locked;

		case "scout":
			if (unlockedKits.contains("SCOUT"))
				return unlocked;
			else
				return locked;

		case "tank":
			if (unlockedKits.contains("TANK"))
				return unlocked;
			else
				return locked;

		}

		return "null";

	}

	public static Inventory getClassInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, 18, ChatColor.BLACK + "Class Select");

		ClassSelectUI.setDescription(ClassSelectUI.f, inv, "Warrior", ClassSelectUI.getLore("Warrior", player), 0);
		ClassSelectUI.setDescription(ClassSelectUI.m, inv, "Marksman", ClassSelectUI.getLore("Marksman", player), 1);
		ClassSelectUI.setDescription(ClassSelectUI.al, inv, "Alchemist", ClassSelectUI.getLore("Alchemist", player), 9);
		ClassSelectUI.setDescription(ClassSelectUI.en, inv, "Engineer", ClassSelectUI.getLore("Engineer", player), 10);
		ClassSelectUI.setDescription(ClassSelectUI.ac, inv, "Acrobat", ClassSelectUI.getLore("Acrobat", player), 15);
		ClassSelectUI.setDescription(ClassSelectUI.v, inv, "Vampire", ClassSelectUI.getLore("Vampire", player), 11);
		ClassSelectUI.setDescription(ClassSelectUI.ma, inv, "Marauder", ClassSelectUI.getLore("Marauder", player), 12);
		ClassSelectUI.setDescription(ClassSelectUI.s, inv, "Scout", ClassSelectUI.getLore("Scout", player), 13);
		ClassSelectUI.setDescription(ClassSelectUI.t, inv, "The Tank", ClassSelectUI.getLore("Tank", player), 14);
		ClassSelectUI.setDescription(new ItemStack(Material.BARRIER), inv, "Close", 17);

		return inv;

	}
}