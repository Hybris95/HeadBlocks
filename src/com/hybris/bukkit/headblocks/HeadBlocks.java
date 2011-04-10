package com.hybris.bukkit.headblocks;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import org.bukkit.command.CommandSender;

public class HeadBlocks extends JavaPlugin{
	
	private Logger log = null;
	private PermissionHandler permissions = null;
	private HeadBlocksExecutor executor = null;
	
	public void onLoad(){}
		
	public void onEnable(){
		log = getServer().getLogger();
		log.info("[HeadBlocks] Enabling...");
		try{
			loadPermissions();
			executor = new HeadBlocksExecutor(this);
			getCommand("hb").setExecutor(executor);
			log.info("[HeadBlocks] Enabled");
		}
		catch(Exception e){
			log.severe("[HeadBlocks] Failed Enabling: " + e.getMessage());
			onDisable();
		}
	}
		
	public void onDisable(){
		if(log != null){
			log.info("[HeadBlocks Disabling...");
			permissions = null;
			executor = null;
			log.info("[HeadBlocks] Disabled");
			log = null;
		}
	}
	
	private void loadPermissions(){
		// TODO
	}
	
	private void unloadPermissions(){
		// TODO
	}
	
	boolean hasPermissions(CommandSender sender, String node){
		return true; // TODO
	}
	
}