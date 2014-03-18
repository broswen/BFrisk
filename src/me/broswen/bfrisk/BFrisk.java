package me.broswen.bfrisk;

import java.util.HashMap;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BFrisk extends JavaPlugin{
	public static BFrisk plugin;
	PlayerListener playerlistener = new PlayerListener();
	public String prefix = ChatColor.AQUA + "[BFrisk] " + ChatColor.RESET;
	public static HashMap<String, Long> friskList = new HashMap<String, Long>();
	public static HashMap<String, Long> friskstickList = new HashMap<String, Long>();
	public static Economy econ = null;
	
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(playerlistener, this);
		this.getCommand("frisk").setExecutor(new FriskCommand(this));
		this.getCommand("bfrisk").setExecutor(new BFriskCommand(this));
		this.getCommand("friskstick").setExecutor(new FriskstickCommand(this));
		
		this.plugin = this;
		loadConfig();
		
		if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - ECONOMY WILL NOT WORK, VAULT IS NOT FOUND!", getDescription().getName()));
            return;
        }
	}
	
	public void onDisable(){
		
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		return true;
	}
	
	public void loadConfig(){
		saveDefaultConfig();
		getConfig().options().copyDefaults(true);
		getConfig().options().copyHeader(true);
		saveConfig();
	}
	
	public Economy getEconomy(){
		return this.econ;
	}
}
