package net.errorcraft.itematic.recipe;

import com.google.gson.JsonObject;
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
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, new Identifier(id));
        return itemRegistry.entryOf(key);
    }

    public static RegistryEntry<Item> getResultItem(JsonObject json) {
        Identifier id = new Identifier(JsonHelper.getString(json, RESULT_KEY));
        return itemRegistry.entryOf(RegistryKey.of(RegistryKeys.ITEM, id));
    }
}
