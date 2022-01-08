package me.joe.bundle_me.item_me.runnables;

import com.comphenix.protocol.ProtocolManager;
import me.joe.bundle_me.item_me.ItemMePlugin;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BlockDigManager {

    private ItemMePlugin plugin;
    private ProtocolManager protocolManager;

    private Map<Player, BlockBreakingRunnable> blockBreakingRunnableMap = new HashMap<>();

    public BlockDigManager(ItemMePlugin plugin, ProtocolManager protocolManager) {
        this.plugin = plugin;
        this.protocolManager = protocolManager;
    }

    public void startBlockBreaking(Player player, Block block, float time) {
        int ticks = Math.round(time * 20 / 9);

        BlockBreakingRunnable runnable = new BlockBreakingRunnable(this.protocolManager, player, block);

        this.blockBreakingRunnableMap.put(player, runnable);

        runnable.runTaskTimer(this.plugin, 0, ticks);
    }

    public void stopBlockBreaking(Player player) {
        BlockBreakingRunnable runnable = this.blockBreakingRunnableMap.get(player);
        runnable.resetBlockAnimation();
        runnable.cancel();
        this.blockBreakingRunnableMap.remove(player);
    }
}
