package net.mcnations.be.utils.xp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import net.mcnations.be.BattleElite;
import net.mcnations.core.common.general.cache.MCNPlayerCache;
import net.mcnations.core.common.general.gameplayers.MCNPlayer;
import net.mcnations.core.common.utils.NumberUtils;
import net.mcnations.core.common.utils.RewardUtils;

public class XPManager {
	
	private static List<Integer> oldXPValues = new ArrayList<Integer>();
	private static List<Player> playerObject = new ArrayList<Player>();
	public static void addKill(Player player) {
		RewardUtils.addXP(player, BattleElite.getCorePlugin().getConfig().getInt("XP.kill"), true);
	}

	public static String getXPDifference(Player player) {
		return NumberUtils.numberCommaFormatting(RewardUtils.getGainedXP(player));
	}
	
	public static void storeOldXP(Player player, int i)
	{
		oldXPValues.add(i);
		playerObject.add(player);
	}
	
	public static void removeXPValues(Player player)
	{
		int location = playerObject.indexOf(player);
		oldXPValues.remove(location);
		playerObject.remove(player);
	}
	
	public static int getOldXP(Player player)
	{
		return oldXPValues.get(playerObject.indexOf(player));
	}

	public static void clearOldXP() {
		oldXPValues.clear();
		playerObject.clear();
	}
	
	public static int getPlayerObjectSize()
	{
		return playerObject.size();
	}
	

}
