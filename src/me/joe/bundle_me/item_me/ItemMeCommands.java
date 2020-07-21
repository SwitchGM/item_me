package me.joe.bundle_me.item_me;

import me.joe.bundle_me.item_me.items.ItemMeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemMeCommands implements CommandExecutor {

    private ItemMePlugin plugin;
    private ItemMeManager manager;

    public ItemMeCommands(ItemMePlugin plugin) {
        this.plugin = plugin;
        this.manager = new ItemMeManager(plugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        ItemStack item = ((Player) commandSender).getInventory().getItemInMainHand();

//        this.plugin.getConfig().set(String.valueOf(System.currentTimeMillis()), item);

//        this.plugin.saveConfig();

        this.manager.saveItem(String.valueOf(System.currentTimeMillis()), item);
        System.out.println(System.currentTimeMillis());

//        ConfigurationSection itemInfo = this.plugin.getConfig().getConfigurationSection("DIAMOND_SWORD");
//        ItemStack item = this.manager.load(itemInfo);
//
//        ((Player) commandSender).getInventory().addItem(item);

        return false;
    }
}
