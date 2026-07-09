package net.errorcraft.itematic.loot.context;

import com.mojang.serialization.Codec;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.StringIdentifiable;

public class ItemStackTargetUtil {
    public static final Codec<LootContext.ItemStackReference> CODEC = StringIdentifiable.createCodec(LootContext.ItemStackReference::values);

    private ItemStackTargetUtil() {}
}
