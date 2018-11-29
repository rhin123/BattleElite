package net.mcnations.be.utils.chest;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import net.mcnations.be.utils.item.ItemBuilder;
import net.mcnations.be.utils.item.PotionBuilder;

public class ChestRefills {

	public static HashMap<Location, Inventory> chests = new HashMap();

	static List<ItemStack> mats = Arrays.asList(
			new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).build(),
			new ItemBuilder(Material.COOKED_BEEF).amount(16).build(), new ItemBuilder(Material.BREAD).amount(5).build(),
			new ItemBuilder(Material.ARROW).amount(16).build(), new ItemBuilder(Material.CHAINMAIL_BOOTS).build(),
			new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).build(),
			new ItemBuilder(Material.CHAINMAIL_LEGGINGS).build(), new ItemBuilder(Material.CHAINMAIL_HELMET).build(),
			new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).build(),
			new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).build(),
			new ItemBuilder(Material.CHAINMAIL_LEGGINGS).build(), new ItemBuilder(Material.CHAINMAIL_BOOTS).build(),
			new ItemBuilder(Material.APPLE).build(), new ItemBuilder(Material.ARROW).amount(6).build(),
			new ItemBuilder(Material.IRON_HELMET).build(), new ItemBuilder(Material.GOLD_PICKAXE).build(),
			new ItemBuilder(Material.COOKED_CHICKEN).amount(5).build(),
			new ItemBuilder(Material.RABBIT_STEW).amount(2).build(), new ItemBuilder(Material.GOLD_SWORD).build(),
			new ItemBuilder(Material.COOKED_FISH).amount(8).build(), new ItemBuilder(Material.MELON).amount(16).build(),
			new ItemBuilder(Material.GOLDEN_APPLE).build(), new ItemBuilder(Material.GOLDEN_CARROT).build(),
			new ItemBuilder(Material.CHAINMAIL_LEGGINGS).build(),
			new ItemBuilder(Material.PUMPKIN_PIE).amount(5).build(),
			PotionBuilder.potionBuilder(PotionType.FIRE_RESISTANCE, PotionEffectType.FIRE_RESISTANCE, 20*10, 0),
			PotionBuilder.potionBuilder(PotionType.INVISIBILITY, PotionEffectType.DAMAGE_RESISTANCE, 20*10, 0,
					"Damage Resistance"));

	/*
	 * public static void fillAllChests() { for (Chunk c :
	 * WorldHelper.getPlayWorld().getLoadedChunks()) { for (BlockState b :
	 * c.getTileEntities()) { if (b instanceof Chest) { Chest chest = (Chest) b;
	 * Inventory inventory =
	 * BattleElite.getCorePlugin().getServer().createInventory(null,
	 * InventoryType.CHEST); chest.getBlockInventory().clear();
	 * chest.update(true, true); Random randomvariable = new Random();
	 * 
	 * for (int cs = 0; cs < 4; cs++) { Collections.shuffle(mats); int x =
	 * randomvariable.nextInt(24); int y = randomvariable.nextInt(mats.size());
	 * 
	 * inventory.setItem(x, mats.get(y)); }
	 * 
	 * chest.getBlockInventory().setContents(inventory.getContents());
	 * chest.update(true, true); } } } }
	 */

	public static void loadChest(Player p, Chest c) {
		Inventory inventory = chests.get(c.getLocation());

		if (inventory == null) {
			inventory = Bukkit.createInventory(null, InventoryType.CHEST);
			Random randomvariable = new Random();

			for (int cs = 0; cs < 4; cs++) {
				Collections.shuffle(mats);
				int x = randomvariable.nextInt(24);
				int y = randomvariable.nextInt(mats.size());

				inventory.setItem(x, mats.get(y));
			}

			c.getBlockInventory().setContents(inventory.getContents());

			chests.put(c.getLocation(), inventory);
		}

		p.openInventory(inventory);
	}

}