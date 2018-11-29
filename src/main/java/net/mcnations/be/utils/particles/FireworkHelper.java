package net.mcnations.be.utils.particles;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import net.mcnations.be.BattleElite;

public class FireworkHelper {

	public static void spawnFirework(Location loc, Type type, Color color) {
		Firework f = (Firework) loc.getWorld().spawn(loc, Firework.class);

		FireworkMeta fm = f.getFireworkMeta();
		fm.addEffect(FireworkEffect.builder().flicker(false).trail(true).with(type).withColor(color)
				.withFade(color).build());
		fm.setPower(1);
		f.setFireworkMeta(fm);
	}

	public static Color fireworkColorTranslator(String str) {
		switch (str) {
		case "blue":
			return Color.BLUE;
		case "red":
			return Color.RED;
		case "green":
			return Color.GREEN;
		case "yellow":
			return Color.YELLOW;
		default:
			return Color.GRAY;
		}
	}
	
}
