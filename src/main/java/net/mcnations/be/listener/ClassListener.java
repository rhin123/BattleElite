package net.mcnations.be.listener;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.mcnations.be.BattleElite;
import net.mcnations.be.classes.ClassSelectUI;
import net.mcnations.be.classes.SetClass;
import net.mcnations.be.team.TeamHelper;
import net.mcnations.be.utils.player.CooldownTimer;
import net.mcnations.be.world.Barrier;
import net.mcnations.be.world.WorldHelper;
import net.mcnations.core.common.general.cache.MCNPlayerCache;
import net.mcnations.core.common.general.gameplayers.MCNPlayer;

//I might have to rename this to something better
public class ClassListener implements Listener {

	public static void archerClassUpdate(Player player) {
		ItemStack[] items = { new ItemStack(Material.ARROW, 1) };

		if (!player.getInventory().contains(Material.ARROW, 8))
			player.getInventory().addItem(items);
	}

	public static void engineerClassUpdate(Player player) {
		ItemStack[] items = { new ItemStack(Material.TNT, 1) };

		if (!player.getInventory().contains(Material.TNT))
			player.getInventory().addItem(items);
	}

	@EventHandler
	public void acrobatClassUpdate(final EntityDamageEvent event) {
		try {
			Player player = (Player) event.getEntity();
			if (event.getCause() == DamageCause.FALL
					&& BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME")
					&& SetClass.getPlayerClass(player) == "acrobat") {
				event.setCancelled(true);
			}
		} catch (Exception e) {
		}
		;
	}

	//private static boolean allowJump = true;
	// Check if engineer List contains e.GetPlayer

	public static void engineerClassUpdate(Player player, PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

			Material m = e.getClickedBlock().getType();
			if (m.equals(Material.WORKBENCH)
					&& BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME")) {
				if(SetClass.getPlayerClass(player) != "engineer")
				{
					e.setCancelled(true);
					player.sendMessage(ChatColor.GOLD+"Only engineers can access crafting tables.");
				}

			}

		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onFlightAttempt(PlayerToggleFlightEvent event) {

		Player p = event.getPlayer();
		if (p.getGameMode() != GameMode.CREATIVE
				&& BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME") && !Barrier.checkBarrier()
				&& !CooldownTimer.cooldown.containsKey(event.getPlayer().getUniqueId()) && SetClass.getPlayerClass(p) == "acrobat") {
			p.getWorld().playSound(p.getLocation(), Sound.IRONGOLEM_THROW, 10, -10);
			event.setCancelled(true);
			p.setAllowFlight(false);
			p.setFlying(false);
			Vector v = p.getLocation().getDirection().multiply(1).setY(1);
			p.setVelocity(v);
			setCooldown(p);
		}
		if (p.getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
			p.setAllowFlight(false);
			p.setFlying(false);
			return;
		}
	}

	private void setCooldown(final Player player) {
		CooldownTimer.cooldown.put(player.getUniqueId(), 15);
	}

	@EventHandler
	public void onGround(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		MCNPlayer corePlayer = MCNPlayerCache.getCache(player.getUniqueId());

		if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
			player.setAllowFlight(true);
			return;
		}

		if (Barrier.checkBarrier() && player.getGameMode() != GameMode.CREATIVE) {
			player.setAllowFlight(false);
			return;
		}

		if (SetClass.getPlayerClass(player) == "acrobat"
				&& BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME") && !Barrier.checkBarrier()) {

			if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR && !CooldownTimer.cooldown.containsKey(player.getUniqueId())) {
				player.setAllowFlight(true);
				corePlayer.sendActionBar("");
			} else if (player.getGameMode() != GameMode.SPECTATOR && CooldownTimer.cooldown.containsKey(player.getUniqueId())) {
				corePlayer.sendActionBar(ChatColor.YELLOW + "Recharging in: " + ChatColor.WHITE + CooldownTimer.cooldown.get(player.getUniqueId()));
				player.setAllowFlight(false);
			}
		}

	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {

		Player player = e.getPlayer();

		engineerClassUpdate(player, e);

		try {
			if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_LOBBY")
					&& e.getItem().getType() == Material.POTION) {
				player.getInventory().setItem(4, ClassSelectUI.al);
				e.setCancelled(true);
			}
		} catch (NullPointerException b) {
		}

	}
	// Change archer to marksman

	public static void updateArcherMethod() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(BattleElite.getCorePlugin(), new Runnable() {
			public void run() {
				try {
					if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME"))
						for (int i = 0; i < SetClass.playerMarksman.size(); i++)
							ClassListener.archerClassUpdate(SetClass.playerMarksman.get(i));

				} catch (Exception e) {
				}

			}

		}, 0, 20*6);

	}

	@EventHandler
	public void engineerListenerMethod(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (SetClass.getPlayerClass(player) == "engineer")
			if (event.getBlock().getType() == Material.IRON_ORE) {
				event.getBlock().getDrops().clear();
				event.getBlock().setType(Material.AIR);
				event.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_INGOT));
				event.setCancelled(true);
			}
	}

	@EventHandler
	public void vampireListenerMethod(PlayerDeathEvent event) {
		Player player = (Player) event.getEntity();
		Player killer = player.getKiller();

		if (killer != null && player != null) {

			if (SetClass.getPlayerClass(killer) == "vampire")
				killer.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 1)));

		}
	}

	@EventHandler
	public static void marauderListenerMethod(EntityDamageByEntityEvent event) {
		try {
			if (event.getEntity() instanceof Player) {
				Player player = (Player) event.getEntity();
				Player damager = (Player) event.getDamager();

				if (player != null && damager != null) {
					if (SetClass.getPlayerClass(damager) == "marauder" && player.getWorld() == WorldHelper.getPlayWorld() && TeamHelper.getPlayerTeam(damager) != TeamHelper.getPlayerTeam(player)) {
						Random rnd = new Random();
						int rand = rnd.nextInt(2);
						if (rand == 0)
							player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 2, 1), false);
					}
				}
			}
		} catch (Exception e) {
		}
	}

	@EventHandler
	public void scoutListenerMethod(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME"))
		if (SetClass.getPlayerClass(player) == "scout")
			for (int i = 0; i < SetClass.armor.length; i++)
				if (SetClass.armor[i].getType() == event.getCurrentItem().getType()) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "Unable to change armor with current class");
				}
	}

	@EventHandler
	public void tankListenerMethod(EntityRegainHealthEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (SetClass.getPlayerClass(player) == "tank")
				if (event.getRegainReason() == RegainReason.SATIATED || event.getRegainReason() == RegainReason.REGEN)
					event.setCancelled(true);
		}
	}

}
