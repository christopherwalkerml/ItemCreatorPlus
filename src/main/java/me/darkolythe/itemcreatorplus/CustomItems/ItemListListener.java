package me.darkolythe.itemcreatorplus.CustomItems;

import me.darkolythe.itemcreatorplus.ItemCreatorPlus;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemListListener implements Listener {

    public ItemCreatorPlus main;
    public ItemListListener(ItemCreatorPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public Map<Player, CustomItem> currentlyEditing = new HashMap<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            Inventory inv = player.getOpenInventory().getTopInventory();
            if (player.getOpenInventory().getTitle().equals(org.bukkit.ChatColor.LIGHT_PURPLE.toString() + org.bukkit.ChatColor.BOLD.toString() + "Saved Items List")) {
                if (event.getClickedInventory() != player.getInventory()) {
                    if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                        int page = 0;
                        if (inv.getItem(47) != null && inv.getItem(47).getType() != Material.AIR) {
                            page = Integer.parseInt(inv.getItem(47).getItemMeta().getDisplayName().replace(ChatColor.GRAY.toString(), "").replaceAll("[^0-9]", ""));
                        }
                        if (event.getRawSlot() == 47) {
                            main.itemlist.openItemList(player, (byte) (page - 1));
                            event.setCancelled(true);
                            return;
                        } else if (event.getRawSlot() == 51) {
                            main.itemlist.openItemList(player, (byte) (page + 1));
                            event.setCancelled(true);
                            return;
                        }
                        if (event.getClick() == ClickType.DOUBLE_CLICK) {
                            event.setCancelled(true);
                            return;
                        }
                        int trueslot = ((page * 45) + event.getRawSlot());
                        CustomItem item = main.itemlist.itemslist.get(trueslot);
                        ItemStack invItem = event.getCurrentItem();
                        if (event.isLeftClick() && !event.isShiftClick()) { //GIVE ITEM
                            if (player.hasPermission("itemcreatorplus.giveitem")) {
                                main.maintools.giveItem(player, item.item, 1);
                                event.setCancelled(true);
                            } else {
                                player.sendMessage(main.prefix + ChatColor.RED + "You do not have permission to do that");
                            }
                        } else if (event.isRightClick() && !event.isShiftClick()) { //DELETE ITEM
                            if (isCreatedByUser(invItem, player)) {
                                main.itemlist.itemslist.remove(trueslot);
                                main.itemlist.saveItemList();
                                event.setCancelled(true);
                                main.itemlist.openItemList(player, (byte) 0);
                            } else {
                                player.sendMessage(main.prefix + ChatColor.RED + "You cannot delete an item you did not create");
                            }
                        } else if (event.isShiftClick()) { //EDIT ITEM
                            if (isCreatedByUser(invItem, player)) {
                                currentlyEditing.put(player, item);
                                event.setCancelled(true);
                                main.itemcreatorgui.getGUI(player, item.item);
                            } else {
                                player.sendMessage(main.prefix + ChatColor.RED + "You cannot edit an item you did not create");
                            }
                        }
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    private boolean isCreatedByUser(ItemStack item, Player player) {
        for (String lore : item.getItemMeta().getLore()) {
            if (lore.replace("Created by: ", "").contains(player.getName())
                    || player.hasPermission("itemcreatorplus.editoverride")) {
                return true;
            }
        }
        return false;
    }
}
