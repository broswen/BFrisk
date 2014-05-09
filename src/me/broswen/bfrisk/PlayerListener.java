package me.broswen.bfrisk;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class PlayerListener implements Listener{
	public static BFrisk plugin;
	
	public ItemStack friskstick = new ItemStack(Material.STICK);
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
		Entity targetPlayer = event.getRightClicked();
		Player player = event.getPlayer();
		this.plugin = BFrisk.plugin;
		
		ItemStack friskStick = new ItemStack(Material.STICK);
		ItemMeta friskStickMeta = friskStick.getItemMeta();
		friskStickMeta.setDisplayName(ChatColor.RESET.AQUA + "Friskstick");
		friskStick.setItemMeta(friskStickMeta);
		
		for(int i = 0; i < 64; i++, friskStick.setAmount(friskStick.getAmount() + 1)){
			
			if(player.getItemInHand().equals(friskStick)){
				
				if(targetPlayer instanceof Player){
					String targetPlayerName = ((HumanEntity) targetPlayer).getName();
					
					if(player.hasPermission("bfrisk.frisk")){
						Bukkit.dispatchCommand(player, "frisk " + targetPlayerName);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event){
		this.plugin = BFrisk.plugin;
		Player player = (Player) event.getWhoClicked();
		
		if(!event.getInventory().getName().startsWith(ChatColor.BLACK + "Frisk")){
			return;
		}
		
		if(event.getCurrentItem() == null || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR){
			return;
		}
		
		ItemStack item = event.getCurrentItem();
		String string = event.getInventory().getName();
		String[] parts = string.split("-");
		String part2 = parts[1];
		Player targetPlayer = plugin.getServer().getPlayer(part2);
		
		if(targetPlayer == null){
			player.sendMessage(plugin.prefix + "That player is no longer online!");
			player.closeInventory();
			return;
		}
		
		if(!plugin.isClose(player, targetPlayer)){
			player.sendMessage(plugin.prefix + "That player is now out of range!");
			player.closeInventory();
			return;
		}
		
		event.setCancelled(true);
		if(item.getType() == Material.INK_SACK && item.getData().getData() == (short) 3 || item.getType() == Material.INK_SACK && item.getData().getData() == (short) 2 || item.getType() == Material.PUMPKIN_SEEDS || item.getType() == Material.MELON_SEEDS || item.getType() == Material.SUGAR || item.getType() == Material.NETHER_STALK || item.getType() == Material.WHEAT || item.getType() == Material.PUMPKIN || item.getType() == Material.SUGAR_CANE){
			
			if(!plugin.hasDrug(targetPlayer)){
				player.sendMessage(plugin.prefix + "That player doesn't have drugs anymore!");
				player.closeInventory();
				return;
			}

			player.closeInventory();
			
			if(plugin.getConfig().getBoolean("use-economy")){
				plugin.giveMoney(plugin.getConfig().getDouble("drug-reward"), player);
				player.sendMessage(plugin.prefix + ChatColor.GREEN + "You recieved $" + plugin.getConfig().getDouble("drug-reward"));
			}
			
			if(plugin.getConfig().getBoolean("use-jail-plugin")){
				plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "jail " + targetPlayer.getName() + " " + plugin.getConfig().getString("jail-name") + " " + plugin.getConfig().getInt("jail-time") + "s");
			}else{
				Location jailLocation = new Location(plugin.getServer().getWorld(plugin.getConfig().getString("jail-location.world")), plugin.getConfig().getDouble("jail-location.x"), plugin.getConfig().getDouble("jail-location.y"), plugin.getConfig().getDouble("jail-location.z"));
				targetPlayer.teleport(jailLocation);
			}
			
			if(plugin.getConfig().getBoolean("move-drugs")){
				plugin.moveDrugs(targetPlayer, player);
			}else{
				plugin.removeDrugs(targetPlayer, player);
			}
			
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
			player.sendMessage(plugin.prefix + ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + " was carrying drugs!");
			targetPlayer.sendMessage(plugin.prefix + ChatColor.BLUE + player.getName() + ChatColor.WHITE + " caught you with drugs!");
			plugin.getServer().broadcastMessage(plugin.prefix + ChatColor.BLUE +  player.getName() + ChatColor.WHITE + " caught " + ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + " with drugs!");
			
			return;
		}
		
		player.sendMessage(plugin.prefix + "You did not find any drugs!");
		player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 2);
		player.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 10, 0));
		player.closeInventory();
		
	}

}
