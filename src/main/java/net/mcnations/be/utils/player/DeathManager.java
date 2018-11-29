package net.mcnations.be.utils.player;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.mcnations.be.world.WorldHelper;
import net.mcnations.core.common.general.cache.MCNPlayerCache;
import net.mcnations.core.common.general.gameplayers.MCNPlayer;

public class DeathManager {

	public static void setDead(Player player, Player killer)
	{//we origionally used Game.getMapCenter, ect.
		MCNPlayer corePlayer = MCNPlayerCache.getCache(player.getUniqueId());
		if(killer != null)
		player.teleport(new Location(WorldHelper.getPlayWorld(), killer.getLocation().getX(),killer.getLocation().getY()+3, killer.getLocation().getZ()));
		else
		player.teleport(new Location(WorldHelper.getPlayWorld(), WorldHelper.getMapCenterX(),WorldHelper.getMapCenterY()+20, WorldHelper.getMapCenterZ()));
		
		player.setGameMode(GameMode.SPECTATOR);
		if(WorldHelper.getPlayWorld().getPlayers().size() >= 3)
		corePlayer.sendTitleBar(20, 40, 20, ChatColor.translateAlternateColorCodes('&',"&cYou are dead!"), "");
	}
	
}
