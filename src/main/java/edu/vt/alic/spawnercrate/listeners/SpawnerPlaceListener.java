package edu.vt.alic.spawnercrate.listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import edu.vt.alic.spawnercrate.SpawnerCrate;

public class SpawnerPlaceListener implements Listener {
	
	public SpawnerPlaceListener() {
		SpawnerCrate.getInstance().getServer().getPluginManager().registerEvents(this, SpawnerCrate.getInstance());
	}
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.LOW)
	public void onSpawnerPlace(BlockPlaceEvent e) {		
		Player p = e.getPlayer();
		
		if (e.getBlock().getState() instanceof CreatureSpawner) {
			if (e.getItemInHand().hasItemMeta() && e.getItemInHand().getItemMeta().hasLore()) {
				if (e.getItemInHand().getItemMeta().getLore().get(0).equalsIgnoreCase("Spawner Crate")) {
					if (!p.hasPermission("spawnercrate.spawner.place")) {
						p.sendMessage(ChatColor.RED + "You do not have the permission to place that.");
						return;
					}
					String entityName = e.getItemInHand().getItemMeta().getDisplayName();
					entityName = entityName.replaceAll(" Spawner", "").replace(" ", "_").toUpperCase();
					
					CreatureSpawner cs = (CreatureSpawner) e.getBlock().getState();
					cs.setSpawnedType(EntityType.valueOf(entityName));
				}
			}
		}
	}

}
