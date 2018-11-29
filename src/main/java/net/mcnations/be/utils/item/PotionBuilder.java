package net.mcnations.be.utils.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class PotionBuilder {
	
	public static ItemStack potionBuilder(PotionType potionType, PotionEffectType potionEffectType, int duration, int effect)
	{
		ItemStack potion = new Potion(potionType).toItemStack(1);
		PotionMeta meta = (PotionMeta) potion.getItemMeta();
		 
		meta.addCustomEffect(new PotionEffect(potionEffectType, duration, effect), false);
		potion.setItemMeta(meta);
		
		return potion;
	}
	
	public static ItemStack potionBuilder(PotionType potionType, PotionEffectType potionEffectType, int duration, int effect, String name)
	{
		ItemStack potion = new Potion(potionType).toItemStack(1);
		PotionMeta meta = (PotionMeta) potion.getItemMeta();
		 
		meta.addCustomEffect(new PotionEffect(potionEffectType, duration, effect), false);
		meta.setDisplayName(name);
		potion.setItemMeta(meta);
		
		return potion;
	}
	
	
}
