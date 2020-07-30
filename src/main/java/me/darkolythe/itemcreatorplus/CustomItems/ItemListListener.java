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

public class ItemListListener implements Listener {

    public ItemCreatorPlus main;
    public ItemListListener(ItemCreatorPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            Inventory inv = player.getOpenInventory().getTopInventory();
            if (player.hasPermission("itemcreatorplus.giveitem")) {
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
                            if (event.isLeftClick() && !event.isShiftClick()) {
                                main.maintools.giveItem(player, main.itemlist.itemslist.get(trueslot));
                                event.setCancelled(true);
                            } else if (event.isRightClick() && !event.isShiftClick()) {
                                main.itemlist.playerlist.remove(main.itemlist.itemslist.get(trueslot));
                                main.itemlist.itemslist.remove(trueslot);
                                event.setCancelled(true);
                                main.itemlist.openItemList(player, (byte) 0);
                            } else if (event.isShiftClick()) {
                                event.setCancelled(true);
                                main.itemcreatorgui.getGUI(player, main.itemlist.itemslist.get(trueslot));
                            }
                        }
                    }
                    event.setCancelled(true);
                }
            }
        }
    }
}
