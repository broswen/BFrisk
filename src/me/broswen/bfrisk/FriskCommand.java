package me.broswen.bfrisk;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

public class FriskCommand implements CommandExecutor{
	BFrisk plugin;
	

	public FriskCommand(BFrisk passedPlugin){
		this.plugin = passedPlugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		return true;
	}
}
