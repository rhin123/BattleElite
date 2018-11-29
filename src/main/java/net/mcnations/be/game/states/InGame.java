package net.mcnations.be.game.states;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.mcnations.be.BattleElite;
import net.mcnations.be.classes.SetClass;
import net.mcnations.be.game.GameUI;
import net.mcnations.be.team.TeamHelper;
import net.mcnations.be.utils.TimerHelper;
import net.mcnations.be.utils.chest.ChestRefills;
import net.mcnations.be.utils.item.Item;
import net.mcnations.be.utils.luckyblocks.LuckyBlocks;
import net.mcnations.be.utils.player.BattlePlayer;
import net.mcnations.be.utils.player.PlayerCache;
import net.mcnations.be.utils.xp.XPManager;
import net.mcnations.be.world.Barrier;
import net.mcnations.be.world.Circle;
import net.mcnations.be.world.WorldHelper;
import net.mcnations.core.common.features.party.PartyAPI;
import net.mcnations.core.common.general.cache.MCNPlayerCache;
import net.mcnations.core.common.general.gameplayers.MCNPlayer;
import net.mcnations.core.common.utils.PlayerUtils;
import net.mcnations.core.common.utils.RewardUtils;
import net.mcnations.core.engine.GameEngine;
import net.mcnations.core.engine.GameState;

public class InGame extends GameState {
	private static int gameTime = 420;
	private static int freezePlayerTime = 10;
	private static String teamName,teamNameRaw;
	public static boolean redOnce = false;
	public static boolean blueOnce = false;
	public static boolean greenOnce = false;
	public static boolean yellowOnce = false;

	public InGame(GameEngine engine, String rawName, String displayName) {
		super(engine, rawName, displayName);
	}

