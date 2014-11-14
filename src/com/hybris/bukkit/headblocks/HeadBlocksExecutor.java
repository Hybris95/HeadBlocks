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

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;

import org.bukkit.entity.Player;
import org.bukkit.entity.HumanEntity;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import org.bukkit.material.Leaves;
import org.bukkit.material.Wool;
import org.bukkit.material.WoodenStep;
import org.bukkit.material.Tree;
import org.bukkit.material.Sandstone;
import org.bukkit.DyeColor;
import org.bukkit.SandstoneType;
import org.bukkit.TreeSpecies;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import org.bukkit.event.world.WorldSaveEvent;

import org.bukkit.event.player.PlayerQuitEvent;

import org.bukkit.event.inventory.InventoryEvent;
//import org.bukkit.event.Event.Result;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.lang.reflect.InvocationTargetException;

class HeadBlocksExecutor implements CommandExecutor, Listener{

    private HeadBlocks plugin;
	private HashMap<String, ItemStack> oldHelmets;
	
	private static String dataFile = "data.obj";

	HeadBlocksExecutor(HeadBlocks plugin){
		this.plugin = plugin;
		// TODO - Load from file then create new if not available
		this.oldHelmets = new HashMap<String, ItemStack>();
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onWorldSave(WorldSaveEvent e)
	{
		// TODO - Save the oldHelmets in a file
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerDisconnect(PlayerQuitEvent e)
	{
		Player who = e.getPlayer();
		if (oldHelmets.containsKey(who.getName())) 
		{
			ItemStack oldItem = oldHelmets.get(who.getName());
			if (oldItem != null) {
				who.getInventory().setHelmet(oldItem);
				oldHelmets.remove(who.getName());
			} else {
				who.getInventory().setHelmet(null);
			}
    	}
	}
	
	// TODO - Disallow dropping or taking away the helmet given - drop it effectively but add it again if doing so
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInventoryEvent(InventoryEvent e)
	{
		InventoryView inview = e.getView();
		HumanEntity viewer = inview.getPlayer();
		if(this.oldHelmets.containsKey(viewer.getName()))
		{
			Inventory inv = e.getInventory();
			if(inv instanceof PlayerInventory)
			{
				PlayerInventory playerInv = (PlayerInventory)inv;
				ItemStack currentHelmet = playerInv.getHelmet();
				Inventory bottomInv = inview.getBottomInventory();
				if(bottomInv instanceof PlayerInventory)
				{
					PlayerInventory transaction = (PlayerInventory)bottomInv;
					if((transaction.getHelmet() != null) && (transaction.getHelmet().getAmount() != 0))
					{
						transaction.setHelmet(null);
						//e.setResult(Result.DENY);
						return;
					}
				}
			}
		}
	}
	
	private boolean parseChangeBlock(CommandSender sender, Player target, String materialName)
	{		
		String data = "";
		if(materialName.contains(":"))
		{
			String[] parts = materialName.split(":");
			materialName = parts[0];
			data = parts[1].toUpperCase();
		}
		materialName = materialName.toUpperCase();
		
		MaterialData materialData = null;
		if(sender instanceof Player && sender == target && plugin.hasPermissions(sender, "self")){}
		else if(plugin.hasPermissions(sender, "other")){}
		else
		{
			// NO RIGHTS
			sender.sendMessage("You do not have rights for that command!");
			return false;
		}
		
		if(data != "")
		{
			try{
				materialData = idParse(materialName).getData().getConstructor().newInstance();
			}
			catch(NoSuchMethodException e){}
			catch(InstantiationException e){}
			catch(IllegalAccessException e){}
			catch(InvocationTargetException e){}
			catch(NullPointerException e){}
			MaterialData givenData = dataParse(data, materialData);
			if(givenData != materialData)
			{
				materialData = givenData;
			}
			else
			{
				sender.sendMessage("Data Not Found !");
			}
		}
		else
		{
			try{
				materialData = idParse(materialName).getData().getConstructor().newInstance();
			}
			catch(NoSuchMethodException e){}
			catch(InstantiationException e){}
			catch(IllegalAccessException e){}
			catch(InvocationTargetException e){}
			catch(NullPointerException e){}
		}
		
		if(materialData == null)
		{
			// MATERIAL NOT FOUND
			sender.sendMessage("Material Not Found !");
			return false;
		}
		else if (!materialData.getItemType().isBlock())
		{
			// IS NOT A BLOCK
			sender.sendMessage("Material given is not a block !");
			return false;
		}
		
		if (!oldHelmets.containsKey(target.getName())) 
		{
    		oldHelmets.put(target.getName(), target.getInventory().getHelmet());
    	}
		ItemStack newItem = materialData.toItemStack();
		target.getInventory().setHelmet(newItem);
		return true;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (label.equals("hb")) {

			if (args.length <= 0) {
				return false;
			}
			if (!args[0].equalsIgnoreCase("self") && !args[0].equalsIgnoreCase("s") && !args[0].equalsIgnoreCase("other") && !args[0].equalsIgnoreCase("o") && !args[0].equalsIgnoreCase("undoself") && !args[0].equalsIgnoreCase("us") && !args[0].equalsIgnoreCase("undoother") && !args[0].equalsIgnoreCase("uo")) {
				return false;
			}

			if (args[0].equalsIgnoreCase("self") || args[0].equalsIgnoreCase("s")) {
				if (args.length <= 1) {
					return false;
				}
				// Self - Only if a Player
				if (sender instanceof Player) {
					Player target = (Player) sender;
					return parseChangeBlock(sender, target, args[1]);
    			} else {
    				return false;
    			}
    		} else if (args[0].equalsIgnoreCase("other") || args[0].equalsIgnoreCase("o")) {
    			if (args.length <= 2) {
    				return false;
    			}

    			String playerName = args[1];
                Player[] onlinePlayers = plugin.getServer().getOnlinePlayers();
    			Player target = null;
                for(int i = 0; i < onlinePlayers.length; i++) {
                    Player onlinePlayer = onlinePlayers[i];
                    if(onlinePlayer.getName().equals(playerName)){
                        target = onlinePlayer;
                        break;
                    }
                }
    			if (target == null) {
                    return false;
    			}
				
				return parseChangeBlock(sender, target, args[2]);
    		} else if (args[0].equalsIgnoreCase("undoself") || args[0].equalsIgnoreCase("us")) {
    			if (sender instanceof Player) {
    				Player player = (Player) sender;
    				if (plugin.hasPermissions(sender, "self")) {
    					ItemStack oldItem = oldHelmets.get(player.getName());
    					if (oldItem != null) {
    						player.getInventory().setHelmet(oldItem);
    						oldHelmets.remove(player.getName());
    						return true;
    					} else {
							player.getInventory().setHelmet(null);
    						return true;
    					}
    				} else {
    					return false;
    				}
    			} else {
    				return false;
    			}
    		} else if (args[0].equalsIgnoreCase("undoother") || args[0].equalsIgnoreCase("uo")) {
    			if (args.length <= 1) {
    				return false;
    			}

    			String playerName = args[1];
                Player[] onlinePlayers = plugin.getServer().getOnlinePlayers();
    			Player player = null;
                for(int i = 0; i < onlinePlayers.length; i++) {
                    Player onlinePlayer = onlinePlayers[i];
                    if(onlinePlayer.getName().equals(playerName)){
                        player = onlinePlayer;
                        break;
                    }
                }
                if(player == null){
                    return false;
                }

    			if (plugin.hasPermissions(sender, "other")) {
    				ItemStack oldItem = oldHelmets.get(player.getName());
    				if (oldItem != null) {
    					player.getInventory().setHelmet(oldItem);
    					oldHelmets.remove(player.getName());
    					return true;
    				} else {
						player.getInventory().setHelmet(null);
    					return true;
    				}
    			} else {
    				return false;
    			}
    		} else {
    			return false; // Should never happen
    		}

    	} else {
    		return false;
    	} // Should never happen

    }

    private Material idParse(String material)
	{
		material = material.toUpperCase();
		
		Material[] values = Material.values();
		for(int i = 0; i < values.length; i++)
		{
			if(material.equals(values[i].toString()))
			{
				return values[i];
			}
		}
		return null;
    }

    private MaterialData dataParse(String data, MaterialData type)
    {
		MaterialData toReturn = type;
		if(type instanceof Leaves)
		{
			Leaves leavesSpecies = null;
			TreeSpecies[] values = TreeSpecies.values();
			for(int i = 0; i < values.length; i++)
			{
				if(data.equals(values[i].toString()))
				{
					leavesSpecies = new Leaves(values[i]);
					break;
				}
			}
			if(leavesSpecies != null)
			{
				toReturn = leavesSpecies;
			}
		}
		else if(type instanceof Sandstone)
		{
			Sandstone sandstone = null;
			SandstoneType[] values = SandstoneType.values();
			for(int i = 0; i < values.length; i++)
			{
				if(data.equals(values[i].toString()))
				{
					sandstone = new Sandstone(values[i]);
					break;
				}
			}
			if(sandstone != null)
			{
				toReturn = sandstone;
			}
		}
		else if(type instanceof Tree)
		{
			Tree tree = null;
			TreeSpecies[] values = TreeSpecies.values();
			for(int i = 0; i < values.length; i++)
			{
				if(data.equals(values[i].toString()))
				{
					tree = new Tree(values[i]);
					break;
				}
			}
			if(tree != null)
			{
				toReturn = tree;
			}
		}
		else if(type instanceof WoodenStep)
		{
			WoodenStep woodenStep = null;
			TreeSpecies[] values = TreeSpecies.values();
			for(int i = 0; i < values.length; i++)
			{
				if(data.equals(values[i].toString()))
				{
					woodenStep = new WoodenStep(values[i]);
					break;
				}
			}
			if(woodenStep != null)
			{
				toReturn = woodenStep;
			}
		}
		else if(type instanceof Wool)
		{
			Wool woolColored = null;
			DyeColor[] values = DyeColor.values();
			for(int i = 0; i < values.length; i++)
			{
				if(data.equals(values[i].toString()))
				{
					woolColored = new Wool(values[i]);
					break;
				}
			}
			if(woolColored != null)
			{
				toReturn = woolColored;
			}
		}
		else
		{
			// Unknown specific data material (Should be maintained in order to offer the most valuable modifyable Materials)
			// TODO - Send a message to the sender ?
		}
		return toReturn;
	}
}
