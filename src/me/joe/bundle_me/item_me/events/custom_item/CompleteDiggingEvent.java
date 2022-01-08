package me.joe.bundle_me.item_me.events.custom_item;

import me.joe.bundle_me.item_me.items.CustomItem;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class CompleteDiggingEvent extends Event implements Listener {
    private static final HandlerList HANDLERS = new HandlerList();

    private CustomItem item;
    private Player player;
    private Block block;

    public CompleteDiggingEvent(CustomItem item, Player player, Block block) {
        this.item = item;
        this.player = player;
        this.block = block;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public CustomItem getItem() {
        return this.item;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Block getBlock() {
        return this.block;
    }
}
