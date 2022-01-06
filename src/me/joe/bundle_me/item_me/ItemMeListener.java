package me.joe.bundle_me.item_me;

import me.joe.bundle_me.item_me.items.CustomItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class ItemMeListener implements Listener {

    private ItemMePlugin plugin;
    private CustomItemManager manager;

    public ItemMeListener(ItemMePlugin plugin, CustomItemManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.manager.discoverItemCraftingRecipesForPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.manager.undiscoverItemCraftingRecipesForPlayer(event.getPlayer());
    }


}
