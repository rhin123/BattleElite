package net.mcnations.be.utils;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import ca.wacos.nametagedit.NametagAPI;
import net.mcnations.be.BattleElite;
import net.mcnations.be.game.states.InGame;
import net.mcnations.be.game.states.Lobby;
import net.mcnations.be.team.TeamHelper;
import net.mcnations.be.utils.luckyblocks.LuckyBlocks;
import net.mcnations.be.utils.particles.FireworkHelper;
import net.mcnations.be.world.Circle;
import net.mcnations.be.world.WorldHelper;
import net.mcnations.core.common.features.statsboard.Statsboard;
import net.mcnations.core.common.features.statsboard.StatsboardAPI;
import net.mcnations.core.common.general.cache.MCNPlayerCache;
import net.mcnations.core.common.general.gameplayers.MCNPlayer;

public class TimerHelper {
	// Circle.load();

	public static boolean fireworks = false;

	public static void setRepeatingTask() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(BattleElite.getCorePlugin(), new Runnable() {
			public void run() {
				
				
				
				// This is a temporary fix to solve sheep's nametags not loading

				for (Player player : Bukkit.getOnlinePlayers()) {
					MCNPlayer corePlayer = MCNPlayerCache.getCache(player.getUniqueId());
					corePlayer.sendBossBar(
							ChatColor.translateAlternateColorCodes('&', "&e&lBattlesElite by &6MCNations"));
					// LAG?
					switch (TeamHelper.getPlayerTeam(player)) {
					case "blue":
						NametagAPI.setPrefix(player.getName(),
								ChatColor.translateAlternateColorCodes('&', "[&9Blue&r] &9"));
						break;

					case "red":
						NametagAPI.setPrefix(player.getName(),
								ChatColor.translateAlternateColorCodes('&', "[&cRed&r] &c"));
						break;

					case "green":
						NametagAPI.setPrefix(player.getName(),
								ChatColor.translateAlternateColorCodes('&', "[&2Green&r] &2"));
						break;

					case "yellow":
						NametagAPI.setPrefix(player.getName(),
								ChatColor.translateAlternateColorCodes('&', "[&eYellow&r] &e"));
						break;

					case "none":
						NametagAPI.setPrefix(player.getName(),
								ChatColor.translateAlternateColorCodes('&', "[&7None&r] &7"));
						break;
					}
				}
			}
		}, 0, 20);
	}

	public static void circleTimer() {
		Bukkit.getServer().getScheduler().runTaskTimer(BattleElite.getCorePlugin(), new Runnable() {
			public void run() {
				try {
					Circle.load();
				} catch (Exception e) {
				}
			}
		}, 0, ((int) (InGame.getGameTime() / Circle.staticCircleSize)) * 20);
	}

	public static void fireworkTimer() {

		new BukkitRunnable() {
			@Override
			public void run() {
				if (fireworks == true) {
					Random rnd = new Random();
					int x = WorldHelper.getMapCenterX();
					int y = WorldHelper.getMapCenterY() + 1;
					int z = WorldHelper.getMapCenterZ();

					x = x + RandomNumber.getRandomNumber(10);
					z = z + RandomNumber.getRandomNumber(10);
					FireworkHelper.spawnFirework(new Location(WorldHelper.getPlayWorld(), x, y + 10, z),
							org.bukkit.FireworkEffect.Type.BALL_LARGE,
							FireworkHelper.fireworkColorTranslator(InGame.getTeamNameRaw()));
				} else {
					cancel();
				}
			}
		}.runTaskTimer(BattleElite.getCorePlugin(), 0L, 10L);
	}

	public static void checkEndGame() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(BattleElite.getCorePlugin(), new Runnable() {
			public void run() {
				if (BattleElite.getGame().getCurrentState() != null)
					if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME")) {
						if (TeamHelper.blueplayer.size() == 0 && TeamHelper.redplayer.size() == 0
								&& TeamHelper.greenplayer.size() == 0 & TeamHelper.yellowplayer.size() == 0) {
							InGame.setTeamName(ChatColor.YELLOW + "No Team");
							InGame.setTeamNameRaw("n!on!e");
							BattleElite.getGame().setState(BattleElite.getGame().getStates().get(0));
						}
						if (TeamHelper.blueplayer.size() != 0 && TeamHelper.redplayer.size() == 0
								&& TeamHelper.greenplayer.size() == 0 & TeamHelper.yellowplayer.size() == 0) {
							InGame.setTeamName(ChatColor.DARK_AQUA + "Blue Team");
							InGame.setTeamNameRaw("blue");
							BattleElite.getGame().setState(BattleElite.getGame().getStates().get(0));
						}
						if (TeamHelper.blueplayer.size() == 0 && TeamHelper.redplayer.size() != 0
								&& TeamHelper.greenplayer.size() == 0 & TeamHelper.yellowplayer.size() == 0) {
							InGame.setTeamName(ChatColor.DARK_RED + "Red Team");
							InGame.setTeamNameRaw("red");
							BattleElite.getGame().setState(BattleElite.getGame().getStates().get(0));
						}
						if (TeamHelper.blueplayer.size() == 0 && TeamHelper.redplayer.size() == 0
								&& TeamHelper.greenplayer.size() != 0 & TeamHelper.yellowplayer.size() == 0) {
							InGame.setTeamName(ChatColor.DARK_GREEN + "Green Team");
							InGame.setTeamNameRaw("green");
							BattleElite.getGame().setState(BattleElite.getGame().getStates().get(0));
						}
						if (TeamHelper.blueplayer.size() == 0 && TeamHelper.redplayer.size() == 0
								&& TeamHelper.greenplayer.size() == 0 & TeamHelper.yellowplayer.size() != 0) {
							InGame.setTeamName(ChatColor.YELLOW + "Yellow Team");
							InGame.setTeamNameRaw("yellow");
							BattleElite.getGame().setState(BattleElite.getGame().getStates().get(0));
						}
					}
			}
		}, 0, 5);
	}

	public static void addHeadParticles() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(BattleElite.getCorePlugin(), new Runnable() {
			public void run() {
				if (BattleElite.getGame().getCurrentState() != null)
					if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME"))
						for (int i = 0; i < LuckyBlocks.savedLocations.size(); i++)
							for (Player player : Bukkit.getOnlinePlayers()) {
								player.playEffect(LuckyBlocks.savedLocations.get(i), Effect.POTION_SWIRL, 1);
							}
			}
		}, 0, 10);
	}

	public static void lobbyChatUpdate() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(BattleElite.getCorePlugin(), new Runnable() {
			public void run() {
				if (BattleElite.getGame().getCurrentState() != null && WorldHelper.getPlayWorld() != null)
					if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_LOBBY")
							&& WorldHelper.getPlayWorld().getPlayers().size() == 0)
						for (Player player : Bukkit.getOnlinePlayers())
							player.sendMessage(ChatColor.GOLD + "" + TeamHelper.playersReady() + "/"
									+ BattleElite.getCorePlugin().getConfig().getInt("MaxPlayers")
									+ " players are in the game." + Lobby.checkEnoughPlayers());

			}
		}, 0, 20 * 30);
	}
	
	public static void checkDroppedItems() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(BattleElite.getCorePlugin(), new Runnable() {
			public void run() {
				if(WorldHelper.getPlayWorld() != null)
					for (Entity entity : WorldHelper.getPlayWorld().getEntities()) {
						
						if(entity instanceof Item && entity.getCustomName() != null)
						if (entity.isOnGround() && entity.getCustomName().equals(ChatColor.WHITE + "Cobweb")) {
							entity.getLocation().getBlock().setType(Material.WEB);
							entity.remove();
						}
					}

			}
		}, 0, 5);
	}
}
