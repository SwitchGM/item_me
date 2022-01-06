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

import java.util.*;

public class CustomItemManager {

    private ItemMePlugin plugin;

    private FileConfiguration config;
    private HashMap<String, CustomItem> items = new HashMap<>();

    public CustomItemManager(ItemMePlugin plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
    }

    public boolean saveItem(String id, ItemStack item) {
        if (this.items.containsKey(id)) {
            // TODO - item already exists
            return false;
        }

        CustomItem customItem = new CustomItem(item);

        this.saveItem(id, customItem);

        return true;
    }

    public boolean saveItem(String id, CustomItem item) {
        if (this.items.containsKey(id)) {
            // TODO - item already exists
            return false;
        }
        HashMap<String, Object> rawItemInfo = this.getRawItemInfo(item);
        this.items.put(id, item);
        this.saveItemInfo(id, rawItemInfo);

        return true;
    }

    public void loadItems() {
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

        this.registerItemCraftingRecipes();
    }

    private void registerItemCraftingRecipes() {
        for (String id : this.items.keySet()) {
            CustomItem item = this.items.get(id);
            for (CustomCraftingRecipe craftingRecipe : item.getRecipes()) {
                this.plugin.getServer().addRecipe(craftingRecipe.getShapedRecipe());
            }
        }
    }

    private void unregisterItemCraftingRecipes() {
        for (String id : this.items.keySet()) {
            CustomItem item = this.items.get(id);
            for (CustomCraftingRecipe craftingRecipe : item.getRecipes()) {
                NamespacedKey key = craftingRecipe.getKey();
                this.plugin.getServer().removeRecipe(key);
            }
        }
    }

    public void reload() {
        this.unregisterItemCraftingRecipes();
        this.items = new HashMap<>();

        this.plugin.reloadConfig();
        this.loadItems();
    }

    public CustomItem loadItemInfo(ConfigurationSection rawItemInfo) {
        HashMap<String, Object> itemInfoMap = this.getItemInfo(rawItemInfo);
        return new CustomItem(itemInfoMap);
    }

    private void saveItemInfo(String id, Map<String, Object> rawItemInfo) {
        this.config.set(id + ".name", rawItemInfo.get("name"));
        this.config.set(id + ".material", rawItemInfo.get("material"));
        this.config.set(id + ".lore", rawItemInfo.get("lore"));
        this.config.set(id + ".enchantments", rawItemInfo.get("enchantments"));
        this.config.set(id + ".durability", rawItemInfo.get("durability"));
        this.config.set(id + ".unbreakable", rawItemInfo.get("unbreakable"));
        this.config.set(id + ".safe_enchanted", rawItemInfo.get("safe_enchanted"));
        this.config.set(id + ".lore_enchanted", rawItemInfo.get("lore_enchanted"));
        this.config.set(id + ".shiny", rawItemInfo.get("shiny"));
        this.config.set(id + ".recipes", rawItemInfo.get("recipes"));
        this.config.set(id + ".model_data", rawItemInfo.get("model_data"));

        this.plugin.saveConfig();
    }

    private HashMap<String, Object> getItemInfo(ConfigurationSection rawItemInfo) {
        HashMap<String, Object> rawItemInfoMap = new HashMap<>();

        rawItemInfoMap.put("name", rawItemInfo.getString("name"));
        rawItemInfoMap.put("lore", this.getItemLore(rawItemInfo.getStringList("lore")));
        rawItemInfoMap.put("durability", rawItemInfo.getInt("durability"));
        rawItemInfoMap.put("unbreakable", rawItemInfo.getBoolean("unbreakable"));
        rawItemInfoMap.put("safe_enchanted", rawItemInfo.getBoolean("safe_enchanted"));
        rawItemInfoMap.put("lore_enchanted", rawItemInfo.getBoolean("lore_enchanted"));
        rawItemInfoMap.put("shiny", rawItemInfo.getBoolean("shiny"));
        rawItemInfoMap.put("model_data", rawItemInfo.getInt("model_data"));

        String rawMaterial = rawItemInfo.getString("material");
        if (rawMaterial != null) {
            rawItemInfoMap.put("material", Material.matchMaterial(rawMaterial.toUpperCase()));
        }

        ConfigurationSection rawEnchantments = rawItemInfo.getConfigurationSection("enchantments");
        if (rawEnchantments != null) {
            rawItemInfoMap.put("enchantments", this.getItemEnchantments(rawEnchantments));
        }

        ConfigurationSection rawCraftingRecipes = rawItemInfo.getConfigurationSection("recipes");
        if (rawCraftingRecipes != null) {
            rawItemInfoMap.put("recipes", this.getItemCraftingRecipes(rawCraftingRecipes));
        }

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
            rawItemInfo.put("unbreakable", meta.isUnbreakable());
            rawItemInfo.put("model_data", meta.getCustomModelData());

            if (meta instanceof Damageable) {
                rawItemInfo.put("durability", ((Damageable) meta).getDamage());
            }

        }

