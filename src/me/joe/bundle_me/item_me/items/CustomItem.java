package me.joe.bundle_me.item_me.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomItem {

    private String name;
    private Material material;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private Integer durability;
    private boolean unbreakable = false;
    private boolean safeEnchantment = true;
    private boolean loreEnchantment = false;
    private boolean shiny = false;

    public CustomItem(Material material) {
        this.material = material;
    }

    public CustomItem(ItemStack item) {
        this.material = item.getType();
        this.enchantments = item.getEnchantments();
        this.durability = item instanceof Damageable ? this.getItemDurability() : null;

        ItemMeta meta = item.getItemMeta();
        this.name = meta != null ? meta.getDisplayName() : null;
        this.lore = meta != null ? meta.getLore() : null;
    }

    public ItemStack getItem() {
        ItemStack item = new ItemStack(this.material);
        if (this.safeEnchantment) item.addEnchantments(this.enchantments); else item.addUnsafeEnchantments(this.enchantments);

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(this.name);
            meta.setLore(this.lore);
            if (meta instanceof Damageable) ((Damageable) meta).setDamage(this.getItemDurability());
            meta.setUnbreakable(this.unbreakable);
            if (this.loreEnchantment) this.setItemLoreEnchantment(meta);
            if (this.shiny) this.setItemSniny(meta);
        }

        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getAmountOfItem(int amount) {
        ItemStack item = this.getItem();
        item.setAmount(amount);
        return item;
    }

    private int getItemDurability() {
        return this.material.getMaxDurability() - this.durability;
    }

    private void setItemLoreEnchantment(ItemMeta meta) {
        // TODO - get lore lines from CustomItemManager config file
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    private void setItemSniny(ItemMeta meta) {
        if (!meta.hasEnchants()) meta.addEnchant(Enchantment.DURABILITY,1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    public String getName() {
        return name;
    }

    public CustomItem setName(String name) {
        this.name = name;
        return this;
    }

    public Material getMaterial() {
        return material;
    }

    public CustomItem setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public List<String> getLore() {
        return lore;
    }

    public CustomItem setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public CustomItem setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public int getDurability() {
        return durability;
    }

    public CustomItem setDurability(int durability) {
        this.durability = durability;
        return this;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public CustomItem setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public boolean isSafeEnchantment() {
        return safeEnchantment;
    }

    public CustomItem setSafeEnchantment(boolean safeEnchantment) {
        this.safeEnchantment = safeEnchantment;
        return this;
    }

    public boolean isLoreEnchantment() {
        return loreEnchantment;
    }

    public CustomItem setLoreEnchantment(boolean loreEnchantment) {
        this.loreEnchantment = loreEnchantment;
        return this;
    }

    public boolean isShiny() {
        return shiny;
    }

    public CustomItem setShiny(boolean shiny) {
        this.shiny = shiny;
        return this;
    }
}
