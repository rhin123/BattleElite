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

import net.mcnations.be.utils.particles.PlayerParticle;
import net.mcnations.be.utils.player.BattlePlayer;
import net.mcnations.be.utils.player.PlayerCache;

public class ParticlesSelectUI {

	private static String particleCost = "500XP";

	public static int getParticleCost() {
		return 500;
	}

	public static Inventory particlesInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.BLACK + "Player Particles");

		// NAME THEM SIMILAR TO THEIR RAW NAMES
		setDescription(new ItemStack(Material.WOOL), inv, "Cloud", getLore("cloud", player), 0);
		setDescription(new ItemStack(Material.DIAMOND_SWORD), inv, "Critical Hit", getLore("criticalhit", player), 1);
		setDescription(new ItemStack(Material.WATER_BUCKET), inv, "Water Drip", getLore("waterdrip", player), 2);
		setDescription(new ItemStack(Material.BOOK), inv, "Enchantment", getLore("enchantment", player), 3);
		setDescription(new ItemStack(Material.TNT), inv, "Explosion", getLore("explosion", player), 4);
		setDescription(new ItemStack(Material.FIREWORK), inv, "Firework", getLore("firework", player), 5);
		setDescription(new ItemStack(Material.FLINT), inv, "Flame", getLore("flame", player), 6);
		setDescription(new ItemStack(Material.LAVA_BUCKET), inv, "Lava", getLore("lava", player), 7);

		setDescription(new ItemStack(Material.DIAMOND_BOOTS), inv, "Player Feet", 22);
		
		setDescription(new ItemStack(Material.BARRIER), inv, "None", 26);

		// 22
		return inv;
	}

	private static void setDescription(ItemStack item, Inventory inv, String name, List<String> lore, int location) {
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.DARK_GREEN + name);
		im.setLore(lore);
		item.setItemMeta(im);
		inv.setItem(location, item);
	}

	private static void setDescription(ItemStack item, Inventory inv, String name, int location) {
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.RED + name);
		item.setItemMeta(im);
		inv.setItem(location, item);
	}

	public static List<String> getLore(String name, Player player) {
		
		//String selectedParticleuse = PlayerParticle.translateParticle("use", PlayerCache.getPlayerCache(player.getUniqueId()).getSelectedParticle());
		//String selectedParticleName = PlayerParticle.translateParticle("name", PlayerCache.getPlayerCache(player.getUniqueId()).getSelectedParticle());
		
		
		
		String selected = "";

		switch (name) {
		case "cloud":

			return Arrays.asList(ChatColor.GRAY + "Self explanatory.",
					checkIfClassUnlocked(name, player).replace("%price", particleCost), selected);

		case "criticalhit":

			return Arrays.asList(ChatColor.GRAY + "Particles from doing a critical hit.",
					checkIfClassUnlocked(name, player).replace("%price", particleCost), selected);

		case "waterdrip":

			return Arrays.asList(ChatColor.GRAY + "Waterdrops from a water block.",
					checkIfClassUnlocked(name, player).replace("%price", particleCost), selected);

		case "enchantment":

			return Arrays.asList(ChatColor.GRAY + "Enchantment particles from bookshelfs.",
					checkIfClassUnlocked(name, player).replace("%price", particleCost), selected);

		case "explosion":

			return Arrays.asList(ChatColor.GRAY + "A small fuse.",
					checkIfClassUnlocked(name, player).replace("%price", particleCost), selected);

		case "firework":

			return Arrays.asList(ChatColor.GRAY + "The trail from a firework.",
					checkIfClassUnlocked(name, player).replace("%price", particleCost), selected);

		case "flame":

			return Arrays.asList(ChatColor.GRAY + "A simple flame.",
					checkIfClassUnlocked(name, player).replace("%price", particleCost), selected);

		case "lava":

			return Arrays.asList(ChatColor.GRAY + "Many lava particles, everywhere.",
					checkIfClassUnlocked(name, player).replace("%price", particleCost), selected);

		default:
			return null;

		}
	}

	static String checkIfClassUnlocked(String particle, Player player) {
		BattlePlayer battlePlayer = PlayerCache.getPlayerCache(player.getUniqueId());
		List<String> unlockedParticles = battlePlayer.getPlayerParticles();

		String locked = ChatColor.RED + "Locked " + ChatColor.GRAY + "(%price)";
		String unlocked = ChatColor.GREEN + "Unlocked";

		for (int i = 0; i < unlockedParticles.size(); i++) {

			if (unlockedParticles.get(i).equals(particle)) {
				return unlocked;
			}
		}

		return locked;

	}

}
