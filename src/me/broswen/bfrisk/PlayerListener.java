package me.broswen.bfrisk;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListener implements Listener{
	public static BFrisk plugin;
	
	public ItemStack friskstick = new ItemStack(Material.STICK);
	
	@EventHandler
	public void onPlayerInteractEntity (PlayerInteractEntityEvent event){
		Player player = event.getPlayer();
		Entity targetPlayer = event.getRightClicked();
		this.plugin = BFrisk.plugin;
		ItemMeta im = friskstick.getItemMeta();
		im.setDisplayName(ChatColor.AQUA + "Friskstick");
		friskstick.setItemMeta(im);
		
		int i = 0;
		for(i = 0; i < 64; i++, friskstick.setAmount(friskstick.getAmount() + 1)){
			
			if(!(targetPlayer instanceof Player)){
				return;
			}
			
			if(!player.getItemInHand().equals(friskstick)){
				return;
			}
			
			player.sendMessage("friskstick");
			
		}
	}
	
}
