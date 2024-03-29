package me.darkolythe.itemcreatorplus.CustomItems;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CustomItem implements ConfigurationSerializable {

    public ItemStack item;
    public List<String> creator = new ArrayList<>();

    public CustomItem(ItemStack newItem, Player newCreator) {
        this.item = newItem;
        this.creator.add(newCreator.getName());
        this.creator.add(newCreator.getUniqueId().toString());
    }

    public CustomItem(ItemStack newItem, List<String> newCreator) {
        this.item = newItem;
        this.creator = newCreator;

        if (newCreator.size() == 0) {
            newCreator.add(null);
            newCreator.add(null);
        }
    }

    public String getCreatorName() {
        return creator.get(0);
    }

    public UUID getCreatorUUID() {
        return (creator.get(1) != null ? UUID.fromString(creator.get(1)) : null);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("item", item);
        result.put("creator", creator);

        return result;
    }

    public static CustomItem deserialize(Map<String, Object> args) {
        ItemStack i = (ItemStack) args.get("item");
        List<String> plr = new ArrayList<>();
        List<?> plrdata = (List<?>) args.get("creator");
        for (Object o : plrdata) {
            plr.add((String) o);
        }

        CustomItem ci = new CustomItem(i, plr);

        return ci;
    }
}
