package me.darkolythe.itemcreatorplus;

import me.darkolythe.itemcreatorplus.CustomItems.*;
import org.bstats.bukkit.Metrics;
import org.bukkit.*;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public final class ItemCreatorPlus extends JavaPlugin {

    public static ItemCreatorPlus plugin;
    public ItemCreatorGUI itemcreatorgui;
    public ItemCreatorListener itemcreatorlistener;
    public ItemEffectListener itemeffectlistener;
    public ItemMain itemmain;
    public ItemList itemlist;
    public ItemListListener itemlistlistener;

    public MainTools maintools;

    public String prefix = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.BLUE.toString() + "ICP" + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "] ";
    public boolean fullchecking;
    public boolean stackingeffects;

    @Override
    public void onEnable() {
        plugin = this;

        ConfigurationSerialization.registerClass(CustomItem.class);

        maintools = new MainTools(plugin);
        itemlist = new ItemList(plugin);
        itemlistlistener = new ItemListListener(plugin);

        getServer().getPluginManager().registerEvents(itemlistlistener, this);

        maintools.setUp();
        itemlist.setUp();

        itemlist.importItemList();

        fullchecking = getConfig().getBoolean("fullchecking");
        stackingeffects = getConfig().getBoolean("stackingeffects");

        Metrics metrics = new Metrics(plugin);

        System.out.println(prefix + "ItemCreatorPlus Enabled!");
    }

    @Override
    public void onDisable() {
        System.out.println(prefix + "ItemCreatorPlus Disabled!");
        itemlist.saveItemList();
    }

    public static ItemCreatorPlus getInstance() {
        return plugin;
    }
}
