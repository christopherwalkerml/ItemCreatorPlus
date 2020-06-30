package me.darkolythe.itemcreatorplus.CustomItems;

import me.darkolythe.itemcreatorplus.ItemCreatorPlus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class ItemMain {

    public ItemCreatorPlus main;
    public ItemMain(ItemCreatorPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public int combinelevel;

    public Map<Player, ItemStack> catchChat = new HashMap<>();
    public Map<Player, String> catchType = new HashMap<>();

    public Map<Player, List<PotionEffectType>> armourEffects = new HashMap<>();
    public Map<Player, Boolean> isFlight = new HashMap<>();
    public Map<Player, String> toOpen = new HashMap<>();

    public List<PotionEffectType> getEffects(Player player) {
        if (armourEffects.containsKey(player)) {
            return armourEffects.get(player);
        } else {
            armourEffects.put(player, new ArrayList<>());
            return armourEffects.get(player);
        }
    }

    public boolean getFlight(Player player) {
        if (isFlight.containsKey(player)) {
            return isFlight.get(player);
        } else {
            isFlight.put(player, false);
            return isFlight.get(player);
        }
    }

    public void addText() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {
                for (Player player : toOpen.keySet()) {
                    if (toOpen.get(player) != null) {
                        ItemStack item = catchChat.get(player).clone();
                        main.itemcreatorgui.getGUI(player, item);
                        catchChat.remove(player);
                        if (toOpen.get(player).equals("lore")) {
                            toOpen.put(player, null);
                            main.itemcreatorgui.loreEditorGUI(player, item);
                            return;
                        }
                        toOpen.put(player, null);
                        main.itemcreatorgui.getGUI(player, item);
                    }
                }
            }
        }, 1L, 5L);
    }
}
