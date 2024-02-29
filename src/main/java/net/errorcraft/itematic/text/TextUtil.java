package net.errorcraft.itematic.text;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.serialization.JsonOps;
import net.errorcraft.itematic.mixin.text.TextAccessor;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.io.StringReader;

public class TextUtil {
    private static final Gson GSON = TextAccessor.SerializationAccessor.gson();

    private TextUtil() {}

    public static String toJsonString(Text text, RegistryWrapper.WrapperLookup lookup) {
        RegistryOps<JsonElement> ops = RegistryOps.of(JsonOps.INSTANCE, lookup);
        JsonElement json = Util.getResult(TextCodecs.CODEC.encodeStart(ops, text), JsonParseException::new);
        return GSON.toJson(json);
    }

    @Nullable
    public static MutableText fromJsonString(String jsonString, RegistryWrapper.WrapperLookup lookup) {
        JsonElement json = JsonParser.parseString(jsonString);
        if (json == null) {
            return null;
        }
        return fromJson(json, lookup);
    }

    @Nullable
    public static MutableText fromJsonStringLenient(String jsonString, RegistryWrapper.WrapperLookup lookup) {
        JsonReader reader = new JsonReader(new StringReader(jsonString));
        reader.setLenient(true);
        JsonElement json = JsonParser.parseReader(reader);
        if (json == null) {
            return null;
        }
        return fromJson(json, lookup);
    }

    private static MutableText fromJson(JsonElement json, RegistryWrapper.WrapperLookup lookup) {
        RegistryOps<JsonElement> ops = RegistryOps.of(JsonOps.INSTANCE, lookup);
        return (MutableText) Util.getResult(TextCodecs.CODEC.parse(ops, json), JsonParseException::new);
    }
}
