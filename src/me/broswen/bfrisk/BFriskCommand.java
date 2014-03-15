package me.broswen.bfrisk;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class BFriskCommand implements CommandExecutor{
	BFrisk plugin;
	
	public BFriskCommand(BFrisk passedPlugin){
		this.plugin = passedPlugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		PluginDescriptionFile pdfFile = plugin.getDescription();
		
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("help")){
				if(!sender.hasPermission("bfrisk.help")){
					sender.sendMessage(plugin.prefix + "You don't have permission!");
					return true;
				}
				
				if(sender instanceof Player){
					Player player = (Player) sender;
					
					player.sendMessage(plugin.prefix + "/frisk 'player' - frisks a player.");
					player.sendMessage(plugin.prefix + "/bfrisk 'reload' - reloads the plugin.");
					player.sendMessage(plugin.prefix + "/bfrisk 'info' - shows info about the plugin.");
					player.sendMessage(plugin.prefix + "/bfrisk 'help' - shows this help screen.");
				}else{
					
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("info")){
				if(!sender.hasPermission("bfrisk.info")){
					sender.sendMessage(plugin.prefix + "You don't have permission!");
					return true;
				}
				
				if(sender instanceof Player){
					Player player = (Player) sender;
					
					player.sendMessage(plugin.prefix + "Version: " + pdfFile.getVersion());
					player.sendMessage(plugin.prefix + "Author: " + pdfFile.getAuthors());
					player.sendMessage(plugin.prefix + "About: " + pdfFile.getDescription());
				}else{
					
					plugin.getLogger().info(plugin.prefix + "Version: " + pdfFile.getVersion());
					plugin.getLogger().info(plugin.prefix + "Author: " + pdfFile.getAuthors());
					plugin.getLogger().info(plugin.prefix + "About: " + pdfFile.getDescription());
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("reload")){
				if(!sender.hasPermission("bfrisk.reload")){
					sender.sendMessage(plugin.prefix + "You don't have permission!");
					return true;
				}
				
				plugin.reloadConfig();
				
				if(sender instanceof Player){
					Player player = (Player) sender;
					
					player.sendMessage(plugin.prefix + "The config was reloaded!");
				}else{
					sender.sendMessage("The config was reloaded!");
				}
				return true;
			}
		}
		
		if(sender instanceof Player){
			Player player = (Player) sender;
			
			player.sendMessage(plugin.prefix + "type '/bfrisk help' for more commands.");
			
		}else{
			plugin.getLogger().info("type '/bfrisk help' for more commands.");
		}
		
		return true;
	}

}
