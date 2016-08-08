package edu.vt.alic.spawnercrate.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import edu.vt.alic.spawnercrate.SpawnerCrate;

public class SpawnerCrateCommand extends AbstractCommand {

	public SpawnerCrateCommand() {
		super("spawnercrate", "spawnercrate.give", true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args[0].equalsIgnoreCase("give")) {
			if (Bukkit.getPlayer(args[1]) == null) {
				sender.sendMessage(ChatColor.RED + "That player is currently not online or does not exist.");
				return;
			}

			Player p = Bukkit.getPlayer(args[1]);
			int invSpace = getInvSpace(p.getInventory());
			ItemStack spawnerCrate = SpawnerCrate.getInstance().getSpawnerCrate();
			int amount = 1;
			
			if (args.length == 2) {
				if (invSpace >= amount) {
					p.getInventory().addItem(spawnerCrate);
				} else {
					p.sendMessage(ChatColor.RED + "Spawner Crate dropped due to insufficient inventory space.");
					p.getWorld().dropItem(p.getLocation(), SpawnerCrate.getInstance().getSpawnerCrate());
				}
			}

			else if (args.length == 3) {
				if (!args[2].matches("^-?\\d+$")) {
					sender.sendMessage(ChatColor.RED + "The amount command argument must be an integer.");
					return;
				}

				amount = Integer.parseInt(args[2]);
				
				if (invSpace >= amount) {
					spawnerCrate.setAmount(amount);
					p.getInventory().addItem(spawnerCrate);
				}
				else {
					p.sendMessage(ChatColor.RED + "Spawner Crates dropped due to insufficient inventory space.");
					spawnerCrate.setAmount(amount);
					p.getWorld().dropItem(p.getLocation(), spawnerCrate);	
				}
				spawnerCrate.setAmount(1);
			}
			else sender.sendMessage(ChatColor.RED + "Incorrect Syntax: /spawnercrate give {player} [amount]");

			sender.sendMessage(ChatColor.BLUE + "You have sent " + ChatColor.GREEN + p.getName() + " " + ChatColor.BLUE 
					+ amount + " Spawner Crates.");
			p.sendMessage(ChatColor.BLUE + "You have received " + amount + " Spawner Crates.");
		} 
		
		else if (args[0].equalsIgnoreCase("giveall")) {
			int amount = 1;
			
			if (args.length == 1) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					int invSpace = getInvSpace(p.getInventory());
					ItemStack spawnerCrate = SpawnerCrate.getInstance().getSpawnerCrate();
					
					if (invSpace >= amount) {
						p.getInventory().addItem(spawnerCrate);
					} else {
						p.sendMessage(ChatColor.RED + "Spawner Crate dropped due to insufficient inventory space.");
						p.getWorld().dropItem(p.getLocation(), SpawnerCrate.getInstance().getSpawnerCrate());
					}
				}
			}
			
			else if (args.length == 2) {
				if (!args[1].matches("^-?\\d+$")) {
					sender.sendMessage(ChatColor.RED + "The amount command argument must be an integer.");
					return;
				}

				amount = Integer.parseInt(args[1]);
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					int invSpace = getInvSpace(p.getInventory());
					ItemStack spawnerCrate = SpawnerCrate.getInstance().getSpawnerCrate();
					
					if (invSpace >= amount) {
						spawnerCrate.setAmount(amount);
						p.getInventory().addItem(spawnerCrate);
					}
					else {
						p.sendMessage(ChatColor.RED + "Spawner Crates dropped due to insufficient inventory space.");
						spawnerCrate.setAmount(amount);
						p.getWorld().dropItem(p.getLocation(), spawnerCrate);	
					}
					spawnerCrate.setAmount(1);
				}
			}
			else sender.sendMessage(ChatColor.RED + "Incorrect Syntax: /spawnercrate giveall [amount]");
			
			sender.sendMessage(ChatColor.GREEN + "You have given everyone a Spawner Crate.");
		} 
		
		else sender.sendMessage(ChatColor.RED + "Incorrect Syntax: /spawnercrate give {player} {amount}");
	}

	private int getInvSpace(Inventory inv) {
		int space = 0;

		for (ItemStack item : inv.getContents()) {
			if (item == null) {
				space++;
			}
		}
		return space;
	}
}
