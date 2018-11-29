package net.mcnations.be.utils.entity;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.mcnations.be.BattleElite;
import net.mcnations.be.world.WorldHelper;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class SpawnSheep {

	public static List<Sheep> sheep = new ArrayList<Sheep>();

	public static void load() {
		Bukkit.getServer().getScheduler().runTaskLater(BattleElite.getCorePlugin(), new Runnable() {
			public void run() {
				initilizeSheep();
			}
		}, 20 * 3);
	}

	private static void initilizeSheep() {
		// 0
		Sheep blue = WorldHelper.getHubWorld()
				.spawn(getHubLoc("Blue", BattleElite.getCorePlugin().getConfig().getInt("Sheep.blue.x"),
						BattleElite.getCorePlugin().getConfig().getInt("Sheep.blue.y"),
						BattleElite.getCorePlugin().getConfig().getInt("Sheep.blue.z")), Sheep.class);
		blue.setColor(DyeColor.CYAN);
		setSheepName(blue, ChatColor.DARK_AQUA + "Team Blue", ChatColor.DARK_AQUA);
		sheep.add(blue);
		// 1
		Sheep red = WorldHelper.getHubWorld()
				.spawn(getHubLoc("Red", BattleElite.getCorePlugin().getConfig().getInt("Sheep.red.x"),
						BattleElite.getCorePlugin().getConfig().getInt("Sheep.red.y"),
						BattleElite.getCorePlugin().getConfig().getInt("Sheep.red.z")), Sheep.class);
		red.setColor(DyeColor.RED);
		setSheepName(red, ChatColor.DARK_RED + "Team Red", ChatColor.DARK_RED);
		sheep.add(red);
		// 2
		Sheep green = WorldHelper.getHubWorld()
				.spawn(getHubLoc("Green", BattleElite.getCorePlugin().getConfig().getInt("Sheep.green.x"),
						BattleElite.getCorePlugin().getConfig().getInt("Sheep.green.y"),
						BattleElite.getCorePlugin().getConfig().getInt("Sheep.green.z")), Sheep.class);
		green.setColor(DyeColor.GREEN);
		setSheepName(green, ChatColor.DARK_GREEN + "Team Green", ChatColor.DARK_GREEN);
		sheep.add(green);
		// 3
		Sheep yellow = WorldHelper.getHubWorld()
				.spawn(getHubLoc("Yellow",
						BattleElite.getCorePlugin().getCorePlugin().getConfig().getInt("Sheep.yellow.x"),
						BattleElite.getCorePlugin().getConfig().getInt("Sheep.yellow.y"),
						BattleElite.getCorePlugin().getConfig().getInt("Sheep.yellow.z")), Sheep.class);
		yellow.setColor(DyeColor.YELLOW);
		setSheepName(yellow, ChatColor.YELLOW + "Team Yellow", ChatColor.YELLOW);
		sheep.add(yellow);

		for (int i = 0; i < sheep.size(); i++)
			freezeEntity(sheep.get(i));
	}

	public static void unload() {
		for(Entity en : WorldHelper.getHubWorld().getEntities())
			en.remove();
	}

	// add y diff later
	public static Location getHubLoc(String name, double xDiff, double yDiff, double zDiff) {
		final Location loc = new Location(WorldHelper.getHubWorld(),
				BattleElite.getCorePlugin().getConfig().getInt("Hub.x") + xDiff,
				BattleElite.getCorePlugin().getConfig().getInt("Hub.y") + yDiff,
				BattleElite.getCorePlugin().getConfig().getInt("Hub.z") + zDiff);

		if (name.contains("Blue"))
			loc.setYaw(getYaw("blue"));
		if (name.contains("Red"))
			loc.setYaw(getYaw("red"));
		if (name.contains("Green"))
			loc.setYaw(getYaw("green"));
		if (name.contains("Yellow"))
			loc.setYaw(getYaw("yellow"));

		// world.spawnEntity(new Location(null, x, y, z, yaw, pitch),
		// EntityType.ZOMBIE);

		return loc;
	}

	public static void setSheepName(Sheep sheep, String str, ChatColor color) {
		Location loc = new Location(WorldHelper.getHubWorld(), sheep.getLocation().getX(),
				sheep.getLocation().getY() + 2, sheep.getLocation().getZ());
		//sheep.setCustomName(color+"[]");
		//sheep.setCustomNameVisible(false);
		Hologram hologram = HologramsAPI.createHologram(BattleElite.getCorePlugin(), loc);
		hologram.appendTextLine(str);

	}

	public static void freezeEntity(Entity en) {
		net.minecraft.server.v1_8_R3.Entity nmsEn = ((CraftEntity) en).getHandle();
		NBTTagCompound compound = new NBTTagCompound();
		nmsEn.c(compound);
		compound.setByte("NoAI", (byte) 1);
		nmsEn.f(compound);
	}

	public static int getYaw(String color) {
		switch (color) {
		case "blue":
			return BattleElite.getCorePlugin().getConfig().getInt("Sheep.blue.yaw");

		case "red":
			return BattleElite.getCorePlugin().getConfig().getInt("Sheep.red.yaw");

		case "green":
			return BattleElite.getCorePlugin().getConfig().getInt("Sheep.green.yaw");

		case "yellow":
			return BattleElite.getCorePlugin().getConfig().getInt("Sheep.yellow.yaw");

		}
		return -1;
	}

}
