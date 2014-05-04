package me.broswen.bfrisk;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class FriskCommand2 implements CommandExecutor{
	BFrisk plugin;
	
	public FriskCommand2(BFrisk passedPlugin){
		this.plugin = passedPlugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!sender.hasPermission("bfrisk.frisk")){
			sender.sendMessage(plugin.prefix + "You don't have permission!");
			return true;
		}
		
		if(!(sender instanceof Player)){
			plugin.getLogger().info("You must be a player to use that command!");
			return true;
		}
		
		final Player player = (Player) sender;	
		
		
		if(args.length != 1){
			player.sendMessage(plugin.prefix + "Format: /frisk 'playername'");
			return true;
		}
		
		Player targetPlayer = plugin.getServer().getPlayer(args[0]);
		
		if(targetPlayer == null){
			player.sendMessage(plugin.prefix + ChatColor.RED + "That player is not online!");
			return true;
		}
		
		if(targetPlayer == player){
			player.sendMessage(plugin.prefix + ChatColor.RED + "You may not frisk yourself!");
			return true;
		}
		
		if(targetPlayer.hasPermission("bfrisk.exempt")){
			player.sendMessage(plugin.prefix + ChatColor.RED + "That player may not be frisked!");
			return true;
		}
		
		if(player.getLocation().distance(targetPlayer.getLocation()) > 5){
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
		Inventory inv = Bukkit.getServer().createInventory(null, 36,  ChatColor.BLACK + "Frisk-" + targetPlayer.getName());
		
		inv.clear();
		plugin.addItemsInventory(inv, targetPlayer);
		player.openInventory(inv);
		
		player.playSound(player.getLocation(), Sound.CHEST_OPEN, 1, 1);
		targetPlayer.sendMessage(plugin.prefix + ChatColor.BLUE + player.getName() + ChatColor.RESET + " just started frisking you!");
		targetPlayer.playSound(targetPlayer.getLocation(), Sound.BAT_TAKEOFF, 1, 1);
		

		return true;
	}
}
