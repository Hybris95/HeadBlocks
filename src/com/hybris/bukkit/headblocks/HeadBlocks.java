package com.hybris.bukkit.headblocks;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class HeadBlocks extends JavaPlugin{
	
	private Logger log = null;
	private PermissionHandler permissions = null;
	
	public void onLoad(){}
		
	public void onEnable(){
		log = getServer().getLogger();
		log.info("[HeadBlocks] Enabling...");
		try{
			// TODO
			playerInv.setHelmet(new ItemStack(Material.GOLD_HELMET));
			log.info("[HeadBlocks] Enabled");
		}
		catch(Exception e){
			onDisable();
			log.severe("[HeadBlocks] Failed Enabling: " + e.getMessage());
			
		}
	}
		
	public void onDisable(){
		if(log != null){
			log.info("[HeadBlocks Disabling...");
			// TODO
			log.info("[HeadBlocks] Disabled");
			log = null;
		}
	}
	
}