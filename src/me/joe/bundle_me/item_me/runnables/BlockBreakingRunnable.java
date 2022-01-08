package me.joe.bundle_me.item_me.runnables;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class BlockBreakingRunnable extends BukkitRunnable {

    private ProtocolManager protocolManager;
    private Player player;
    private Block block;

    private int id = new Random().nextInt();
    private int animationFrame = -1;

    public BlockBreakingRunnable(ProtocolManager protocolManager, Player player, Block block) {
        this.protocolManager = protocolManager;
        this.player = player;
        this.block = block;
    }

    @Override
    public void run() {
        if (this.animationFrame > 8) {
            this.breakBlock();
            this.cancel();
        } else {
            this.animationFrame++;
            this.sendAnimationChangePacket(this.animationFrame);
        }
    }

    public void resetBlockAnimation() {
        this.sendAnimationChangePacket(-1);
    }

    private void breakBlock() {
        try {
            Material material = this.block.getType();
            Location location = this.block.getLocation().add(0.5, 0.5, 0.5);
            ItemStack drop = this.block.getDrops().iterator().next();

            this.block.setType(Material.AIR);
            this.player.getWorld().playEffect(location, Effect.STEP_SOUND, material);
            this.player.getWorld().dropItem(location, drop);
            this.player.getWorld().playSound(location, Sound.valueOf(this.materialTypeToBreakSound(material)), 1F, 1F);
        } catch (IllegalArgumentException e) {
        }

        this.resetBlockAnimation();
    }

    private void sendAnimationChangePacket(int frame) {
        try {
            PacketContainer packet = this.protocolManager.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
            packet.getIntegers().write(0, this.id);

            Location loc = this.block.getLocation();
            BlockPosition position = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            packet.getBlockPositionModifier().write(0, position);

            packet.getIntegers().write(1, frame);
            this.protocolManager.broadcastServerPacket(packet);
        } catch (NullPointerException e) {
        }
    }

    private String materialTypeToBreakSound(Material material) {
        String rawMaterial = material.name().replace("_BLOCK", "");
        return "BLOCK_" + rawMaterial + "_BREAK";
    }
}
