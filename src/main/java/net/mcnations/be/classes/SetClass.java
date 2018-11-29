package net.mcnations.be.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import net.mcnations.be.BattleElite;
import net.mcnations.be.game.GameUI;
import net.mcnations.be.team.TeamHelper;
import net.mcnations.be.utils.item.Item;
import net.mcnations.be.utils.item.PotionBuilder;

public class SetClass {
	// player.getInventory().clear();
	// ItemStack[] items = {new ItemStack(Material.WOOD_SWORD), new
	// ItemStack(Material.APPLE,4)};
	// player.getInventory().addItem(items);
	// else player.getInventory().setItem(8, new ItemStack(Material.PAPER));

	public static List<Player> playerWarrior = new ArrayList<Player>();
	public static List<Player> playerMarksman = new ArrayList<Player>();
	public static List<Player> playerAlchemist = new ArrayList<Player>();
	public static List<Player> playerEngineer = new ArrayList<Player>();
	public static List<Player> playerAcrobat = new ArrayList<Player>();
	public static List<Player> playerVampire = new ArrayList<Player>();
	public static List<Player> playerMarauder = new ArrayList<Player>();
	public static List<Player> playerScout = new ArrayList<Player>();
	public static List<Player> playerTank = new ArrayList<Player>();

	public static ItemStack[] armor = { new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS),
			new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_HELMET) };

	public static void warrior(Player player) {
		// playerWarrior.add(player);
		// Make sure to clear inventories
		player.getInventory().clear();
		ItemStack[] items = { new ItemStack(Material.WOOD_SWORD), new ItemStack(Material.APPLE, 4) };
		player.getInventory().addItem(items);
		setArmor(player);
	}

	// Costs 10k xp
	public static void marksman(Player player) {
		// playerMarksman.add(player);
		player.getInventory().clear();
		ItemStack[] items = { new ItemStack(Material.WOOD_AXE), new ItemStack(Material.BOW),
				new ItemStack(Material.APPLE, 5), new ItemStack(Material.ARROW, 3) };
		player.getInventory().addItem(items);
		setArmor(player);
	}

	// Costs 15k xp
	public static void alchemest(Player player) {
		// playerAlchemist.add(player);
		player.getInventory().clear();
		ItemStack[] items = { new ItemStack(Material.WOOD_SWORD), new ItemStack(Material.APPLE, 5) };

		player.getInventory().addItem(items);
		player.getInventory().addItem(
				PotionBuilder.potionBuilder(PotionType.FIRE_RESISTANCE, PotionEffectType.FIRE_RESISTANCE, 600, 0));
		player.getInventory()
				.addItem(PotionBuilder.potionBuilder(PotionType.SPEED, PotionEffectType.SPEED, 20 * 10, 1));
		player.getInventory()
				.addItem(PotionBuilder.potionBuilder(PotionType.REGEN, PotionEffectType.REGENERATION, 20 * 15, 0));

		setArmor(player);

	}

	// Costs 25k xp
	public static void engineer(Player player) {
		// playerEngineer.add(player);
		player.getInventory().clear();
		ItemStack[] items = { new ItemStack(Material.IRON_PICKAXE), new ItemStack(Material.STONE_AXE),
				new ItemStack(Material.STONE_SPADE), new ItemStack(Material.APPLE, 5),
				new ItemStack(Material.WORKBENCH), new ItemStack(Material.LOG), new ItemStack(Material.TNT) };
		player.getInventory().addItem(items);
		setArmor(player);
	}

	// Costs 25k xp
	public static void acrobat(Player player) {

		// playerAcrobat.add(player);
		player.getInventory().clear();
		ItemStack[] items = { new ItemStack(Material.WOOD_SWORD), new ItemStack(Material.APPLE, 5) };
		player.getInventory().addItem(items);
		setArmor(player);
		player.setAllowFlight(true);
	}

	public static void vampire(Player player) {

		player.getInventory().clear();
		ItemStack[] items = { new ItemStack(Material.WOOD_SWORD), new ItemStack(Material.APPLE, 5) };
		player.getInventory().addItem(items);
		setArmor(player);
	}

	public static void marauder(Player player) {

		player.getInventory().clear();
		ItemStack sword = new ItemStack(Material.STONE_AXE);
		ItemMeta swordMeta = sword.getItemMeta();

		swordMeta.setDisplayName(ChatColor.RED + "Marauder Axe");
		sword.setItemMeta(swordMeta);
		ItemStack[] items = { sword, new ItemStack(Material.APPLE, 5) };
		player.getInventory().addItem(items);
		setArmor(player);

		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1), false);
	}

	public static void scout(Player player) {

		// playerAcrobat.add(player);
		player.getInventory().clear();
		ItemStack[] items = { new ItemStack(Material.WOOD_SWORD), new ItemStack(Material.APPLE, 5) };
		player.getInventory().addItem(items);
		setArmor(player);
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1), false);
	}

	public static void tank(Player player) {

		// playerAcrobat.add(player);
		player.getInventory().clear();
		ItemStack[] items = { new ItemStack(Material.WOOD_SWORD), new ItemStack(Material.APPLE, 5) };
		player.getInventory().addItem(items);
		setTankArmor(player);
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1), false);
		player.setMaxHealth(30.0);
		player.setHealth(30);
	}

	public static void setArmor(Player player) {
		String team = TeamHelper.getPlayerTeam(player);
		for (int i = 0; i < armor.length; i++) {
			LeatherArmorMeta lma = (LeatherArmorMeta) armor[i].getItemMeta();
			switch (team) {
			case "blue":
				lma.setColor(Color.fromRGB(30, 144, 255));
				break;
			case "red":
				lma.setColor(Color.fromRGB(178, 34, 34));
				break;
			case "green":
				lma.setColor(Color.fromRGB(34, 139, 34));
				break;
			case "yellow":
				lma.setColor(Color.fromRGB(255, 255, 0));
				break;
			}
			lma.setLore(Arrays.asList(ChatColor.RED + "Linked Item"));
			armor[i].setItemMeta(lma);

		}

		player.getInventory().setArmorContents(armor);
	}

	public static void setTankArmor(Player player) {
		String team = TeamHelper.getPlayerTeam(player);
		for (int i = armor.length - 1; i < armor.length; i++) {
			LeatherArmorMeta lma = (LeatherArmorMeta) armor[i].getItemMeta();
			switch (team) {
			case "blue":
				lma.setColor(Color.fromRGB(30, 144, 255));
				break;
			case "red":
				lma.setColor(Color.fromRGB(178, 34, 34));
				break;
			case "green":
				lma.setColor(Color.fromRGB(34, 139, 34));
				break;
			case "yellow":
				lma.setColor(Color.fromRGB(255, 255, 0));
				break;
			}
			armor[i].setItemMeta(lma);

		}

		ItemStack[] mArmor = { new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_LEGGINGS),
				new ItemStack(Material.IRON_CHESTPLATE), armor[3] };

		player.getInventory().setArmorContents(mArmor);
	}

	public static void applyClass(Player player) {

		//Remove previous class effects
		player.setFoodLevel(20);
		player.setMaxHealth(20);
		player.setHealth(20);
		GameUI.removeArmor(player);
		
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
		
		
		switch (getPlayerClass(player)) {
		case "warrior":
			warrior(player);
			break;
		case "marksman":
			marksman(player);
			break;
		case "alchemist":
			alchemest(player);
			break;
		case "engineer":
			engineer(player);
			break;
		case "acrobat":
			acrobat(player);
			break;
		case "vampire":
			vampire(player);
			break;
		case "marauder":
			marauder(player);
			break;
		case "scout":
			scout(player);
			break;
		case "tank":
			tank(player);
			break;
		case "none":
			warrior(player);
			break;
		}
	}

	public static String getPlayerClass(Player player) {
		for (int i = 0; i < SetClass.playerWarrior.size(); i++)
			if (player == SetClass.playerWarrior.get(i))
				return "warrior";
		for (int i = 0; i < SetClass.playerMarksman.size(); i++)
			if (player == SetClass.playerMarksman.get(i))
				return "marksman";
		for (int i = 0; i < SetClass.playerAlchemist.size(); i++)
			if (player == SetClass.playerAlchemist.get(i))
				return "alchemist";
		for (int i = 0; i < SetClass.playerEngineer.size(); i++)
			if (player == SetClass.playerEngineer.get(i))
				return "engineer";
		for (int i = 0; i < SetClass.playerAcrobat.size(); i++)
			if (player == SetClass.playerAcrobat.get(i))
				return "acrobat";
		for (int i = 0; i < SetClass.playerVampire.size(); i++)
			if (player == SetClass.playerVampire.get(i))
				return "vampire";
		for (int i = 0; i < SetClass.playerMarauder.size(); i++)
			if (player == SetClass.playerMarauder.get(i))
				return "marauder";
		for (int i = 0; i < SetClass.playerScout.size(); i++)
			if (player == SetClass.playerScout.get(i))
				return "scout";
		for (int i = 0; i < SetClass.playerTank.size(); i++)
			if (player == SetClass.playerTank.get(i))
				return "tank";

		return "none";
	}

	public static void removePlayerFromClass(Player player) {
		for (int i = 0; i < SetClass.playerWarrior.size(); i++)
			if (player == SetClass.playerWarrior.get(i))
				SetClass.playerWarrior.remove(i);
		for (int i = 0; i < SetClass.playerMarksman.size(); i++)
			if (player == SetClass.playerMarksman.get(i))
				SetClass.playerMarksman.remove(i);
		for (int i = 0; i < SetClass.playerAlchemist.size(); i++)
			if (player == SetClass.playerAlchemist.get(i))
				SetClass.playerAlchemist.remove(i);
		for (int i = 0; i < SetClass.playerEngineer.size(); i++)
			if (player == SetClass.playerEngineer.get(i))
				SetClass.playerEngineer.remove(i);
		for (int i = 0; i < SetClass.playerAcrobat.size(); i++)
			if (player == SetClass.playerAcrobat.get(i))
				SetClass.playerAcrobat.remove(i);
		for (int i = 0; i < SetClass.playerVampire.size(); i++)
			if (player == SetClass.playerVampire.get(i))
				SetClass.playerVampire.remove(i);
		for (int i = 0; i < SetClass.playerMarauder.size(); i++)
			if (player == SetClass.playerMarauder.get(i))
				SetClass.playerMarauder.remove(i);
		for (int i = 0; i < SetClass.playerScout.size(); i++)
			if (player == SetClass.playerScout.get(i))
				SetClass.playerScout.remove(i);
		for (int i = 0; i < SetClass.playerTank.size(); i++)
			if (player == SetClass.playerTank.get(i))
				SetClass.playerTank.remove(i);
	}

	public static void setName(String name, ItemStack item) {
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		item.setItemMeta(itemMeta);
	}

	public static void initalizeClass(Player player, String clazz) {

		boolean inGame = BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME");

		ItemStack f = new ItemStack(Material.STONE_SWORD);
		ItemStack m = new ItemStack(Material.BOW);
		ItemStack al = new ItemStack(Material.POTION);
		ItemStack en = new ItemStack(Material.STONE_PICKAXE);
		ItemStack ac = new ItemStack(Material.FEATHER);
		ItemStack v = new ItemStack(Material.REDSTONE);
		ItemStack ma = new ItemStack(Material.STONE_AXE);
		ItemStack s = new ItemStack(Material.LEATHER_CHESTPLATE);
		ItemStack t = new ItemStack(Material.IRON_CHESTPLATE);

		setName(ChatColor.GOLD + "Warrior", f);
		setName(ChatColor.GOLD + "Marksman", m);
		setName(ChatColor.GOLD + "Alchemist", al);
		setName(ChatColor.GOLD + "Engineer", en);
		setName(ChatColor.GOLD + "Acrobat", ac);
		setName(ChatColor.GOLD + "Vampire", v);
		setName(ChatColor.GOLD + "Marauder", ma);
		setName(ChatColor.GOLD + "Scout", s);
		setName(ChatColor.GOLD + "Scout", t);

		switch (clazz) {
		// doing work here
		case "warrior":

			if (!inGame) {
				removePlayerFromClass(player);
				SetClass.playerWarrior.add(player);

				player.getInventory().setItem(4, f);
			} else if (inGame) {
				// IF IN GAME
				player.getInventory().clear();
				removePlayerFromClass(player);
				SetClass.playerWarrior.add(player);

				applyClass(player);

				// Adds back our netherstar
				Item.setDescription(GameUI.classSelect, player.getInventory(),
						ChatColor.translateAlternateColorCodes('&', "&b&lSelect Class"), 8);

			}

			break;

		case "marksman":

			if (!inGame) {

				removePlayerFromClass(player);
				SetClass.playerMarksman.add(player);
				player.getInventory().setItem(4, m);

			} else if (inGame) {
				// IF IN GAME
				player.getInventory().clear();
				removePlayerFromClass(player);
				SetClass.playerMarksman.add(player);

				applyClass(player);

				// Adds back our netherstar
				Item.setDescription(GameUI.classSelect, player.getInventory(),
						ChatColor.translateAlternateColorCodes('&', "&b&lSelect Class"), 8);

			}

			break;

		case "alchemist":

			if (!inGame) {

				removePlayerFromClass(player);
				SetClass.playerAlchemist.add(player);
				player.getInventory().setItem(4, al);

			} else if (inGame) {
				// IF IN GAME
				player.getInventory().clear();
				removePlayerFromClass(player);
				SetClass.playerAlchemist.add(player);

				applyClass(player);

				// Adds back our netherstar
				Item.setDescription(GameUI.classSelect, player.getInventory(),
						ChatColor.translateAlternateColorCodes('&', "&b&lSelect Class"), 8);

			}

			break;

		case "engineer":

			if (!inGame) {

				removePlayerFromClass(player);
				SetClass.playerEngineer.add(player);
				player.getInventory().setItem(4, en);

			} else if (inGame) {
				// IF IN GAME
				player.getInventory().clear();
				removePlayerFromClass(player);
				SetClass.playerEngineer.add(player);

				applyClass(player);

				// Adds back our netherstar
				Item.setDescription(GameUI.classSelect, player.getInventory(),
						ChatColor.translateAlternateColorCodes('&', "&b&lSelect Class"), 8);

			}

			break;

		case "acrobat":

			if (!inGame) {

				removePlayerFromClass(player);
				SetClass.playerAcrobat.add(player);
				player.getInventory().setItem(4, ac);

			} else if (inGame) {
				// IF IN GAME
				player.getInventory().clear();
				removePlayerFromClass(player);
				SetClass.playerAcrobat.add(player);

				applyClass(player);

				// Adds back our netherstar
				Item.setDescription(GameUI.classSelect, player.getInventory(),
						ChatColor.translateAlternateColorCodes('&', "&b&lSelect Class"), 8);

			}

			break;

		case "vampire":

			if (!inGame) {

				removePlayerFromClass(player);
				SetClass.playerVampire.add(player);
				player.getInventory().setItem(4, v);

			} else if (inGame) {
				// IF IN GAME
				player.getInventory().clear();
				removePlayerFromClass(player);
				SetClass.playerVampire.add(player);

				applyClass(player);

				// Adds back our netherstar
				Item.setDescription(GameUI.classSelect, player.getInventory(),
						ChatColor.translateAlternateColorCodes('&', "&b&lSelect Class"), 8);

			}

			break;

		case "marauder":

			if (!inGame) {

				removePlayerFromClass(player);
				SetClass.playerMarauder.add(player);
				player.getInventory().setItem(4, ma);

			} else if (inGame) {
				// IF IN GAME
				player.getInventory().clear();
				removePlayerFromClass(player);
				SetClass.playerMarauder.add(player);

				applyClass(player);

				// Adds back our netherstar
				Item.setDescription(GameUI.classSelect, player.getInventory(),
						ChatColor.translateAlternateColorCodes('&', "&b&lSelect Class"), 8);

			}

			break;

		case "scout":

			if (!inGame) {

				removePlayerFromClass(player);
				SetClass.playerScout.add(player);
				player.getInventory().setItem(4, s);

			} else if (inGame) {
				// IF IN GAME
				player.getInventory().clear();
				removePlayerFromClass(player);
				SetClass.playerScout.add(player);

				applyClass(player);

				// Adds back our netherstar
				Item.setDescription(GameUI.classSelect, player.getInventory(),
						ChatColor.translateAlternateColorCodes('&', "&b&lSelect Class"), 8);

			}

			break;

		case "tank":

			if (!inGame) {

				removePlayerFromClass(player);
				SetClass.playerTank.add(player);
				player.getInventory().setItem(4, t);

			} else if (inGame) {
				// IF IN GAME
				player.getInventory().clear();
				removePlayerFromClass(player);
				SetClass.playerTank.add(player);

				applyClass(player);

				// Adds back our netherstar
				Item.setDescription(GameUI.classSelect, player.getInventory(),
						ChatColor.translateAlternateColorCodes('&', "&b&lSelect Class"), 8);

			}

			break;

		default:

			Random rnd = new Random();
			int randNum = rnd.nextInt(2);
			
			if(randNum == 0)
				initalizeClass(player, "warrior");
			
			if(randNum == 1)
				initalizeClass(player, "marksman");
			
			break;

		}

	}

	public static String translatePlayerClass(String in) {
		String out = WordUtils.capitalize(in);

		if (in == "tank")
			return ChatColor.translateAlternateColorCodes('&', "&e&l") + "The Tank";

		if (in != "none")
			return ChatColor.translateAlternateColorCodes('&', "&e&l") + out;
		else
			return ChatColor.translateAlternateColorCodes('&', "&e&l") + "Warrior";
	}

}
