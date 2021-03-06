package org.getspout.spout;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.getspout.spout.config.ConfigReader;

public class PlayerManager {
	private HashMap<String, Integer> timer = new HashMap<String, Integer>();
	
	public void onPlayerJoin(Player player){
		timer.put(player.getName(), ConfigReader.getAuthenticateTicks());
	}
	
	public void onServerTick() {
		if (!ConfigReader.isForceClient()) {
			return;
		}
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (timer.containsKey(player.getName())) {
				int ticksLeft = timer.get(player.getName());
				if (--ticksLeft > 0) {
					timer.put(player.getName(), ticksLeft);
				}
				else {
					timer.remove(player.getName());
					System.out.println("[Spout] Failed to authenticate " + player.getName() + "'s client in " + ConfigReader.getAuthenticateTicks() + " server ticks.");
					if (ConfigReader.isForceClient()) {
						System.out.println("[Spout] Kicking " + player.getName() + " for not running Spout client");
						player.kickPlayer(ConfigReader.getKickMessage());
					}
				}
			}
		}
	}
	
	public void onBukkitContribSPEnable(Player player) {
		timer.remove(player.getName());
	}

}
