package me.joe.bundle_me.item_me;

import me.joe.bundle_me.item_me.events.CustomItemEventListener;
import me.joe.bundle_me.item_me.items.CustomItemManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemMePlugin extends JavaPlugin {

    private CustomItemManager manager;

    @Override
    public void onEnable() {
        this.manager = new CustomItemManager(this);

        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new CustomItemEventListener(this, this.manager), this);
        this.getCommand("itemme").setExecutor(new ItemMeCommands(this));
    }
}