	@Override
	public boolean onStateBegin() {
		InGame.resetEliminations();
		Barrier.addBarrier();
		LuckyBlocks.initilizeSkulls();
		
		// BattleElite.playersReady = 0;

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!TeamHelper.getPlayerTeam(player).equals("none")) {
				outputMapInfo(player);
				setSpawns(player);
				player.getInventory().clear();
				SetClass.applyClass(player);
				player.setGameMode(GameMode.SURVIVAL);
				//ChestRefills.fillAllChests();
				// Work here
				XPManager.storeOldXP(player, MCNPlayerCache.getCache(player.getUniqueId()).getXP());
				
				
				
				//Gives the player the netherstar
				Item.setDescription(GameUI.classSelect, player.getInventory(),
						ChatColor.translateAlternateColorCodes('&', "&b&lSelect Class"), 8);
				
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lLast chance to choose your kit!"));
				
			}
		}

		return true;
	}

	@Override
	public boolean onStateEnd() {
		//Bukkit.getServer().broadcastMessage(getTeamName() + ChatColor.GOLD + " Has Won!");
		Circle.resetCircleSize();
		//I moved this method to the delayed task
		//Barrier.setCheckBarrier(true);
		ChestRefills.chests.clear();
		TimerHelper.fireworks = true;
		TimerHelper.fireworkTimer();
		
		
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			MCNPlayer corePlayer = MCNPlayerCache.getCache(player.getUniqueId());
			// Reset's players food & health in playworld.
			player.setFoodLevel(20);
			player.setMaxHealth(20);
			player.setHealth(20);
			LuckyBlocks.removeAllMetadata(player);
			
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 5F, 1F);
			
			if (TeamHelper.isPlayerOnWinningTeam(player)) {
				BattlePlayer battlePlayer = PlayerCache.getPlayerCache(player.getUniqueId());
				
				RewardUtils.addXP(player, BattleElite.getCorePlugin().getConfig().getInt("XP.win"), true);
				battlePlayer.setWins(battlePlayer.getWins() + 1);
				
			} else if (!TeamHelper.isPlayerOnWinningTeam(player)) {
				BattlePlayer battlePlayer = PlayerCache.getPlayerCache(player.getUniqueId());
				
				RewardUtils.addXP(player, BattleElite.getCorePlugin().getConfig().getInt("XP.loose"), true);
				battlePlayer.setLosses(battlePlayer.getLosses() + 1);
			}

			corePlayer.sendTitleBar(50, 20*5, 50, getTeamName() + ChatColor.GOLD + " has won!", "");
			//player.sendMessage(ChatColor.GOLD + "You have gained " + ChatColor.RED + XPManager.getXPDifference(player) + " XP");
			RewardUtils.announceWinings(player, 
					ChatColor.translateAlternateColorCodes('&',"&e&lBattles Elite"),
					getTeamName() + ChatColor.translateAlternateColorCodes('&'," &ahas won the game!"),
					"",
					ChatColor.translateAlternateColorCodes('&',"&6You gained " + RewardUtils.getGainedXP(player) + " xp this game!"));
		}
		Bukkit.getServer().getScheduler().runTaskLater(BattleElite.getCorePlugin(), new Runnable() {
			public void run() {

				TeamHelper.clearTeams();
				TimerHelper.fireworks = false;
				resetFreezeTime();
				resetGameTime();
				Barrier.setCheckBarrier(true);
				Barrier.setBarrierTime(Barrier.getDefualtBarrierTime());
				RewardUtils.clearGainedXP();
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					player.setGameMode(GameMode.ADVENTURE);
					GameUI.giveHubItems(player);
					// For acrobat class
					if (player.getGameMode() != GameMode.CREATIVE)
						player.setAllowFlight(false);
					
					
					XPManager.clearOldXP();
					LuckyBlocks.clearSkullLocations();
					
					//RANDOM TEAM SET HERE
					TeamHelper.setRandomTeam(player);
				}
				
				RewardUtils.randomiseRewards();
				
				WorldHelper.setRandomWorld();
				WorldHelper.deleteWorld(false);
				new BukkitRunnable() {
					@Override
					public void run() {
						WorldHelper.loadWorld();
					}
				}.runTaskLater(BattleElite.getCorePlugin(), 2 * 20L);

			}
		}, 200);
		
		
		//Update statboard
		BattleElite.statsboard.updateInfo();
		return true;
	}

	@Override
	public void run() {
		
		WorldHelper.setAlwaysDay(WorldHelper.getPlayWorld());
		WorldHelper.setAlwaysDay(WorldHelper.getPlayWorld());

		reduceGameTime();
		Barrier.removeBarrier();

		// if (WorldHelper.getPlayWorld().isAutoSave())
		// WorldHelper.getPlayWorld().setAutoSave(false);

		if (freezePlayerTime >= 0) {
			for (Player player : WorldHelper.getPlayWorld().getPlayers()) {
				MCNPlayer corePlayer = MCNPlayerCache.getCache(player.getUniqueId());
				corePlayer.sendTitleBar(20, 40, 20, ChatColor.YELLOW + "Prepare for battle",
						ChatColor.GREEN + "" + freezePlayerTime);

				if (freezePlayerTime <= 5 && freezePlayerTime != 0)
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 5F, 1F);
				if (freezePlayerTime == 0)
				{
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 5F, 10F);
					
					//Remove the netherstar from player
					player.getInventory().remove(Material.NETHER_STAR);
					player.updateInventory();
				}
			}
			freezePlayerTime--;
		}

		for (Player player : Bukkit.getOnlinePlayers()) {
			MCNPlayer corePlayer = MCNPlayerCache.getCache(player.getUniqueId());
			PlayerUtils.sendScoreboard(player, new String[] { ChatColor.translateAlternateColorCodes('&', "&e&lBATTLESELITE"),
									ChatColor.GOLD + " ",
									ChatColor.translateAlternateColorCodes('&',
											"&aYour Team: &f&l"
													+ TeamHelper.getTeamSize(TeamHelper.getPlayerTeam(player))),
					ChatColor.BLACK + "   ",
					ChatColor.translateAlternateColorCodes('&',
							"&4Enemy Team: &f&l" + TeamHelper.getEnemyTeamSize(TeamHelper.getPlayerTeam(player))),
					ChatColor.WHITE + "    ",
					ChatColor.translateAlternateColorCodes('&', "&eTime Left: &f&l" + InGame.getGameTimeClock()),
					ChatColor.GRAY + "      ", " ", ChatColor.translateAlternateColorCodes('&', "&fMCNations.net") });

			// Fixes dumbasses falling while in specator mode
			if (player.getGameMode() == GameMode.SPECTATOR && !player.isFlying()) {
				player.getAllowFlight();
				//should be player.setAllowFlight(true);
				player.setFlying(true);
			}

		}

		if (TeamHelper.blueplayer.size() == 0 && TeamHelper.redplayer.size() == 0
				&& TeamHelper.greenplayer.size() == 0 & TeamHelper.yellowplayer.size() == 0) {
			setTeamName(ChatColor.YELLOW + "No Team");
			setTeamNameRaw("n!on!e");
			BattleElite.getGame().setState(BattleElite.getGame().getStates().get(0));
		}
		if (TeamHelper.blueplayer.size() != 0 && TeamHelper.redplayer.size() == 0
				&& TeamHelper.greenplayer.size() == 0 & TeamHelper.yellowplayer.size() == 0) {
			setTeamName(ChatColor.DARK_AQUA + "Blue Team");
			setTeamNameRaw("blue");
			BattleElite.getGame().setState(BattleElite.getGame().getStates().get(0));
		}
		if (TeamHelper.blueplayer.size() == 0 && TeamHelper.redplayer.size() != 0
				&& TeamHelper.greenplayer.size() == 0 & TeamHelper.yellowplayer.size() == 0) {
			setTeamName(ChatColor.DARK_RED + "Red Team");
			setTeamNameRaw("red");
			BattleElite.getGame().setState(BattleElite.getGame().getStates().get(0));
		}
		if (TeamHelper.blueplayer.size() == 0 && TeamHelper.redplayer.size() == 0
				&& TeamHelper.greenplayer.size() != 0 & TeamHelper.yellowplayer.size() == 0) {
			setTeamName(ChatColor.DARK_GREEN + "Green Team");
			setTeamNameRaw("green");
			BattleElite.getGame().setState(BattleElite.getGame().getStates().get(0));
		}
		if (TeamHelper.blueplayer.size() == 0 && TeamHelper.redplayer.size() == 0
				&& TeamHelper.greenplayer.size() == 0 & TeamHelper.yellowplayer.size() != 0) {
			setTeamName(ChatColor.YELLOW + "Yellow Team");
			setTeamNameRaw("yellow");
			BattleElite.getGame().setState(BattleElite.getGame().getStates().get(0));
		}

		if (TeamHelper.blueplayer.size() == 0 && !blueOnce) {
			Bukkit.broadcastMessage(
					ChatColor.translateAlternateColorCodes('&', "&9&lBlue Team &r Has Been Eliminated!"));
			for(Player player : Bukkit.getOnlinePlayers())
				if(!Barrier.checkBarrier() && BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME"))
				player.playSound(player.getLocation(), Sound.BLAZE_DEATH, 5F, 1F);
				
			blueOnce = true;
		}

		if (TeamHelper.redplayer.size() == 0 && !redOnce) {
			Bukkit.broadcastMessage(
					ChatColor.translateAlternateColorCodes('&', "&c&lRed Team &r Has Been Eliminated!"));
			for(Player player : Bukkit.getOnlinePlayers())
				if(!Barrier.checkBarrier() && BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME"))
				player.playSound(player.getLocation(), Sound.BLAZE_DEATH, 5F, 1F);
			redOnce = true;
		}

		if (TeamHelper.greenplayer.size() == 0 && !greenOnce) {
			Bukkit.broadcastMessage(
					ChatColor.translateAlternateColorCodes('&', "&2&lGreen Team &r Has Been Eliminated!"));
			for(Player player : Bukkit.getOnlinePlayers())
				if(!Barrier.checkBarrier() && BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME"))
				player.playSound(player.getLocation(), Sound.BLAZE_DEATH, 5F, 1F);
			greenOnce = true;
		}

		if (TeamHelper.yellowplayer.size() == 0 && !yellowOnce) {
			Bukkit.broadcastMessage(
					ChatColor.translateAlternateColorCodes('&', "&e&lYellow Team &r Has Been Eliminated!"));
			for(Player player : Bukkit.getOnlinePlayers())
				if(!Barrier.checkBarrier() && BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME"))
				player.playSound(player.getLocation(), Sound.BLAZE_DEATH, 5F, 1F);
			yellowOnce = true;

		}

		if (gameTime <= 0) {
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6&lTime ran out!"));
			setTeamName(ChatColor.YELLOW + "No Team");
			setTeamNameRaw("n!on!e");
			BattleElite.getGame().setState(BattleElite.getGame().getStates().get(0));
		}

	}

	public static void setSpawns(Player player) {

		player.setGameMode(GameMode.SURVIVAL);

		if (TeamHelper.getPlayerTeam(player) == "blue") {
			
			TeamHelper.teleportToTeam(player, "blue");

			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You have joined &3Team Blue"));
			// Teleport to blue spawn
			// Give player paper
		}

		if (TeamHelper.getPlayerTeam(player) == "red") {
			
			TeamHelper.teleportToTeam(player, "red");
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You have joined &4Team Red"));
		}

		if (TeamHelper.getPlayerTeam(player) == "green") {
			
			TeamHelper.teleportToTeam(player, "green");

			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You have joined &2Team Green"));
		}

		if (TeamHelper.getPlayerTeam(player) == "yellow") {
			
			TeamHelper.teleportToTeam(player, "yellow");

			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You have joined &eTeam Yellow"));
		}
	}

	public static int getGameTime() {
		return gameTime;
	}

	public static String getGameTimeClock() {
		SimpleDateFormat formatter = new SimpleDateFormat("mm:ss", Locale.UK);

		Date date = new Date(gameTime * 1000);
		return formatter.format(date);
	}

	public static void resetFreezeTime() {
		freezePlayerTime = 10;
	}

	public static void setGameTime(int i) {
		gameTime = i;
	}

	public static void resetGameTime() {
		gameTime = 305;
	}

	public static void reduceGameTime() {
		gameTime--;
	}

	public static int getFreezeTime() {
		return freezePlayerTime;
	}

	public static void outputMapInfo(Player player) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l======================================="));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cGame - &6&lBattlesElite"));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "A game of the elite"));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&aMap - &6&l" + WorldHelper.getMapName() + "&r&7 created by &6&l" + WorldHelper.getMapAuthor()));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l======================================="));

	}
	
	static void resetEliminations() {
		redOnce = false;
		blueOnce = false;
		greenOnce = false;
		yellowOnce = false;
	}

	public static String getTeamName() {
		return teamName;
	}

	public static void setTeamName(String teamName) {
		InGame.teamName = teamName;
	}

	public static String getTeamNameRaw() {
		return teamNameRaw;
	}

	public static void setTeamNameRaw(String teamNameRaw) {
		InGame.teamNameRaw = teamNameRaw;
	}

}
