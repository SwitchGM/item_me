package me.joe.bundle_me.item_me;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemMeCommands implements CommandExecutor {

    private ItemMePlugin plugin;

    public ItemMeCommands(ItemMePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        ItemStack item = ((Player) commandSender).getInventory().getItemInHand();

        this.plugin.getConfig().set(item.getType().name(), item.serialize());
        this.plugin.saveConfig();

        return false;
    }
}
