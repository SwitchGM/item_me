package me.joe.bundle_me.item_me.events.custom_item;

import me.joe.bundle_me.item_me.items.CustomItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

public class UseEvent extends Event implements Listener {

    private static final HandlerList HANDLERS = new HandlerList();

    private CustomItem item;
    private Player player;
    private Action action;

    public UseEvent(CustomItem item, Player player, Action action) {
        this.item = item;
        this.player = player;
        this.action = action;
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

    public Action getAction() {
        return this.action;
    }
}