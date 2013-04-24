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

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import org.bukkit.event.world.WorldSaveEvent;

import org.bukkit.event.inventory.InventoryEvent;
//import org.bukkit.event.Event.Result;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

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
	
	// TODO - Disallow dropping or taking away the helmet given
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
					Player player = (Player) sender;

					byte dataByte = 0;
					String idOrMaterial = args[1];
    				Material material = null;
    				byte data = 0;
    				if (idOrMaterial.contains(":")) {
    					String[] parts = idOrMaterial.split(":");
    					idOrMaterial = parts[0];
    					material = idParse(idOrMaterial);
    					data = dataParse(parts[1], material.getId());
    				} else {
    					material = idParse(idOrMaterial);
    				}

    				if (material == null) {
    					return false;
    				}
    				if (!material.isBlock()) {
    					return false;
    				}

    				if (plugin.hasPermissions(sender, "self")) {
    					if (!oldHelmets.containsKey(player.getName())) {
    						oldHelmets.put(player.getName(), player.getInventory().getHelmet());
    					}
    					ItemStack newItem = new ItemStack(material, 1, (short) 1);
    					newItem.setData(new MaterialData(material, data));
    					player.getInventory().setHelmet(newItem);
    					return true;
    				} else {
    					return false;
    				}
    			} else {
    				return false;
    			}
    		} else if (args[0].equalsIgnoreCase("other") || args[0].equalsIgnoreCase("o")) {
    			if (args.length <= 2) {
    				return false;
    			}

    			String playerName = args[1];
    			Player player = plugin.getServer().getPlayer(playerName);
    			if (player == null) {
    				return false;
    			}

    			byte dataByte = 0;
    			String idOrMaterial = args[2];
    			Material material = null;
    			byte data = 0;
    			if (idOrMaterial.contains(":")) {
    				String[] parts = idOrMaterial.split(":");
    				idOrMaterial = parts[0];
    				material = idParse(idOrMaterial);
    				data = dataParse(parts[1], material.getId());
    			} else {
    				material = idParse(idOrMaterial);
    			}

    			if (material == null) {
    				return false;
    			}
    			if (!material.isBlock()) {
    				return false;
    			}

    			if (plugin.hasPermissions(sender, "other")) {
    				if (!oldHelmets.containsKey(player.getName())) {
    					oldHelmets.put(player.getName(), player.getInventory().getHelmet());
    				}
    				ItemStack newItem = new ItemStack(material, 1, (short) 1);
    				newItem.setData(new MaterialData(material, data));
    				player.getInventory().setHelmet(newItem);
    				return true;
    			} else {
    				return false;
    			}
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
    			Player player = plugin.getServer().getPlayer(playerName);

    			if (plugin.hasPermissions(sender, "other")) {
    				ItemStack oldItem = oldHelmets.get(player.getName());
    				if (oldItem != null) {
    					player.getInventory().setHelmet(oldItem);
    					oldHelmets.remove(player.getName());
    					return true;
    				} else {
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

    private byte dataParse(String data,int id)
    {
        try {
             return Byte.parseByte(data);
        } catch (NumberFormatException e) {
            switch (id)
            {
                case 17:
                case 18:
                    if (data.equalsIgnoreCase("redwood")) {
                        return 1;
                    } else if (data.equalsIgnoreCase("birch") || data.equalsIgnoreCase("bouleau")) {
                        return 2;
                    }
                    return 0;
                case 43:
                case 44:
                    if (data.equalsIgnoreCase("sandstone")) {
                        return 1;
                    } else if (data.equalsIgnoreCase("wood")) {
                        return 2;
                    } else if (data.equalsIgnoreCase("cobble")) {
                        return 3;
                    }
                    return 0;
                case 35:
                    if (data.equalsIgnoreCase("orange")) {
                        return 1;
                    } else if (data.equalsIgnoreCase("magenta")) {
                        return 2;
                    } else if (data.equalsIgnoreCase("lightblue")) {
                        return 3;
                    } else if (data.equalsIgnoreCase("yellow")) {
                        return 4;
                    } else if (data.equalsIgnoreCase("lightgreen")) {
                        return 5;
                    } else if (data.equalsIgnoreCase("pink")) {
                        return 6;
                    } else if (data.equalsIgnoreCase("grey")) {
                        return 7;
                    } else if (data.equalsIgnoreCase("lightgrey")) {
                        return 8;
                    } else if (data.equalsIgnoreCase("cyan")) {
                        return 9;
                    } else if (data.equalsIgnoreCase("purple")) {
                        return 10;
                    } else if (data.equalsIgnoreCase("blue")) {
                        return 11;
                    } else if (data.equalsIgnoreCase("brown")) {
                        return 12;
                    } else if (data.equalsIgnoreCase("green")) {
                        return 13;
                    } else if (data.equalsIgnoreCase("red")) {
                        return 14;
                    } else if (data.equalsIgnoreCase("black")) {
                        return 15;
                    }
		    default:
			    return 0;
            }
        }
    }

}
