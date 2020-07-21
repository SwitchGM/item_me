package me.joe.bundle_me.item_me.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemMeItem extends ItemStack {

    public ItemMeItem(Material material) {
        super(material);
    }

    public ItemMeItem(ItemStack item) {
        super(item);
    }

    public ItemStack getItemStack() {
        return this;
    }
}
