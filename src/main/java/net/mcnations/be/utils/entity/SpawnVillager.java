package net.mcnations.be.utils.entity;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.mcnations.be.BattleElite;
import net.mcnations.be.world.WorldHelper;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class SpawnVillager {

	public static List<Villager> villager = new ArrayList<Villager>();

	public static void load() {
		Bukkit.getServer().getScheduler().runTaskLater(BattleElite.getCorePlugin(), new Runnable() {
			public void run() {
				SpawnVillager.initilizeVillager();
			}
		}, 20 * 3);
	}

	public static void initilizeVillager() {
		Villager kitVillager = WorldHelper.getHubWorld()
				.spawn(getHubLoc(ChatColor.YELLOW + "Customize your player!",
						BattleElite.getCorePlugin().getCorePlugin().getConfig().getInt("Villager.x"),
						BattleElite.getCorePlugin().getConfig().getInt("Villager.y"),
						BattleElite.getCorePlugin().getConfig().getInt("Villager.z")), Villager.class);
		freezeEntity(kitVillager);
	}

	public static Location getHubLoc(String holoName, double xDiff, double yDiff, double zDiff) {

		Location loc = null;
		
			loc = new Location(WorldHelper.getHubWorld(),
					BattleElite.getCorePlugin().getConfig().getInt("Hub.x") + xDiff,
					BattleElite.getCorePlugin().getConfig().getInt("Hub.y") + yDiff,
					BattleElite.getCorePlugin().getConfig().getInt("Hub.z") + zDiff,
					BattleElite.getCorePlugin().getConfig().getInt("Villager.yaw"), 0);

		final Location holoLoc = new Location(WorldHelper.getHubWorld(),
				BattleElite.getCorePlugin().getConfig().getInt("Hub.x") + xDiff,
				(BattleElite.getCorePlugin().getConfig().getInt("Hub.y") + yDiff) + 3,
				BattleElite.getCorePlugin().getConfig().getInt("Hub.z") + zDiff);

		Hologram hologram = HologramsAPI.createHologram(BattleElite.getCorePlugin(), holoLoc);
		hologram.appendTextLine(holoName);

		// world.spawnEntity(new Location(null, x, y, z, yaw, pitch),
		// EntityType.ZOMBIE);

		return loc;
	}

	public static void freezeEntity(Entity en) {
		net.minecraft.server.v1_8_R3.Entity nmsEn = ((CraftEntity) en).getHandle();
		NBTTagCompound compound = new NBTTagCompound();
		nmsEn.c(compound);
		compound.setByte("NoAI", (byte) 1);
		nmsEn.f(compound);
	}

}
