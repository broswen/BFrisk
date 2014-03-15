package me.broswen.bfrisk;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FriskstickCommand implements CommandExecutor{
	BFrisk plugin;
	public ItemStack friskstick = new ItemStack(Material.STICK);
	

	public FriskstickCommand(BFrisk passedPlugin) {
		this.plugin = passedPlugin;
		
		ItemMeta im = friskstick.getItemMeta();
		im.setDisplayName(ChatColor.AQUA + "Friskstick");
		friskstick.setItemMeta(im);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		
		if(!sender.hasPermission("bfrisk.friskstick")){
			sender.sendMessage(plugin.prefix + "You don't have permission!");
			return true;
		}
		
		if(sender instanceof Player){
			Player player = (Player) sender;
			
			player.sendMessage(plugin.prefix + "You recieved a friskstick!");
			player.getInventory().addItem(friskstick);
		}else{
			plugin.getLogger().info("You must be a player to use this command!");
		}
		
		return true;
	}

}
