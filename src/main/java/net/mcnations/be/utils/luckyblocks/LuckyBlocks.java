package net.mcnations.be.utils.luckyblocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.mcnations.be.BattleElite;
import net.mcnations.be.team.TeamHelper;
import net.mcnations.be.utils.item.SkullHelper;
import net.mcnations.be.world.WorldHelper;
import net.mcnations.core.common.utils.ItemBuilder;

public class LuckyBlocks {

	public static List<Location> savedLocations = new ArrayList<Location>();

	// if(!MysteryBlocks.restrictedMaterials().contains(currentBlock.getRelative(BlockFace.DOWN).getType())
	// && currentBlock.getRelative(BlockFace.UP).getType() == Material.AIR)
	public static List<Material> restrictedMaterials() {
		return Arrays.asList(Material.AIR, Material.BARRIER, Material.STAINED_GLASS, Material.STATIONARY_LAVA,
				Material.LAVA, Material.FIRE, Material.LADDER, Material.STAINED_GLASS_PANE, Material.THIN_GLASS,
				Material.STONE_SLAB2, Material.STONE_BUTTON, Material.WOOD_BUTTON, Material.YELLOW_FLOWER,
				Material.RED_ROSE, Material.LONG_GRASS, Material.DOUBLE_PLANT, Material.RED_MUSHROOM,
				Material.BROWN_MUSHROOM);
	}

	public static boolean isPositionAppropiate(Block currentBlock) {
		return (!LuckyBlocks.restrictedMaterials().contains(currentBlock.getRelative(BlockFace.DOWN).getType())
				&& currentBlock.getRelative(BlockFace.UP).getType() == Material.AIR
				&& currentBlock.getType() == Material.AIR);

	}

	public static Location getPlayWorldCenter() {
		// OUR MAP CENTER IS REDUCED!!!!!!!!
		return new Location(WorldHelper.getPlayWorld(), WorldHelper.getMapCenterX(), WorldHelper.getMapCenterY() - 2,
				WorldHelper.getMapCenterZ());
	}

	public static void setBlockSkull(Block block) {
		block.setType(Material.SKULL);
		if (block.getType() == Material.SKULL) {
			Skull skull = (Skull) block.getState();
			skull.setSkullType(SkullType.PLAYER);
			skull.setOwner(getRandomOwner());
			skull.update(true);
		}
	}

	public static void initilizeSkulls() {
		for (int i = 0; i < 4; i++) {
			List<Block> blocks = WorldHelper.blocksFromTwoPoints(LuckyBlocks.getPlayWorldCenter(),
					WorldHelper.getCornerLocation(i));
			for (int b = blocks.size() - 1; b >= 0; b--) {
				Block currentBlock = blocks.get(b);

				if (!LuckyBlocks.isPositionAppropiate(currentBlock)) {
					blocks.remove(b);
				}
			}
								//# of lucky blocks
			for (int x = 0; x < 8; x++) {
				Random rnd = new Random();
				Block luckyBlock = blocks.get(rnd.nextInt(blocks.size() - 1));
				// work
				LuckyBlocks.setBlockSkull(luckyBlock);
				Location lbLoc = luckyBlock.getLocation();
				Location loc = new Location(lbLoc.getWorld(), lbLoc.getX() + 0.5, lbLoc.getY() + 1, lbLoc.getZ() + 0.5);
				LuckyBlocks.addSkullLocation(loc);

			}
		}
	}

