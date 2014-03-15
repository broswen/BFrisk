package me.broswen.bfrisk;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class BFrisk extends JavaPlugin{
	public static BFrisk plugin;
	PlayerListener playerlistener = new PlayerListener();
	public String prefix = ChatColor.AQUA + "[BFrisk] " + ChatColor.RESET;
	public static HashMap<String, Long> hashmap = new HashMap<String, Long>();
	
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(playerlistener, this);
		this.getCommand("frisk").setExecutor(new FriskCommand(this));
		this.getCommand("bfrisk").setExecutor(new BFriskCommand(this));
		this.getCommand("friskstick").setExecutor(new FriskstickCommand(this));
		
		this.plugin = this;
		loadConfig();
	}
	
	public void onDisable(){
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		return true;
	}
	
	public void loadConfig(){
		saveDefaultConfig();
		this.getConfig().options().copyHeader(true);
		saveConfig();
	}
}
