package net.mcnations.be.utils.particles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.mcnations.be.BattleElite;

public class ParticleHelper {

	// Combine these into a hashmap later
	private static List<ParticleEffect> particleList = new ArrayList<ParticleEffect>();

	private static int cloudSize = 9;

	// recode this later, please.. d:
	public static void initilizeParticleHelper() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(BattleElite.getCorePlugin(), new Runnable() {
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.hasMetadata("playerParticleEffect") && player.getGameMode() != GameMode.SPECTATOR) {
						String name = player.getMetadata("playerParticleEffect").get(0).asString();
						
						ParticleEffect particle = PlayerParticle
								.translateEffect(name);

						particle.display(0, -1.5F, 0, 0, 4, player.getLocation(), 1000);

					}
				}
			}
		}, 0, 9);
	}

	public static Location getCloudLocation(Player player, int i) {
		Location[] loc = {
				new Location(player.getWorld(), player.getLocation().getX() - 0.5, player.getLocation().getY(),
						player.getLocation().getZ()),
				new Location(player.getWorld(), player.getLocation().getX() - .25, player.getLocation().getY(),
						player.getLocation().getZ() - 0.25),
				new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - .25,
						player.getLocation().getZ()),
				new Location(player.getWorld(), player.getLocation().getX() + .25, player.getLocation().getY() + .65,
						player.getLocation().getZ() - 0.5),
				new Location(player.getWorld(), player.getLocation().getX() + .5, player.getLocation().getY() + .25,
						player.getLocation().getZ() - 0.25),
				new Location(player.getWorld(), player.getLocation().getX() + .25, player.getLocation().getY() - .5,
						player.getLocation().getZ() + 0.25),
				new Location(player.getWorld(), player.getLocation().getX() - .5, player.getLocation().getY() + .5,
						player.getLocation().getZ() - 0.25),
				new Location(player.getWorld(), player.getLocation().getX() - .25, player.getLocation().getY() + .25,
						player.getLocation().getZ() - 0.25),
				new Location(player.getWorld(), player.getLocation().getX() - .75, player.getLocation().getY(),
						player.getLocation().getZ() - 0.25),
				new Location(player.getWorld(), player.getLocation().getX() + .10, player.getLocation().getY(),
						player.getLocation().getZ() - 0.5) };

		return loc[i];
	}

}
