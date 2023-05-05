package net.errorcraft.itematic.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class RecipeSerializerUtil {
    private static final String ITEM_KEY = "item";
    private static final String RESULT_KEY = "result";
    private static Registry<Item> itemRegistry;

    private RecipeSerializerUtil() {}

    public static Registry<Item> getItemRegistry() {
        return itemRegistry;
    }

    public static void setItemRegistry(Registry<Item> itemRegistry) {
        RecipeSerializerUtil.itemRegistry = itemRegistry;
    }

    public static RegistryEntry<Item> getItem(JsonObject json) {
        String id = JsonHelper.getString(json, ITEM_KEY);
        RegistryEntry<Item> item = itemRegistry.getEntry(RegistryKey.of(RegistryKeys.ITEM, new Identifier(id))).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + id + "'"));
        if (item.matchesKey(ItemKeys.AIR)) {
            throw new JsonSyntaxException("Invalid item: " + id);
        }
        return item;
    }

    public static RegistryEntry<Item> getResultItem(JsonObject json) {
        Identifier id = new Identifier(JsonHelper.getString(json, RESULT_KEY));
        return itemRegistry.entryOf(RegistryKey.of(RegistryKeys.ITEM, id));
    }

    public static int getRawId(RegistryEntry<Item> entry) {
        if (entry == null) {
            return 0;
        }
        return itemRegistry.getRawId(entry.value());
    }
}
