package me.joe.bundle_me.item_me.events;

import me.joe.bundle_me.item_me.ItemMePlugin;
import me.joe.bundle_me.item_me.items.CustomItem;
import me.joe.bundle_me.item_me.items.CustomItemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CustomItemEventListener implements Listener {

    private ItemMePlugin plugin;
    private CustomItemManager manager;

    public CustomItemEventListener(ItemMePlugin plugin, CustomItemManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        CustomItem customItem = this.manager.getCustomItem(item);
        if (customItem == null) {
            return;
        }

        Player player = event.getPlayer();
        Action action = event.getAction();

        Bukkit.getPluginManager().callEvent(new CustomItemUseEvent(customItem, player, action));
    }
}
