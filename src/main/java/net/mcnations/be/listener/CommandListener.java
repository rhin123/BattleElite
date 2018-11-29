package net.mcnations.be.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

	@EventHandler
	  public void onCommand(PlayerCommandPreprocessEvent e)
	  {
		String message = e.getMessage();
		if(message.contains("/me") || message.contains("/minecraft:me"))
			e.setCancelled(true);
	  }
	
}
