package net.mcnations.be.game;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import net.mcnations.be.BattleElite;
import net.mcnations.be.game.states.InGame;
import net.mcnations.be.game.states.Lobby;
import net.mcnations.core.CorePlugin;
import net.mcnations.core.engine.GameEngine;
import net.mcnations.core.engine.GameLogger;
import net.mcnations.core.engine.GameState;

public class Game extends GameEngine
{
	
	public Game(GameLogger logger, String rawName, String displayName, CorePlugin corePlugin) 
	{
		super(logger, rawName, displayName, corePlugin);
		
		List<GameState> gameStates = getStates();
		gameStates.add(new Lobby(this, "IN_LOBBY", "Lobby"));
		gameStates.add(new InGame(this, "IN_GAME", "Game"));
		setStates(gameStates);
		
		setMinGamePlayers(getCorePlugin().getConfig().getInt("MaxPlayersReady"));
		setMaxGamePlayers(getCorePlugin().getConfig().getInt("MaxPlayers"));
		setMaxJoinedSpectators(0);
		
		World world = Bukkit.getWorld("Hub world name?");
        double x = getCorePlugin().getConfig().getDouble("Hub.x");
        double y = getCorePlugin().getConfig().getDouble("Hub.y");
        double z = getCorePlugin().getConfig().getDouble("Hub.z");
        float yaw = (float) 0;
        float pitch = (float) 0;
        setLobbyLocation(new Location(world, x, y, z, yaw, pitch));
	}
	
	public static String formatInteger(int i)
    {
        return NumberFormat.getNumberInstance(Locale.UK).format(i);
    }

}