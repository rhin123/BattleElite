package net.mcnations.be.utils.particles;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import net.mcnations.be.BattleElite;
import net.mcnations.be.utils.player.BattlePlayer;
import net.mcnations.be.utils.player.PlayerCache;

public class PlayerParticle {

	public static String translateParticle(String whatToGet, String input)
	{
		if(whatToGet == "name")
		{
			return input = input.substring(0, ordinalIndexOf(input, '|',0));
		}
		
		if(whatToGet == "use")
		{
			return input = input.substring(ordinalIndexOf(input,'|',0)+1, input.length());
		}
		return null;
	}

	private static int ordinalIndexOf(String str, char c, int n) {
		int pos = str.indexOf(c, 0);
		while (n-- > 0 && pos != -1)
			pos = str.indexOf(c, pos + 1);
		return pos;
	}

	public static ParticleEffect translateEffect(String input) {
		switch (input) {
		case "barrier":
			return ParticleEffect.BARRIER;

		case "blockcrack":
			return ParticleEffect.BLOCK_CRACK;

		case "blockdust":
			return ParticleEffect.BLOCK_DUST;

		case "cloud":
			return ParticleEffect.CLOUD;

		case "criticalhit":
			return ParticleEffect.CRIT;

		case "criticalmagic":
			return ParticleEffect.CRIT_MAGIC;

		case "lavadrip":
			return ParticleEffect.DRIP_LAVA;

		case "waterdrip":
			return ParticleEffect.DRIP_WATER;

		case "enchantment":
			return ParticleEffect.ENCHANTMENT_TABLE;

		case "explosion":
			return ParticleEffect.EXPLOSION_NORMAL;

		case "firework":
			return ParticleEffect.FIREWORKS_SPARK;

		case "flame":
			return ParticleEffect.FLAME;

		case "footstep":
			return ParticleEffect.FOOTSTEP;

		case "heart":
			return ParticleEffect.HEART;

		case "itemcrack":
			return ParticleEffect.ITEM_CRACK;

		case "itemtake":
			return ParticleEffect.ITEM_TAKE;

		case "lava":
			return ParticleEffect.LAVA;

		case "mobappearance":
			return ParticleEffect.MOB_APPEARANCE;

		case "note":
			return ParticleEffect.NOTE;

		case "portal":
			return ParticleEffect.PORTAL;

		case "redstone":
			return ParticleEffect.REDSTONE;

		case "slime":
			return ParticleEffect.SLIME;

		case "smoke":
			return ParticleEffect.SMOKE_NORMAL;

		case "snowshovel":
			return ParticleEffect.SNOW_SHOVEL;

		case "snowball":
			return ParticleEffect.SNOWBALL;

		case "spell":
			return ParticleEffect.SPELL;

		case "spellinstant":
			return ParticleEffect.SPELL_INSTANT;

		case "spellmob":
			return ParticleEffect.SPELL_MOB;

		case "spellmobabient":
			return ParticleEffect.SPELL_MOB_AMBIENT;

		case "spellwitch":
			return ParticleEffect.SPELL_WITCH;

		case "suspended":
			return ParticleEffect.SUSPENDED;

		case "townaura":
			return ParticleEffect.TOWN_AURA;

		case "villagerangry":
			return ParticleEffect.VILLAGER_ANGRY;

		case "villagerhappy":
			return ParticleEffect.VILLAGER_HAPPY;

		case "watersplash":
			return ParticleEffect.WATER_SPLASH;

		case "waterwake":
			return ParticleEffect.WATER_WAKE;

		default:
			return ParticleEffect.WATER_BUBBLE;

		}
	}

	public static void removeParticleMetadata(Player player) {

		if(player.hasMetadata("arrowParticleEffect"))
		{
			player.removeMetadata("arrowParticleEffect", BattleElite.getCorePlugin());
		}
		
		if(player.hasMetadata("playerParticleEffect"))
		{
			player.removeMetadata("playerParticleEffect", BattleElite.getCorePlugin());
		}
		
	}
	
	public static void loadParticleMetadata(Player player)
	{	
		BattlePlayer battlePlayer = PlayerCache.getPlayerCache(player.getUniqueId());
		
		String playerParticle = battlePlayer.getSelectedParticle("player");
		String arrowParticle = battlePlayer.getSelectedParticle("arrow");
		
		
		if(playerParticle.length() > 0)
		player.setMetadata("playerParticleEffect",
				new FixedMetadataValue(BattleElite.getCorePlugin(), playerParticle));
		
		if(arrowParticle.length() > 0)
		player.setMetadata("arrowParticleEffect",
				new FixedMetadataValue(BattleElite.getCorePlugin(), arrowParticle));
		
	}

}
