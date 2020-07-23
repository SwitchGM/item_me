package me.joe.bundle_me.item_me;

import me.joe.bundle_me.item_me.items.CustomItemManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemMeCommands implements CommandExecutor {

    private ItemMePlugin plugin;
    private CustomItemManager manager;

    public ItemMeCommands(ItemMePlugin plugin) {
        this.plugin = plugin;
        this.manager = new CustomItemManager(plugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            // help command yo
            return true;
        }

        String subCommand = strings[0];
        switch (subCommand) {
            case "load":
                this.loadItem(commandSender, strings);
                break;
            case "save":
                this.saveItem(commandSender, strings);
                break;
            case "list":
                this.listItems(commandSender);
            default:
                // help command yo
        }
        return false;
    }

    private void loadItem(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) {
            System.out.println("you you must be a player to run this command!");
            return;
        }

        Player player = (Player) commandSender;

        if (args.length == 1) {
            this.sendError(player, "You did not specify an item id to load");
            return;
        }

        if (!(this.manager.getItems().contains(args[1]))) {
            this.sendError(player, "This item does not exist");
            return;
        }

        ItemStack item = this.manager.loadItem(args[1]);
        player.getInventory().addItem(item);

        this.sendSuccess(player, "You successfully loaded an item");
    }

    private void saveItem(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) {
            System.out.println("you you must be a player to run this command!");
            return;
        }

        Player player = (Player) commandSender;

        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            this.sendError(player, "You must be holding an item to save it");
            return;
        }

        if (args.length == 1) {
            this.sendError(player, "You did not specify an item id to save");
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        boolean wasSuccess = this.manager.saveItem(args[1], item);
        if (wasSuccess) {
            this.sendSuccess(player, "You successfully saved an item");
        } else {
            this.sendError(player, "The item could not be saved");
        }
    }

    private void listItems(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            System.out.println("you you must be a player to run this command!");
            return;
        }

        Player player = (Player) commandSender;

        this.sendSuccess(player, "Current items in config:");
        for (String item : this.manager.getItems()) {
            player.sendMessage(item);
        }
    }

    private void sendError(Player player, String error) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + error));
    }

    private void sendSuccess(Player player, String sucess) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + sucess));
    }
}
