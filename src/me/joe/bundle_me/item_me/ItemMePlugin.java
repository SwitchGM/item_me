package me.joe.bundle_me.item_me;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.joe.bundle_me.item_me.events.CustomItemEventListener;
import me.joe.bundle_me.item_me.items.CustomItemManager;
import me.joe.bundle_me.item_me.runnables.BlockDigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemMePlugin extends JavaPlugin {

    private CustomItemManager manager;
    private BlockDigManager blockDigManager;
    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        this.manager = new CustomItemManager(this);
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.blockDigManager = new BlockDigManager(this, this.protocolManager);

        this.saveDefaultConfig();

        this.getServer().getPluginManager().registerEvents(new ItemMeListener(this, this.manager, this.blockDigManager), this);
        this.getServer().getPluginManager().registerEvents(new CustomItemEventListener(this, this.manager,this.protocolManager), this);

        this.getCommand("itemme").setExecutor(new ItemMeCommands(this, this.manager));

        this.manager.loadItems();
    }
}
