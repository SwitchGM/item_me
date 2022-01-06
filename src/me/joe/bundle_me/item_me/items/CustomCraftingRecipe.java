package me.joe.bundle_me.item_me.items;

import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class CustomCraftingRecipe {
    private NamespacedKey key;
    private ItemStack result;
    private List<String> shape;
    private Map<Character, Material> materials;

    public CustomCraftingRecipe(NamespacedKey key) {
        this.key = key;
    }

    public ShapedRecipe getShapedRecipe() {
        if (this.result == null) {
            return null;
        }

        ShapedRecipe shapedRecipe = new ShapedRecipe(this.key, this.result);
        shapedRecipe.shape(this.shape.get(0), this.shape.get(1), this.shape.get(2));

        for (Character character : this.materials.keySet()) {
            shapedRecipe.setIngredient(character, this.materials.get(character));
        }

        return shapedRecipe;
    }

    public CustomCraftingRecipe setResult(ItemStack result) {
        this.result = result;
        return this;
    }

    public CustomCraftingRecipe setShape(List<String> shape) {
        this.shape = shape;
        return this;
    }

    public CustomCraftingRecipe setMaterials(Map<Character, Material> materials) {
        this.materials = materials;
        return this;
    }

    public Map<Character, Material> getMaterials() {
        return this.materials;
    }

    public NamespacedKey getKey() {
        return this.key;
    }
}
