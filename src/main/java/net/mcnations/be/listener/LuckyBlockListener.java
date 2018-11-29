package net.mcnations.be.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import net.mcnations.be.BattleElite;
import net.mcnations.be.team.TeamHelper;
import net.mcnations.be.utils.luckyblocks.LuckyBlocks;
import net.mcnations.be.world.Barrier;

public class LuckyBlockListener implements Listener {

	Map<String, ArrayList<Block>> vineMap = new HashMap();
	public static List<Integer> noClimbBlocks = Arrays.asList(Integer.valueOf(44),
			Integer.valueOf(Material.THIN_GLASS.getId()),
			Integer.valueOf(
					126), /* Integer.valueOf(Material.WOOD_STAIRS.getId()), */ // Integer.valueOf(
			// Material.JUNGLE_WOOD_STAIRS.getId()),
			// Integer.valueOf(Material.BIRCH_WOOD_STAIRS.getId()),
			// Integer.valueOf(Material.SPRUCE_WOOD_STAIRS.getId()),
			// Integer.valueOf(Material.COBBLESTONE_STAIRS.getId()),
			// Integer.valueOf(Material.BRICK_STAIRS.getId()),
			// Integer.valueOf(Material.WOOD_STAIRS.getId()),
			// Integer.valueOf(Material.SMOOTH_STAIRS.getId()),
			Integer.valueOf(Material.FENCE.getId()), Integer.valueOf(Material.FENCE_GATE.getId()),
			Integer.valueOf(Material.NETHER_FENCE.getId()), Integer.valueOf(Material.LADDER.getId()),
			Integer.valueOf(Material.VINE.getId()), Integer.valueOf(Material.BED.getId()),
			Integer.valueOf(Material.BED_BLOCK.getId()), Integer.valueOf(Material.IRON_FENCE.getId()),
			Integer.valueOf(Material.SNOW.getId()), Integer.valueOf(Material.SIGN.getId()),
			Integer.valueOf(Material.LEVER.getId()), Integer.valueOf(Material.TRAP_DOOR.getId()),
			Integer.valueOf(Material.PISTON_EXTENSION.getId()), Integer.valueOf(Material.PISTON_MOVING_PIECE.getId()),
			Integer.valueOf(Material.TRIPWIRE_HOOK.getId()), Integer.valueOf(93), Integer.valueOf(94),
			Integer.valueOf(Material.BOAT.getId()), Integer.valueOf(Material.MINECART.getId()),
			Integer.valueOf(Material.CAKE.getId()), Integer.valueOf(Material.CAKE_BLOCK.getId()),
			Integer.valueOf(Material.WATER.getId()), Integer.valueOf(Material.STATIONARY_WATER.getId()),
			Integer.valueOf(Material.LAVA.getId()), Integer.valueOf(Material.STATIONARY_LAVA.getId()));

