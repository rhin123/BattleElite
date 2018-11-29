package net.mcnations.be.utils.misc;

import net.md_5.bungee.api.ChatColor;

public class Debug {

	public static String out(String input)
	{
		return ChatColor.translateAlternateColorCodes('&', "&8[&cDEBUG&8]&e "+input);
	}
	
}
