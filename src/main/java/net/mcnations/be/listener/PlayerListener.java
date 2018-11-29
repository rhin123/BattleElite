package net.mcnations.be.listener;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Villager;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
//Fire Events
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.Colorable;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import net.mcnations.be.BattleElite;
import net.mcnations.be.classes.ClassSelectUI;
import net.mcnations.be.classes.ParticlesSelectUI;
import net.mcnations.be.classes.SetClass;
import net.mcnations.be.game.GameUI;
import net.mcnations.be.game.states.InGame;
import net.mcnations.be.team.TeamHelper;
import net.mcnations.be.team.TeamQue;
import net.mcnations.be.team.parties.PartyTest;
import net.mcnations.be.utils.chest.ChestRefills;
import net.mcnations.be.utils.item.Item;
import net.mcnations.be.utils.misc.Debug;
import net.mcnations.be.utils.particles.ParticleEffect;
import net.mcnations.be.utils.particles.PlayerParticle;
import net.mcnations.be.utils.player.BattlePlayer;
import net.mcnations.be.utils.player.DeathManager;
import net.mcnations.be.utils.player.PlayerCache;
import net.mcnations.be.utils.xp.XPManager;
import net.mcnations.be.world.Barrier;
import net.mcnations.be.world.WorldHelper;
import net.mcnations.core.common.enums.GameType;
import net.mcnations.core.common.features.party.Party;
import net.mcnations.core.common.features.party.PartyAPI;
import net.mcnations.core.common.features.party.PartyRequestTeamEvent;
import net.mcnations.core.common.general.cache.MCNPlayerCache;
import net.mcnations.core.common.general.gameplayers.MCNPlayer;
import net.mcnations.core.common.utils.ServerRoute;
import net.mcnations.core.common.utils.managers.TitleManager;

public class PlayerListener implements Listener {

	@EventHandler
	public void createClassSelectUI(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		if (player.getItemInHand().getType() == GameUI.classSelect.getType()
				&& TeamHelper.getPlayerTeam(player) != "none"
				&& (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			player.openInventory(ClassSelectUI.getClassInventory(player));
		} else if (player.getItemInHand().getType().equals(GameUI.classSelect)
				&& TeamHelper.getPlayerTeam(player) == "none")
			player.sendMessage(ChatColor.GOLD + "Choose a team first.");
	}

	// Defines Class UI
	@EventHandler
	public void ClassSelectUI(InventoryClickEvent e) {
		// Not it
		Player player = (Player) e.getWhoClicked();
		ItemStack clicked = e.getCurrentItem();
		Inventory inventory = e.getInventory();

		BattlePlayer battlePlayer = PlayerCache.getPlayerCache(player.getUniqueId());
		MCNPlayer corePlayer = MCNPlayerCache.getCache(player.getUniqueId());
		List<String> unlockedKits = battlePlayer.getKits();

		try {
			if (!e.getInventory().getName().equalsIgnoreCase(ClassSelectUI.getClassInventory(player).getName()))
				return;
			if (e.getCurrentItem().getItemMeta() == null)
				return;
			// check for xp values

			if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Warrior")) {
				// Combine these into methods
				e.setCancelled(true);
				player.sendMessage(ChatColor.GOLD + "You selected Warrior");

				SetClass.initalizeClass(player, "warrior");

				e.getWhoClicked().closeInventory();
			}

			if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Marksman")) {
				// Combine these into methods
				e.setCancelled(true);
				player.sendMessage(ChatColor.GOLD + "You selected Marksman");

				SetClass.initalizeClass(player, "marksman");

				e.getWhoClicked().closeInventory();
			}
			// Alchemist
			if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Alchemist")) {
				e.setCancelled(true);
				if (unlockedKits.contains("ALCHEMIST")) {
					SetClass.initalizeClass(player, "alchemist");
					player.sendMessage(ChatColor.GOLD + "You selected Alchemist");
					e.getWhoClicked().closeInventory();
				} else {
					if (!(corePlayer.canPurchaseByXP(5000, false))) {
						player.sendMessage(ChatColor.GOLD + "You do not have enough xp! (5k needed)");
						e.getWhoClicked().closeInventory();
					} else {
						// Purchase it!
						battlePlayer.unlockKit("ALCHEMIST");
						SetClass.initalizeClass(player, "alchemist");
						corePlayer.takeXP(5000);
						player.sendMessage(ChatColor.GOLD + "You unlocked Alchemist");
						e.getWhoClicked().closeInventory();
					}

				}
			}

