package me.joe.bundle_me.item_me.events;

import me.joe.bundle_me.item_me.items.CustomItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

public class CustomItemUseEvent extends Event implements Listener {

    private static final HandlerList HANDLERS = new HandlerList();

    private CustomItem item;
    private Player player;
    private Action action;

    public CustomItemUseEvent(CustomItem item, Player player, Action action) {
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
        return item;
    }

    public Player getPlayer() {
        return player;
    }

    public Action getAction() {
        return action;
    }
}