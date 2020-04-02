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
        ItemStack pitem;
        boolean isproperitem = true;
        if (item != null) {
            if (event.getInventory().getType() == InventoryType.CHEST) {
                if (event.getClickedInventory() != player.getInventory()) {
                    if (title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Enchantment GUI")) {
                        event.setCancelled(true);
                        pitem = event.getInventory().getItem((((Enchantment.values().length / 9) + 2) * 9) - 5);
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
                        if (event.getSlot() == (((Enchantment.values().length / 9) + 2) * 9) - 1) {
                            main.itemcreatorgui.getGUI(player, pitem);
                        }
                    } else if (title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Special Modifiers GUI")) {
                        pitem = event.getInventory().getItem(22);
                        event.setCancelled(true);
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
                            if (event.getSlot() == 26) {
                                main.itemcreatorgui.getGUI(player, pitem);
                            }
                        }
                    } else if (title.equals(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Choose equipment slot")) {
                        event.setCancelled(true);
                        pitem = event.getInventory().getItem(22);
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
                        } else if (event.getSlot() == 26) {
                            main.itemcreatorgui.modifiersGUI(player, pitem);
                        }
                    } else if (title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Potion Effects GUI")) {
                        event.setCancelled(true);
                        pitem = event.getInventory().getItem(40);
                        if (event.getSlot() >= 0 && event.getSlot() <= event.getInventory().getContents().length - 2) {
                            boolean contains = false;
                            if (item.hasItemMeta()) {
                                ItemMeta itemmeta = item.getItemMeta();
                                String enchant = itemmeta.getLore().get(0).replace(ChatColor.GOLD.toString(), ChatColor.GRAY.toString());
                                System.out.println(enchant);
                                List<String> lore = new ArrayList<>();
                                ItemMeta pmeta = pitem.getItemMeta();
                                if (pmeta != null) {
                                    if (pmeta.hasLore()) {
                                        for (String l : pmeta.getLore()) {
                                            if (l.contains(enchant)) {
                                                int level = Integer.parseInt(l.replace(enchant + " ", ""));
                                                if (event.isLeftClick()) {
                                                    lore.add(enchant + " " + (level + 1));
                                                } else if (event.isRightClick()) {
                                                    if (level - 1 > 0) {
                                                        lore.add(enchant + " " + (level - 1));
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
                                        newlore.add(enchant + " " + 1);
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
                        if (event.getSlot() == 44) {
                            main.itemcreatorgui.getGUI(player, pitem);
                        }
                    } else if (title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Item Creator GUI")) {
                        event.setCancelled(true);
                        if (item.hasItemMeta()) {
                            ItemMeta itemmeta = item.getItemMeta();
                            if (itemmeta.getDisplayName().equals(ChatColor.DARK_PURPLE + "Status Effects")) {
                                main.itemcreatorgui.potionEffectsGUI(player, event.getInventory().getItem(22));
                            } else if (itemmeta.getDisplayName().equals(ChatColor.BLUE + "Special Modifiers")) {
                                main.itemcreatorgui.modifiersGUI(player, event.getInventory().getItem(22));
                            } else if (itemmeta.getDisplayName().equals(ChatColor.BLUE + "Item Lore")) {
                                if (event.isLeftClick()) {
                                    main.itemmain.catchChat.put(player, event.getInventory().getItem(22));
                                    main.itemmain.catchType.put(player, "lore");
                                    player.closeInventory();
                                    player.sendMessage(main.prefix + ChatColor.GRAY + "Enter a single line of lore in chat. Type 'cancel' to cancel.");
                                } else if (event.isRightClick()) {
                                    ItemStack i = event.getInventory().getItem(22);
                                    ItemMeta m = i.getItemMeta();
                                    m.setLore(null);
                                    i.setItemMeta(m);
                                }
                            } else if (itemmeta.getDisplayName().equals(ChatColor.GOLD + "Item Name")) {
                                if (event.isLeftClick()) {
                                    main.itemmain.catchChat.put(player, event.getInventory().getItem(22));
                                    main.itemmain.catchType.put(player, "name");
                                    player.closeInventory();
                                    player.sendMessage(main.prefix + ChatColor.GRAY + "Enter item name in chat. Type 'cancel' to cancel.");
                                } else if (event.isRightClick()) {
                                    ItemStack i = event.getInventory().getItem(22);
                                    ItemMeta m = i.getItemMeta();
                                    m.setDisplayName(null);
                                    i.setItemMeta(m);
                                }
                            } else if (itemmeta.getDisplayName().equals(ChatColor.LIGHT_PURPLE + "Item Enchantments")) {
                                if (event.isLeftClick()) {
                                    main.itemcreatorgui.enchantGUI(player, event.getInventory().getItem(22));
                                } else if (event.isRightClick()) {
                                    ItemStack i = event.getInventory().getItem(22);
                                    for (Enchantment enchant : i.getEnchantments().keySet()) {
                                        i.removeEnchantment(enchant);
                                    }
                                }
                            } else if (event.getSlot() == 22) {
                                player.getInventory().setItemInMainHand(item);
                                player.closeInventory();
                            }
                        }
                        if (event.getSlot() == 26) {
                            main.itemlist.itemslist.add(player.getOpenInventory().getTopInventory().getItem(22));
                            player.sendMessage(main.prefix + ChatColor.GREEN + "Item saved successfully");
                            event.setCancelled(true);
                        }
                        if (event.getSlot() == 22) {
                            player.getInventory().setItemInMainHand(item);
                            player.closeInventory();
                        }
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
                        title.equals(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Choose equipment slot") ||
                        title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Potion Effects GUI") ||
                        title.equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Item Creator GUI")) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (main.itemmain.catchChat.containsKey(player)) {
            if (!main.itemmain.catchType.get(player).equals("search")) {
                event.setCancelled(true);
                ItemStack item = main.itemmain.catchChat.get(player).clone();
                if (!event.getMessage().equals("cancel")) {
                    ItemMeta meta = item.getItemMeta();
                    if (main.itemmain.catchType.get(player).equals("name")) {
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
                    } else if (main.itemmain.catchType.get(player).equals("lore")) {
                        meta.setLore(addLore(meta, ChatColor.translateAlternateColorCodes('&', event.getMessage()), false));
                    }
                    item.setItemMeta(meta);
                }
                main.itemmain.toOpen.put(player, ChatColor.translateAlternateColorCodes('&', event.getMessage()));
                main.itemmain.catchChat.put(player, item);
            } else {
                return;
            }
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
                        pmeta.setLore(addLore(pmeta, ChatColor.BLUE + WordUtils.capitalize(esname + at.name().replace("GENERIC_", "").toLowerCase().replace("_", " ")) + " " + Math.round((am.getAmount() + amount) * 10) / 10.0, true));
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
        pmeta.setLore(addLore(pmeta, ChatColor.BLUE + WordUtils.capitalize(esname + at.name().replace("GENERIC_", "").toLowerCase().replace("_", " ")) + " " + Math.round(amount * 10) / 10.0, true));
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

    public List<String> addLore(ItemMeta meta, String line, boolean top) {
        List<String> newlore = new ArrayList<>();
        if (!top) {
            if (meta.hasLore()) {
                newlore = meta.getLore();
            } else {
                newlore = new ArrayList<>();
            }
            newlore.add(line);
        } else {
            if (meta.hasLore()) {
                List<String> lore = meta.getLore();
                newlore.add(line);
                for (String str : lore) {
                    newlore.add(str);
                }
            } else {
                newlore.add(line);
            }
        }
        return newlore;
    }
}
