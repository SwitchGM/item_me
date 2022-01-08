package me.joe.bundle_me.item_me;

import me.joe.bundle_me.item_me.events.custom_item.CancelledDiggingEvent;
import me.joe.bundle_me.item_me.events.custom_item.CompleteDiggingEvent;
import me.joe.bundle_me.item_me.events.custom_item.StartDiggingEvent;
import me.joe.bundle_me.item_me.items.CustomItemManager;
import me.joe.bundle_me.item_me.runnables.BlockDigManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class ItemMeListener implements Listener {

    private ItemMePlugin plugin;
    private CustomItemManager manager;
    private BlockDigManager blockDigManager;

    public ItemMeListener(ItemMePlugin plugin, CustomItemManager manager, BlockDigManager blockDigManager) {
        this.plugin = plugin;
        this.manager = manager;
        this.blockDigManager = blockDigManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.manager.discoverItemCraftingRecipesForPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.manager.undiscoverItemCraftingRecipesForPlayer(event.getPlayer());
    }

    @EventHandler
    public void onStartDigging(StartDiggingEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        int time = event.getItem().getBlockBreakTime() / 1000; // convert from milliseconds to seconds

        this.blockDigManager.startBlockBreaking(player, block, time);
    }

    @EventHandler
    public void onCompleteDigging(CompleteDiggingEvent event) {
        Player player = event.getPlayer();

        this.blockDigManager.stopBlockBreaking(player);
    }

    @EventHandler
    public void onCancelledDigging(CancelledDiggingEvent event) {
        Player player = event.getPlayer();

        this.blockDigManager.stopBlockBreaking(player);
    }
}
