package me.broswen.bfrisk;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class FriskCommand implements CommandExecutor{
	BFrisk plugin;
	

	public FriskCommand(BFrisk passedPlugin){
		this.plugin = passedPlugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Economy econ = plugin.getEconomy();
		
		if(!sender.hasPermission("bfrisk.frisk")){
			sender.sendMessage(plugin.prefix + "You don't have permission!");
			return true;
		}
		
		if(sender instanceof Player){
			final Player player = (Player) sender;
			
			if(args.length == 1){
				
				Player targetPlayer = player.getServer().getPlayer(args [0]);
				
				if(player == targetPlayer){
					player.sendMessage(plugin.prefix + ChatColor.RED + "You may not frisk yourself!");
					return true;
				}
				
				if(targetPlayer == null){
					player.sendMessage(plugin.prefix + ChatColor.RED + "That player is not online!");
					return true;
				}
				
				if(targetPlayer.hasPermission("bfrisk.exempt")){
					player.sendMessage(plugin.prefix + ChatColor.RED + "That player may not be frisked!");
					return true;
				}
				
				if(targetPlayer.getLocation().distance(player.getLocation()) > 5){
					player.sendMessage(plugin.prefix + ChatColor.RED + "That player is out of range!");
					return true;
				}
				
				if(plugin.getConfig().getBoolean("use-frisk-delay")){
					if(plugin.friskList.containsKey(player.getName())){
						player.sendMessage(plugin.prefix + ChatColor.RED + "You must wait " + plugin.getConfig().getInt("frisk-delay") + " seconds between each frisk!");
						return true;
					}
					
					plugin.friskList.put(player.getName(), null);
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

						@Override
						public void run() {
							plugin.friskList.remove(player.getName());
							
						}
						
					}, plugin.getConfig().getInt("frisk-delay") * 20);
				}
				
				if(hasDrug(targetPlayer)){
					player.sendMessage(plugin.prefix + ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + " was carrying drugs!");
					targetPlayer.sendMessage(plugin.prefix + ChatColor.BLUE + player.getName() + ChatColor.WHITE + " caught you with drugs!");
					plugin.getServer().broadcastMessage(plugin.prefix + ChatColor.BLUE +  player.getName() + ChatColor.WHITE + " caught " + ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + " with drugs!");
					
					if(plugin.getConfig().getBoolean("use-economy")){
						player.sendMessage(plugin.prefix + ChatColor.GREEN + "You recieved $" + plugin.getConfig().getDouble("drug-reward"));
						econ.depositPlayer(player.getName(), plugin.getConfig().getDouble("drug-reward"));
					}
					
					if(plugin.getConfig().getBoolean("use-jail-plugin")){
						plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "jail " + targetPlayer.getName() + " " + plugin.getConfig().getString("jail-name") + " " + plugin.getConfig().getInt("jail-time") + "s");
					}else{
						Location jailLocation = new Location(plugin.getServer().getWorld(plugin.getConfig().getString("jail-location.world")), plugin.getConfig().getDouble("jail-location.x"), plugin.getConfig().getDouble("jail-location.y"), plugin.getConfig().getDouble("jail-location.z"));
						targetPlayer.teleport(jailLocation);
					}
					
					if(plugin.getConfig().getBoolean("move-drugs")){
						moveDrugs(targetPlayer, player);
					}else{
						removeDrugs(targetPlayer, player);
					}
					
					return true;
				}
				
				if(hasPara(targetPlayer)){
					player.sendMessage(plugin.prefix + ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + " was carrying paraphernalia!");
					targetPlayer.sendMessage(plugin.prefix + ChatColor.BLUE + player.getName() + ChatColor.WHITE + " caught you with paraphernalia!");
					plugin.getServer().broadcastMessage(plugin.prefix + ChatColor.BLUE +  player.getName() + ChatColor.WHITE + " caught " + ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + " with paraphernalia!");
					
					if(plugin.getConfig().getBoolean("use-economy")){
						player.sendMessage(plugin.prefix + ChatColor.GREEN + "You recieved $" + plugin.getConfig().getDouble("para-reward"));
						econ.depositPlayer(player.getName(), plugin.getConfig().getDouble("para-reward"));
					}
					
					if(plugin.getConfig().getBoolean("use-jail-plugin")){
						plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "jail " + targetPlayer.getName() + " " + plugin.getConfig().getString("jail-name") + " " + plugin.getConfig().getInt("jail-time") + "s");
					}else{
						Location jailLocation = new Location(plugin.getServer().getWorld(plugin.getConfig().getString("jail-location.world")), plugin.getConfig().getDouble("jail-location.x"), plugin.getConfig().getDouble("jail-location.y"), plugin.getConfig().getDouble("jail-location.z"));
						targetPlayer.teleport(jailLocation);
					}
					
					if(plugin.getConfig().getBoolean("move-drugs")){
						moveDrugs(targetPlayer, player);
					}else{
						removeDrugs(targetPlayer, player);
					}
					
					return true;
				}
				
				player.sendMessage(plugin.prefix + ChatColor.RED + "That player does not have any drugs! You were punished for false frisking!" );
				
				
			}else{
				player.sendMessage(plugin.prefix + "Format: /frisk 'playername'");
			}
			
			
		}else{
			plugin.getLogger().info("You must be a player to use that command!");
		}
		return true;
	}
	
	public boolean hasDrug(Player targetPlayer){
		Inventory inv = targetPlayer.getInventory();
		
		if(inv.contains(Material.SUGAR) || inv.contains(Material.NETHER_STALK) || inv.contains(Material.PUMPKIN_SEEDS) || inv.contains(Material.WHEAT) || inv.contains(Material.MELON_SEEDS) || hasItem(targetPlayer, Material.INK_SACK, (short) 2) || hasItem(targetPlayer, Material.INK_SACK, (short) 3)){
			return true;
		}
	
		return false;
	}
	
	public boolean hasPara(Player targetPlayer){
		Inventory inv = targetPlayer.getInventory();
		
		if(inv.contains(Material.PUMPKIN) || inv.contains(Material.SUGAR_CANE)){
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
}
