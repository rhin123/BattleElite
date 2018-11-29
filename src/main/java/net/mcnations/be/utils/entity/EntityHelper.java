package net.mcnations.be.utils.entity;

import org.bukkit.Bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import net.mcnations.be.BattleElite;

public class EntityHelper {
	public static void disableSound() {
		ProtocolLibrary.getProtocolManager().addPacketListener(
				new PacketAdapter(BattleElite.getCorePlugin(), PacketType.Play.Server.NAMED_SOUND_EFFECT) {
					@Override
					public void onPacketSending(PacketEvent event) {
						// You can also get the location of the sound effect -
						// see PacketWrapper's WrapperPlayServerNamedSoundEffect
						String soundName = event.getPacket().getStrings().read(0);
						if (soundName.startsWith("mob.sheep.") || soundName.startsWith("fireworks.launch")
								|| soundName.startsWith("mob.villager."))
							event.setCancelled(true);
					}
				});
	}
}
