package me.joe.bundle_me.item_me.items;

import me.joe.bundle_me.item_me.ItemMePlugin;
import org.bukkit.ChatColor;
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

public class CustomItemManager {

    private ItemMePlugin plugin;

    private FileConfiguration config;
    private HashMap<String, CustomItem> items = new HashMap<>();

    public CustomItemManager(ItemMePlugin plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
        this.loadItems();
    }

    public boolean saveItem(String id, ItemStack item) { // TODO
        if (this.items.containsKey(id)) {
            // TODO - item already exists
            return false;
        }

        CustomItem customItem = new CustomItem(item);
        HashMap<String, Object> rawItemInfo = this.getRawItemInfo(customItem);
        this.saveItemInfo(rawItemInfo);

    }

    public void loadItems() {  // TODO
        HashMap<String, CustomItem> items = new HashMap<>();
        for (String id : this.config.getKeys(false)) {
            ConfigurationSection itemInfo = this.config.getConfigurationSection(id);
            if (itemInfo == null) {
                // TODO - something might go horribly wrong
                continue;
            }
            items.put(id, this.loadItemInfo(itemInfo));
        }

        this.items = items;
    }

    public CustomItem loadItemInfo(ConfigurationSection rawItemInfo) { // TODO
        HashMap<String, Object> itemInfoMap = this.getItemInfo(rawItemInfo);
        return new CustomItem(itemInfoMap);
    }

    private void saveItemInfo(Map<String, Object> rawItemInfo) {

    }

    private HashMap<String, Object> getItemInfo(ConfigurationSection rawItemInfo) { // TODO
        HashMap<String, Object> rawItemInfoMap = new HashMap<>();
        rawItemInfoMap.put("name", rawItemInfo.getString("name"));
        rawItemInfoMap.put("material", Material.valueOf(rawItemInfo.getString("material")));
        rawItemInfoMap.put("lore", this.getItemLore(rawItemInfo.getStringList("lore")));

        ConfigurationSection rawEnchantments = rawItemInfo.getConfigurationSection("enchantments");
        if (rawEnchantments != null) {
            rawItemInfoMap.put("enchantments", this.getItemEnchantments(rawEnchantments));
        }

        rawItemInfoMap.put("durability", rawItemInfo.getInt("durability"));
        rawItemInfoMap.put("unbreakable", rawItemInfo.getBoolean("unbreakable"));
        rawItemInfoMap.put("safe_enchanted", rawItemInfo.getBoolean("safe_enchanted"));
        rawItemInfoMap.put("lore_enchanted", rawItemInfo.getBoolean("lore_enchanted"));
        rawItemInfoMap.put("shiny", rawItemInfo.getBoolean("shiny"));

        return rawItemInfoMap;
    }

    private HashMap<String, Object> getRawItemInfo(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        HashMap<String, Object> rawItemInfo = new HashMap<>();
        rawItemInfo.put("material", item.getType().getKey().getKey());
        rawItemInfo.put("enchantments", this.getRawItemEnchantments(item.getEnchantments()));
        if (meta != null) {
            rawItemInfo.put("name", this.reverseToAlternateColor(meta.getDisplayName()));
            rawItemInfo.put("lore", this.reverseToAlternateColor(meta.getLore()));
            if (meta instanceof Damageable) {
                rawItemInfo.put("durability", ((Damageable) meta).getDamage());
            }

            rawItemInfo.put("unbreakable", meta.isUnbreakable());
        }

        return rawItemInfo;
    }

    private HashMap<String, Object> getRawItemInfo(CustomItem item) {
        HashMap<String, Object> rawItemInfo = this.getRawItemInfo(item);
        rawItemInfo.put("safe_enchantment", item.isSafeEnchanted());
        rawItemInfo.put("lore_enchantment", item.isLoreEnchanted());
        rawItemInfo.put("shiny", item.isShiny());

        return rawItemInfo;
    }

    private List<String> getItemLore(List<String> rawLore) { // TODO
        List<String> lore = new ArrayList<>();
        for (String line : rawLore) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        return lore;
    }

    private Map<Enchantment, Integer> getItemEnchantments(ConfigurationSection rawEnchantments) { // TODO
        HashMap<Enchantment, Integer> enchantments = new HashMap<>();
        for (String rawEnchantment : rawEnchantments.getKeys(false)) {
            Enchantment enchantment = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(rawEnchantment));
            int level = rawEnchantments.getInt(rawEnchantment);

            enchantments.put(enchantment, level);
        }

        return enchantments;
    }

    private Map<String, Integer> getRawItemEnchantments(Map<Enchantment, Integer> enchantments) {
        HashMap<String, Integer> rawEnchantments = new HashMap<>();
        for (Enchantment enchantment : enchantments.keySet()) {
            String rawEnchantment = enchantment.getKey().getKey();
            int level = enchantments.get(enchantment);

            rawEnchantments.put(rawEnchantment, level);
        }

        return rawEnchantments;
    }

    private String reverseToAlternateColor(String text) {
        char[] chars = text.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ChatColor.COLOR_CHAR && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(chars[i + 1]) > -1) {
                chars[i] = '&';
                chars[i + 1] = Character.toLowerCase(chars[i + 1]);
            }
        }

        return new String(chars);
    }

    private List<String> reverseToAlternateColor(List<String> list) {
        if (list == null) {
            return null;
        }

        ArrayList<String> newList = new ArrayList<>();

        for (String line : list) {
            newList.add(this.reverseToAlternateColor(line));
        }

        return newList;
    }

    public boolean saveItem(String id, ItemStack item) {
        if (this.items.containsKey(id)) {
            // TODO - item id already exists
            return false;
        }

        CustomItem customItem = new CustomItem(item);
        HashMap<String, Object> rawItemInfo = this.getRawItemInfo(customItem);
    }

    public boolean _saveItem(String id, ItemStack item) {
        if (this.items.containsKey(id)) {
            return false;
        }

        if (this.config.contains(id)) {
            return false;
        }

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

        this.items.put(id, item);
        this.saveItemInfo(id, itemInfo);
        return true;
    }

    public void _saveItemInfo(String id, HashMap<String, Object> itemInfo) {
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

    public CustomItem getCustomItem(ItemStack item) {
        for (String id : this.items.keySet()) {
            CustomItem checkItem = this.items.get(id);
            if (checkItem.isSimilar(item)) {
                return checkItem;
            }
        }

        return null;
    }

    public List<String> getItems() {
        return new ArrayList<>(this.items.keySet());
    }
}
