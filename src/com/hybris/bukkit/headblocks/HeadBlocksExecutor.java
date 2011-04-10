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

                    byte dataByte = 0;
                    String idOrMaterial = args[1];
                    Material material = null;
                    if (idOrMaterial.contains(":")) {
                        String[] parts = idOrMaterial.split(":");
                        idOrMaterial = parts[0];
                        material = idParse(idOrMaterial);
                        String data = dataParse(parts[1],material.getId() );
                        try {
                             dataByte = Byte.parseByte(data);
                        } catch (NumberFormatException e) {
                            dataByte = 0;
                        }
                    }
                    else
                        material = idParse(idOrMaterial);

                    if(material == null){return false;}
                    if(!material.isBlock())//TODO Message d'erreur
                        {return false;}
                    if(plugin.hasPermissions(sender, "self")){
                        // TODO Recuperer l'ancien et le mettre dans l'inventaire (si possible - sinon abandonner et return true)
                        player.getInventory().setHelmet(new ItemStack(material, 1, (short)1, (byte)dataByte));
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

                byte dataByte = 0;
                String idOrMaterial = args[2];
                Material material = null;
                if (idOrMaterial.contains(":")) {
                    String[] parts = idOrMaterial.split(":");
                    idOrMaterial = parts[0];
                    material = idParse(idOrMaterial);
                    String data = dataParse(parts[1],material.getId() );
                    try {
                         dataByte = Byte.parseByte(data);
                    } catch (NumberFormatException e) {
                        dataByte = 0;
                    }
                }
                else
                    material = idParse(idOrMaterial);

                if(material == null){return false;}
                if(!material.isBlock())//TODO Message d'erreur
                        {return false;}
                if(plugin.hasPermissions(sender, "other")){
                    // TODO Recuperer l'ancien et le mettre dans l'inventaire (si possible - sinon abandonner et return true)
                    player.getInventory().setHelmet(new ItemStack(material, 1, (short)1, (byte)dataByte)); // TODO Permettre de changer la couleur de la laine par exemple
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
                    } else if (data.equalsIgnoreCase("wood") {
                        return 2;
                    } else if (data.equalsIgnoreCase("cobble") {
                        return 3;
                    }
                    return 0;
                case 35:
                    if (data.equalsIgnoreCase("orange")) {
                        return 1;
                    } else if (data.equalsIgnoreCase("magenta") {
                        return 2;
                    } else if (data.equalsIgnoreCase("lightblue") {
                        return 3;
                    } else if (data.equalsIgnoreCase("yellow") {
                        return 4;
                    } else if (data.equalsIgnoreCase("lightgreen") {
                        return 5;
                    } else if (data.equalsIgnoreCase("pink") {
                        return 6;
                    } else if (data.equalsIgnoreCase("grey") {
                        return 7;
                    } else if (data.equalsIgnoreCase("lightgrey") {
                        return 8;
                    } else if (data.equalsIgnoreCase("cyan") {
                        return 9;
                    } else if (data.equalsIgnoreCase("purple") {
                        return 10;
                    } else if (data.equalsIgnoreCase("blue") {
                        return 11;
                    } else if (data.equalsIgnoreCase("brown") {
                        return 12;
                    } else if (data.equalsIgnoreCase("green") {
                        return 13;
                    } else if (data.equalsIgnoreCase("red") {
                        return 14;
                    } else if (data.equalsIgnoreCase("black") {
                        return 15;
                    }
                    return 0;
            }
            dataByte = 0;
        }
    }

}
