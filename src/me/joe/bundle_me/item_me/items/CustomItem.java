package me.joe.bundle_me.item_me.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomItem {

    private String name;
    private Material material;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private Integer durability = 0;
    private boolean unbreakable = false;
    private boolean safeEnchanted = false;
    private boolean loreEnchanted = false;
    private boolean shiny = false;
    private List<CustomCraftingRecipe> recipes = new ArrayList<>();
    private Integer modelData = 0;
    private Integer blockBreakTime = 999999999;

    public CustomItem(Material material) {
        this.material = material;
    }

    public CustomItem(ItemStack item) {
        this.material = item.getType();
        this.enchantments = item.getEnchantments();

        ItemMeta meta = item.getItemMeta();
        this.name = meta != null ? meta.getDisplayName() : null;
        this.lore = meta != null ? meta.getLore() : null;
        this.durability = meta instanceof Damageable ? ((Damageable) meta).getDamage() : null;
        this.modelData = meta != null ? meta.getCustomModelData() : 0;
    }

    @SuppressWarnings("unchecked")
    public CustomItem(Map<String, Object> itemInfo) {
        this.name = (String) itemInfo.get("name");
        this.material = (Material) itemInfo.get("material");
        this.durability = (Integer) itemInfo.get("durability");
        this.unbreakable = (Boolean) itemInfo.get("unbreakable");
        this.safeEnchanted = (Boolean) itemInfo.get("safe_enchanted");
        this.loreEnchanted = (Boolean) itemInfo.get("lore_enchanted");
        this.shiny = (Boolean) itemInfo.get("shiny");
        this.modelData = (Integer) itemInfo.get("model_data");
        this.blockBreakTime = (Integer) itemInfo.get("block_break_time");

        try {
            this.lore = (List<String>) itemInfo.get("lore");
            this.enchantments = (Map<Enchantment, Integer>) itemInfo.get("enchantments");
            this.recipes = (List<CustomCraftingRecipe>) itemInfo.get("recipes");
            for (CustomCraftingRecipe craftingRecipe : this.recipes) {
                craftingRecipe.setResult(this.getItem());
            }
        } catch (ClassCastException e) {
            throw new Error(e.getMessage());
        }
    }

    public ItemStack getItem() {
        ItemStack item = new ItemStack(this.material);
        item.addUnsafeEnchantments(this.enchantments);

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(this.translateColors(this.name));
            List<String> tranLore = this.translateColors(this.lore);
            meta.setLore(tranLore);
            meta.setUnbreakable(this.unbreakable);
            meta.setCustomModelData(this.modelData);

            if (this.material.getMaxDurability() != 0) {
                ((Damageable) meta).setDamage(this.getDurability() - this.material.getMaxDurability());
            }

            if (this.loreEnchanted) {
                this.setItemLoreEnchantment(meta);
            }

            if (this.shiny) {
                this.setItemShiny(meta);
            }
        }

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getAmountOfItem(int amount) {
        ItemStack item = this.getItem();
        item.setAmount(amount);
        return item;
    }

    private void setItemLoreEnchantment(ItemMeta meta) {
        // TODO - get lore lines from CustomItemManager config file
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    private void setItemShiny(ItemMeta meta) {
        if (!meta.hasEnchants()) {
            meta.addEnchant(Enchantment.DURABILITY,1, true);
        }

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    private String translateColors(String string) {
        if (string == null) {
            return "";
        }

        return ChatColor.translateAlternateColorCodes('&', string);
    }

    private List<String> translateColors(List<String> list) {
        ArrayList<String> newList = new ArrayList<>();
        if (list == null) {
            return newList;
        }

        for (String line : list) {
            newList.add(this.translateColors(line));
        }

        return newList;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public List<String> getLore() {
        return lore;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public int getDurability() {
        return this.material.getMaxDurability() == 0 ? this.durability : this.material.getMaxDurability();
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    @Deprecated
    public boolean isSafeEnchanted() {
        return safeEnchanted;
    }

    public boolean isLoreEnchanted() {
        return loreEnchanted;
    }

    public boolean isShiny() {
        return shiny;
    }

    public List<CustomCraftingRecipe> getRecipes() {
        return this.recipes;
    }

    public Integer getModelData() {
        return this.modelData;
    }

    public Integer getBlockBreakTime() {
        return this.blockBreakTime;
    }

    public CustomItem setName(String name) {
        this.name = name;
        return this;
    }

    public CustomItem setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public CustomItem setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public CustomItem setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public CustomItem setDurability(int durability) {
        this.durability = durability;
        return this;
    }

    public CustomItem setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    @Deprecated
    public CustomItem setSafeEnchanted(boolean safeEnchanted) {
        this.safeEnchanted = safeEnchanted;
        return this;
    }

    public CustomItem setLoreEnchanted(boolean loreEnchanted) {
        this.loreEnchanted = loreEnchanted;
        return this;
    }

    public CustomItem setShiny(boolean shiny) {
        this.shiny = shiny;
        return this;
    }

    public CustomItem setRecipes(List<CustomCraftingRecipe> recipes) {
        this.recipes = recipes;
        return this;
    }

    public CustomItem setModelData(Integer modelData) {
        this.modelData = modelData;
        return this;
    }

    public CustomItem setBlockBreakTime(Integer blockBreakTime) {
        this.blockBreakTime = blockBreakTime;
        return this;
    }

    public boolean isSimilar(ItemStack item) {
        ItemStack thisItem = this.getItem();
        return thisItem.isSimilar(item);
    }
}