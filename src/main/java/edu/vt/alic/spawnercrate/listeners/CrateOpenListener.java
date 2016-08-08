package edu.vt.alic.spawnercrate.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import edu.vt.alic.spawnercrate.SpawnerCrate;

public class CrateOpenListener implements Listener {

	public CrateOpenListener() {
		SpawnerCrate.getInstance().getServer().getPluginManager().registerEvents(this, SpawnerCrate.getInstance());
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onInventoryClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		if (e.getItem() == null) {
			return;
		}

		if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()) {
			if (!e.getItem().getItemMeta().getDisplayName().contains("Spawner Crate")) {
				return;
			}
		}
				
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			e.setCancelled(true);

			if (!p.hasPermission("spawnercrate.crate.use")) {
				p.sendMessage(ChatColor.RED + "You do not have the permission to use that.");
				return;
			}
			
			ItemStack spawner = getRandomSpawner();
			
			while (!spawner.hasItemMeta()) {
				spawner = getRandomSpawner();
			}

			p.sendMessage(ChatColor.BLUE + "You have received a " 
					+ ChatColor.GREEN + spawner.getItemMeta().getDisplayName() + ChatColor.BLUE + ".");


			if (p.getInventory().firstEmpty() == -1) {
				p.sendMessage(ChatColor.RED + "Your inventory is too full to receive the spawner.");
				return;
			}

			if (e.getItem().getAmount() > 1) {
				e.getItem().setAmount(e.getItem().getAmount() - 1);
			} else {
				p.getInventory().removeItem(e.getItem());
			}

			p.getInventory().addItem(spawner);
		}
	}

	private ItemStack getRandomSpawner() {
		Random rand = new Random();
		int num = rand.nextInt(100) + 1;

		ItemStack i = new ItemStack(Material.MOB_SPAWNER);
		ItemMeta im = i.getItemMeta();

		HashMap<String, Integer> spawnerList = SpawnerCrate.getInstance().getSpawnerList();

		List<String> spawners = new ArrayList<String>(spawnerList.keySet());
		Collections.shuffle(spawners);

		for (String spawner : spawners) {
			if (num <= spawnerList.get(spawner)) {
				spawner = spawner.substring(0, 1) + spawner.substring(1).toLowerCase();
				spawner = spawner.replace("_", " ");
				im.setDisplayName(spawner + " Spawner");
				im.setLore(Arrays.asList("Spawner Crate"));
				break;
			}

		}

		i.setItemMeta(im);
		return i;
	}
}
