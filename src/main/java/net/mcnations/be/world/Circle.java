package net.mcnations.be.world;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import net.mcnations.be.BattleElite;

public class Circle {

	public static double circleSize = BattleElite.getCorePlugin().getConfig().getInt("MapSize");
	public static double staticCircleSize = BattleElite.getCorePlugin().getConfig().getInt("MapSize");
	public static boolean circleEnabled = false;

	public static void load() {
		// if (BattleElite.gameStarted &&
		// (WorldHelper.getPlayWorld().getPlayers().size() >
		// BattleElite.getConfig.getInt("CircleStop") || circleEnabled)) {
		
		//check b
		if (BattleElite.getGame().getCurrentState().getRawName()
				.equals("IN_GAME") && !Barrier.checkBarrier()) {

			if (circleSize > 0) {
				circleSize--;
				// if(circleSize < 1)
				// BattleElite.getGame().setState(BattleElite.getGame().getAllStates().get(0));

				createCircle(0, 720, Material.AIR);

			}
		}

	}

	public static void createCircle(double start, double size, Material material) {
		for (double i = start; i < size; i++) {
			double angle = i;
			double x = (circleSize + 2) * Math.cos(angle);
			double z = (circleSize + 2) * Math.sin(angle);
			// Bukkit.getWorld(BattleElite.getConfig.getString("Center.world")).getBlockAt((int)BattleElite.getConfig.getInt("Center.x")+(int)x,(int)BattleElite.getConfig.getInt("Center.y"),(int)BattleElite.getConfig.getInt("Center.z")+(int)z).setType(Material.GLOWSTONE);
			// WorldHelper.getWorld.getBlockAt(new
			// Location(WorldHelper.getWorld,(double)0+(double)x,(double)75,(double)0+(double)z)).setType(Material.GLOWSTONE);
			// work

			// Location fbLoc = new Location(WorldHelper.getPlayWorld(),
			// WorldHelper.getMapCenterX() + (double) x,
			// (WorldHelper.getMapCenterY()-3) , WorldHelper.getMapCenterZ() +
			// (double) z);
			// fbLoc.getBlock().getWorld().spawnFallingBlock(fbLoc,
			// fbLoc.getBlock().getType(), fbLoc.getBlock().getData());

			for (int v = -18; v < 25; v++) {
				Location loc = new Location(WorldHelper.getPlayWorld(), WorldHelper.getMapCenterX() + (double) x,
						(WorldHelper.getMapCenterY()) + v, WorldHelper.getMapCenterZ() + (double) z);

				if (loc.getBlock().getType() != Material.AIR) {

					if (loc.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR || grassItems().contains(loc.getBlock().getRelative(BlockFace.UP).getType()))
						loc.getWorld().spawnFallingBlock(loc, loc.getBlock().getType(), loc.getBlock().getData());

				}
				loc.getBlock().setType(material);
			}

		}
	}

	public static void resetCircleSize() {
		circleSize = staticCircleSize;
	}
	
	public static List<Material> grassItems()
	{
		return Arrays.asList(Material.YELLOW_FLOWER, Material.RED_ROSE, Material.LONG_GRASS, Material.DOUBLE_PLANT, Material.RED_MUSHROOM, Material.BROWN_MUSHROOM, Material.SNOW);
	}

}
