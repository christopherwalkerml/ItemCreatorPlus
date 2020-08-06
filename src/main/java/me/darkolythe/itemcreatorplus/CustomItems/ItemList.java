package me.darkolythe.itemcreatorplus.CustomItems;

import me.darkolythe.itemcreatorplus.ItemCreatorPlus;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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

    public List<CustomItem> itemslist = new ArrayList<>();

    public FileConfiguration itemscfg;
    public File items;

    public void openItemList(Player player, byte page) {
        Inventory gui = Bukkit.getServer().createInventory(player, 54, org.bukkit.ChatColor.LIGHT_PURPLE.toString() + org.bukkit.ChatColor.BOLD.toString() + "Saved Items List");

        int i;
        for (i = 0; i < min(45, itemslist.size() - (page * 45)); i++) {
            ItemStack item = itemslist.get(i + (page * 45)).item.clone();
            ItemMeta itemmeta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            if (itemmeta.hasLore()) {
                lore = itemmeta.getLore();
            }
            lore.add(ChatColor.GRAY + "---------------------");
            lore.add(ChatColor.GRAY + "Left click to duplicate");
            lore.add(ChatColor.GRAY + "Shift click to edit");
            lore.add(ChatColor.GRAY + "Right click to delete");
            lore.add(ChatColor.GRAY + "Created by: " + ChatColor.RED + ((itemslist.get(i).creator != null && itemslist.get(i).getCreatorName() != null)
                                                                        ? itemslist.get(i).getCreatorName() : "Unknown"));
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
            Object identifier = itemscfg.get("items");
            if (identifier instanceof List) {
                List<?> temp = itemscfg.getList("items");
                for (Object o : temp) {
                    itemslist.add((CustomItem) o);
                }
            } else {
                for (String item : itemscfg.getConfigurationSection("items").getKeys(false)) {
                    ItemStack i;
                    OfflinePlayer p;
                    CustomItem ci;
                    if (itemscfg.contains("items." + item + ".item")) {
                        i = itemscfg.getItemStack("items." + item + ".item");
                        p = (OfflinePlayer) itemscfg.get("items." + item + ".player");
                        List<String> plr = new ArrayList<>();
                        plr.add(p.getName());
                        plr.add(p.getUniqueId().toString());
                        ci = new CustomItem(i, plr);
                    } else {
                        i = itemscfg.getItemStack("items." + item);
                        ci = new CustomItem(i, new ArrayList<>());
                    }
                    itemslist.add(ci);
                }
            }
        }
    }

    public void saveItemList() {
        itemscfg.set("items", null);
        itemscfg.set("items", itemslist);

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
        for (CustomItem i : itemslist) {
            UUID uuid = i.getCreatorUUID();
            if (uuid != null && uuid.equals(player.getUniqueId())) {
                count += Collections.frequency(itemslist, i);
            }
        }
        return count;
    }
}
