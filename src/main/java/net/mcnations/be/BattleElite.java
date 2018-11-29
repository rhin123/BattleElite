package net.mcnations.be;

import java.util.concurrent.CompletableFuture;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import net.mcnations.be.commands.GameCommands;
import net.mcnations.be.game.Game;
import net.mcnations.be.listener.ClassListener;
import net.mcnations.be.listener.CommandListener;
import net.mcnations.be.listener.LuckyBlockListener;
import net.mcnations.be.listener.PlayerListener;
import net.mcnations.be.listener.ThrowingTNTListener;
import net.mcnations.be.utils.TimerHelper;
import net.mcnations.be.utils.entity.EntityHelper;
import net.mcnations.be.utils.entity.SpawnSheep;
import net.mcnations.be.utils.entity.SpawnVillager;
import net.mcnations.be.utils.particles.ParticleHelper;
import net.mcnations.be.utils.player.CooldownTimer;
import net.mcnations.be.world.WorldHelper;
import net.mcnations.core.CorePlugin;
import net.mcnations.core.common.enums.GameType;
import net.mcnations.core.common.features.achievements.AchievementsAttribute;
import net.mcnations.core.common.features.chat.ChatAttribute;
import net.mcnations.core.common.features.cosmetics.CosmeticsAttribute;
import net.mcnations.core.common.features.gadgets.GadgetsAttribute;
import net.mcnations.core.common.features.mounts.MountsAttribute;
import net.mcnations.core.common.features.npc.NPCAttribute;
import net.mcnations.core.common.features.party.PartyAttribute;
import net.mcnations.core.common.features.pets.PetsAttribute;
import net.mcnations.core.common.features.profile.ProfileAttribute;
import net.mcnations.core.common.features.statistics.StatisticsAttribute;
import net.mcnations.core.common.features.statsboard.Statsboard;
import net.mcnations.core.common.features.statsboard.StatsboardAPI;
import net.mcnations.core.common.features.statsboard.StatsboardAttribute;
import net.mcnations.core.database.DatabaseConfig;
import net.mcnations.core.database.server.CoreServer;
import net.mcnations.core.database.server.ServerManager;
import net.mcnations.core.engine.GameLogger;

public class BattleElite extends CorePlugin {

	protected static Game game;
	public static Statsboard statsboard;
	public BattleElite() {
		super(GameType.BATTLES_ELITE);

		FileConfiguration database = DatabaseConfig.getConfig();
		this.coreServer = new CoreServer(database.getString("server.id"), database.getString("server.name"),
				getServer().getOnlinePlayers().size(), getServer().getMaxPlayers(), "IN_LOBBY",
				database.getString("server.host"), database.getInt("server.port"), database.getString("server.extra"),
				getGameType());
	}

	@Override
	public void enabled() {
		game = new Game(new GameLogger(), "BE", "Battles Elite", getCorePlugin());
		game.lock();

		boolean toContinue = true;
		
		// If someone fucks up configuring the server, you won't get moaned at ;)
		try {
			// We add our timers here
			this.getServer().getPluginManager().registerEvents(new ClassListener(), this);
			this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
			this.getServer().getPluginManager().registerEvents(new ThrowingTNTListener(), this);
			this.getServer().getPluginManager().registerEvents(new LuckyBlockListener(), this);
			this.getServer().getPluginManager().registerEvents(new CommandListener(), this);

			// Creates a config for the dumbass setting the plugin up
			saveDefaultConfig();

			// Loads our spawn sheep
			SpawnSheep.unload();
			SpawnSheep.load();

			EntityHelper.disableSound();

			SpawnVillager.load();

			// Adds our repeating tasks
			ClassListener.updateArcherMethod();
			this.getServer().getScheduler().runTaskTimer(this, new CooldownTimer(), 0, 20L);

			// We add our repeating timer tasks here
			TimerHelper.setRepeatingTask();
			TimerHelper.circleTimer();
			TimerHelper.checkEndGame();
			TimerHelper.addHeadParticles();
			TimerHelper.lobbyChatUpdate();
			TimerHelper.checkDroppedItems();
			//TimerHelper.updateStatboard();
			
			ParticleHelper.initilizeParticleHelper();
			
			Statsboard board = StatsboardAPI.registerNewBoard("default", getGameType());
			statsboard = board;
			
			CompletableFuture.runAsync(ServerManager::updateServer);

		} catch(Exception e) {
			getLogger().info("You fucked up the config you stupid bitch.");
			e.printStackTrace(); 
			toContinue = false;
		}
		
		if(toContinue && game.nextState())
		{
			getLogger().info("has been enabled.");
			game.unlock();
		}
	}

	@Override
	public void disable() {
		getCoreServer().setStatus("OFFLINE");
		CompletableFuture.runAsync(ServerManager::updateServer);

		getLogger().info("has been disabled.");
	}

	@Override
	public void registerAttributes() {
		if (!getCorePlugin().getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
			// Shut down
			getLogger().severe("ProtocolLib Not Found!");
			getCorePlugin().getServer().shutdown();
		}

		WorldHelper.setRandomWorld();
		WorldHelper.deleteWorld(false);

		new BukkitRunnable() {
			@Override
			public void run() {
				WorldHelper.loadWorld();
			}
		}.runTaskLater(this, 2 * 20L);

		getCorePlugin().registerAttributes(false,
				/* new ResourceAttribute(this), */ new ChatAttribute(this), /*new GuildAttribute(this)*/
				new PartyAttribute(this), new ProfileAttribute(this), new AchievementsAttribute(this),
				new StatisticsAttribute(this),
				/* new GeoAttribute(this), */
				new CosmeticsAttribute(this), new PetsAttribute(this), new MountsAttribute(this),
				// new TransformationsAttribute(this),
				new GadgetsAttribute(this), new NPCAttribute(this), new StatsboardAttribute(this));

		getCommandMap().register("be", new GameCommands(this, "be"));
	}

	public static Game getGame() {
		return game;
	}

}