	public static void setAbility(String rawName, Player player) {

		ItemStack is = player.getItemInHand();
		if (is.getAmount() > 1)
			is.setAmount(is.getAmount() - 1);

		else
			player.setItemInHand(null);

		switch (rawName) {
		case "resistance":
			player.playSound(player.getLocation(), Sound.ITEM_BREAK, 5F, 1F);
			resistanceAbility(player);
			break;

		case "spider":
			player.playSound(player.getLocation(), Sound.SPIDER_IDLE, 5F, 1F);
			spiderAbility(player);
			break;

		case "bow":
			player.playSound(player.getLocation(), Sound.SHOOT_ARROW, 5F, 1F);
			bowAbility(player);
			break;

		case "absorption":
			player.playSound(player.getLocation(), Sound.CAT_PURR, 5F, 1F);
			absorptionAbility(player);
			break;

		case "fireball":
			player.playSound(player.getLocation(), Sound.GHAST_MOAN, 5F, 1F);
			fireballAbility(player);
			break;

		case "snow":
			player.playSound(player.getLocation(), Sound.DIG_SNOW, 5F, 1F);
			snowAbility(player);
			break;

		case "damage":
			player.playSound(player.getLocation(), Sound.ZOMBIE_HURT, 5F, 1F);
			damageAbility(player);
			break;

		case "huntsman":
			player.playSound(player.getLocation(), Sound.ANVIL_USE, 5F, 1F);
			huntsmanAbility(player);
			break;

		case "lessabsorption":
			player.playSound(player.getLocation(), Sound.CAT_PURREOW, 5F, 1F);
			lessabsorptionAbility(player);
			break;

		default:
			player.sendMessage(ChatColor.GOLD + "Work in progres...");
		}

	}

	private static void lessabsorptionAbility(Player player) {
		player.setMaxHealth(player.getMaxHealth() + 2);
		player.setHealth(player.getHealth() + 2);
	}

	private static void huntsmanAbility(Player player) {

		player.setMetadata("huntsmanability", new FixedMetadataValue(BattleElite.getCorePlugin(), "huntsmanability"));
	}

	private static void damageAbility(Player player) {
		player.setMetadata("damageability", new FixedMetadataValue(BattleElite.getCorePlugin(), "damageability"));
	}

	private static void snowAbility(Player player) {

		player.getInventory().addItem(new ItemBuilder(Material.SNOW_BALL).setAmount(5)
				.setDisplayName(ChatColor.LIGHT_PURPLE + "Explosive Snowball").build());
	}

	private static void fireballAbility(Player player) {
		player.getInventory().addItem(new ItemBuilder(Material.FIREBALL).setAmount(1)
				.setDisplayName(ChatColor.LIGHT_PURPLE + "Fireball").build());
	}

