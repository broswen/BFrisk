package me.broswen.bfrisk;

import java.util.HashMap;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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
		this.getCommand("frisk").setExecutor(new FriskCommand2(this));
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

	public void addItemsInventory(Inventory inv, Player targetPlayer) {
		Inventory inv2 = targetPlayer.getInventory();
		int invSize = inv2.getSize();
		
		for(int i = 0 ; i < invSize ; i++) {
			ItemStack item = inv2.getItem(i);
			
			if(item != null){
				int amount = item.getAmount();
				short dura = item.getDurability();
				
				ItemStack item2 = new ItemStack(item.getType());
				item2.setDurability(dura);
				
				for(int i2=0; i2<amount; i2++){
					inv.addItem(item2);
				}
			}
		}
	}

	public void giveMoney(Double money, Player player){
		econ.depositPlayer(player.getName(), money);
	}

	public void removeDrugs(Player targetPlayer, Player player){
		Inventory targetPlayerInventory = targetPlayer.getInventory();
		int targetPlayerInventorySize = targetPlayerInventory.getSize();
		
		for(int i = 0 ; i < targetPlayerInventorySize ; i++) {
			ItemStack item = targetPlayerInventory.getItem(i);
			
			if(item != null){
			    if(item.getType() == Material.INK_SACK && item.getData().getData() == (short) 3 || item.getType() == Material.INK_SACK && item.getData().getData() == (short) 2 || item.getType() == Material.PUMPKIN_SEEDS || item.getType() == Material.MELON_SEEDS || item.getType() == Material.SUGAR || item.getType() == Material.NETHER_STALK || item.getType() == Material.WHEAT || item.getType() == Material.PUMPKIN || item.getType() == Material.SUGAR_CANE){
			    	targetPlayer.getInventory().remove(item);
			    	targetPlayer.updateInventory();
			    }
			}
		}
	}

	public void moveDrugs(Player targetPlayer, Player player){
		
		Inventory targetPlayerInventory = targetPlayer.getInventory();
		int targetPlayerInventorySize = targetPlayerInventory.getSize();
		
		for(int i = 0 ; i < targetPlayerInventorySize ; i++) {
			ItemStack item = targetPlayerInventory.getItem(i);
			
			if(item != null){
			    if(item.getType() == Material.INK_SACK && item.getData().getData() == (short) 3 || item.getType() == Material.INK_SACK && item.getData().getData() == (short) 2 || item.getType() == Material.PUMPKIN_SEEDS || item.getType() == Material.MELON_SEEDS || item.getType() == Material.SUGAR || item.getType() == Material.NETHER_STALK || item.getType() == Material.WHEAT || item.getType() == Material.PUMPKIN || item.getType() == Material.SUGAR_CANE){
			    	targetPlayer.getInventory().remove(item);
			    	targetPlayer.updateInventory();
			    	
			    	if(player.getInventory().firstEmpty() == -1){
			    		player.getWorld().dropItemNaturally(player.getLocation(), item);
			    	}else{
			    		player.getInventory().addItem(item);
				    	player.updateInventory();
			    	}
			    }
			}
		}
	}

	public boolean isClose(Player player, Player targetPlayer){
		
		if(player.getLocation().distance(targetPlayer.getLocation()) > 5){
			return false;
		}
		return true;
	}
	
	public boolean hasDrug(Player player){
		if(hasItem(player, Material.INK_SACK, (short) 3) || hasItem(player, Material.INK_SACK, (short) 2) || player.getInventory().contains(Material.SUGAR) || player.getInventory().contains(Material.PUMPKIN_SEEDS) || player.getInventory().contains(Material.MELON_SEEDS) || player.getInventory().contains(Material.WHEAT) || player.getInventory().contains(Material.NETHER_STALK)){
			return true;
		}
		return false;
	}
	
	
	public boolean hasItem(Player p, Material m, short s){
	    Inventory inv = p.getInventory();
	    for(ItemStack item : inv){
	        //This will return an NullPointerException if you do not have this if statement.
	        if(item != null){
	            if(item.getType() == m && item.getData().getData() == s){
	                return true;
	            }
	        }
	    }
	    return false;
	}
}



