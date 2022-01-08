package me.joe.bundle_me.item_me.events;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.joe.bundle_me.item_me.ItemMePlugin;
import me.joe.bundle_me.item_me.events.custom_item.CancelledDiggingEvent;
import me.joe.bundle_me.item_me.events.custom_item.CompleteDiggingEvent;
import me.joe.bundle_me.item_me.events.custom_item.StartDiggingEvent;
import me.joe.bundle_me.item_me.events.custom_item.UseEvent;
import me.joe.bundle_me.item_me.items.CustomItem;
import me.joe.bundle_me.item_me.items.CustomItemManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class CustomItemEventListener implements Listener {

    private ItemMePlugin plugin;
    private CustomItemManager manager;
    private ProtocolManager protocolManager;

    public CustomItemEventListener(ItemMePlugin plugin, CustomItemManager manager, ProtocolManager protocolManager) {
        this.plugin = plugin;
        this.manager = manager;
        this.protocolManager = protocolManager;

        this.addBlockDigListener();
    }

    public void addBlockDigListener() {
        CustomItemEventListener listener = this;

        this.protocolManager.addPacketListener(new PacketAdapter(this.plugin, ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();

                CustomItem customItem = listener.getPlayerCustomItem(player);
                if (customItem == null) {
                    return;
                }

                Block block = player.getTargetBlock(null, 4);
                if (block.getType().getHardness() < 0) {
                    return;
                }

                EnumWrappers.PlayerDigType type = packet.getPlayerDigTypes().readSafely(0);
                if (type.equals(EnumWrappers.PlayerDigType.START_DESTROY_BLOCK)) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getPluginManager().callEvent(new StartDiggingEvent(customItem, player, block));
                        }
                    }.runTask(plugin);
                } else if (type.equals(EnumWrappers.PlayerDigType.STOP_DESTROY_BLOCK)) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getPluginManager().callEvent(new CompleteDiggingEvent(customItem, player, block));
                        }
                    }.runTask(plugin);
                } else if (type.equals(EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK)) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getPluginManager().callEvent(new CancelledDiggingEvent(customItem, player, block));
                        }
                    }.runTask(plugin);
                }
            }
        });
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        CustomItem customItem = this.getPlayerCustomItem(player);
        if (customItem == null) {
            return;
        }

        Action action = event.getAction();

        Bukkit.getPluginManager().callEvent(new UseEvent(customItem, player, action));
    }

    private CustomItem getPlayerCustomItem(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null) {
            return null;
        }

        return this.manager.getCustomItem(item);
    }
}
