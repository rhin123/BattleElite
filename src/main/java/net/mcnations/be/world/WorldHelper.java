package net.mcnations.be.world;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.scheduler.BukkitRunnable;

import net.mcnations.be.BattleElite;

public class WorldHelper {

	// FIX: some things are capatalised & some are not
	public static String gameworld = "playWorld";

	public static void loadWorld() {
		try {
			_loadMap(gameworld, false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				World world = Bukkit.getWorld(gameworld);
				if (world == null) {
					BattleElite.getCorePlugin().getLogger()
							.severe("Unable to load world, this time I have debug messages!");
					WorldCreator creator = new WorldCreator(gameworld);
					creator.environment(World.Environment.NORMAL);
					creator.generateStructures(true);
					world = creator.createWorld();
					world.setAutoSave(false);
					world.setTime(0);
					world.setStorm(false);
					world.setWeatherDuration(9999);

					BattleElite.getCorePlugin().getLogger().info(gameworld + " generated");
				}
			}
		}.runTaskLater(BattleElite.getCorePlugin(), 2 * 20L);
	}

	private static String _loadMap(String mapName, boolean fallback) throws IOException {
		BattleElite.getCorePlugin().getLogger().info("Copying template to game world.");
		File templateWorld = new File("gamefiles/" + mapName + "/");
		if (!templateWorld.exists() || !templateWorld.isDirectory()) {
			BattleElite.getCorePlugin().getLogger().severe("Template world is not a directory or does not exist.");
			return null;
		}

		String rollback = gameworld;
		File gameWorld = new File(rollback);
		if (gameWorld.exists()) {
			try {
				FileUtils.deleteDirectory(gameWorld);
				if (gameWorld.exists() && gameWorld.isDirectory()) {
					throw new IOException("gameWorld directory was not deleted.");
				}
			} catch (IOException e) {
				BattleElite.getCorePlugin().getLogger().log(Level.SEVERE, "Unable to delete game world directory", e);
				if (fallback) {
					BattleElite.getCorePlugin().getLogger().warning("Failed fallback maps load: stopping");
					return null;
				} else {
					return _loadMap(mapName, true);
				}
			}
		}

		try {
			FileUtils.copyDirectory(templateWorld, gameWorld);
		} catch (IOException e) {
			BattleElite.getCorePlugin().getLogger().log(Level.SEVERE, "Unable to copy template to game world", e);
			return null;
		}

		BattleElite.getCorePlugin().getLogger().info("Finished copying template to game world.");

		return rollback;
	}

	public static void deleteWorld(boolean beforeMap) {
		final World gameWorld = Bukkit.getWorld(gameworld);
		if (gameWorld != null) {
			if (Bukkit.unloadWorld(gameWorld, false)) {
				BattleElite.getCorePlugin().getLogger().info(gameWorld.getName() + " successfully unloaded.");
				if (!beforeMap)
					BattleElite.getCorePlugin().getServer().getScheduler()
							.runTaskLaterAsynchronously(BattleElite.getCorePlugin(), new Runnable() {
								private int tries = 0;

								@Override
								public void run() {
									try {
										for (int i = 0; i < worldAmount(); i++) {
											if (new File(gameWorld.getName() + i).exists())
												FileUtils.deleteDirectory(new File(gameWorld.getName() + i));
										}

										BattleElite.getCorePlugin().getLogger().info("World folder deleted");
									} catch (IOException e) {
										BattleElite.getCorePlugin().getLogger().log(Level.SEVERE,
												"Unable to delete world directory (try: " + tries + ")", e);
										if (tries < 2) {
											tries++;
											BattleElite.getCorePlugin().getServer().getScheduler()
													.runTaskLaterAsynchronously(BattleElite.getCorePlugin(), this,
															(tries + 1) * 20L);
										}
									}
								}
							}, 5L);
			} else {
				BattleElite.getCorePlugin().getLogger().severe("Unable to unload world" + gameWorld.getName());
			}
		}
	}

	public static World getNether() {
		return Bukkit.getServer().getWorld("hubWorld_nether");
	}

	public static World getHubWorld() {
		return Bukkit.getServer().getWorlds().get(0);
	}

	public static World getPlayWorld() {
		return Bukkit.getServer().getWorld(gameworld);
	}

	public static String getPlayWorldName() {
		return Bukkit.getServer().getWorld(gameworld).getName().toLowerCase();
	}

	public static void setAlwaysDay(World w) {
		w.setTime(1100);
		w.setStorm(false);
		w.setWeatherDuration(9999);
		w.setMonsterSpawnLimit(0);
		w.setAutoSave(false);
		for (Entity entity : w.getEntities()) {

			if (entity instanceof Monster)
				entity.remove();
		}
	}

	public static int worldAmount() {
		return new File("gamefiles/").listFiles().length;
	}

	public static void setRandomWorld() {
		Random rnd = new Random();
		gameworld = "playWorld" + rnd.nextInt(worldAmount());
	}

	public static String getMapName() {
		return BattleElite.getCorePlugin().getConfig().getString(gameworld.toLowerCase() + ".name");
	}

	public static String getMapAuthor() {
		return BattleElite.getCorePlugin().getConfig().getString(gameworld.toLowerCase() + ".author");
	}

	// playWorldCenter
	public static int getMapCenterX() {
		return BattleElite.getCorePlugin().getConfig().getInt(WorldHelper.getPlayWorldName() + ".Center.x");
	}

	public static int getMapCenterY() {
		return BattleElite.getCorePlugin().getConfig().getInt(WorldHelper.getPlayWorldName() + ".Center.y");
	}

	public static int getMapCenterZ() {
		return BattleElite.getCorePlugin().getConfig().getInt(WorldHelper.getPlayWorldName() + ".Center.z");
	}

	public static Location getCornerLocation(int i) {
		int centerX = getMapCenterX();
		int centerY = getMapCenterY() - 2;
		int centerZ = getMapCenterZ();
		switch (i) {
		case 0:
			return new Location(WorldHelper.getPlayWorld(), centerX - 25, centerY + 18, centerZ + 25);
			
		case 1:
			return new Location(WorldHelper.getPlayWorld(), centerX + 25, centerY + 18, centerZ + 25);
			
		case 2:
			return new Location(WorldHelper.getPlayWorld(), centerX + 25, centerY + 18, centerZ - 25);
			
		case 3:
			return new Location(WorldHelper.getPlayWorld(), centerX - 25, centerY + 18, centerZ - 25);
			
		default:
			return null;
		}
	}

	public static List<Block> blocksFromTwoPoints(Location loc1, Location loc2) {
		List<Block> blocks = new ArrayList<Block>();

		int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
		int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

		int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
		int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

		int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
		int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

		for (int x = bottomBlockX; x <= topBlockX; x++) {
			for (int z = bottomBlockZ; z <= topBlockZ; z++) {
				for (int y = bottomBlockY; y <= topBlockY; y++) {
					Block block = loc1.getWorld().getBlockAt(x, y, z);

					blocks.add(block);
				}
			}
		}

		return blocks;
	}
	
	public static Location getLobbyCenter()
	{
		return new Location(WorldHelper.getHubWorld(), BattleElite.getCorePlugin().getConfig().getInt("Hub.x"),
				BattleElite.getCorePlugin().getConfig().getInt("Hub.y"), BattleElite.getCorePlugin().getConfig().getInt("Hub.z"));
	}

}
