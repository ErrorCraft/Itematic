package net.errorcraft.itematic.loot.condition;

import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.loot.LootDataType;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.util.dynamic.Codecs;

public class LootConditionUtil {
    public static final Codec<LootCondition> CODEC = Codecs.JSON_ELEMENT.flatXmap(element -> {
        try {
            return DataResult.success(LootDataType.PREDICATES.getGson().fromJson(element, LootCondition.class));
        } catch (JsonSyntaxException e) {
            return DataResult.error(e::getMessage);
        }
    }, lootCondition -> {
        try {
            return DataResult.success(LootDataType.PREDICATES.getGson().toJsonTree(lootCondition));
        } catch (Exception e) {
            return DataResult.error(e::getMessage);
        }
    });
}
