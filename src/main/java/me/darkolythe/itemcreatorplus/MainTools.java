package me.darkolythe.itemcreatorplus;

import me.darkolythe.itemcreatorplus.CustomItems.*;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.stream.Stream;

public class MainTools {

    public ItemCreatorPlus main;
    public MainTools(ItemCreatorPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public void setUp() {

        main.itemcreatorgui = new ItemCreatorGUI(main);
        main.itemcreatorlistener = new ItemCreatorListener(main);
        main.itemeffectlistener = new ItemEffectListener(main);
        main.itemmain = new ItemMain(main);

        getConfigData();

        main.getCommand("itemcreatorplus").setExecutor(new CommandHandler());
        final PluginManager pluginManager = Bukkit.getPluginManager();
        Stream.of(main.itemcreatorlistener,
                main.itemeffectlistener).forEach(it -> pluginManager.registerEvents(it, main));

        main.itemmain.addText();
    }

    public void getConfigData() {
        main.saveDefaultConfig();
        main.itemlist.maxItems = main.getConfig().getInt("saveitemlimit");
        main.itemmain.combinelevel = main.getConfig().getInt("combinelevel");
        main.saveConfig();
    }

    public boolean giveItem(Player player, ItemStack item) {
        if (player.getInventory().addItem(item).keySet().size() != 0) {
            return false;
        }
        return true;
    }

    public ArrayList<Enchantment> getTrueEnchantments() {
        ArrayList<Enchantment> enchants = new ArrayList<>();
        for (Enchantment enchant : Enchantment.values()) {
            if (enchant.getClass().getPackage().getName().startsWith("org.bukkit")) {
                enchants.add(enchant);
            }
        }
        return enchants;
    }
}
