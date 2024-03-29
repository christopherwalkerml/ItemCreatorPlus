package me.darkolythe.itemcreatorplus;

import me.darkolythe.itemcreatorplus.CustomItems.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
                        } else if (args[0].equals("reload")) {
                            if (player.hasPermission("itemcreatorplus.reload")) {
                                main.itemlist.setUp();
                                main.itemlist.importItemList();
                                sender.sendMessage(main.prefix + ChatColor.GREEN + "Reloaded items from config");
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
        if (args.length == 3 || args.length == 4) {
            if (sender.hasPermission("itemcreatorplus.giveitem")) {
                if (args[0].equals("give")) {
                    for (Player players : Bukkit.getServer().getOnlinePlayers()) {
                        if (args[1].equalsIgnoreCase(players.getName())) {
                            String name = args[2].replace("_", " ");
                            for (CustomItem item : main.itemlist.itemslist) {
                                if (item.item.hasItemMeta()) {
                                    ItemMeta meta = item.item.getItemMeta();
                                    if (meta.hasDisplayName()) {
                                        String metaName = meta.getDisplayName();

                                        if (metaName.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', name))
                                                || ChatColor.stripColor(metaName).equalsIgnoreCase(ChatColor.stripColor(name))
                                                || containsAll(ChatColor.stripColor(metaName), name)) {
                                            int amt = 1;
                                            if (args.length == 4) {
                                                try {
                                                    amt = Integer.parseInt(args[3]);
                                                } catch (Exception e) {
                                                    sender.sendMessage(main.prefix + ChatColor.RED + "Item amount must be a number");
                                                    return true;
                                                }
                                            }
                                            main.maintools.giveItem(players, item.item, amt);
                                            if (main.maintools.giveMessage) {
                                                players.sendMessage(main.prefix + ChatColor.GREEN + "You have been given " + meta.getDisplayName());
                                            }
                                            return true;
                                        }
                                    }
                                }
                            }
                            sender.sendMessage(main.prefix + ChatColor.RED + "Item " + name + " not found.");
                            return true;
                        }
                    }
                    sender.sendMessage(main.prefix + ChatColor.RED + "Player " + args[1] + " not found.");
                    return true;
                } else {
                    sender.sendMessage(main.prefix + ChatColor.RED + "Invalid command. Usage: /icp give player your_item_name [amount]");
                }
            } else {
                sender.sendMessage(main.prefix + ChatColor.RED + "Invalid permission");
            }
        } else if (args.length > 0 && args[0].equals("give")) {
            sender.sendMessage(main.prefix + ChatColor.RED + "Invalid command. Usage: /icp give player your_item_name [amount]");
            return true;
        } else if (args.length == 1) {
            if (args[0].equals("reload")) {
                main.itemlist.setUp();
                main.itemlist.importItemList();
                sender.sendMessage(main.prefix + ChatColor.GREEN + "Reloaded items from config");
                return true;
            }
            sender.sendMessage(main.prefix + ChatColor.RED + "Invalid command. Usage: /icp [items, give]");
            return true;
        } else if (args.length == 0) {
            sender.sendMessage(main.prefix + ChatColor.RED + "Invalid command. Usage: /icp give player your_item_name [amount]");
            return true;
        }
        sender.sendMessage(main.prefix + "You cannot use that command as the console.");
        return true;
    }

    private boolean containsAll(String str1, String str2) {
        String[] list = str2.split(" ");
        for (String s : list) {
            if (!str1.toLowerCase().contains(s.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}
