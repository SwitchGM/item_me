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
            Object value = itemInfo.get(key);
            if (key.equalsIgnoreCase("name")) {
                if (this.checkItemField("name", value)) {
                    this.config.set(id + ".name", value);
                }
            } else if (key.equalsIgnoreCase("lore")) {
                if (this.checkItemField("lore", value)) {
                    this.config.set(id + ".lore", value);
                }
            } else if (key.equalsIgnoreCase("material")) {
                if (this.checkItemField("material", value)) {
                    this.config.set(id + ".material", value);
                }
            } else if (key.equalsIgnoreCase("amount")) {
                if (this.checkItemField("amount", value)) {
                    this.config.set(id + ".amount", value);
                }
            } else if (key.equalsIgnoreCase("durability")) {
                if (this.checkItemField("durability", value)) {
                    this.config.set(id + ".durability", value);
                }
            } else if (key.equalsIgnoreCase("enchantments")) {
                if (this.checkItemField("enchantments", value)) {
                    this.config.set(id + ".enchantments", value);
                }
            }
        }
    }

    public ItemStack loadItem(String id) {
        if (!(this.config.contains(id))) {
            System.out.println("the item id: " + id + " :does not exist");
            return null;
        }

        ConfigurationSection rawItemInfo = this.config.getConfigurationSection(id);
        HashMap<String, Object> itemInfo = this.loadItemInfo(rawItemInfo);

        String name = (String) itemInfo.get("name");
        List<String> lore = (List<String>) itemInfo.get("lore");
        Material material = Material.getMaterial((String) itemInfo.get("material"));
        Integer amount = (Integer) itemInfo.get("amount");
//        Integer durability = (Integer) itemInfo.get("durability");

        ConfigurationSection rawEnchantmentsFromConfig = rawItemInfo.getConfigurationSection("enchantments");
        Map<Enchantment, Integer> enchantments = this.getEnchantmentsFromConfig(rawEnchantmentsFromConfig);

        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);

        item.setItemMeta(meta);
        item.addUnsafeEnchantments(enchantments);
        return item;
    }

    public HashMap<String, Object> loadItemInfo(ConfigurationSection rawItemInfo) {
        HashMap<String, Object> itemInfo = new HashMap<>();
        for (String key : rawItemInfo.getKeys(false)) {
            Object value = rawItemInfo.get(key);
            if (key.equalsIgnoreCase("name")) {
                if (this.checkItemField("name", value)) {
                    itemInfo.put("name", value);
                }
            } else if (key.equalsIgnoreCase("lore")) {
                if (this.checkItemField("lore", value)) {
                    itemInfo.put("lore", value);
                }
            } else if (key.equalsIgnoreCase("material")) {
                if (this.checkItemField("material", value)) {
                    itemInfo.put("material", value);
                }
            } else if (key.equalsIgnoreCase("amount")) {
                if (this.checkItemField("amount", value)) {
                    itemInfo.put("amount", value);
                }
            } else if (key.equalsIgnoreCase("durability")) {
                if (this.checkItemField("durability", value)) {
                    itemInfo.put("durability", value);
                }
            } else if (key.equalsIgnoreCase("enchantments")) {
                if (this.checkItemField("enchantments", value)) {
                    itemInfo.put("enchantments", value);
                }
            }
        }

        return itemInfo;
    }

    public boolean checkItemField(String id, Object value) {
        if (id.equalsIgnoreCase("name")) {
            if (value instanceof String) {
                return true;
            }
        } else if (id.equalsIgnoreCase("lore")) {
            if (value instanceof List) {
                return true;
            }
        } else if (id.equalsIgnoreCase("material")) {
            if (value instanceof String) {
                return true;
            }
        } else if (id.equalsIgnoreCase("amount")) {
            if (value instanceof Integer) {
                return true;
            }
        } else if (id.equalsIgnoreCase("durability")) {
            if (value instanceof Integer) {
                return true;
            }
        } else if (id.equalsIgnoreCase("enchantments")) {
            if (value instanceof Map) {
                return true;
            }
        } else {
            System.out.println("item field : " + id + " : was not recognised");
            return false;
        }
        System.out.println("item : " + id + " : was not defined correctly");
        return false;
    }

    public Map<Enchantment, Integer> getEnchantmentsFromConfig(ConfigurationSection enchantmentsFromConfig) {
        HashMap<String, Integer> rawEnchantments = new HashMap<>();
        for (String rawEnchantment : enchantmentsFromConfig.getKeys(false)) {
            rawEnchantments.put(rawEnchantment, enchantmentsFromConfig.getInt(rawEnchantment));
        }

        return this.getEnchantments(rawEnchantments);
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

    public Map<Enchantment, Integer> getEnchantments(HashMap<String, Integer> rawEnchantments) {
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        for (String rawEnchantment : rawEnchantments.keySet()) {
            Enchantment enchantment = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(rawEnchantment));
            Integer level = rawEnchantments.get(rawEnchantment);

            enchantments.put(enchantment, level);
        }

        return enchantments;
    }
}
