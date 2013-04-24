/*
    HeadBlocks - The CraftBukkit plugin that allows you to change your Head item
    Copyright (C) 2013  Hybris95
    hybris_95@hotmail.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.hybris.bukkit.headblocks;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HeadBlocks extends JavaPlugin{
    
	private Logger log = null;
	private HeadBlocksExecutor executor = null;
	
	public void onLoad(){}
		
	public void onEnable(){
		log = getServer().getLogger();
		log.info("[HeadBlocks] Enabling...");
		try{
			executor = new HeadBlocksExecutor(this);
			getCommand("hb").setExecutor(executor);
    		getServer().getPluginManager().registerEvents(executor, this);
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
			executor = null;
			log.info("[HeadBlocks] Disabled");
			log = null;
		}
	}
	
	boolean hasPermissions(CommandSender sender, String node){
		String realNode = "headblocks." + node;
		if(sender instanceof Player){
			Player player = (Player)sender;
            return player.hasPermission(realNode) ? true : player.isOp();
		}
        else
        {
            return sender.isOp();
        }
	}
	
}