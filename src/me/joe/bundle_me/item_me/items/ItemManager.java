package me.joe.bundle_me.item_me.items;

import me.joe.bundle_me.item_me.ItemMePlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemManager {

    private ItemMePlugin plugin;
    private FileConfiguration config;

    public ItemManager(ItemMePlugin plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
    }

    public void save(String id, ItemStack item) {
        if (this.config.getKeys(false).contains(id)) {
            System.out.println("the item : " + id + " : already exists");
            return;
        }

        this.saveItemToConfig(id, item);

        this.plugin.saveConfig();
    }

    private void saveItemToConfig(String id, ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        String name = meta.getDisplayName();
        List<String> lore = meta.getLore();

        String material = item.getType().name();
        int amount = item.getAmount();

        Integer durability = null;
        if (meta instanceof Damageable){
            durability = ((Damageable) meta).getDamage();
        }

        this.config.set(id + ".name", name);
        this.config.set(id + ".lore", lore);
        this.config.set(id + ".material", material);
        this.config.set(id + ".amount", amount);
        this.config.set(id + ".durability", durability);

        Map<Enchantment, Integer> enchantments = item.getEnchantments();
        for (Enchantment enchantment : enchantments.keySet()) {
            this.config.set(id + ".enchantments." + enchantment.getKey().getKey(), enchantments.get(enchantment));
        }
    }

    public ItemStack load(ConfigurationSection itemSection) {
        String id = itemSection.getName();
        String name = itemSection.getString("name");
        List<String> lore = itemSection.getStringList("lore");

        String rawMaterial = itemSection.getString("material");
        Material material;
        if (rawMaterial != null) {
            material = Material.getMaterial(rawMaterial);
        } else {
            material = Material.STONE;
        }
        int amount = itemSection.getInt("amount");
//        int durability = itemSection.getInt("durability");

        ConfigurationSection rawEnchantments = itemSection.getConfigurationSection("enchantments");
        Map<Enchantment, Integer> enchantments = new HashMap<>();

        if (rawEnchantments != null) {
            for (String rawEnchantment : rawEnchantments.getKeys(false)) {
                Enchantment enchantment = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(rawEnchantment));
                Integer level = rawEnchantments.getInt(rawEnchantment);

                enchantments.put(enchantment, level);
            }
        }

        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);

//        ((Damageable) meta).setDamage(durability);

        item.setItemMeta(meta);
        item.addUnsafeEnchantments(enchantments);
        return item;
    }
}