	@EventHandler
	public void luckyBlockClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		try { // put instanceof here
			if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
					&& item.getType() == Material.SKULL_ITEM && !Barrier.checkBarrier()) {
				SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
				player.sendMessage(LuckyBlocks.getName(skullMeta.getOwner()) + ChatColor.GOLD + " has been actived!");
				LuckyBlocks.setAbility(LuckyBlocks.getRawName(skullMeta.getOwner()), player);
			} else if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
					&& item.getType() == Material.SKULL_ITEM && Barrier.checkBarrier()) {
				player.sendMessage(ChatColor.GOLD + "Cannot use while the barrier is up!");
			}
		} catch (Exception e) {
		}
	}

	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		Inventory inv = player.getInventory();
		ItemStack item = event.getItem().getItemStack();
		if (item.getType() == Material.SKULL_ITEM && !item.getItemMeta().hasDisplayName()) {
			player.sendMessage(ChatColor.GOLD + "You obtained a lucky block!");
			SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
			skullMeta.setDisplayName(LuckyBlocks.getName(skullMeta.getOwner()));
			skullMeta.setLore(LuckyBlocks.getBlockLore(LuckyBlocks.getRawName(skullMeta.getOwner())));
			item.setItemMeta(skullMeta);
		}

	}

	@EventHandler
	public void luckyBlockClick(ProjectileLaunchEvent event) {
		if (event.getEntity().getShooter() instanceof Player) {
			Player player = (Player) event.getEntity().getShooter();
			EntityType entityType = event.getEntityType();

			if (entityType == EntityType.ARROW && player.hasMetadata("bowability")) {
				event.getEntity().setCustomName(event.getEntity().getCustomName() + ChatColor.DARK_GRAY + "");
			}

		}
	}

	@EventHandler
	public void onProjectileHit(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Projectile) {
			Projectile projectile = (Projectile) event.getDamager();
			Player shooter = (Player) projectile.getShooter();
			Player victim = (Player) event.getEntity();

			if (projectile.getCustomName().contains(ChatColor.DARK_GRAY + "")
					&& !projectile.getCustomName().contains(TeamHelper.getPlayerTeam(victim))) {
				victim.damage(6);
				shooter.playSound(shooter.getLocation(), Sound.NOTE_PLING, 2F, 2F);
			}
		}
	}

	public void setVines(Player player, ArrayList<Block> vines) {
		this.vineMap.put(player.getName(), vines);
	}

	public BlockFace yawToFace(float yaw) {
		BlockFace[] axis = { BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST };
		return axis[(Math.round(yaw / 90.0F) & 0x3)];
	}

	@EventHandler
	public void onSnowballHit(ProjectileHitEvent event) {
		if (event.getEntity() instanceof Snowball) {

			/*
			 * Snowball snowball = (Snowball) event.getEntity(); Location loc =
			 * snowball.getLocation(); Vector vector = snowball.getVelocity();
			 * if (snowball.getCustomName().contains(ChatColor.GRAY + "")) {
			 * Location hitBlockLoc = new Location(loc.getWorld(), loc.getX() +
			 * vector.getX(), loc.getY() + vector.getY(), loc.getZ() +
			 * vector.getZ());
			 * 
			 * hitBlockLoc.getBlock().setType(Material.AIR);
			 * hitBlockLoc.getWorld().playEffect(hitBlockLoc, Effect.EXPLOSION,
			 * 5); hitBlockLoc.getWorld().playSound(hitBlockLoc, Sound.EXPLODE,
			 * 5F, 1F); }
			 */
			Snowball snowball = (Snowball) event.getEntity();
			if (snowball.getCustomName() != null)
				if (snowball.getCustomName().contains(ChatColor.GRAY + "")) {
					BlockIterator iterator = new BlockIterator(event.getEntity().getWorld(),
							event.getEntity().getLocation().toVector(), event.getEntity().getVelocity().normalize(),
							0.0D, 4);

					Block hitBlock = null;

					while (iterator.hasNext()) {
						hitBlock = iterator.next();

						if (hitBlock.getTypeId() != 0) {
							break;
						}
					}

					hitBlock.getWorld().createExplosion(hitBlock.getLocation(), 1);
					hitBlock.setType(Material.AIR);
				}
		}
	}

	@EventHandler
	public void onSnowballThrow(ProjectileLaunchEvent event) {
		if (event.getEntity() instanceof Snowball) {
			Snowball snowball = (Snowball) event.getEntity();
			Player shooter = (Player) snowball.getShooter();
			if (shooter.getInventory().getItemInHand().getItemMeta().getDisplayName().toLowerCase()
					.contains("explosive")) {
				snowball.setCustomName(ChatColor.GRAY + "");
			}

		}
	}

	@EventHandler
	public static void PlayerAttack(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (event.getDamager() instanceof Player) {
				Player damager = (Player) event.getDamager();

				if (damager.hasMetadata("damageability")) {
					player.damage(4);
					damager.sendMessage(ChatColor.RED + "You have dealt double damage");
					damager.removeMetadata("damageability", BattleElite.getCorePlugin());
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		// changed
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (player.getKiller() != null) {
				Player killer = player.getKiller();

				if (killer.hasMetadata("huntsmanability")) {
					killer.getInventory().addItem(LuckyBlocks.getRandomSkull());
					killer.removeMetadata("huntsmanability", BattleElite.getCorePlugin());
				}
			}
		}
	}

	@EventHandler
	public void fireBallThrow(PlayerInteractEvent event) {
		if (event.getItem() instanceof ItemStack) {
			Player player = event.getPlayer();
			ItemStack item = event.getItem();

			if (item.getType() == Material.FIREBALL) {
				Projectile ball = player.launchProjectile(Fireball.class);
				ball.setVelocity(ball.getVelocity().multiply(50));
				ball.setCustomName(TeamHelper.getPlayerTeam(player));
				ball.setShooter(player);
				player.getInventory().remove(item);
			}
		}
	}

	@EventHandler
	public void cobwebThrow(PlayerInteractEvent event) {
		if (event.getItem() instanceof ItemStack) {
			Player player = event.getPlayer();
			ItemStack item = event.getItem();
			
			if (item.getType() == Material.WEB
					&& item.getItemMeta().getDisplayName().equals(ChatColor.LIGHT_PURPLE + "Spider Web")) {

				Item droppedItem = player.getWorld().dropItem(player.getLocation(), item);

				Vector beforeOffset = player.getEyeLocation().getDirection().normalize();
	            Vector finalvel = beforeOffset.multiply(2);
	            droppedItem.setVelocity(finalvel);
				droppedItem.setCustomName(ChatColor.WHITE + "Cobweb");
				player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 5F, 1F);
				
				if (item.getAmount() > 1)
					item.setAmount(item.getAmount() - 1);
				else
					player.setItemInHand(new ItemStack(Material.AIR));
			}
		}
	}
	@EventHandler
	public void onCobwebItemPickup(PlayerPickupItemEvent event) {
		if(event.getItem().getCustomName() != null)
			if(event.getItem().getItemStack().getType() == Material.WEB)
			{
				event.getItem().getLocation().getBlock().setType(Material.WEB);
				event.getItem().remove();
				event.setCancelled(true);
			}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if(event.getItemDrop().getItemStack().getType() == Material.WEB)
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED+"You can only throw cobwebs!");
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {

		Block block = event.getBlock();
		Player player = event.getPlayer();
		
		
		if(block.getType() == Material.WEB)
			event.setCancelled(true);
		
	}

	@EventHandler
	public void onBreakBlock(BlockBreakEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();

		if (block.getType() == Material.SKULL || block.getType() == Material.SKULL_ITEM) {
			if (LuckyBlocks.savedLocations
					.contains(new Location(block.getWorld(), block.getX() + 0.5, block.getY() + 1, block.getZ() + 0.5)))
				LuckyBlocks.savedLocations.remove(
						new Location(block.getWorld(), block.getX() + 0.5, block.getY() + 1, block.getZ() + 0.5));
		}
	}

}
