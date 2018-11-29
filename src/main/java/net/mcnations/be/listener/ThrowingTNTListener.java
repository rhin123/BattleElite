package net.mcnations.be.listener;

import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.mcnations.be.BattleElite;
import net.mcnations.be.classes.SetClass;
import net.mcnations.be.team.TeamHelper;
import net.mcnations.be.utils.particles.ParticleEffect;
import net.mcnations.be.utils.player.CooldownTimer;
import net.mcnations.be.world.Barrier;
import net.mcnations.core.common.general.cache.MCNPlayerCache;
import net.mcnations.core.common.general.gameplayers.MCNPlayer;
import net.md_5.bungee.api.ChatColor;

public class ThrowingTNTListener implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.LEFT_CLICK_AIR) {
			if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME") && !Barrier.checkBarrier())
				if (event.getPlayer().getItemInHand().getType() == Material.TNT
						&& SetClass.getPlayerClass(event.getPlayer()) == "engineer"
						&& !CooldownTimer.cooldown.containsKey(event.getPlayer().getUniqueId())) {

					// event.getPlayer().getInventory().removeItem(event.getPlayer().getInventory().getItemInHand());

					// event.getPlayer().updateInventory();

					final TNTPrimed tnt = (TNTPrimed) event.getPlayer().getWorld()
							.spawn(event.getPlayer().getLocation(), TNTPrimed.class);
					// tnt.setCustomName("Throwing TNT");
					tnt.setVelocity(event.getPlayer().getLocation().getDirection().normalize().multiply(2));
					tnt.setFuseTicks(BattleElite.getCorePlugin().getConfig().getInt("TntFuseDelay"));
					tnt.setCustomName(TeamHelper.getPlayerTeam(event.getPlayer()));

					CooldownTimer.cooldown.put(event.getPlayer().getUniqueId(), 35);
					new BukkitRunnable() {

						@Override
						public void run() {
							if (tnt.getFuseTicks() == 0) {
								cancel();
							} else {
								ParticleEffect.FIREWORKS_SPARK.display(0, 0, 0, 0, 1000, tnt.getLocation(), 1000);
							}
						}
					}.runTaskTimer(BattleElite.getCorePlugin(), 0L, 1L);
				} else if (CooldownTimer.cooldown.containsKey(event.getPlayer().getUniqueId())
						&& event.getPlayer().getItemInHand().getType() == Material.TNT) {
					MCNPlayer corePlayer = MCNPlayerCache.getCache(event.getPlayer().getUniqueId());
					corePlayer
							.sendActionBar(ChatColor.translateAlternateColorCodes('&', "&eI am reloading for another&f "
									+ CooldownTimer.cooldown.get(event.getPlayer().getUniqueId()) + " &esecond(s)."));
				} else if (SetClass.getPlayerClass(event.getPlayer()) != "engineer"
						&& event.getPlayer().getItemInHand().getType() == Material.TNT) {
					event.getPlayer().sendMessage(ChatColor.GOLD + "Only Engineers can throw tnt!");
				}
		}

	}
}