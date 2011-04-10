package com.hybris.bukkit.headblocks;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;

import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

class HeadBlocksExecutor implements CommandExecutor{
	
	private HeadBlocks plugin;
	
	HeadBlocksExecutor(HeadBlocks plugin){
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		
		if(label.equals("hb")){
			
			if(args.length <= 1){return false;}
			if(!args[0].equalsIgnoreCase("self") && !args[0].equalsIgnoreCase("s") && !args[0].equalsIgnoreCase("other") && !args[0].equalsIgnoreCase("o")){return false;}
			
			if(args[0].equalsIgnoreCase("self") || args[0].equalsIgnoreCase("s")){
				// Self - Only if a Player
				if(sender instanceof Player){
					Player player = (Player)sender;
					String idOrMaterial = args[1];
					Material material = idParse(idOrMaterial);
					if(material == null){return false;}
					
					if(plugin.hasPermissions(sender, "self")){
						// TODO Recuperer l'ancien et le mettre dans l'inventaire (si possible - sinon abandonner et return true)
						player.getInventory().setHelmet(new ItemStack(material, 1, (short)1, (byte)0)); // TODO Permettre de changer la couleur de la laine par exemple
						return true;
					}
					else{return false;}
				}
				else{return false;}
			}
			else if(args[0].equalsIgnoreCase("other") || args[0].equalsIgnoreCase("o")){
				if(args.length <= 2){return false;}
				
				String playerName = args[1];
				Player player = plugin.getServer().getPlayer(playerName);
				if(player == null){return false;}
				
				String idOrMaterial = args[2];
				Material material = idParse(idOrMaterial);
				if(material == null){return false;}
				
				if(plugin.hasPermissions(sender, "other")){
					// TODO Recuperer l'ancien et le mettre dans l'inventaire (si possible - sinon abandonner et return true)
					player.getInventory().setHelmet(new ItemStack(material, 1, (short)1, (byte)0)); // TODO Permettre de changer la couleur de la laine par exemple
					return true;
				}
				else{return false;}
			}
			else{
				return false; // Should never happen
			}
			
		}
		else{return false;} // Should never happen
		
	}
	
	private Material idParse(String idOrMaterial){
		Material material = null;
		int id = -1;
		try{
			id = Integer.parseInt(idOrMaterial);
			material = Material.getMaterial(id);
		}
		catch(NumberFormatException e){}
		
		if(material == null){
			material = Material.matchMaterial(idOrMaterial);
		}
		return material;
	}
	
}