	private static void absorptionAbility(Player player) {
		String team = TeamHelper.getPlayerTeam(player);
		List<Player> playerTeam = TeamHelper.getTeam(team);
		for (int i = 0; i < playerTeam.size(); i++) {
			playerTeam.get(i).addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 10, 0), false);
			if (playerTeam.get(i) != player)
				playerTeam.get(i).sendMessage(ChatColor.GOLD + player.getName() + " has gave you absorption!");
		}

	}

	private static void bowAbility(Player player) {

		if (!player.getInventory().contains(Material.BOW))
			player.getInventory().addItem(
					new ItemBuilder(Material.BOW).setDisplayName(ChatColor.LIGHT_PURPLE + "Marksman Bow").build());
		player.getInventory().addItem(new ItemBuilder(Material.ARROW).setAmount(1).build());

		player.setMetadata("bowability", new FixedMetadataValue(BattleElite.getCorePlugin(), "bowability"));
		BattleElite.getCorePlugin().getScheduler().runTaskLater(BattleElite.getCorePlugin(), new Runnable() {
			@Override
			public void run() {
				if (player != null && player.hasMetadata("bowability"))
					player.removeMetadata("bowability", BattleElite.getCorePlugin());
			}

		}, 20 * 10);
	}

	private static void spiderAbility(Player player) {
		player.getInventory().addItem(new ItemBuilder(Material.WEB).setAmount(5)
				.setDisplayName(ChatColor.LIGHT_PURPLE + "Spider Web").build());
	}

	private static void resistanceAbility(Player player) {
		player.setMetadata("resistanceability",
				new FixedMetadataValue(BattleElite.getCorePlugin(), "resistanceability"));
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 10, 0), false);

		BattleElite.getCorePlugin().getScheduler().runTaskLater(BattleElite.getCorePlugin(), new Runnable() {
			@Override
			public void run() {
				if (player != null && player.hasMetadata("resistanceability"))
					player.removeMetadata("resistanceability", BattleElite.getCorePlugin());
				player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
				player.sendMessage(ChatColor.GOLD + "Your damage resistance has worn off!");
			}

		}, 20 * 10);
	}

	public static String getRandomOwner() {
		Random rnd = new Random();
		List<String> headNames = Arrays.asList("MHF_Present1", "MHF_Spider", "SeerPotion", "MHF_Present2", "CruXXx",
				"Hannah4848", "haohanklliu", "Vire", "Thanauser");

		// Bukkit.broadcastMessage("Loading:"
		// +headNames.get(rnd.nextInt(headNames.size())));
		return headNames.get(rnd.nextInt(headNames.size()));
	}

	public static ItemStack getRandomSkull() {
		String owner = getRandomOwner();
		if (owner == "Vire")
			getRandomSkull();

		ItemStack skull = SkullHelper.getSkull(owner);

		ItemMeta im = skull.getItemMeta();
		im.setDisplayName(getName(owner));
		skull.setItemMeta(im);

		return skull;
	}

	public static String getName(String owner) {
		switch (owner) {
		case "MHF_Present1":
			return ChatColor.LIGHT_PURPLE + "Resistance Block";

		case "MHF_Spider":
			return ChatColor.LIGHT_PURPLE + "Spider Block";

		case "SeerPotion":
			return ChatColor.LIGHT_PURPLE + "Marksman Block";

		case "MHF_Present2":
			return ChatColor.LIGHT_PURPLE + "Absorbtion Block";

		case "CruXXx":
			return ChatColor.LIGHT_PURPLE + "Fire Block";

		case "Hannah4848":
			return ChatColor.LIGHT_PURPLE + "Snow Block";

		case "haohanklliu":
			return ChatColor.LIGHT_PURPLE + "Damage Block";

		case "Vire":
			return ChatColor.LIGHT_PURPLE + "Huntsman Block";

		case "Thanauser":
			return ChatColor.LIGHT_PURPLE + "Lesser Absorbtion Block";

		default:
			return "Rhin's a shitty coder";
		}
	}

	public static String getRawName(String owner) {
		switch (owner) {
		case "MHF_Present1":
			return "resistance";

		case "MHF_Spider":
			return "spider";

		case "SeerPotion":
			return "bow";

		case "MHF_Present2":
			return "absorption";

		case "CruXXx":
			return "fireball";

		case "Hannah4848":
			return "snow";

		case "haohanklliu":
			return "damage";

		case "Vire":
			return "huntsman";

		case "Thanauser":
			return "lessabsorption";

		default:
			return "Rhin's a shitty coder";
		}
	}

	public static void addSkullLocation(Location loc) {
		savedLocations.add(loc);
	}

	public static void clearSkullLocations() {
		savedLocations.clear();
	}

	public static void removeSkullLocation(Location loc) {
		savedLocations.remove(loc);
	}

	public static List<String> getBlockLore(String name) {
		switch (name) {
		case "resistance":
			return Arrays.asList(ChatColor.GRAY + "Resistance for 10 secs.");

		case "spider":
			return Arrays.asList(ChatColor.GRAY + "Throw 5 sticky webs");

		case "bow":
			return Arrays.asList(ChatColor.GRAY + "Double bow damage", ChatColor.GRAY + "Bow & 1 Arrow");

		case "absorption":
			return Arrays.asList(ChatColor.GRAY + "Give all players on your team Absorption I for 10 secs.");

		case "fireball":
			return Arrays.asList(ChatColor.GRAY + "Spawn a throwable fireball");

		case "snow":
			return Arrays.asList(ChatColor.GRAY + "Adds 5 explosive snowballs");

		case "damage":
			return Arrays.asList(ChatColor.GRAY + "Deal double damage on your next hit");

		case "huntsman":
			return Arrays.asList(ChatColor.GRAY + "Obtain a random head from killing a player");

		case "lessabsorption":
			return Arrays.asList(ChatColor.GRAY + "Gain an additional heart");

		default:
			return Arrays.asList(ChatColor.GRAY + "This is an error, preston, if you are seeing this, im sorry ):");

		}
	}

	public static void removeAllMetadata(Player player) {
		player.removeMetadata("huntsmanability", BattleElite.getCorePlugin());
		player.removeMetadata("damageability", BattleElite.getCorePlugin());
		player.removeMetadata("bowability", BattleElite.getCorePlugin());
		player.removeMetadata("spiderability", BattleElite.getCorePlugin());
		player.removeMetadata("resistanceability", BattleElite.getCorePlugin());

	}
}
