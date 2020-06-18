package me.darkolythe.itemcreatorplus.CustomItems;

import me.darkolythe.itemcreatorplus.ItemCreatorPlus;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ItemCreatorGUI {

    public ItemCreatorPlus main;
    public ItemCreatorGUI(ItemCreatorPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public boolean getGUI(Player player, ItemStack pitem) {
        if (pitem.getType() != Material.AIR) {
            Inventory gui = Bukkit.getServer().createInventory(player, 27, ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Item Creator GUI");
            ItemStack item = new ItemStack(Material.GLASS_BOTTLE, 1);
            ItemMeta itemmeta = item.getItemMeta();
            itemmeta.setDisplayName(ChatColor.DARK_PURPLE + "Status Effects");
            itemmeta.setLore(Arrays.asList(ChatColor.GREEN + "Click to edit the item's status effects", "", ChatColor.GRAY + "Some examples:",
                    ChatColor.GRAY + "Jump Boost", ChatColor.GRAY + "Haste", ChatColor.GRAY + "Health", ChatColor.GRAY + "Flight"));
            item.setItemMeta(itemmeta);
            gui.setItem(6, item);

            item = new ItemStack(Material.DIAMOND, 1);
            itemmeta = item.getItemMeta();
            itemmeta.setDisplayName(ChatColor.BLUE + "Special Modifiers");
            itemmeta.setLore(Arrays.asList(ChatColor.GRAY + "Left click to edit the item's modifiers", "", ChatColor.GRAY + "Some examples:",
                    ChatColor.GRAY + "Armour Strength", ChatColor.GRAY + "Durability", ChatColor.GRAY + "Attack Damage"));
            item.setItemMeta(itemmeta);
            gui.setItem(5, item);

            item = new ItemStack(Material.BOOK, 1);
            itemmeta = item.getItemMeta();
            itemmeta.setDisplayName(ChatColor.BLUE + "Item Lore");
            itemmeta.setLore(Arrays.asList(ChatColor.GRAY + "Left click to edit the item's lore", ChatColor.GRAY + "Right click to remove the item's lore"));
            item.setItemMeta(itemmeta);
            gui.setItem(4, item);

            item = new ItemStack(Material.NAME_TAG, 1);
            itemmeta = item.getItemMeta();
            itemmeta.setDisplayName(ChatColor.GOLD + "Item Name");
            itemmeta.setLore(Arrays.asList(ChatColor.GRAY + "Left click to edit the item's name", ChatColor.GRAY + "Right click to remove the item's name"));
            item.setItemMeta(itemmeta);
            gui.setItem(3, item);

            item = new ItemStack(Material.ENCHANTED_BOOK, 1);
            itemmeta = item.getItemMeta();
            itemmeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Item Enchantments");
            itemmeta.setLore(Arrays.asList(ChatColor.GRAY + "Left click to edit the item's enchantments", ChatColor.GRAY + "Right click to remove all enchantments"));
            item.setItemMeta(itemmeta);
            gui.setItem(2, item);

            item = new ItemStack(Material.GREEN_CONCRETE, 1);
            itemmeta = item.getItemMeta();
            itemmeta.setDisplayName(ChatColor.GREEN + "Save Item");
            itemmeta.setLore(Arrays.asList(ChatColor.GRAY + "To view saved items, type '/icp items' in chat", ChatColor.GRAY + "Saved items can be duplicated, edited, or deleted from the saved items menu"));
            item.setItemMeta(itemmeta);
            gui.setItem(26, item);

            gui.setItem(22, pitem);
            
            player.openInventory(gui);

            return true;
        } else {
            player.sendMessage(main.prefix + ChatColor.RED + "The item to customize must be in your main hand!");
        }
        return false;
    }

    public void enchantGUI(Player player, ItemStack item) {
        Inventory gui = Bukkit.getServer().createInventory(player, ((Enchantment.values().length / 9) + 2) * 9, ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Enchantment GUI");
        int index = 0;
        gui.setItem((((Enchantment.values().length / 9) + 2) * 9) - 5, item);
        Enchantment[] enchants = Enchantment.values();
        Arrays.sort(enchants, Comparator.comparing(Enchantment::toString));
        for (Enchantment enchant: enchants) {
            ItemStack i = new ItemStack(Material.BOOK, 1);
            ItemMeta meta = i.getItemMeta();
            meta.setLore(Arrays.asList(ChatColor.GOLD + WordUtils.capitalize(enchant.toString().split(",")[0].replace("Enchantment[minecraft:", "").replace("_", " ")),
                    "", ChatColor.GRAY + "Left click to increase by 1", ChatColor.GRAY + "Right click to decrease by 1"));
            i.setItemMeta(meta);
            if (item.containsEnchantment(enchant)) {
                i.addUnsafeEnchantment(enchant, item.getEnchantmentLevel(enchant));
                i.setAmount(item.getEnchantmentLevel(enchant));
            }
            gui.setItem(index, i);
            index += 1;
        }
        ItemStack i = new ItemStack(Material.BARRIER, 1);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(ChatColor.RED + "Go back to main menu");
        i.setItemMeta(m);
        gui.setItem((((Enchantment.values().length / 9) + 2) * 9) - 1, i);
        player.openInventory(gui);
    }

    public void modifiersGUI(Player player, ItemStack item) {
        Inventory gui = Bukkit.getServer().createInventory(player, 27, ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Special Modifiers GUI");
        ItemStack i = new ItemStack(Material.IRON_SWORD, 1, (short) 150);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Edit item durability");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to decrease durability by 1", ChatColor.GRAY + "Shift left click to decrease durability by 10",
                                    ChatColor.GRAY + "Right click to increase durability by 1", ChatColor.GRAY + "Shift right click to increase durability by 10"));
        i.setItemMeta(m);
        gui.setItem(0, i.clone());

        i = new ItemStack(Material.BEDROCK, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Set unbreakable");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to make item unbreakable", ChatColor.GRAY + "Right click to make item breakable"));
        i.setItemMeta(m);
        gui.setItem(1, i.clone());

        i = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Edit armour strength");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to increase armour by 1", ChatColor.GRAY + "Right click to decrease armour by 1"));
        i.setItemMeta(m);
        gui.setItem(2, i.clone());

        i = new ItemStack(Material.LEATHER, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Edit armour toughness");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to increase toughness by 1", ChatColor.GRAY + "Right click to decrease toughness by 1"));
        i.setItemMeta(m);
        gui.setItem(3, i.clone());

        i = new ItemStack(Material.DIAMOND_SWORD, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Edit attack damage");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to increase damage by 1", ChatColor.GRAY + "Right click to decrease damage by 1"));
        i.setItemMeta(m);
        gui.setItem(4, i.clone());

        i = new ItemStack(Material.GLISTERING_MELON_SLICE, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Edit health");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to increase health by 1", ChatColor.GRAY + "Right click to decrease health by 1"));
        i.setItemMeta(m);
        gui.setItem(5, i.clone());

        i = new ItemStack(Material.FEATHER, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Edit speed");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to increase speed by 0.1", ChatColor.GRAY + "Right click to decrease speed by 0.1"));
        i.setItemMeta(m);
        gui.setItem(6, i.clone());

        i = new ItemStack(Material.TRIDENT, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Edit attack speed");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to increase attack speed by 0.1", ChatColor.GRAY + "Right click to decrease attack speed by 0.1"));
        i.setItemMeta(m);
        gui.setItem(7, i.clone());

        i = new ItemStack(Material.OBSIDIAN, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Edit knockback resistance");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to increase knockback resistance by 1", ChatColor.GRAY + "Right click to decrease knockback resistance by 1"));
        i.setItemMeta(m);
        gui.setItem(8, i.clone());

        i = new ItemStack(Material.NAME_TAG, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Hide attribute tags");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to hide attribute tags", ChatColor.GRAY + "Right click to show attribute tags"));
        i.setItemMeta(m);
        gui.setItem(11, i.clone());

        i = new ItemStack(Material.NAME_TAG, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Hide Enchantments tag");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to hide enchantment tags", ChatColor.GRAY + "Right click to show enchantment tags"));
        i.setItemMeta(m);
        gui.setItem(12, i.clone());

        i = new ItemStack(Material.NAME_TAG, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Hide Unbreaking tag");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to hide unbreaking tags", ChatColor.GRAY + "Right click to show unbreaking tags"));
        i.setItemMeta(m);
        gui.setItem(14, i.clone());

        i = new ItemStack(Material.NAME_TAG, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.BLUE + "Hide Potion effects tag");
        m.setLore(Arrays.asList("", ChatColor.GRAY + "Left click to hide potion effects tags", ChatColor.GRAY + "Right click to show potion effects tags"));
        i.setItemMeta(m);
        gui.setItem(15, i.clone());

        gui.setItem(22, item);

        i = new ItemStack(Material.BARRIER, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.RED + "Go back to main menu");
        i.setItemMeta(m);
        gui.setItem(26, i);

        player.openInventory(gui);
    }

    public void potionEffectsGUI(Player player, ItemStack item) {
        Inventory gui = Bukkit.getServer().createInventory(player, 45, ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Potion Effects GUI");
        int index = 0;
        for (PotionEffectType effect: PotionEffectType.values()) {
            if (effect != null) {
                ItemStack i = new ItemStack(Material.GLASS_BOTTLE, 1);
                ItemMeta m = i.getItemMeta();
                m.setLore(Arrays.asList(ChatColor.GOLD + WordUtils.capitalize(effect.getName().toLowerCase().replace("_", " ")), "",
                        ChatColor.GRAY + "Left click to increase by 1", ChatColor.GRAY + "Right click to decrease by 1",
                        ChatColor.GRAY + "Shift left click to increase enemy by 1", ChatColor.GRAY + "Shift right click to decrease enemy by 1"));
                i.setItemMeta(m);
                String enchant = m.getLore().get(0).replace(ChatColor.GOLD.toString(), ChatColor.GRAY.toString());
                if (item.hasItemMeta()) {
                    if (item.getItemMeta().hasLore()) {
                        for (String l : item.getItemMeta().getLore()) {
                            if (enchant.replace(ChatColor.GRAY.toString(), "").trim().equals(
                                    l.replace(ChatColor.RED.toString(), "").replaceAll("([^A-z ])", "").trim())) {
                                if (!l.contains(ChatColor.RED + "-")) {
                                    ItemMeta im = i.getItemMeta();
                                    im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                                    im.setLore(main.itemcreatorlistener.addLore(im, ChatColor.GREEN + "Player level: " + l.replace(ChatColor.GRAY.toString(), "").replaceAll("\\D", ""), 1));
                                    i.setItemMeta(im);
                                    i.setType(Material.POTION);
                                    PotionMeta pm = (PotionMeta) i.getItemMeta();
                                    pm.setColor(Color.RED);
                                    i.setItemMeta(pm);
                                } else {
                                    ItemMeta im = i.getItemMeta();
                                    im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                                    im.setLore(main.itemcreatorlistener.addLore(im, ChatColor.GREEN + "Enemy level: " + l.replace(ChatColor.GRAY.toString(), "").replaceAll("\\D", ""), 1));
                                    i.setItemMeta(im);
                                    i.setType(Material.POTION);
                                    PotionMeta pm = (PotionMeta) i.getItemMeta();
                                    pm.setColor(Color.BLUE);
                                    i.setItemMeta(pm);
                                }
                            }
                        }
                    }
                }
                gui.setItem(index, i);
                index += 1;
            }
        }
        ItemStack i = new ItemStack(Material.FEATHER, 1);
        ItemMeta m = i.getItemMeta();
        m.setLore(Arrays.asList(ChatColor.GOLD + "Flight", "", ChatColor.GRAY + "Left click to add flight", ChatColor.GRAY + "Right click to remove flight"));
        i.setItemMeta(m);
        gui.setItem(index, i.clone());

        i = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
        m = i.getItemMeta();
        m.setLore(Arrays.asList(ChatColor.GOLD + "XP Boost", "", ChatColor.GRAY + "Left click to add xp boost by 1", ChatColor.GRAY + "Right click to remove xp boost by 1"));
        i.setItemMeta(m);
        gui.setItem(index + 1, i);

        gui.setItem(40, item);

        i = new ItemStack(Material.BARRIER, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.RED + "Go back to main menu");
        i.setItemMeta(m);
        gui.setItem(44, i);

        player.openInventory(gui);
    }

    public void equipmentSlotGUI(Player player, ItemStack item, double amount, Attribute at, AttributeModifier.Operation op) {
        Inventory gui = Bukkit.getServer().createInventory(player, 27, ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Choose equipment slot");

        ItemStack i = new ItemStack(Material.DIAMOND_HELMET);
        ItemMeta m = i.getItemMeta();
        m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        m.setDisplayName(ChatColor.AQUA + "Head Slot");
        m.setLore(Arrays.asList(ChatColor.GRAY + "Add " + amount + " to this slot", ChatColor.GRAY + at.name(), ChatColor.GRAY + op.name()));
        i.setItemMeta(m);
        gui.setItem(1, i.clone());

        i.setType(Material.DIAMOND_CHESTPLATE);
        m.setDisplayName(ChatColor.AQUA + "Chest Slot");
        i.setItemMeta(m);
        gui.setItem(2, i.clone());

        i.setType(Material.DIAMOND_LEGGINGS);
        m.setDisplayName(ChatColor.AQUA + "Legs Slot");
        i.setItemMeta(m);
        gui.setItem(3, i.clone());

        i.setType(Material.DIAMOND_BOOTS);
        m.setDisplayName(ChatColor.AQUA + "Feet Slot");
        i.setItemMeta(m);
        gui.setItem(4, i.clone());

        i.setType(Material.DIAMOND_SWORD);
        m.setDisplayName(ChatColor.AQUA + "Hand Slot");
        i.setItemMeta(m);
        gui.setItem(5, i.clone());

        i.setType(Material.TOTEM_OF_UNDYING);
        m.setDisplayName(ChatColor.AQUA + "Off_Hand Slot");
        i.setItemMeta(m);
        gui.setItem(6, i.clone());

        i.setType(Material.ARMOR_STAND);
        m.setDisplayName(ChatColor.AQUA + "All Slot");
        i.setItemMeta(m);
        gui.setItem(7, i.clone());

        gui.setItem(22, item);

        i = new ItemStack(Material.BARRIER, 1);
        m = i.getItemMeta();
        m.setDisplayName(ChatColor.RED + "Go back to special modifiers menu");
        i.setItemMeta(m);
        gui.setItem(26, i);

        player.openInventory(gui);
    }
}

