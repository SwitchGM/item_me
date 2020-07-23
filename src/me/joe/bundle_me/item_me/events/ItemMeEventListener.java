package me.joe.bundle_me.item_me.events;

import me.joe.bundle_me.item_me.ItemMePlugin;
import me.joe.bundle_me.item_me.items.CustomItemManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemMeEventListener implements Listener {

    private ItemMePlugin plugin;
    private CustomItemManager manager;

    public ItemMeEventListener(ItemMePlugin plugin, CustomItemManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        if (this.manager.isCustomItem(item)) {
            return;
        }
    }
}