			if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Engineer")) {
				e.setCancelled(true);
				if (unlockedKits.contains("ENGINEER")) {
					SetClass.initalizeClass(player, "engineer");
					player.sendMessage(ChatColor.GOLD + "You selected Engineer");
					e.getWhoClicked().closeInventory();
				} else {
					if (!(corePlayer.canPurchaseByXP(5000, false))) {
						player.sendMessage(ChatColor.GOLD + "You do not have enough xp! (5k needed)");
						e.getWhoClicked().closeInventory();
					} else {
						// Purchase it!
						battlePlayer.unlockKit("ENGINEER");
						SetClass.initalizeClass(player, "engineer");
						corePlayer.takeXP(5000);
						player.sendMessage(ChatColor.GOLD + "You unlocked Engineer");
						e.getWhoClicked().closeInventory();
					}

				}
			}

			if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Acrobat")) {
				e.setCancelled(true);
				if (unlockedKits.contains("ACROBAT")) {
					SetClass.initalizeClass(player, "acrobat");
					player.sendMessage(ChatColor.GOLD + "You selected Acrobat");
					e.getWhoClicked().closeInventory();
				} else {
					if (!(corePlayer.canPurchaseByXP(5000, false))) {
						player.sendMessage(ChatColor.GOLD + "You do not have enough xp! (5k needed)");
						e.getWhoClicked().closeInventory();
					} else {
						// Purchase it!
						battlePlayer.unlockKit("ACROBAT");
						SetClass.initalizeClass(player, "acrobat");
						corePlayer.takeXP(5000);
						player.sendMessage(ChatColor.GOLD + "You unlocked Acrobat");
						e.getWhoClicked().closeInventory();
					}

				}
			}

			if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Vampire")) {
				e.setCancelled(true);
				if (unlockedKits.contains("VAMPIRE")) {
					SetClass.initalizeClass(player, "vampire");
					player.sendMessage(ChatColor.GOLD + "You selected Vampire");
					e.getWhoClicked().closeInventory();
				} else {
					if (!(corePlayer.canPurchaseByXP(10000, false))) {
						player.sendMessage(ChatColor.GOLD + "You do not have enough xp! (10k needed)");
						e.getWhoClicked().closeInventory();
					} else {
						// Purchase it!
						battlePlayer.unlockKit("VAMPIRE");
						SetClass.initalizeClass(player, "vampire");
						corePlayer.takeXP(10000);
						player.sendMessage(ChatColor.GOLD + "You unlocked Vampire");
						e.getWhoClicked().closeInventory();
					}

				}
			}

			if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Marauder")) {
				e.setCancelled(true);
				if (unlockedKits.contains("MARAUDER")) {
					SetClass.initalizeClass(player, "marauder");
					player.sendMessage(ChatColor.GOLD + "You selected Marauder");
					e.getWhoClicked().closeInventory();
				} else {
					if (!(corePlayer.canPurchaseByXP(10000, false))) {
						player.sendMessage(ChatColor.GOLD + "You do not have enough xp! (10k needed)");
						e.getWhoClicked().closeInventory();
					} else {
						// Purchase it!
						battlePlayer.unlockKit("MARAUDER");
						SetClass.initalizeClass(player, "marauder");
						corePlayer.takeXP(10000);
						player.sendMessage(ChatColor.GOLD + "You unlocked Marauder");
						e.getWhoClicked().closeInventory();
					}

				}
			}

			if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Scout")) {
				e.setCancelled(true);
				if (unlockedKits.contains("SCOUT")) {
					SetClass.initalizeClass(player, "scout");
					player.sendMessage(ChatColor.GOLD + "You selected Scout");
					e.getWhoClicked().closeInventory();
				} else {
					if (!(corePlayer.canPurchaseByXP(7000, false))) {
						player.sendMessage(ChatColor.GOLD + "You do not have enough xp! (7k needed)");
						e.getWhoClicked().closeInventory();
					} else {
						// Purchase it!
						battlePlayer.unlockKit("SCOUT");
						SetClass.initalizeClass(player, "scout");
						corePlayer.takeXP(7000);
						player.sendMessage(ChatColor.GOLD + "You unlocked Scout");
						e.getWhoClicked().closeInventory();
					}

				}
			}

			if (e.getCurrentItem().getItemMeta().getDisplayName().contains("The Tank")) {
				e.setCancelled(true);
				if (unlockedKits.contains("TANK")) {
					SetClass.initalizeClass(player, "tank");
					player.sendMessage(ChatColor.GOLD + "You selected The Tank");
					e.getWhoClicked().closeInventory();
				} else {
					if (!(corePlayer.canPurchaseByXP(10000, false))) {
						player.sendMessage(ChatColor.GOLD + "You do not have enough xp! (10k needed)");
						e.getWhoClicked().closeInventory();
					} else {
						// Purchase it!
						battlePlayer.unlockKit("TANK");
						SetClass.initalizeClass(player, "tank");
						corePlayer.takeXP(10000);
						player.sendMessage(ChatColor.GOLD + "You unlocked The Tank");
						e.getWhoClicked().closeInventory();
					}

				}
			}

			if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Close")) {
				e.setCancelled(true);
				e.getWhoClicked().closeInventory();
			}

		} catch (Exception x) {

		}
	}

	@EventHandler
	public void teamUI(PlayerInteractEvent e) {
		try {
			Player player = e.getPlayer();
			if (player.getItemInHand().getType() == GameUI.teamSelector.getType()
					&& (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {

				// player.getWorld().playSound(player.getLocation(),
				// Sound.CLICK, 1, 0);

				try {
					GameUI.teamUIInv = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Choose Team");
					Item.setDescription(GameUI.teamRed, GameUI.teamUIInv, ChatColor.DARK_RED + "Team Red", 0);
					Item.setDescription(GameUI.teamBlue, GameUI.teamUIInv, ChatColor.BLUE + "Team Blue", 1);
					Item.setDescription(GameUI.teamGreen, GameUI.teamUIInv, ChatColor.DARK_GREEN + "Team Green", 2);
					Item.setDescription(GameUI.teamYellow, GameUI.teamUIInv, ChatColor.YELLOW + "Team Yellow", 3);
					player.openInventory(GameUI.teamUIInv);
				} catch (Exception x) {
				}
			}

		} catch (Exception x) {
		}
	}

	@EventHandler
	public void stopCropTrampling(PlayerInteractEvent event) {
		if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_LOBBY"))
			if (event.getAction() == Action.PHYSICAL) {
				Block block = event.getClickedBlock();

				if (block == null)
					return;

				int blockType = block.getTypeId();

				if (blockType == Material.SOIL.getId()) {
					event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
					event.setCancelled(true);

					block.setTypeId(blockType);
					block.setData(block.getData());
				}
			}
	}

	// new stuff
	@EventHandler
	public void onCustomizationUI(InventoryClickEvent e) {
		try {
			if (e.getInventory() != null) {

				Player player = (Player) e.getWhoClicked();

				if (e.getInventory().getName().equalsIgnoreCase(GameUI.customizationInv().getName())) {
					if (e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase().contains("kits")) {
						player.openInventory(ClassSelectUI.getClassInventory(player));
					}

					if (e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase().contains("particles")) {
						player.openInventory(ParticlesSelectUI.particlesInventory(player));
					}

					e.setCancelled(true);
				}
			}
		} catch (Exception ex) {
		}
	}

	@EventHandler
	public void onParticlesUI(InventoryClickEvent e) {
		try {
			if (e.getInventory() != null) {

				Player player = (Player) e.getWhoClicked();

				// Does this if statement even work?
				if (e.getInventory().getName()
						.equalsIgnoreCase(ParticlesSelectUI.particlesInventory(player).getName())) {
					int particleLocationSlot = 22;

					BattlePlayer battlePlayer = PlayerCache.getPlayerCache(player.getUniqueId());
					MCNPlayer corePlayer = MCNPlayerCache.getCache(player.getUniqueId());
					List<String> unlockedParticles = battlePlayer.getPlayerParticles();

					if (e.getSlot() != particleLocationSlot) {
						String use = null;

						if (ChatColor.stripColor(
								e.getClickedInventory().getItem(particleLocationSlot).getItemMeta().getDisplayName())
								.toLowerCase().contains("feet"))
							use = "player";

						if (ChatColor.stripColor(
								e.getClickedInventory().getItem(particleLocationSlot).getItemMeta().getDisplayName())
								.toLowerCase().contains("arrow"))
							use = "arrow";

						String name = ChatColor.stripColor(
								e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase().replaceAll(" ", ""));

						if (unlockedParticles.contains(name)) {

							player.setMetadata(use + "ParticleEffect",
									new FixedMetadataValue(BattleElite.getCorePlugin(), name));

							battlePlayer.setSelectedParticle(use, name);

							player.sendMessage(ChatColor.GOLD + "You selected "
									+ e.getCurrentItem().getItemMeta().getDisplayName());

							e.getWhoClicked().closeInventory();
						} else {
							if (!(corePlayer.canPurchaseByXP(ParticlesSelectUI.getParticleCost(), false))) {
								player.sendMessage(ChatColor.GOLD + "You do not have enough xp! ("
										+ ParticlesSelectUI.getParticleCost() + "XP needed)");
								e.getWhoClicked().closeInventory();
							} else {
								// Purchase it!

								// example: |name|use|effect|size|
								// we dont need use, REMOVE IT FROM BACKBONE
								// UGHHHH

								battlePlayer.unlockPlayerParticle(name);
								corePlayer.takeXP(ParticlesSelectUI.getParticleCost());

								player.setMetadata(use + "ParticleEffect",
										new FixedMetadataValue(BattleElite.getCorePlugin(), name));
								
								
								battlePlayer.setSelectedParticle(use, name);

								player.sendMessage(ChatColor.GOLD + "You unlocked "
										+ e.getCurrentItem().getItemMeta().getDisplayName());
								e.getWhoClicked().closeInventory();
							}

						}
					}
					// Do rest of stuff up here

					if (e.getSlot() == particleLocationSlot) {
						if (e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase().contains("feet")) {
							Item.setDescription(new ItemStack(Material.ARROW), e.getClickedInventory(),
									ChatColor.RED + "Player Arrow", particleLocationSlot);
							e.setCancelled(true);
							player.openInventory(e.getClickedInventory());
							return;
						}

						if (e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase().contains("arrow")) {
							Item.setDescription(new ItemStack(Material.DIAMOND_BOOTS), e.getClickedInventory(),
									ChatColor.RED + "Player Feet", particleLocationSlot);
							e.setCancelled(true);
							player.openInventory(e.getClickedInventory());
							return;
						}
					}

					e.setCancelled(true);
				}
			}
		} catch (Exception ex) {
		}
	}

	@EventHandler
	public void onTeamSelect(InventoryClickEvent e) {
		try {
			Player player = (Player) e.getWhoClicked();
			ItemStack clicked = e.getCurrentItem();
			Inventory inventory = e.getInventory();

			if (TeamHelper.playersReady() > BattleElite.getCorePlugin().getConfig().getInt("MaxPlayers")
					&& !player.getInventory().getItem(0).equals(GameUI.teamColor)) {
				player.sendMessage(ChatColor.GOLD + "Max players reached. Sorry >:");
				e.getWhoClicked().closeInventory();
				player.getInventory().setItem(0, GameUI.teamColor);
			}

			if (!e.getInventory().getName().equalsIgnoreCase(GameUI.teamUIInv.getName()))
				return;
			if (e.getCurrentItem().getItemMeta() == null)
				return;
			if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Red")) {
				GameUI.setTeamRed(player, false);
				e.setCancelled(true);
				e.getWhoClicked().closeInventory();
			}

			if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Blue")) {
				GameUI.setTeamBlue(player, false);
				e.setCancelled(true);
				e.getWhoClicked().closeInventory();
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Green")) {
				GameUI.setTeamGreen(player, false);
				e.setCancelled(true);
				e.getWhoClicked().closeInventory();

			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Yellow")) {
				GameUI.setTeamYellow(player, false);
				e.setCancelled(true);
				e.getWhoClicked().closeInventory();

			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Cancel")) {
				if (!player.getInventory().getItem(0).equals(GameUI.teamColor) && TeamHelper.playersReady() > 0) {
					TeamHelper.removePlayerFromTeam(player);
				}
				e.setCancelled(true);
				player.sendMessage(ChatColor.GOLD + "You canceled");
				player.getInventory().setItem(8, GameUI.teamColor);
				TeamQue.removePlayer(player);
				e.getWhoClicked().closeInventory();

			}

		} catch (Exception x) {
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		PlayerInventory playerInventory = player.getInventory();

		// Stops the player from dropping the nether star in game
		if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME") && InGame.getFreezeTime() >= 0) {
			event.setCancelled(true);
			player.updateInventory();
		}

		// Make this into a class in the future.
		for (int i = 0; i < SetClass.armor.length; i++)
			if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME")
					&& SetClass.armor[i].getType() == event.getItemDrop().getItemStack().getType()
					&& SetClass.getPlayerClass(player) != "scout") {
				player.sendMessage(ChatColor.RED + "Destroying linked item...");
				player.playSound(player.getLocation(), Sound.ITEM_BREAK, 2F, 1F);
				event.getItemDrop().remove();
			}

		if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_LOBBY")) {
			if ((playerInventory.getHeldItemSlot() == 8) || (playerInventory.getHeldItemSlot() == 0)
					|| (playerInventory.getHeldItemSlot() == 4) || (playerInventory.getHeldItemSlot() == 1)) {
				event.setCancelled(true);

			}
		}
		if (playerInventory.getItemInHand() == GameUI.classSelect)
			event.setCancelled(true);
	}

	// Join

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(final PlayerJoinEvent event) {

		Player player = event.getPlayer();
		PlayerCache.cachePlayer(player.getUniqueId(), new BattlePlayer(player.getUniqueId()));

		GameUI.giveHubItems(player);

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BattleElite.getCorePlugin(), new Runnable() {
			public void run() {
				PlayerParticle.loadParticleMetadata(player);
			}
		}, 25L);

		//TeamHelper.setRandomTeam(player);
		
		
		

		new TitleManager(event.getPlayer().getUniqueId(),
				ChatColor.translateAlternateColorCodes('&', "&6Welcome to &cBattlesElite"), "", 1, 2, 1);
	}

	@EventHandler
	public void onPlayerDissconect(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		GameUI.giveHubItems(player);
		PlayerCache.removePlayerCache(player.getUniqueId());

		PlayerParticle.removeParticleMetadata(player);
		
		PartyTest.removePlayer(player.getDisplayName());

	}

	// OnItemMove
	@EventHandler
	public void onClickSlot(InventoryClickEvent e) {
		// not itf
		try {
			if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME"))
				if (e.getCurrentItem().getType() == Material.NETHER_STAR) {
					e.setResult(Result.DENY);
					e.setCancelled(true);
				}

			if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_LOBBY"))
				if (e.getSlot() == 0 || e.getSlot() == 8 || e.getSlot() == 4 || e.getSlot() == 1) {
					e.setResult(Result.DENY);
					e.setCancelled(true);
				}
		} catch (Exception ex) {
		}
	}

	// Death
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		// for (int i = 0; i < SetClass.armor.length; i++)
		// if (SetClass.armor[i].getType() ==
		// event.getItemDrop().getItemStack().getType()) {

		if (event.getEntity() instanceof Player) {

			Player player = (Player) event.getEntity();
			Player killer = player.getKiller();
			if (killer != null && player != null) {
				// Begin of item handling
				for (int i = 0; i < event.getDrops().size(); i++)
					for (int x = 0; x < SetClass.armor.length; x++)
						if (SetClass.armor[x].getType() == event.getDrops().get(i).getType())
							event.getDrops().remove(i);

				if (event.getDrops().contains(new ItemStack(Material.WOOD_SWORD)))
					event.getDrops().remove(new ItemStack(Material.WOOD_SWORD));

				// End of item handling

				BattlePlayer battlePlayer = PlayerCache.getPlayerCache(player.getUniqueId());
				BattlePlayer battleKiller = PlayerCache.getPlayerCache(killer.getUniqueId());

				battlePlayer.setDeaths((battlePlayer.getDeaths() + 1));
				battleKiller.setKills((battleKiller.getKills() + 1));

				XPManager.addKill(killer);

				event.setDeathMessage(TeamHelper.getTeamColor(killer) + killer.getName()
						+ ChatColor.translateAlternateColorCodes('&', " &rhas killed ")
						+ TeamHelper.getTeamColor(player) + player.getName());

				new BukkitRunnable() {
					public void run() {
						killer.sendMessage(
								ChatColor.translateAlternateColorCodes('&',
										"&6You have gained &a&l+"
												+ BattleElite.getCorePlugin().getConfig().getInt("XP.kill")
												+ "&r&6 xp for killing " + player.getName()));
					}
				}.runTaskLaterAsynchronously(BattleElite.getCorePlugin(), 2L);

				killer.playSound(player.getLocation(), Sound.NOTE_PLING, 8F, 2F);
			} else {

				BattlePlayer battlePlayer = PlayerCache.getPlayerCache(player.getUniqueId());

				for (int i = 0; i < event.getDrops().size(); i++)
					for (int x = 0; x < SetClass.armor.length; x++)
						if (SetClass.armor[x].getType() == event.getDrops().get(i).getType())
							event.getDrops().remove(i);

				if (event.getDrops().contains(new ItemStack(Material.WOOD_SWORD)))
					event.getDrops().remove(new ItemStack(Material.WOOD_SWORD));

				try {
					EntityDamageEvent ede = player.getLastDamageCause();
					if (ede.getCause() == DamageCause.ENTITY_EXPLOSION)
						event.setDeathMessage(TeamHelper.getTeamColor(player) + player.getName()
								+ ChatColor.translateAlternateColorCodes('&', " &rhas exploded!"));

					else
						event.setDeathMessage(TeamHelper.getTeamColor(player) + player.getName()
								+ ChatColor.translateAlternateColorCodes('&', " &rhas committed suicide"));

				} catch (Exception e) {
					event.setDeathMessage(TeamHelper.getTeamColor(player) + player.getName()
							+ ChatColor.translateAlternateColorCodes('&', " &rhas committed suicide"));
				}
				battlePlayer.setDeaths(battlePlayer.getDeaths() + 1);
				event.getDrops().clear();

			}

			// FIX

			// SetClass.removePlayerFromClass(player);

			TeamHelper.addOldTeam(player);
			TeamHelper.removePlayerFromTeam(player);

			player.setHealth(player.getMaxHealth());

			if (player.getWorld() == WorldHelper.getPlayWorld())
				DeathManager.setDead(player, killer);

		}

	}

	// Quit
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.getPlayer().getInventory().clear();
		SetClass.removePlayerFromClass(event.getPlayer());
		TeamHelper.removePlayerFromTeam(event.getPlayer());
		try {
			XPManager.removeXPValues(event.getPlayer());
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	// These method's don't really apply to the player, they should honestly be
	// in a different class

	// Move
	@EventHandler
	public static void onGround(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_LOBBY")) {

			// this would check if player didn't have their hub items

			if (player.getFoodLevel() <= 19 || player.getHealth() <= 19) {
				player.setFoodLevel(20);
				player.setHealth(20);
			}
		}

		// if
		// (BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME")
		// && InGame.getFreezeTime() >= 0)
		// TeamHelper.teleportToTeam(player, TeamHelper.getPlayerTeam(player));

		if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME") && InGame.getFreezeTime() >= 0)
			if (event.getTo().getX() - event.getFrom().getX() != 0
					|| event.getTo().getZ() - event.getFrom().getZ() != 0) {
				event.getPlayer().teleport(event.getFrom());
			}

		if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME") && player.getLocation().getY() < -62)
			player.damage(666);

		if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_LOBBY")
				&& player.getLocation().getY() < -62)
			player.teleport(WorldHelper.getLobbyCenter());
	}

	@EventHandler
	public void onBlockBurn(BlockBurnEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockSpread(BlockFromToEvent event) {
		if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME"))
			event.setCancelled(true);
	}

	@EventHandler
	public void onIgnite(BlockIgniteEvent event) {
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public static void PlayerAttack(EntityDamageByEntityEvent event) {
		try {
			if (event.getEntity() instanceof Player) {
				Player player = (Player) event.getEntity();
				Player damager = (Player) event.getDamager();

				// this if statement doesnt work
				if (player.getWorld() == WorldHelper.getHubWorld()) {
					event.setCancelled(true);
				}

				if (TeamHelper.getPlayerTeam(player) == TeamHelper.getPlayerTeam(damager)) {
					event.setCancelled(true);
				}

			}
		} catch (Exception v) {
		} // Caused by tnt explosion
		if (event.getEntity() instanceof Sheep || event.getEntity() instanceof Villager)
			event.setCancelled(true);

	}

	@EventHandler
	public void onRightClick(PlayerInteractEntityEvent e) {
		Player player = e.getPlayer();
		if (e.getRightClicked().getType() == EntityType.SHEEP) {
			e.setCancelled(true);
			player.updateInventory();
			DyeColor color = ((Colorable) e.getRightClicked()).getColor();

			if (TeamHelper.playersReady() < BattleElite.getCorePlugin().getConfig().getInt("MaxPlayers")
					&& BattleElite.getGame().getCurrentState().getRawName().equals("IN_LOBBY")) {

				if (color == DyeColor.RED) {
					GameUI.setTeamRed(player, false);
					return;
				}
				if (color == DyeColor.CYAN) {
					GameUI.setTeamBlue(player, false);
					return;
				}
				if (color == DyeColor.GREEN) {
					GameUI.setTeamGreen(player, false);
					return;
				}
				if (color == DyeColor.YELLOW) {
					GameUI.setTeamYellow(player, false);
					return;
				}
			} else if (TeamHelper.playersReady() >= BattleElite.getCorePlugin().getConfig().getInt("MaxPlayers")
					&& TeamHelper.getPlayerTeam(player) != "none")
				player.sendMessage(ChatColor.GOLD + "Teams Full");
			else if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME"))
				player.sendMessage(ChatColor.GOLD + "Wait for the current game to end");
		}

		if (e.getRightClicked().getType() == EntityType.VILLAGER) {
			e.setCancelled(true);
			player.updateInventory();
			if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_LOBBY")) {
				player.openInventory(GameUI.customizationInv());
			}
		}

	}

	@EventHandler
	public void playerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String team = TeamHelper.getPlayerTeam(player);

		if (player.getWorld() == WorldHelper.getPlayWorld()
				&& BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME")) {

			switch (team) {
			case "blue":
				for (Player bluePlayer : TeamHelper.blueplayer)
					bluePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "[&9Team Blue&r] ")
							+ player.getName() + ChatColor.GRAY + " >> " + event.getMessage());
				break;
			case "red":
				for (Player redPlayer : TeamHelper.redplayer)
					redPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "[&4Team Red&r] ")
							+ player.getName() + ChatColor.GRAY + " >> " + ChatColor.WHITE + event.getMessage());
				break;
			case "green":
				for (Player greenPlayer : TeamHelper.greenplayer)
					greenPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "[&2Team Green&r] ")
							+ player.getName() + ChatColor.GRAY + " >> " + ChatColor.WHITE + event.getMessage());
				break;
			case "yellow":
				for (Player yellowPlayer : TeamHelper.yellowplayer)
					yellowPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "[&eTeam Yellow&r] ")
							+ player.getName() + ChatColor.GRAY + " >> " + ChatColor.WHITE + event.getMessage());
				break;
			case "none":
				if (TeamHelper.getPlayerTeam(player) == "none") {
					for (Player noTeamPlayer : Bukkit.getOnlinePlayers()) {
						if (TeamHelper.getPlayerTeam(noTeamPlayer) == "none")
							noTeamPlayer.sendMessage(
									ChatColor.translateAlternateColorCodes('&', "[&7None&r] ") + player.getName()
											+ ChatColor.GRAY + " >> " + ChatColor.WHITE + event.getMessage());
					}
					break;
				}
			}
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDamage(final EntityDamageEvent event) {
		Entity e = event.getEntity();
		if (e instanceof Player) {
			Player player = (Player) e;
			// check if breaks
			if (player.getWorld() == WorldHelper.getHubWorld() || Barrier.checkBarrier()
			// Make a better method for checking if the game is over
					|| player.getGameMode() == GameMode.SPECTATOR
					|| BattleElite.getGame().getCurrentState().getRawName().equals("IN_LOBBY"))
				event.setCancelled(true);

		}
	}

	@EventHandler
	public void onHurt(EntityDamageByEntityEvent event) {
		Entity entity = event.getDamager();
		Entity e = event.getEntity();
		if (e instanceof Player) {
			Player player = (Player) e;
			if (event.getCause() == DamageCause.ENTITY_EXPLOSION
					&& TeamHelper.getPlayerTeam(player) == entity.getCustomName()) {
				event.setDamage(0.0);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBreakBlock(BlockBreakEvent event) {
		if (Barrier.checkBarrier() && BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME")) {
			if (event.getBlock().getType() == Material.STAINED_GLASS || InGame.getFreezeTime() >= 0)
				event.setCancelled(true);

			if (event.getBlock().getType() == Material.CHEST)
				event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {

		Block block = event.getBlock();
		Player player = event.getPlayer();

		if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_GAME")) {

			if (block.getType() == Material.SKULL || block.getType() == Material.SKULL_ITEM) {
				event.setCancelled(true);
				return;
			}

			if (InGame.getFreezeTime() >= 0) {
				event.setCancelled(true);
				return;
			}

			if (Barrier.checkBarrier()) {
				if (event.getBlock().getY() > (WorldHelper.getMapCenterY() - 1)) {
					event.getPlayer().sendMessage(
							ChatColor.RED + "You have reached the height/ground limit with the current barrier.");
					event.setCancelled(true);
					return;
				}
			}

			if (event.getBlock().getY() > (WorldHelper.getMapCenterY() + 8)
					|| event.getBlock().getY() < (WorldHelper.getMapCenterY() - 7)) {
				event.getPlayer().sendMessage(ChatColor.RED + "You have reached the height/ground limit.");
				event.setCancelled(true);
				return;
			}

			if (event.getBlock().getLocation().subtract(0, 3, 0).getBlock().getType() == Material.AIR
					&& event.getBlock().getLocation().subtract(0, 2, 0).getBlock().getType() == Material.AIR
					&& event.getBlock().getLocation().subtract(0, 1, 0).getBlock().getType() == Material.AIR
					&& event.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
				event.getPlayer().sendMessage(ChatColor.RED + "You cannot place blocks sidewards in the air.");
				event.setCancelled(true);
				return;
			}

			if (block.getType() == Material.TNT) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "TNT can only be thrown!.");
				return;
			}

			if (block.getType() == Material.CHEST) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "Chest's cannot be placed");
				return;
			}

		}
	}

	@EventHandler
	public void shootBowEvent(EntityShootBowEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Arrow arrow = (Arrow) event.getProjectile();

			arrow.setCustomName(TeamHelper.getPlayerTeam(player));
		}
	}

	@EventHandler
	public void onArrowDamageEvent(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Fireball) {
			if (event.getEntity() instanceof Player) {
				Fireball fireball = (Fireball) event.getDamager();
				Player player = (Player) event.getEntity();
				Player damager = (Player) fireball.getShooter();

				if (fireball.getCustomName().contains(TeamHelper.getPlayerTeam(player)))
					event.setCancelled(true);
			}
		}

		if (event.getDamager() instanceof Arrow) {
			if (event.getEntity() instanceof Player) {
				Arrow arrow = (Arrow) event.getDamager();
				Player player = (Player) event.getEntity();
				Player damager = (Player) arrow.getShooter();

				if (arrow.getCustomName().contains(TeamHelper.getPlayerTeam(player)))
					event.setCancelled(true);

			}
		}
	}

	@EventHandler
	public void onInventoryOpen(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock().getType() == Material.CHEST && InGame.getFreezeTime() < 0) {
				event.setCancelled(true);
				ChestRefills.loadChest(event.getPlayer(), (Chest) event.getClickedBlock().getState());
			}
		}
	}

	/*
	 * @EventHandler public void onEntityCombustEvent(EntityCombustByBlockEvent
	 * event) { final Entity victim = event.getEntity(); if (victim instanceof
	 * Player) { event.setCancelled(true); ((Player) victim).setFireTicks(80); }
	 * }
	 */

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();

		if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_LOBBY")
				&& player.getInventory().getHeldItemSlot() == 4) {
			event.setCancelled(true);

			SetClass.initalizeClass(player, SetClass.getPlayerClass(player));
			player.getInventory().setChestplate(null);
		}
	}

	@EventHandler
	public void backToHub(PlayerInteractEvent event) {
		try {
			if (BattleElite.getGame().getCurrentState().getRawName().equals("IN_LOBBY"))
				if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
					if (event.getItem().getType().equals(Material.ENDER_PEARL)) {
						Player p = event.getPlayer();

						ServerRoute.routeToBestServer(p, GameType.HUB, true);

						event.setCancelled(true);

					}
		} catch (Exception ex) {
		}
	}

	@EventHandler
	public void onPartyRequestTeam(PartyRequestTeamEvent event) {
		Party targetParty = event.getParty();

		
		String[] members = new String[targetParty.getCurrentOnline().size()];
		
		for(int i = 0; i < targetParty.getCurrentOnline().size(); i++)
		{
			members[i] = Bukkit.getPlayer(targetParty.getCurrentOnline().get(i)).getDisplayName();
		}
		
		PartyTest.addNewParty(members);
		
		Bukkit.broadcastMessage(
				Debug.out(event.getPlayer().getName() + "'s party has " + targetParty.getCurrentOnline().size()));

	}

	@EventHandler
	public void onPayerShootBowEvnet(EntityShootBowEvent event) {

		if (event.getEntity() instanceof Player == false)
			return;

		Player player = (Player) event.getEntity();

		if (!player.hasMetadata("arrowParticleEffect"))
			return;

		String name = player.getMetadata("arrowParticleEffect").get(0).asString();

		new BukkitRunnable() {
			@Override
			public void run() {
				Arrow arrow = (Arrow) event.getProjectile();

				PlayerParticle.translateEffect(name).display(0, 0, 0, 0, 4, arrow.getLocation(), 1000);

				if (arrow.isOnGround() || arrow.isDead())
					cancel();

			}
		}.runTaskTimer(BattleElite.getCorePlugin(), 0L, 2L);

	}

	@EventHandler
	public void lobbyCarpetBounce(PlayerMoveEvent event) {
		if (event.getPlayer().getLocation().getBlock().getRelative(BlockFace.SELF).getType() == Material.CARPET && event
				.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SLIME_BLOCK)
			event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(1).setY(1.5));
	}

	/*@EventHandler
	public void preJoinEvent(AsyncPlayerPreLoginEvent event) {
		UUID playerUUID = event.getUniqueId();
		String name = event.getName();
		Party targetParty = PartyAPI.getAssociatedParty(playerUUID);
		
		if (!playerUUID.equals(targetParty.getPartyOwner()) && PartyAPI.getAssociatedParty(playerUUID) != null) {
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL,
					ChatColor.RED + "The party leader can only join minigames!");
			return;
		}

		if (targetParty.getCurrentOnline().size() > 5 && Bukkit.getOnlinePlayers().size() > 20) {
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL,
					ChatColor.RED + "Your party is too big to balance!");
			return;
		}
	}*/

}
