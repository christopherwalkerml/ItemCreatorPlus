package me.darkolythe.itemcreatorplus.CustomItems;

import com.google.common.collect.Multimap;
import me.darkolythe.itemcreatorplus.ItemCreatorPlus;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemCreatorListener implements Listener {

    public ItemCreatorPlus main;
    public ItemCreatorListener(ItemCreatorPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        String title = player.getOpenInventory().getTitle();
        boolean isproperitem = true;
        if (item != null) {
            if (event.getInventory().getType() == InventoryType.CHEST) {
                if (event.getClickedInventory() != player.getInventory()) {
                    if (title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Enchantment GUI")) {
                        event.setCancelled(true);
                        clickEnchantmentGUI(event, player);
                    } else if (title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Special Modifiers GUI")) {
                        event.setCancelled(true);
                        clickSpecialModifiersGUI(event, player);
                    } else if (title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Choose equipment slot")) {
                        event.setCancelled(true);
                        clickAttributesGUI(event, player);
                    } else if (title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Potion Effects GUI")) {
                        event.setCancelled(true);
                        clickPotionEffectsGUI(event, player);
                    } else if (title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Item Creator GUI")) {
                        event.setCancelled(true);
                        clickGeneralGUI(event, player);
                    } else if (title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Lore Editor GUI")) {
                        event.setCancelled(true);
                        clickLoreGUI(event, player);
                    }
                } else {
                    isproperitem = false;
                }
            } else {
                isproperitem = false;
            }
            if (!isproperitem) {
                if (title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Enchantment GUI") ||
                        title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Special Modifiers GUI") ||
                        title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Choose equipment slot") ||
                        title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Potion Effects GUI") ||
                        title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Item Creator GUI") ||
                        title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Lore Editor GUI")) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (main.itemmain.catchChat.containsKey(player)) {
            event.setCancelled(true);
            ItemStack item = main.itemmain.catchChat.get(player).clone();
            if (!event.getMessage().equals("cancel")) {
                ItemMeta meta = item.getItemMeta();
                if (main.itemmain.catchType.get(player).equals("name")) {
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
                } else if (main.itemmain.catchType.get(player).contains("lore")) {
                    String data = main.itemmain.catchType.get(player);
                    if (data.equals("lore")) {
                        meta.setLore(addLore(meta, ChatColor.translateAlternateColorCodes('&', event.getMessage()), -1));

                        item.setItemMeta(meta);
                        main.itemmain.toOpen.put(player, "lore");
                        main.itemmain.catchChat.put(player, item);
                        return;
                    } else {
                        int index = Integer.parseInt(data.replace("lore ", ""));
                        List<String> lore = meta.getLore();
                        lore.set(index, ChatColor.translateAlternateColorCodes('&', event.getMessage()));
                        meta.setLore(lore);

                        item.setItemMeta(meta);
                        main.itemmain.toOpen.put(player, "lore");
                        main.itemmain.catchChat.put(player, item);
                        return;
                    }
                }
                item.setItemMeta(meta);
            }
            main.itemmain.toOpen.put(player, ChatColor.translateAlternateColorCodes('&', event.getMessage()));
            main.itemmain.catchChat.put(player, item);
        }
    }

    private void clickEnchantmentGUI(InventoryClickEvent event, Player player) {
        ItemStack pitem = event.getInventory().getItem((((Enchantment.values().length / 9) + 2) * 9) - 5);
        if (event.getRawSlot() < ((Enchantment.values().length / 9) + 1) * 9 && event.getRawSlot() >= 0 && pitem != null) {
            Enchantment[] enchants = Enchantment.values();
            Arrays.sort(enchants, Comparator.comparing(Enchantment::toString));
            Enchantment enchant = enchants[event.getRawSlot()];
            if (event.isLeftClick()) {
                if (pitem.containsEnchantment(enchant)) {
                    pitem.addUnsafeEnchantment(enchant, pitem.getEnchantmentLevel(enchant) + 1);
                } else {
                    pitem.addUnsafeEnchantment(enchant, 1);
                }
            } else if (event.isRightClick()) {
                if (pitem.getEnchantmentLevel(enchant) == 1) {
                    pitem.removeEnchantment(enchant);
                }
                if (pitem.containsEnchantment(enchant)) {
                    pitem.addUnsafeEnchantment(enchant, pitem.getEnchantmentLevel(enchant) - 1);
                }
            }
            main.itemcreatorgui.enchantGUI(player, pitem);
        }
        if (event.getCurrentItem().equals(main.itemcreatorgui.createBackButton())) {
            main.itemcreatorgui.getGUI(player, pitem);
        }
    }

    private void clickSpecialModifiersGUI(InventoryClickEvent event, Player player) {
        ItemStack pitem = event.getInventory().getItem(22);
        if (event.getSlot() <= 33 && event.getSlot() >= 0 && pitem != null) {
            if (event.isLeftClick()) {
                if (event.getSlot() == 0) {
                    if (event.isShiftClick()) {
                        pitem.setDurability((short) (pitem.getDurability() + 10));
                    } else {
                        pitem.setDurability((short) (pitem.getDurability() + 1));
                    }
                } else if (event.getSlot() == 1) {
                    ItemMeta pmeta = pitem.getItemMeta();
                    pmeta.setUnbreakable(true);
                    pitem.setItemMeta(pmeta);
                } else if (event.getSlot() == 2) {
                    main.itemcreatorgui.equipmentSlotGUI(player, pitem, 1, Attribute.GENERIC_ARMOR, AttributeModifier.Operation.ADD_NUMBER);
                } else if (event.getSlot() == 3) {
                    main.itemcreatorgui.equipmentSlotGUI(player, pitem, 1, Attribute.GENERIC_ARMOR_TOUGHNESS, AttributeModifier.Operation.ADD_NUMBER);
                } else if (event.getSlot() == 4) {
                    main.itemcreatorgui.equipmentSlotGUI(player, pitem, 1, Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifier.Operation.ADD_NUMBER);
                } else if (event.getSlot() == 5) {
                    main.itemcreatorgui.equipmentSlotGUI(player, pitem, 1, Attribute.GENERIC_MAX_HEALTH, AttributeModifier.Operation.ADD_NUMBER);
                } else if (event.getSlot() == 6) {
                    main.itemcreatorgui.equipmentSlotGUI(player, pitem, 0.10, Attribute.GENERIC_MOVEMENT_SPEED, AttributeModifier.Operation.ADD_SCALAR);
                } else if (event.getSlot() == 7) {
                    main.itemcreatorgui.equipmentSlotGUI(player, pitem, 0.10, Attribute.GENERIC_ATTACK_SPEED, AttributeModifier.Operation.ADD_SCALAR);
                } else if (event.getSlot() == 8) {
                    main.itemcreatorgui.equipmentSlotGUI(player, pitem, 1, Attribute.GENERIC_KNOCKBACK_RESISTANCE, AttributeModifier.Operation.ADD_NUMBER);
                } else if (event.getSlot() == 11) {
                    ItemMeta pmeta = pitem.getItemMeta();
                    if (pmeta != null) {
                        pmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    }
                    pitem.setItemMeta(pmeta);
                } else if (event.getSlot() == 12) {
                    ItemMeta pmeta = pitem.getItemMeta();
                    if (pmeta != null) {
                        pmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }
                    pitem.setItemMeta(pmeta);
                } else if (event.getSlot() == 14) {
                    ItemMeta pmeta = pitem.getItemMeta();
                    if (pmeta != null) {
                        pmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                    }
                    pitem.setItemMeta(pmeta);
                } else if (event.getSlot() == 15) {
                    ItemMeta pmeta = pitem.getItemMeta();
                    if (pmeta != null) {
                        pmeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    }
                    pitem.setItemMeta(pmeta);
                }
            } else if (event.isRightClick()) {
                if (event.getSlot() == 0) {
                    if (event.isShiftClick()) {
                        pitem.setDurability((short) (pitem.getDurability() - 10));
                    } else {
                        pitem.setDurability((short) (pitem.getDurability() - 1));
                    }
                } else if (event.getSlot() == 1) {
                    ItemMeta pmeta = pitem.getItemMeta();
                    pmeta.setUnbreakable(false);
                    pitem.setItemMeta(pmeta);
                } else if (event.getSlot() == 2) {
                    main.itemcreatorgui.equipmentSlotGUI(player, pitem, -1, Attribute.GENERIC_ARMOR, AttributeModifier.Operation.ADD_NUMBER);
                } else if (event.getSlot() == 3) {
                    main.itemcreatorgui.equipmentSlotGUI(player, pitem, -1, Attribute.GENERIC_ARMOR_TOUGHNESS, AttributeModifier.Operation.ADD_NUMBER);
                } else if (event.getSlot() == 4) {
                    main.itemcreatorgui.equipmentSlotGUI(player, pitem, -1, Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifier.Operation.ADD_NUMBER);
                } else if (event.getSlot() == 5) {
                    main.itemcreatorgui.equipmentSlotGUI(player, pitem, -1, Attribute.GENERIC_MAX_HEALTH, AttributeModifier.Operation.ADD_NUMBER);
                } else if (event.getSlot() == 6) {
                    main.itemcreatorgui.equipmentSlotGUI(player, pitem, -0.10, Attribute.GENERIC_MOVEMENT_SPEED, AttributeModifier.Operation.ADD_SCALAR);
                } else if (event.getSlot() == 7) {
                    main.itemcreatorgui.equipmentSlotGUI(player, pitem, -0.10, Attribute.GENERIC_ATTACK_SPEED, AttributeModifier.Operation.ADD_SCALAR);
                } else if (event.getSlot() == 8) {
                    main.itemcreatorgui.equipmentSlotGUI(player, pitem, -1, Attribute.GENERIC_KNOCKBACK_RESISTANCE, AttributeModifier.Operation.ADD_NUMBER);
                } else if (event.getSlot() == 11) {
                    ItemMeta pmeta = pitem.getItemMeta();
                    if (pmeta != null) {
                        if (pmeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
                            pmeta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                            pitem.setItemMeta(pmeta);
                        }
                    }
                } else if (event.getSlot() == 12) {
                    ItemMeta pmeta = pitem.getItemMeta();
                    if (pmeta != null) {
                        if (pmeta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
                            pmeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                            pitem.setItemMeta(pmeta);
                        }
                    }
                } else if (event.getSlot() == 14) {
                    ItemMeta pmeta = pitem.getItemMeta();
                    if (pmeta != null) {
                        if (pmeta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE)) {
                            pmeta.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                            pitem.setItemMeta(pmeta);
                        }
                    }
                } else if (event.getSlot() == 15) {
                    ItemMeta pmeta = pitem.getItemMeta();
                    if (pmeta != null) {
                        if (pmeta.hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)) {
                            pmeta.removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                            pitem.setItemMeta(pmeta);
                        }
                    }
                }
            }
            if (event.getCurrentItem().equals(main.itemcreatorgui.createBackButton())) {
                main.itemcreatorgui.getGUI(player, pitem);
            }
        }
    }

    private void clickPotionEffectsGUI(InventoryClickEvent event, Player player) {
        ItemStack item = event.getCurrentItem();
        ItemStack pitem = event.getInventory().getItem(40);
        if (event.getSlot() >= 0 && event.getSlot() <= event.getInventory().getContents().length - 2) {
            boolean contains = false;
            if (item.hasItemMeta()) {
                ItemMeta itemmeta = item.getItemMeta();
                String enchant = itemmeta.getLore().get(0).replace(ChatColor.GOLD.toString(), ChatColor.GRAY.toString());
                List<String> lore = new ArrayList<>();
                ItemMeta pmeta = pitem.getItemMeta();
                if (pmeta != null) {
                    if (pmeta.hasLore()) {
                        for (String l : pmeta.getLore()) {
                            String enchantClear = enchant.replace(ChatColor.GRAY.toString(), "").trim();
                            String loreClear = l.replace(ChatColor.RED.toString(), "").replaceAll("([^A-z ])", "").trim();
                            if (enchantClear.equals(loreClear) && ((event.isShiftClick() && l.contains("-")) || (!event.isShiftClick() && !l.contains("-")))) {
                                int level = Integer.parseInt(l.replace(ChatColor.RED.toString(), "").replace(ChatColor.GRAY.toString(), "").replaceAll("([^0-9 ])", "").trim());
                                if (event.isLeftClick()) {
                                    if (event.isShiftClick()) {
                                        lore.add(ChatColor.RED + "- " + enchant + " " + (level + 1));
                                    } else {
                                        lore.add(enchant + " " + (level + 1));
                                    }
                                } else if (event.isRightClick()) {
                                    if (level - 1 > 0) {
                                        if (event.isShiftClick()) {
                                            lore.add(ChatColor.RED + "- " + enchant + " " + (level - 1));
                                        } else {
                                            lore.add(enchant + " " + (level - 1));
                                        }
                                    }
                                }
                                contains = true;
                            } else {
                                lore.add(l);
                            }
                        }
                    }
                }
                if (!contains) {
                    List<String> newlore = new ArrayList<>();
                    if (event.isLeftClick()) {
                        if (event.isShiftClick()) {
                            lore.add(ChatColor.RED + "- " + enchant + " " + 1);
                        } else {
                            lore.add(enchant + " " + 1);
                        }
                    }
                    for (String l : lore) {
                        newlore.add(l);
                    }
                    pmeta.setLore(newlore);
                } else {
                    pmeta.setLore(lore);
                }
                pitem.setItemMeta(pmeta);
            }
            main.itemcreatorgui.potionEffectsGUI(player, pitem);
        }
        if (event.getCurrentItem().equals(main.itemcreatorgui.createBackButton())) {
            main.itemcreatorgui.getGUI(player, pitem);
        }
    }

    private void clickGeneralGUI(InventoryClickEvent event, Player player) {
        ItemStack item = event.getCurrentItem();
        ItemStack pitem = event.getInventory().getItem(22);
        if (item.hasItemMeta()) {
            ItemMeta itemmeta = item.getItemMeta();
            if (itemmeta.getDisplayName().equals(ChatColor.DARK_PURPLE + "Status Effects")) {
                if (player.hasPermission("itemcreatorplus.status")) {
                    main.itemcreatorgui.potionEffectsGUI(player, pitem);
                } else {
                    player.sendMessage(main.prefix + ChatColor.RED + "You don't have permission to use that part of Item Creator Plus.");
                }
            } else if (itemmeta.getDisplayName().equals(ChatColor.BLUE + "Special Modifiers")) {
                if (player.hasPermission("itemcreatorplus.specmod")) {
                    main.itemcreatorgui.modifiersGUI(player, pitem);
                } else {
                    player.sendMessage(main.prefix + ChatColor.RED + "You don't have permission to use that part of Item Creator Plus.");
                }
            } else if (itemmeta.getDisplayName().equals(ChatColor.BLUE + "Item Lore")) {
                if (player.hasPermission("itemcreatorplus.lore")) {
                    main.itemcreatorgui.loreEditorGUI(player, pitem);
                } else {
                    player.sendMessage(main.prefix + ChatColor.RED + "You don't have permission to use that part of Item Creator Plus.");
                }
            } else if (itemmeta.getDisplayName().equals(ChatColor.GOLD + "Item Name")) {
                if (player.hasPermission("itemcreatorplus.name")) {
                    if (event.isLeftClick()) {
                        main.itemmain.catchChat.put(player, pitem);
                        main.itemmain.catchType.put(player, "name");
                        player.closeInventory();
                        player.sendMessage(main.prefix + ChatColor.GRAY + "Enter item name in chat. Type 'cancel' to cancel.");
                    } else if (event.isRightClick()) {
                        ItemMeta m = pitem.getItemMeta();
                        m.setDisplayName(null);
                        pitem.setItemMeta(m);
                    }
                } else {
                    player.sendMessage(main.prefix + ChatColor.RED + "You don't have permission to use that part of Item Creator Plus.");
                }
            } else if (itemmeta.getDisplayName().equals(ChatColor.LIGHT_PURPLE + "Item Enchantments")) {
                if (player.hasPermission("itemcreatorplus.enchant")) {
                    if (event.isLeftClick()) {
                        main.itemcreatorgui.enchantGUI(player, pitem);
                    } else if (event.isRightClick()) {
                        for (Enchantment enchant : pitem.getEnchantments().keySet()) {
                            pitem.removeEnchantment(enchant);
                        }
                    }
                } else {
                    player.sendMessage(main.prefix + ChatColor.RED + "You don't have permission to use that part of Item Creator Plus.");
                }
            } else if (event.getSlot() == 22) {
                if (player.hasPermission("itemcreatorplus.takeitem")) {
                    player.getInventory().setItemInMainHand(item);
                    player.closeInventory();
                }
            }
        }
        if (event.getSlot() == 26) {
            ItemStack citem = player.getOpenInventory().getTopInventory().getItem(22);
            if (player.hasPermission("itemcreatorplus.saveitem")) {
                if (main.itemlistlistener.currentlyEditing.containsKey(player)) {
                    event.setCancelled(true);

                    ItemStack oldItem = main.itemlistlistener.currentlyEditing.get(player);

                    main.itemlist.playerlist.remove(oldItem); //remove old item from player list
                    main.itemlist.itemslist.set(main.itemlist.itemslist.indexOf(oldItem), citem); //replace old item with new item
                    main.itemlistlistener.currentlyEditing.remove(player);

                    main.itemlist.playerlist.put(citem, player); //save item to player list
                    player.sendMessage(main.prefix + ChatColor.GREEN + "Item saved successfully");

                    player.closeInventory();
                } else if (main.itemlist.playerItemCount(player) < main.itemlist.maxItems || player.hasPermission("itemcreatorplus.nosavelimit") || main.itemlist.maxItems == -1) {
                    main.itemlist.itemslist.add(citem);
                    main.itemlist.playerlist.put(citem, player);
                    player.sendMessage(main.prefix + ChatColor.GREEN + "Item saved successfully");
                    event.setCancelled(true);
                } else {
                    player.sendMessage(main.prefix + ChatColor.RED + "You cannot save any more items!");
                }
            } else {
                player.sendMessage(main.prefix + ChatColor.RED + "You don't have permission to use that part of Item Creator Plus.");
            }
        }
        if (event.getCurrentItem().equals(main.itemcreatorgui.createBackButton())) {
            player.getInventory().setItemInMainHand(item);
            player.closeInventory();
        }
    }

    private void clickAttributesGUI(InventoryClickEvent event, Player player) {
        ItemStack pitem = event.getInventory().getItem(22);
        String slot = "";
        if (event.getRawSlot() >= 1 && event.getRawSlot() <= 8) {
            slot = event.getCurrentItem().getItemMeta().getDisplayName().replace(" Slot", "").replace(ChatColor.AQUA.toString(), "").toUpperCase();
            if (slot.equals("ALL")) {
                slot = null;
            }
        }
        if (event.getRawSlot() >= 1 && event.getRawSlot() <= 7) {
            setAttribute(Attribute.valueOf(event.getCurrentItem().getItemMeta().getLore().get(1).replace(ChatColor.GRAY.toString(), "")),
                    pitem, Double.parseDouble(event.getCurrentItem().getItemMeta().getLore().get(0).replace(ChatColor.GRAY + "Add ", "").replace(" to this slot", "")),
                    AttributeModifier.Operation.valueOf(event.getCurrentItem().getItemMeta().getLore().get(2).replace(ChatColor.GRAY.toString(), "")),
                    slot);
        } else if (event.getRawSlot() == 8) {
            setAttribute(Attribute.valueOf(event.getCurrentItem().getItemMeta().getLore().get(1).replace(ChatColor.GRAY.toString(), "")),
                    pitem, Double.parseDouble(event.getCurrentItem().getItemMeta().getLore().get(0).replace(ChatColor.GRAY + "Add ", "").replace(" to this slot", "")),
                    AttributeModifier.Operation.valueOf(event.getCurrentItem().getItemMeta().getLore().get(2).replace(ChatColor.GRAY.toString(), "")),
                    slot);
        } else if (event.getCurrentItem().equals(main.itemcreatorgui.createBackButton())) {
            main.itemcreatorgui.modifiersGUI(player, pitem);
        }
    }

    private void clickLoreGUI(InventoryClickEvent event, Player player) {
        ItemStack pitem = event.getInventory().getItem(event.getInventory().getSize() - 5);
        List<String> lore = pitem.getItemMeta().getLore();
        if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN.toString() + "Add a line of lore")) {
            main.itemmain.catchChat.put(player, pitem);
            main.itemmain.catchType.put(player, "lore");
            player.closeInventory();
            player.sendMessage(main.prefix + ChatColor.GRAY + "Enter a single line of lore in chat. Type 'cancel' to cancel.");
        } else if (event.isLeftClick() && !event.getCurrentItem().equals(main.itemcreatorgui.createBackButton())) {
            main.itemmain.catchChat.put(player, pitem);
            main.itemmain.catchType.put(player, "lore " + event.getSlot());
            player.closeInventory();
            player.sendMessage(main.prefix + ChatColor.GRAY + "Enter a single line of lore in chat. Type 'cancel' to cancel.");
        } else if (event.isRightClick()) {
            ItemMeta m = pitem.getItemMeta();
            lore.remove(event.getSlot());
            m.setLore(lore);
            pitem.setItemMeta(m);
            event.getInventory().setItem(event.getInventory().getSize() - 5, pitem);
            main.itemcreatorgui.loreEditorGUI(player, event.getInventory().getItem(event.getInventory().getSize() - 5));
        } else if (event.getCurrentItem().equals(main.itemcreatorgui.createBackButton())) {
            main.itemcreatorgui.getGUI(player, pitem);
        }
    }

    private void setAttribute(Attribute at, ItemStack pitem, double amount, AttributeModifier.Operation operation, String eqs) {
        EquipmentSlot es;
        String esname;
        if (eqs != null) {
            es = EquipmentSlot.valueOf(eqs);
            esname = es.name().toLowerCase() + " ";
        } else {
            for (EquipmentSlot e : EquipmentSlot.values()) {
                setAttribute(at, pitem, amount, operation, e.name());
            }
            return;
        }
        ItemMeta pmeta = pitem.getItemMeta();
        if (pmeta != null && pmeta.hasAttributeModifiers()) {
            Multimap<Attribute, AttributeModifier> ams = pmeta.getAttributeModifiers(es);
            for (AttributeModifier am : ams.values()) {
                if (am.getSlot() == es && am.getName().equals(at.name())) {
                    pmeta.removeAttributeModifier(at, am);
                    pmeta.addAttributeModifier(at, new AttributeModifier(UUID.randomUUID(), at.name(), am.getAmount() + amount, operation, es));
                    pmeta.setLore(removeLoreLine(pmeta.getLore(), WordUtils.capitalize(esname + at.name().replace("GENERIC_", "").toLowerCase().replace("_", " "))));
                    if (am.getAmount() + amount < -0.09 || am.getAmount() + amount > 0.09) {
                        pmeta.setLore(addLore(pmeta, ChatColor.BLUE + WordUtils.capitalize(esname + at.name().replace("GENERIC_", "").toLowerCase().replace("_", " ")) + " " + Math.round((am.getAmount() + amount) * 10) / 10.0, 0));
                    } else {
                        pmeta.removeAttributeModifier(at, am);
                    }
                    pitem.setItemMeta(pmeta);
                    return;
                }
            }
        }
        pmeta.addAttributeModifier(at, new AttributeModifier(UUID.randomUUID(), at.name(), amount, operation, es));
        pmeta.setLore(removeLoreLine(pmeta.getLore(), WordUtils.capitalize(esname + at.name().replace("GENERIC_", "").toLowerCase()).replace("_", " ")));
        pmeta.setLore(addLore(pmeta, ChatColor.BLUE + WordUtils.capitalize(esname + at.name().replace("GENERIC_", "").toLowerCase().replace("_", " ")) + " " + Math.round(amount * 10) / 10.0, 0));
        pitem.setItemMeta(pmeta);
    }

    private List<String> removeLoreLine(List<String> lore, String line) {
        if (lore != null) {
            Iterator<String> i = lore.iterator();
            String s;
            while (i.hasNext()) {
                s = i.next();
                if (s.contains(line)) {
                    i.remove();
                }
            }
        }
        return lore;
    }

    public List<String> addLore(ItemMeta meta, String line, int level) {
        List<String> newlore = new ArrayList<>();

        if (level == -1) {
            List<String> lore = meta.getLore();
            if (lore != null) {
                newlore.addAll(lore);
            }
            newlore.add(line);
            return newlore;
        }

        if (meta.hasLore()) {
            int index = 0;
            List<String> lore = meta.getLore();
            for (String str : lore) {
                if (index == level) {
                    newlore.add(line);
                }
                index++;
                newlore.add(str);
            }
        } else {
            newlore.add(line);
        }
        return newlore;
    }
}
