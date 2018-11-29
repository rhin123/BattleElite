package net.mcnations.be.game.states;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.mcnations.be.BattleElite;
import net.mcnations.be.classes.SetClass;
import net.mcnations.be.game.Game;
import net.mcnations.be.team.TeamHelper;
import net.mcnations.be.team.TeamQue;
import net.mcnations.be.world.WorldHelper;
import net.mcnations.core.common.general.cache.MCNPlayerCache;
import net.mcnations.core.common.general.gameplayers.MCNPlayer;
import net.mcnations.core.common.utils.PlayerUtils;
import net.mcnations.core.common.utils.managers.TeamManager;
import net.mcnations.core.engine.GameEngine;
import net.mcnations.core.engine.GameState;

public class Lobby extends GameState {

	private static int lobbyTimeLeft = BattleElite.getCorePlugin().getConfig().getInt("LobbyCooldown");

	public Lobby(GameEngine engine, String rawName, String displayName) {
		super(engine, rawName, displayName);
	}

	@Override
	public boolean onStateBegin() {
		return true;
	}

	@Override
	public boolean onStateEnd() {
		TeamQue.removeQues();
		
		Bukkit.getOnlinePlayers().forEach(player -> {
			if(TeamHelper.getPlayerTeam(player).equalsIgnoreCase("none"))
				TeamHelper.setRandomTeam(player);
		});
		return true;
	}

	@Override
	public void run() {
		WorldHelper.setAlwaysDay(WorldHelper.getHubWorld());
		TeamQue.checkQueStatus();
		
		if(TeamHelper.playersReady() < BattleElite.getCorePlugin().getConfig().getInt("MaxPlayersReady"))
			resetLobbyTime();
		
		//if(TeamHelper.playersReady() == BattleElite.getCorePlugin().getConfig().getInt("MaxPlayersReady"))
		//	lobbyTimeLeft = getDefaultLobbyTime();
		
		if(TeamHelper.playersReady() > BattleElite.getCorePlugin().getConfig().getInt("MaxPlayersReady") && lobbyTimeLeft > 30)
			lobbyTimeLeft = getDefaultLobbyTime()-30;

		for (Player player : Bukkit.getOnlinePlayers()) {
			MCNPlayer corePlayer = MCNPlayerCache.getCache(player.getUniqueId());

			if (!TeamQue.checkQue(player)
					&& getLobbyTimeLeft() == BattleElite.getCorePlugin().getConfig().getInt("LobbyCooldown")
					&& player.getWorld() == WorldHelper.getHubWorld())
				corePlayer.sendActionBar(ChatColor.translateAlternateColorCodes('&', "&2&lCurrent Class: &r")
						+ SetClass.translatePlayerClass(SetClass.getPlayerClass(player)));

			sendHubScoreboard(player);

			if (TeamHelper.playersReady() >= BattleElite.getCorePlugin().getConfig().getInt("MaxPlayersReady")
					&& WorldHelper.getHubWorld().getPlayers().size() == Bukkit.getOnlinePlayers().size()) {

				// reduceLobbyTime();

				// work
				if (getLobbyTimeLeft() % 10 == 0 && getLobbyTimeLeft() > 0)
					corePlayer.sendTitleBar(20, 40, 20, ChatColor.YELLOW + "Game Starts In",
							ChatColor.GREEN + "" + (getLobbyTimeLeft()));

				if (getLobbyTimeLeft() != 0)
					corePlayer.sendActionBar(ChatColor.translateAlternateColorCodes('&', "&6Starting game in: &r&l")
							+ (getLobbyTimeLeft() - 1));
				else
					corePlayer.sendActionBar(ChatColor.translateAlternateColorCodes('&', "&6Starting game in: &r&l")
							+ (getLobbyTimeLeft()));

				if (getLobbyTimeLeft() <= 0) {
					BattleElite.getGame().setState(BattleElite.getGame().getStates().get(1));
					resetLobbyTime();
				}

			} else if (TeamHelper.playersReady() < BattleElite.getCorePlugin().getConfig().getInt("MaxPlayersReady")) {
				resetLobbyTime();
			}
			// player.sendMessage(CoreCache.getCachedData(player.getUniqueId()).getXP(false)
			// + " <-- Your XP");
		}

		// This if statement is called twice, b/c if we did it in the
		// onlinePlayers() forloop, reducing the lobby time would call twice.
		if (TeamHelper.playersReady() >= BattleElite.getCorePlugin().getConfig().getInt("MaxPlayersReady")
				&& WorldHelper.getHubWorld().getPlayers().size() == Bukkit.getOnlinePlayers().size())
			reduceLobbyTime();
	}

	public static int getLobbyTimeLeft() {
		return lobbyTimeLeft;
	}

	public void setLobbyTime(int i) {
		lobbyTimeLeft = i;
	}

	public void reduceLobbyTime() {
		lobbyTimeLeft--;
	}

	public static void resetLobbyTime() {
		lobbyTimeLeft = getDefaultLobbyTime();
	}

	public static int getDefaultLobbyTime() {
		return 60;
	}

	public static void sendHubScoreboard(Player player) {
		int timeLeft = getLobbyTimeLeft();
		if(timeLeft == -1)
			timeLeft = 0;

		MCNPlayer corePlayer = MCNPlayerCache.getCache(player.getUniqueId());
		PlayerUtils.sendScoreboard(player,
				new String[] { ChatColor.translateAlternateColorCodes('&', "&e&lBATTLESELITE"), ChatColor.GOLD + " ",
						ChatColor.translateAlternateColorCodes('&',
								"&6Player: &f&l" + ((int) TeamHelper.playersReady()) + "/"
										+ BattleElite.getCorePlugin().getConfig().getInt("MaxPlayers")),
				ChatColor.AQUA + "  ",
				ChatColor.translateAlternateColorCodes('&', "&6Time Left: &f&l" + timeLeft),
				ChatColor.GRAY + "      ",
				ChatColor.translateAlternateColorCodes('&',
						"&6Player XP: &r&l" + (Game.formatInteger(corePlayer.getXP()))),
				ChatColor.WHITE + "    ",
				ChatColor.translateAlternateColorCodes('&',
						"&6Nation Coins: &f&l" + Game.formatInteger(corePlayer.getNationCoins())), " ",
				ChatColor.DARK_AQUA + "       ", ChatColor.translateAlternateColorCodes('&', "&fMCNations.net") });
	}

	public static String checkEnoughPlayers() {
		if(TeamHelper.playersReady() < BattleElite.getCorePlugin().getConfig().getInt("MaxPlayersReady"))
			return " "+BattleElite.getCorePlugin().getConfig().getInt("MaxPlayersReady")+" players are needed to start the game.";
		else
			return "";
	}

}