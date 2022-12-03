package errorcraft.itematic.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class JsonUtil {
    private JsonUtil() {}

    public static Item getItem(JsonObject json, String name, DynamicRegistryManager registryManager) {
        if (json.has(name)) {
            return getRegistryEntry(json, name, registryManager.get(RegistryKeys.ITEM));
        }
        throw new JsonSyntaxException("Missing " + name + ", expected to find an item");
    }

    public static NbtCompound getNbtCompound(JsonObject json, String name, NbtCompound defaultValue) {
        if (json.has(name)) {
            return asNbtCompound(json.get(name), name);
        }
        return defaultValue;
    }

    public static NbtCompound asNbtCompound(JsonElement json, String key) {
        try {
            return StringNbtReader.parse(JsonHelper.asString(json, key));
        } catch (CommandSyntaxException exception) {
            throw new JsonSyntaxException("Expected " + key + "to be NBT: " + exception.getMessage());
        }
    }

    public static <T> T getRegistryEntry(JsonObject json, String name, Registry<T> registry) {
        if (json.has(name)) {
            return asRegistryEntry(json.get(name), name, registry);
        }
        throw new JsonSyntaxException("Missing " + name + ", expected to find an entry of " + registry.getKey().getValue());
    }

    public static <T> T asRegistryEntry(JsonElement json, String key, Registry<T> registry) {
        if (json.isJsonPrimitive()) {
            return registry.get(new Identifier(json.getAsString()));
        }
        throw new JsonSyntaxException("Expected " + key + " to be an entry of " + registry.getKey().getValue());
    }
}