        return rawItemInfo;
    }

    private HashMap<String, Object> getRawItemInfo(CustomItem item) {
        HashMap<String, Object> rawItemInfo = this.getRawItemInfo(item.getItem());
        rawItemInfo.put("safe_enchantment", item.isSafeEnchanted());
        rawItemInfo.put("lore_enchantment", item.isLoreEnchanted());
        rawItemInfo.put("shiny", item.isShiny());
        rawItemInfo.put("recipes", this.getRawItemCraftingRecipes(item.getRecipes()));

        return rawItemInfo;
    }

    private List<String> getItemLore(List<String> rawLore) {
        List<String> lore = new ArrayList<>();
        for (String line : rawLore) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        return lore;
    }

    private Map<Enchantment, Integer> getItemEnchantments(ConfigurationSection rawEnchantments) {
        HashMap<Enchantment, Integer> enchantments = new HashMap<>();
        for (String rawEnchantment : rawEnchantments.getKeys(false)) {
            Enchantment enchantment = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(rawEnchantment));
            int level = rawEnchantments.getInt(rawEnchantment);

            enchantments.put(enchantment, level);
        }

        return enchantments;
    }

    private List<CustomCraftingRecipe> getItemCraftingRecipes(ConfigurationSection rawCraftingRecipes) {
        List<CustomCraftingRecipe> craftingRecipes = new ArrayList<>();
        for (String recipeKey : rawCraftingRecipes.getKeys(false)) {
            ConfigurationSection rawCraftingRecipe = rawCraftingRecipes.getConfigurationSection(recipeKey);
            if (rawCraftingRecipe != null) {
                CustomCraftingRecipe shapedRecipe = this.getCraftingRecipe(rawCraftingRecipe, recipeKey);
                craftingRecipes.add(shapedRecipe);
            }
        }

        return craftingRecipes;
    }

    private CustomCraftingRecipe getCraftingRecipe(ConfigurationSection rawCraftingRecipe, String recipeKey) {
        NamespacedKey namespacedKey = new NamespacedKey(this.plugin, recipeKey);
        CustomCraftingRecipe craftingRecipe = new CustomCraftingRecipe(namespacedKey);

        craftingRecipe.setShape(rawCraftingRecipe.getStringList("shape"));

        ConfigurationSection rawMaterials = rawCraftingRecipe.getConfigurationSection("materials");
        Map<Character, Material> materials = new HashMap<>();
        if (rawMaterials != null) {
            for (String materialCharacter : rawMaterials.getKeys(false)) {
                String rawMaterial = rawMaterials.getString(materialCharacter);
                if (rawMaterial != null) {
                    materials.put(materialCharacter.charAt(0), Material.matchMaterial(rawMaterial.toUpperCase()));
                }
            }
        }

        craftingRecipe.setMaterials(materials);

        return craftingRecipe;
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

    private Map<Integer, Map<String, Object>> getRawItemCraftingRecipes(List<CustomCraftingRecipe> craftingRecipes) {
        HashMap<Integer, Map<String, Object>> rawCraftingRecipes = new HashMap<>();
        for (CustomCraftingRecipe craftingRecipe : craftingRecipes) {
            HashMap<String, Object> rawCraftingRecipe = new HashMap<>();
            List<String> rawShape = Arrays.asList(craftingRecipe.getShapedRecipe().getShape());

            HashMap<String, String> rawMaterials = new HashMap<>();
            for (Character character : craftingRecipe.getMaterials().keySet()) {
                Material material = craftingRecipe.getMaterials().get(character);
                rawMaterials.put(String.valueOf(character), material.getKey().getKey());
            }

            rawCraftingRecipe.put("shape", rawShape);
            rawCraftingRecipe.put("materials", rawMaterials);

            rawCraftingRecipes.put(craftingRecipes.lastIndexOf(craftingRecipe), rawCraftingRecipe);
        }

        return rawCraftingRecipes;
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

    public CustomItem getCustomItem(String itemId) {
        if (!this.items.containsKey(itemId)) {
            return null;
        }

        return this.items.get(itemId);
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