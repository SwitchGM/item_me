# Item Me
<img src="https://img.shields.io/badge/Minecraft-1.13%20%2B-green"> <img src="https://img.shields.io/badge/Spigot-1.16.1-orange">

**Item Me** is a plugin and API for creating custom items in Minecraft. Built from Spigot 1.16.1, and supports Minecraft versions from 1.13+.

## Basic Commands
List saved items
```markdown
/itemme list
```
Give a saved item to a player
```markdown
/itemme give (item_id) <player>
```
Save an item in hand to config
```markdown
/itemme save (item_id)
```
Reload config
```markdown
/itemme reload
```
## Basic Documentation
### Creating Custom Items
You can create a custom item by simply defining the material for the item.
```java
CustomItem customItem = new CustomItem(Material.STONE);
```
Or you can build it from an existing Minecraft ItemStack
```java
ItemStack minecraftItem = new ItemStack(Material.GRASS);
CustomItem customItem = new CustomItem(minecraftItem);
```
You can also create the custom item by passing in a map of the item info
```java
Map<String, Object> itemInfo = new HashMap(){};
itemInfo.put("material", "stick");

CustomItem customItem = new CustomItem(itemInfo);
```
### Editing Custom Items
Setting custom item information is just like setting regular ItemStack info but without the need for item meta. And getting this information is just as easy.

You can set the custom item's display name.
```java
CustomItem item = new CustomItem(Material.STICK);
item.setName("Here's a name");

String name = item.getName();
```
You can set the material.
```java
CustomItem item = new CustomItem(Material.STICK);
item.setMaterial(Material.DIAMOND);

Material material = item.getMaterial();
```
You can set the custom item's lore.
```java
CustomItem item = new CustomItem(Material.STICK);
ArrayList<String> lore = new ArrayList<>();
lore.add("Line 1 of the lore");
lore.add("Line two of the lore");

item.setLore(lore);

ArrayList<String> itemLore = item.getLore();
```
You can set the custom item's enchantments.
```java
CustomItem item = new CustomItem(Material.STICK);
HashMap<Enchantment, Integer> enchantments = new HashMap<>();
enchantments.put(Enchantment.DIAMOND_SWORD, 1);

item.setEnchantments(enchantments);

HashMap<Enchantment, Integer> itemEnchantments = item.getEnchantments();
```
You can set the custom item's durability. This is done on an absoloute scape, ie: 1 durability sets the items durability to 1 out of it's maximum durability.
```java
CustomItem item = new CustomItem(Material.DIAMOND_SWORD);
item.setDurability(1);

int durability = item.getDurability();
```
You can set whether the custom item is unbreakable.
```java
CustomItem item = new CustomItem(Material.DIAMOND_SWORD);
item.setUnbreakable(true);

boolean unbreakable = item.isUnbreakable();
```
You can set whether the custom item obeys safe enchantments (regular minecraft enchantment levels). Setting this to false will mean you can set enchantments with level 20 etc.
```java
CustomItem item = new CustomItem(Material.DIAMOND_SWORD);
item.setSafeEnchanted(true);

boolean isSafeEnchanted = item.isSafeEnchanted();
```
You can set whether the custom item is shiny (has an enchanted effect). This will only be noticeable in items that aren't already enchanted.
```java
CustomItem item = new CustomItem(Material.STICK);
item.setShiny(true);

boolean isShiny = item.isShiny();
```
You can set the crafting recipe for the item (for custom crafting) 
```java
// But not just yet :D
```
You can set the item model data (used with resource pack textures) 
```java
CustomItem item = new CustomItem(Material.STICK);
item.setModelData(1);

Integer modelData = item.getModelData();
```

### The Custom Item Manager
The custom item manager is responsible for handling the saving, getting and loading of custom items from the custom item yml list.

*I'll be putting documentation in soon regarding this*
