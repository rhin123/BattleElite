package net.mcnations.be.world;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.mcnations.be.BattleElite;

public class Barrier {
	// Make configurable
	private static int barrierTime = BattleElite.getCorePlugin().getConfig().getInt("BarrierTime");
	private static boolean barrierActive = true;

	public static void addBarrier() {

		Location loc = new Location(WorldHelper.getPlayWorld(), WorldHelper.getMapCenterX(),
				WorldHelper.getMapCenterY(), WorldHelper.getMapCenterZ());

		createBarrierMethod(loc, Material.STAINED_GLASS);

	}

	public static void removeBarrier() {

		final Location loc = new Location(WorldHelper.getPlayWorld(), WorldHelper.getMapCenterX(),
				WorldHelper.getMapCenterY(), WorldHelper.getMapCenterZ());

		if (barrierActive) {
			// && BattleElite.gameStarted
			setBarrierTime(getBarrierTime() - 1);

			if (getBarrierTime() <= 0) {
				createBarrierMethod(loc, Material.AIR);
				barrierActive = false;
				setBarrierTime(getDefualtBarrierTime());

				for (Player player : Bukkit.getOnlinePlayers())
					player.playSound(player.getLocation(), Sound.PISTON_EXTEND, 5F, 0F);
				
				
				
			}
		}
		/*
		 * Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BattleElite
		 * .plugin, new Runnable() { public void run() {
		 * 
		 * 
		 * 
		 * } }, 300);
		 */
	}

	public static int getMapSize() {
		return BattleElite.getCorePlugin().getConfig().getInt("MapSize");
	}

	static void createBarrierMethod(Location loc, Material m) {
		for (int c = 0; c < 4; c++) {
			for (int y = -2; y < 11; y++)
				for (int i = 0; i < getMapSize(); i++) {

					switch (c) {
					case 0:
						loc.setX(WorldHelper.getMapCenterX() + i);
						loc.setZ(WorldHelper.getMapCenterZ());
						loc.setY(WorldHelper.getMapCenterY() + y);
						if (y < 2) {
							loc.getBlock().setType(m);
							loc.getBlock().setData(DyeColor.BLACK.getData());
						} else if (y > 2 && m != Material.AIR)
							loc.getBlock().setType(Material.BARRIER);
						else if(m == Material.AIR)
							loc.getBlock().setType(Material.AIR);

						break;

					case 1:
						loc.setX(WorldHelper.getMapCenterX() - i);
						loc.setZ(WorldHelper.getMapCenterZ());
						loc.setY(WorldHelper.getMapCenterY() + y);
						if (y < 2) {
							loc.getBlock().setType(m);
							loc.getBlock().setData(DyeColor.BLACK.getData());
						} else if (y > 2 && m != Material.AIR)
							loc.getBlock().setType(Material.BARRIER);
						else if(m == Material.AIR)
							loc.getBlock().setType(Material.AIR);

						break;

					case 2:
						loc.setX(WorldHelper.getMapCenterX());
						loc.setZ(WorldHelper.getMapCenterZ() + i);
						loc.setY(WorldHelper.getMapCenterY() + y);
						if (y < 2) {
							loc.getBlock().setType(m);
							loc.getBlock().setData(DyeColor.BLACK.getData());
						} else if (y > 2 && m != Material.AIR)
							loc.getBlock().setType(Material.BARRIER);
						else if(m == Material.AIR)
							loc.getBlock().setType(Material.AIR);

						break;

					case 3:
						loc.setX(WorldHelper.getMapCenterX());
						loc.setZ(WorldHelper.getMapCenterZ() - i);
						loc.setY(WorldHelper.getMapCenterY() + y);
						if (y < 2) {
							loc.getBlock().setType(m);
							loc.getBlock().setData(DyeColor.BLACK.getData());
						} else if (y > 2 && m != Material.AIR)
							loc.getBlock().setType(Material.BARRIER);
						else if(m == Material.AIR)
							loc.getBlock().setType(Material.AIR);

						break;

					}

				}

		}
	}

	public static int getBarrierTime() {
		return barrierTime;
	}

	public static int getDefualtBarrierTime() {
		// change to config.yml
		return BattleElite.getCorePlugin().getConfig().getInt("BarrierTime");
	}

	public static void setBarrierTime(int i) {
		barrierTime = i;
	}

	public static boolean checkBarrier() {
		return barrierActive;
	}

	public static void setCheckBarrier(boolean b) {
		barrierActive = b;
	}

}
