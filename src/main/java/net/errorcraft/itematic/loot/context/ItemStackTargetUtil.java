package net.errorcraft.itematic.loot.context;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.storage.loot.LootContext;

public class ItemStackTargetUtil {
    public static final Codec<LootContext.ItemStackTarget> CODEC = StringRepresentable.fromEnum(LootContext.ItemStackTarget::values);

    private ItemStackTargetUtil() {}
}
