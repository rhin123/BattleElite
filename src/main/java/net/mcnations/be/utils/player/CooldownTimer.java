package net.mcnations.be.utils.player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownTimer implements Runnable
{

    public static ConcurrentHashMap<UUID, Integer> cooldown = new ConcurrentHashMap<>();

    @Override
    public void run()
    {
        cooldown.forEach((uuid, integer) -> {
            int i = cooldown.get(uuid);
            if(--i == 0)
                cooldown.remove(uuid);
            else cooldown.put(uuid, i);
        });
    }
}
