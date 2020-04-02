package me.darkolythe.itemcreatorplus.CustomItems;

import me.darkolythe.itemcreatorplus.ItemCreatorPlus;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class ItemEffectListener implements Listener {

    public ItemCreatorPlus main;
    public ItemEffectListener(ItemCreatorPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        checkForUpdateDelayed(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if (!event.isCancelled()) {
            if (event.getClickedInventory() != null) {
                if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
                    checkForUpdateDelayed(player);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPickupItem(EntityPickupItemEvent event) {
        if (!event.isCancelled()) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                    Map<Integer, ItemStack> slots = new HashMap<>();
                    addArmour(slots, player);
                    if (player.getInventory().getHeldItemSlot() == 0) {
                        slots.put(0, event.getItem().getItemStack());
                        updatePlayerEffects(player, slots);
                    } else if (player.getInventory().getItem(player.getInventory().getHeldItemSlot() - 1) != null) {
                        if (player.getInventory().getItem(player.getInventory().getHeldItemSlot() - 1).getType() != Material.AIR) {
                            slots.put(0, event.getItem().getItemStack());
                            updatePlayerEffects(player, slots);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemBreak(PlayerItemBreakEvent event) {
        Map<Integer, ItemStack> slots = new HashMap<>();
        addArmour(slots, event.getPlayer());
        slots.put(0, event.getPlayer().getInventory().getItemInMainHand());
        slots.remove(event.getBrokenItem());
        updatePlayerEffects(event.getPlayer(), slots);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        if (!event.isCancelled()) {
            Map<Integer, ItemStack> slots = new HashMap<>();
            addArmour(slots, event.getPlayer());
            slots.put(0, event.getPlayer().getInventory().getItemInMainHand());
            slots.remove(event.getItemDrop());
            updatePlayerEffects(event.getPlayer(), slots);
        }
    }

    @EventHandler
    public void onItemHeldEvent(PlayerItemHeldEvent event) {
        Map<Integer, ItemStack> slots = new HashMap<>();
        addArmour(slots, event.getPlayer());
        slots.put(0, event.getPlayer().getInventory().getItem(event.getNewSlot()));
        updatePlayerEffects(event.getPlayer(), slots);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            checkForUpdateDelayed(event.getPlayer());
        }
    }

    @EventHandler
    public void onXpGain(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();
        Map<Integer, ItemStack> slots = new HashMap<>();
        addArmour(slots, event.getPlayer());
        slots.put(0, event.getPlayer().getInventory().getItemInMainHand());

        for (int index: slots.keySet()) {
            ItemStack item = slots.get(index);
            if (item != null) {
                if (item.hasItemMeta()) {
                    if (item.getItemMeta().hasLore()) {
                        List<String> lore = item.getItemMeta().getLore();
                        for (String l : lore) {
                            if (l.contains("XP Boost")) {
                                int level = Integer.parseInt(l.replace(ChatColor.GRAY.toString() + "XP Boost ", ""));
                                player.giveExp((int)(event.getAmount() * (0.3 * level)));
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onArmourDispense(BlockDispenseArmorEvent event) {
        if (event.getTargetEntity() instanceof Player) {
            checkForUpdateDelayed((Player) event.getTargetEntity());
        }
    }

    @EventHandler
    public void onSwapHands(PlayerSwapHandItemsEvent event) {
        checkForUpdateDelayed(event.getPlayer());
    }

    private void addArmour(Map<Integer, ItemStack> slots, Player player) {
        for (int i = 36; i < 41; i++) {
            slots.put(i, player.getInventory().getItem(i));
        }
    }

    private void updatePlayerEffects(Player player, Map<Integer, ItemStack> items) {
        List<PotionEffectType> playerEffects = main.itemmain.getEffects(player);
        for (PotionEffect pe: player.getActivePotionEffects()) {
            if (pe.getDuration() < 1000000000 && pe.getDuration() > 999900000) {
                player.removePotionEffect(pe.getType());
            }
            Iterator<PotionEffectType> pet = playerEffects.iterator();
            while (pet.hasNext()) {
                PotionEffectType PET = pet.next();
                if (PET == pe.getType()) {
                    pet.remove();
                    player.removePotionEffect(pe.getType());
                }
            }
        }
        boolean isFlying = player.isFlying();
        if (player.getGameMode() != GameMode.CREATIVE && main.itemmain.getFlight(player)) {
            player.setFlying(false);
            player.setAllowFlight(false);
            main.itemmain.isFlight.put(player, false);
        }
        List<PotionEffect> effects = new ArrayList<>();
        for (int index: items.keySet()) {
            ItemStack item = items.get(index);
            if (item != null) {
                if (item.hasItemMeta()) {
                    if (item.getItemMeta().hasLore()) {
                        List<String> lore = item.getItemMeta().getLore();
                        for (String l : lore) {
                            if (l.contains("Flight")) {
                                player.setAllowFlight(true);
                                player.setFlying(isFlying);
                                main.itemmain.isFlight.put(player, true);
                            }
                            for (PotionEffectType effect: PotionEffectType.values()) {
                                if (l.contains(ChatColor.GRAY.toString() + WordUtils.capitalize(effect.getName().toLowerCase().replace("_", " ")))) {
                                    if (l.replace(ChatColor.GRAY + WordUtils.capitalize(effect.getName().toLowerCase().replace("_", " ")) + " ", "").replaceAll("\\d", "").equals("")) {
                                        int level = Integer.parseInt(l.replace(ChatColor.GRAY + WordUtils.capitalize(effect.getName().toLowerCase().replace("_", " ")) + " ", ""));
                                        int pl = 0;
                                        for (int j = 0; j < effects.size(); j++) {
                                            if (effects.get(j).getType() == effect) {
                                                effects.set(j, new PotionEffect(effect, 1000000000, effects.get(j).getAmplifier() + 1, false, false, false));
                                            }
                                        }
                                        PotionEffect pe = new PotionEffect(effect, 1000000000, level - 1 + pl, false, false, false);
                                        effects.add(pe);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (PotionEffect eff: effects) {
            player.addPotionEffect(eff);
            playerEffects.add(eff.getType());
        }
        main.itemmain.armourEffects.put(player, playerEffects);
    }

    private void checkForUpdateDelayed(Player player) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {
                Map<Integer, ItemStack> slots = new HashMap<>();
                addArmour(slots, player);
                slots.put(0, player.getInventory().getItemInMainHand());
                updatePlayerEffects(player, slots);
            }
        }, 1);
    }
}
