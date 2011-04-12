package com.hybris.bukkit.headblocks;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
			unloadPermissions();
			executor = null;
			log.info("[HeadBlocks] Disabled");
			log = null;
		}
	}
	
	private void loadPermissions(){
		Plugin test = getServer().getPluginManager().getPlugin("Permissions");
			
		if (permissions == null) {
			if (test != null) {
				getServer().getPluginManager().enablePlugin(test);
				permissions = ((Permissions) test).getHandler();
				log.info("[HeadBlocks] successfully loaded Permissions.");
			}
			else {
				log.info("[HeadBlocks] not using Permissions. Permissions not detected");
			}
		}
	}
	
	private void unloadPermissions(){
		permissions = null;
	}
	
	boolean hasPermissions(CommandSender sender, String node){
		String realNode = "headblocks." + node;
		if(sender instanceof Player){
			Player player = (Player)sender;
			if(permissions != null){
				return permissions.has(player, realNode);
			}
			else{
				return player.isOp();
			}
		}
		else if(sender.isOp()){
			return true;
		}
		else{
			return false;
		}
	}
	
}