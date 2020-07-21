package me.joe.bundle_me.item_me;

import me.joe.bundle_me.item_me.items.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemMeCommands implements CommandExecutor {

    private ItemMePlugin plugin;
    private ItemManager manager;

    public ItemMeCommands(ItemMePlugin plugin) {
        this.plugin = plugin;
        this.manager = new ItemManager(plugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
//        ItemStack item = ((Player) commandSender).getInventory().getItemInMainHand();

//        this.manager.save(item.getType().name(), item);

        ConfigurationSection itemInfo = this.plugin.getConfig().getConfigurationSection("DIAMOND_SWORD");
        ItemStack item = this.manager.load(itemInfo);

        ((Player) commandSender).getInventory().addItem(item);

        return false;
    }
}
