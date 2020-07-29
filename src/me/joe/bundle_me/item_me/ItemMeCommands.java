package me.joe.bundle_me.item_me;

import me.joe.bundle_me.item_me.items.CustomItem;
import me.joe.bundle_me.item_me.items.CustomItemManager;
import org.bukkit.Bukkit;
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
            case "give":
                this.giveItem(commandSender, strings);
                break;
            case "save":
                this.saveItem(commandSender, strings);
                break;
            case "list":
                this.listItems(commandSender);
                break;
            case "reload":
                this.reload(commandSender);
                break;
            default:
                this.test(commandSender);
        }
        return false;
    }

    private void test(CommandSender commandSender) {
        // anything testy during development :D
    }

    private void giveItem(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) {
            System.out.println("you must be a player to run this command!");
            return;
        }

        Player player = (Player) commandSender;

        if (args.length == 1) {
            this.sendError(player, "You did not specify an item id to give");
            return;
        }

        if (!(this.manager.getItems().contains(args[1]))) {
            this.sendError(player, "This item does not exist");
            return;
        }

        CustomItem item = this.manager.getCustomItem(args[1]);

        if (args.length == 3) {
            Player targetPlayer = Bukkit.getServer().getPlayerExact(args[2]);
            if (targetPlayer == null) {
                this.sendError(player, "The target player was not found");
                return;
            }

            targetPlayer.getInventory().addItem(item.getItem());

            this.sendSuccess(player, "You successfully gave an item");
            return;
        }

        player.getInventory().addItem(item.getItem());

        this.sendSuccess(player, "You successfully gave an item");
    }

    private void saveItem(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) {
            System.out.println("you must be a player to run this command!");
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
            System.out.println("you must be a player to run this command!");
            return;
        }

        Player player = (Player) commandSender;

        this.sendSuccess(player, "Current items in config:");
        for (String item : this.manager.getItems()) {
            player.sendMessage(item);
        }
    }

    private void reload(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            System.out.println("you must be a player to run this command!");
            return;
        }

        Player player = (Player) commandSender;

        this.manager.reload();
        this.sendSuccess(player, "Successfully reloaded config");
    }

    private void sendError(Player player, String error) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + error));
    }

    private void sendSuccess(Player player, String sucess) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + sucess));
    }
}
