package me.darkolythe.itemcreatorplus;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandHandler implements CommandExecutor {

    private ItemCreatorPlus main = ItemCreatorPlus.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("itemcreatorplus")) { //if the player has permission, and the command is right
                if (player.hasPermission("itemcreatorplus.command")) {
                    if (args.length == 0) {
                        if (main.itemcreatorgui.getGUI(player, player.getInventory().getItemInMainHand()))
                            player.sendMessage(main.prefix + ChatColor.WHITE + "Remember to " + ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "click the item in the main menu" + ChatColor.WHITE + " when you're done." + ChatColor.RED.toString() + ChatColor.BOLD.toString() + " Hitting escape will lose the item!");
                        return true;
                    } else if (args.length == 1) {
                        if (args[0].equals("items")) {
                            if (player.hasPermission("itemcreatorplus.savemenu")) {
                                main.itemlist.openItemList(player, (byte) 0);
                            } else {
                                player.sendMessage(main.prefix + ChatColor.RED + "Invalid permission");
                            }
                            return true;
                        }
                    }
                } else {
                    player.sendMessage(main.prefix + ChatColor.RED + "Invalid permission");
                    return true;
                }
            }
        }
        if (args.length == 3) {
            if (sender.hasPermission("itemcreatorplus.giveitem")) {
                if (args[0].equals("give")) {
                    for (Player players : Bukkit.getServer().getOnlinePlayers()) {
                        if (args[1].equalsIgnoreCase(players.getName())) {
                            for (ItemStack item : main.itemlist.itemslist) {
                                if (item.hasItemMeta()) {
                                    ItemMeta meta = item.getItemMeta();
                                    if (meta.hasDisplayName()) {
                                        if (meta.getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', args[2].replace("_", " ")))) {
                                            main.maintools.giveItem(players, item);
                                            players.sendMessage(main.prefix + ChatColor.GREEN + "You have been given " + meta.getDisplayName());
                                            return true;
                                        }
                                    }
                                }
                                if (WordUtils.capitalize(item.getType().toString().toLowerCase()).equals(ChatColor.translateAlternateColorCodes('&', args[2].replace("_", " ")))) {
                                    main.maintools.giveItem(players, item);
                                    players.sendMessage(main.prefix + ChatColor.GREEN + "You have been given " + WordUtils.capitalize(item.getType().toString().toLowerCase()));
                                    return true;
                                }
                            }
                            sender.sendMessage(main.prefix + ChatColor.RED + "Item " + args[2] + " not found.");
                            return true;
                        }
                    }
                    sender.sendMessage(main.prefix + ChatColor.RED + "Player " + args[1] + " not found.");
                    return true;
                } else {
                    sender.sendMessage(main.prefix + ChatColor.RED + "Invalid command. Usage: /icp give player itemname");
                }
            } else {
                sender.sendMessage(main.prefix + ChatColor.RED + "Invalid permission");
            }
        } else if ((args.length == 1 || args.length == 2) && args[0].equals("give")) {
            sender.sendMessage(main.prefix + ChatColor.RED + "Invalid command. Usage: /icp give player itemname");
        } else if (args.length == 1) {
            sender.sendMessage(main.prefix + ChatColor.RED + "Invalid command. Usage: /icp [items, give]");
        }
        return true;
    }
}
