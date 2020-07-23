package me.joe.bundle_me.item_me;

import org.bukkit.plugin.java.JavaPlugin;

public class ItemMePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

//        this.getServer().getPluginManager().registerEvents(new ItemMeListener(), this);

        this.getCommand("itemme").setExecutor(new ItemMeCommands(this));
    }
}
