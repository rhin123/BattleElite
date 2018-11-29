package net.mcnations.be.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.metadata.FixedMetadataValue;

import net.mcnations.be.BattleElite;
import net.mcnations.be.game.states.InGame;
import net.mcnations.be.team.TeamHelper;
import net.mcnations.be.team.parties.PartyTest;
import net.mcnations.be.utils.entity.SpawnSheep;
import net.mcnations.be.utils.entity.SpawnVillager;
import net.mcnations.be.utils.luckyblocks.LuckyBlocks;
import net.mcnations.be.utils.player.BattlePlayer;
import net.mcnations.be.utils.player.PlayerCache;
import net.mcnations.be.world.Circle;
import net.mcnations.be.world.WorldHelper;
import net.mcnations.core.CorePlugin;

public class GameCommands extends BukkitCommand {

	CorePlugin corePlugin;
	public GameCommands(CorePlugin corePlugin, String name)
	{
		super(name);
		this.corePlugin = corePlugin;
		this.description = "Multiple Game commands";
		this.usageMessage = "/be <subcommand>";
		this.setPermission("mcn.rank.staff");
		this.setAliases(new ArrayList<>());
	}
    @Override
    public boolean execute(CommandSender sender, String label, String[] args)
    {
        if ((sender instanceof Player))
        {
        	
			if (label.equalsIgnoreCase("be")) {
				Player player = (Player) sender;
				if (args.length > 0 ) {
					BattlePlayer battlePlayer = PlayerCache.getPlayerCache(player.getUniqueId());
					
					if(args[0].equalsIgnoreCase("getwins"))
						player.sendMessage("wins: "+battlePlayer.getWins());
					
					if(args[0].equalsIgnoreCase("setwins"))
						battlePlayer.setWins(Integer.parseInt(args[1]));
					
					if(args[0].equalsIgnoreCase("getkills"))
						player.sendMessage("kills: "+battlePlayer.getKills());
					
					if(args[0].equalsIgnoreCase("setkills"))
						battlePlayer.setKills(Integer.parseInt(args[1]));
					
					if(args[0].equalsIgnoreCase("unloadmap"))
						Bukkit.unloadWorld(WorldHelper.getPlayWorld(), false);
					
					if(args[0].equalsIgnoreCase("spawnsheep"))
					{
						SpawnSheep.load();
						SpawnVillager.load();
					}
					
					if(args[0].equalsIgnoreCase("removesheep"))
					{
						SpawnSheep.unload();
					}
					
					if(args[0].equalsIgnoreCase("giverandomskull"))
					{
						player.getInventory().addItem(LuckyBlocks.getRandomSkull());
					}
					
					if(args[0].equalsIgnoreCase("getoldteam"))
					{
						player.sendMessage(TeamHelper.getOldTeam(player));
					}
					
					if(args[0].equalsIgnoreCase("getbalancedteam"))
					{
						
						Bukkit.broadcastMessage("Player name: "+player.getName());
						int pos = Integer.parseInt(args[1]);
						player.sendMessage("Members:");
						for(int i = 0; i < PartyTest.Teams[pos].getPlayers().size(); i++)
						{
							Bukkit.broadcastMessage(PartyTest.Teams[pos].getPlayers().get(i));
						}
					}
					
					if(args[0].equalsIgnoreCase("setparticle"))
					{
						String particle = args[1].toLowerCase().replace(" ", "");
						String use = args[2].toLowerCase().replace(" ", "");
						player.setMetadata(use+"ParticleEffect",
								new FixedMetadataValue(BattleElite.getCorePlugin(), particle));
						
						player.sendMessage("Particle: "+particle);
						player.sendMessage("Use: "+use);
						player.sendMessage("Raw Name:"+particle+"|"+use);
					}
					
					if(args[0].equalsIgnoreCase("setarea"))
					{
						List<Block> blocks = WorldHelper.blocksFromTwoPoints(LuckyBlocks.getPlayWorldCenter(), WorldHelper.getCornerLocation(Integer.parseInt(args[1])));	
						for(int i = blocks.size()-1; i >= 0; i--)
						{
							Block currentBlock = blocks.get(i);
							
							if(!LuckyBlocks.isPositionAppropiate(currentBlock))
							{
								blocks.remove(i);
							}
						}
						
						for(int i = 0; i < 3; i ++)
						{
							Random rnd = new Random();
							Block luckyBlock = blocks.get(rnd.nextInt(blocks.size()-1));
							LuckyBlocks.setBlockSkull(luckyBlock);
						}
					}
						
						//circle size 60
						//
						
						if(args[0].toLowerCase().startsWith("stopgame") && player.isOp())
						{
							InGame.setTeamName(ChatColor.YELLOW + "No Team");
							InGame.setTeamNameRaw("n!on!e");
							BattleElite.getGame().setState(BattleElite.getGame().getStates().get(0));
							player.sendMessage(ChatColor.GOLD+"Game Stopped");
						}
						if(args[0].toLowerCase().startsWith("startgame") && player.isOp())
						{
							BattleElite.getGame().setState(BattleElite.getGame().getStates().get(1));
							player.sendMessage(ChatColor.GOLD+"Game Started");
						}
						
						if(args[0].toLowerCase().startsWith("resetcircle"))
						{
							
							for(int size = 64; size > 0; size--)
							{
								player.sendMessage(ChatColor.GOLD+"Deleting Radius "+size);
							for(double i = 0.0; i < 720.0;i++) {
								double angle = i;
								double x =  size*Math.cos(angle);
								double z =  size*Math.sin(angle);
								//Bukkit.getWorld(BattleElite.getConfig.getString("World1")).getBlockAt((int)BattleElite.getConfig.getInt("Center.x")+(int)x,(int)BattleElite.getConfig.getInt("Center.y"),(int)BattleElite.getConfig.getInt("Center.z")+(int)z).setType(Material.AIR);
								
								//Fix this please!!
								for(int v = 75; v > WorldHelper.getMapCenterY()-25; v--)
								WorldHelper.getPlayWorld().getBlockAt((int)0+(int)x, v,(int)0+(int)z).setType(Material.AIR);
								
							}
							}
						}
						
						if(args[0].toLowerCase().startsWith("savemap"))
						{
							player.sendMessage(ChatColor.GOLD+"Ill implement this later if anyone bitches about it");
						}
						
						if(args[0].toLowerCase().startsWith("tpplaymap"))
						{
							Location loc = new Location(WorldHelper.getPlayWorld(), 0,90,0);
									player.teleport(loc);
						}
						
						if(args[0].toLowerCase().startsWith("enablecircle"))
							Circle.circleEnabled = true;
						if(args[0].toLowerCase().startsWith("disablecircle"))
							Circle.circleEnabled = false;
						
						if(args[0].toLowerCase().startsWith("getcentercoords"))
							player.sendMessage(new Location(WorldHelper.getPlayWorld(), WorldHelper.getMapCenterX(), WorldHelper.getMapCenterY(),
				WorldHelper.getMapCenterZ())+"");
						
					}
			 }	 
        }

        return false;
    }
}
