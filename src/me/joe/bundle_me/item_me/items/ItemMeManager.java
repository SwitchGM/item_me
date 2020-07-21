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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemMeManager {

    private ItemMePlugin plugin;
    private FileConfiguration config;

    public ItemMeManager(ItemMePlugin plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
    }

    public void saveItem(String id, ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        HashMap<String, Object> itemInfo = new HashMap<>();
        itemInfo.put("name", meta.getDisplayName());
        itemInfo.put("lore", meta.getLore());
        itemInfo.put("material", item.getType().name());
        itemInfo.put("amount", item.getAmount());
        if (meta instanceof Damageable){
            itemInfo.put("durability", ((Damageable) meta).getDamage());
        }
        itemInfo.put("enchantments", this.getRawEnchantments(new HashMap<>(item.getEnchantments())));

        this.saveItemInfo(id, itemInfo);
        this.plugin.saveConfig();
    }

    public void saveItemInfo(String id, HashMap<String, Object> itemInfo) {
        for (String key : itemInfo.keySet()) {
            System.out.println(key);
            Object value = itemInfo.get(key);
            if (key.equalsIgnoreCase("name")) {
                assert value instanceof String : "item name has not been properly defined";
                System.out.println(value);
                this.config.set(id + ".name", value);

            } else if (key.equalsIgnoreCase("lore")) {
                assert value instanceof String : "item lore has not been properly defined";
                this.config.set(id + ".lore", value);

            } else if (key.equalsIgnoreCase("material")) {
                assert value instanceof String : "item material has not been properly defined";
                this.config.set(id + ".material", value);

            } else if (key.equalsIgnoreCase("amount")) {
                assert value instanceof Integer : "item amount has not been properly defined";
                this.config.set(id + ".amount", value);

            } else if (key.equalsIgnoreCase("durability")) {
                assert value instanceof Integer : "item durability has not been properly defined";
                this.config.set(id + ".durability", value);
            } else if (key.equalsIgnoreCase("enchantments")) {
                assert value instanceof HashMap: "item enchantments have not been properly defined";
                this.config.set(id + ".enchantments", value);
            }
        }
    }

    public HashMap<String, Integer> getRawEnchantments(HashMap<Enchantment, Integer> enchantments) {
        HashMap<String, Integer> rawEnchantments = new HashMap<>();
        for (Enchantment enchantment : enchantments.keySet()) {
            String rawEnchantment = enchantment.getKey().getKey();
            Integer level = enchantments.get(enchantment);

            rawEnchantments.put(rawEnchantment, level);
        }

        return rawEnchantments;
    }

    public HashMap<Enchantment, Integer> getEnchantments(HashMap<String, Integer> rawEnchantments) {
        HashMap<Enchantment, Integer> enchantments = new HashMap<>();
        for (String rawEnchantment : rawEnchantments.keySet()) {
            Enchantment enchantment = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(rawEnchantment));
            Integer level = rawEnchantments.get(rawEnchantment);

            enchantments.put(enchantment, level);
        }

        return enchantments;
    }

    private void saveItem(String id, ItemMeItem item) {

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
