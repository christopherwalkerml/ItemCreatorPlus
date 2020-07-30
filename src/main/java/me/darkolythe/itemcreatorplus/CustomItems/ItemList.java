package me.darkolythe.itemcreatorplus.CustomItems;

import me.darkolythe.itemcreatorplus.ItemCreatorPlus;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.min;

public class ItemList {

    public ItemCreatorPlus main;
    public ItemList(ItemCreatorPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public int maxItems = 0;

    public List<ItemStack> itemslist = new ArrayList<>();
    public Map<ItemStack, Player> playerlist = new HashMap<>();

    public FileConfiguration itemscfg;
    public File items;

    public void openItemList(Player player, byte page) {
        Inventory gui = Bukkit.getServer().createInventory(player, 54, org.bukkit.ChatColor.LIGHT_PURPLE.toString() + org.bukkit.ChatColor.BOLD.toString() + "Saved Items List");

        int i;
        for (i = 0; i < min(45, itemslist.size() - (page * 45)); i++) {
            ItemStack item = itemslist.get(i + (page * 45)).clone();
            ItemMeta itemmeta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            if (itemmeta.hasLore()) {
                lore = itemmeta.getLore();
            }
            lore.add(ChatColor.GRAY + "---------------------");
            lore.add(ChatColor.GRAY + "Left click to duplicate");
            lore.add(ChatColor.GRAY + "Shift click to edit");
            lore.add(ChatColor.GRAY + "Right click to delete");
            lore.add(ChatColor.GRAY + "Created by: " + ChatColor.RED + (playerlist.get(item) != null ? playerlist.get(item).getName() : "Unknown"));
            lore.add(ChatColor.GRAY + "---------------------");
            itemmeta.setLore(lore);
            item.setItemMeta(itemmeta);

            gui.setItem(i, item);
        }

        if (itemslist.size() > (page * 45) + 45) {
            ItemStack pageitem = new ItemStack(Material.ARROW);
            ItemMeta pagemeta = pageitem.getItemMeta();
            pagemeta.setDisplayName(org.bukkit.ChatColor.GRAY + "Click to go to page " + (page + 2));
            pageitem.setItemMeta(pagemeta);
            gui.setItem(51, pageitem.clone());
        }
        if (page > 0) {
            ItemStack pageitem = new ItemStack(Material.ARROW);
            ItemMeta pagemeta = pageitem.getItemMeta();
            pagemeta.setDisplayName(org.bukkit.ChatColor.GRAY + "Click to go to page " + page);
            pageitem.setItemMeta(pagemeta);
            gui.setItem(47, pageitem.clone());
        }

        player.openInventory(gui);
    }

    public void importItemList() {
        if (itemscfg.contains("items")) {
            for (String item : itemscfg.getConfigurationSection("items").getKeys(false)) {
                if (itemscfg.contains("items." + item + ".item")) {
                    itemslist.add(itemscfg.getItemStack("items." + item + ".item"));
                    playerlist.put(itemscfg.getItemStack("items." + item + ".item"), (Player) itemscfg.get("items." + item + ".player"));
                } else {
                    itemslist.add(itemscfg.getItemStack("items." + item));
                    playerlist.put(itemscfg.getItemStack("items." + item), null);
                }
            }
        }
    }

    public void saveItemList() {
        itemscfg.set("items", null);

        int c = 0;

        for (int i = 0; i < itemslist.size(); i++) {
            itemscfg.set("items." + c + ".player", playerlist.get(itemslist.get(i)));
            itemscfg.set("items." + c + ".item", itemslist.get(i));
            c++;
        }

        try {
            itemscfg.save(items);
        } catch (IOException e) {

        }
    }

    public void setUp() {
        items = new File(main.getDataFolder(), "itemslist.yml");

        if (!items.exists()) {
            try {
                items.createNewFile();
                System.out.println(main.prefix + ChatColor.GREEN + "itemslist.yml has been created");
            } catch (IOException e) {
                System.out.println(main.prefix + ChatColor.RED + "Could not create itemslist.yml");
            }
        }
        itemscfg = YamlConfiguration.loadConfiguration(items);
    }

    public int playerItemCount(Player player) {
        int count = 0;
        for (ItemStack i : playerlist.keySet()) {
            Player p = playerlist.get(i);
            if (p != null && p.getName().equals(player.getName())) {
                count += Collections.frequency(itemslist, i);
            }
        }
        return count;
    }
}
