package edu.vt.alic.spawnercrate.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import edu.vt.alic.spawnercrate.SpawnerCrate;

public abstract class AbstractCommand implements CommandExecutor {
	
	public abstract void execute(CommandSender sender, String[] args);
	
	private final String commandName;
	private final String permission;
	private final boolean canConsoleUse;
	
	public AbstractCommand(String commandName, String permission, boolean canConsoleUse) {
		this.commandName = commandName;
		this.permission = permission;
		this.canConsoleUse = canConsoleUse;
		
		SpawnerCrate.getInstance().getCommand(commandName).setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getLabel().equalsIgnoreCase(commandName)) {
            return true;
		}
		
        if(!sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.RED + "You do not have the permission to use that command.");
            return true;
        }
        
        if(!canConsoleUse && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command must be executed by a player.");
            return true;
        }
        
        execute(sender, args);
        return true;
	}
}
