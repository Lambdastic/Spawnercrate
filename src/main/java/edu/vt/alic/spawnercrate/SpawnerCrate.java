package edu.vt.alic.spawnercrate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import edu.vt.alic.spawnercrate.commands.SpawnerCrateCommand;
import edu.vt.alic.spawnercrate.listeners.CrateOpenListener;
import edu.vt.alic.spawnercrate.listeners.SpawnerPlaceListener;

public class SpawnerCrate extends JavaPlugin {

	private static SpawnerCrate plugin;
	
	private ItemStack spawnerCrate;
	private HashMap<String, Integer> spawnerList;
	
	public static SpawnerCrate getInstance() {
		return plugin;
	}
	
	@Override
	public void onEnable() {
		plugin = this;
		
		setupConfig();
		populateSpawnerList();
		createSpawnerCrate();
		
		new SpawnerCrateCommand();
		new CrateOpenListener();
		new SpawnerPlaceListener();
	}
	
	private void setupConfig() {
		if (!new File(getDataFolder(), "config.yml").exists()) {
			getConfig().getDefaults();
			getConfig().options().copyDefaults(true);
			saveConfig();
		}
	}
	
	private void populateSpawnerList() {
		spawnerList = new HashMap<String, Integer>();
		
		for (String s : getConfig().getConfigurationSection("Spawners").getKeys(false)) {
			spawnerList.put(s, getConfig().getInt("Spawners." + s + ".Percent"));
		}
	}
	
	private void createSpawnerCrate() {
		spawnerCrate = new ItemStack(Material.CHEST);
		ItemMeta im = spawnerCrate.getItemMeta();
			
		List<String> lore = new ArrayList<String>();
		
		lore.add(ChatColor.GRAY + "Right click to receive one of the following:");
		lore.add("");
		
		for (String s : getConfig().getConfigurationSection("Spawners").getKeys(false)) {
			String color = getConfig().getString("Spawners." + s + ".Color");
			String spawner = s.substring(0, 1) + s.substring(1).toLowerCase();
			spawner = spawner.replace("_", " ");
			
			lore.add(ChatColor.GOLD + "* " + ChatColor.valueOf(color) + spawner + " Spawner");
		}
		
		im.setDisplayName(ChatColor.AQUA + "Spawner Crate");
		im.setLore(lore);
		
		spawnerCrate.setItemMeta(im);
	}
	
	public ItemStack getSpawnerCrate() {
		return spawnerCrate;
	}
	
	public HashMap<String, Integer> getSpawnerList() {
		return spawnerList;
	}
}
