package me.darkolythe.itemcreatorplus.CustomItems;

import me.darkolythe.itemcreatorplus.ItemCreatorPlus;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemPlaceListener implements Listener {

    public ItemCreatorPlus main;
    public ItemPlaceListener(ItemCreatorPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        ItemStack i = event.getItemInHand();
        ItemMeta m = i.getItemMeta();

        if (m != null && m.hasLore()) {
            for (String s : m.getLore()) {
                String noplace = ChatColor.translateAlternateColorCodes('&', (String) main.getConfig().get("noplace"));
                if (s.equals(noplace)) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(main.prefix + ChatColor.RED + "You cannot place this item.");
                }
            }
        }
    }
}
