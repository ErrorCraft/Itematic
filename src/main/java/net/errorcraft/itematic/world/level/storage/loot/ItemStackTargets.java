package net.errorcraft.itematic.world.level.storage.loot;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.storage.loot.LootContext;

public class ItemStackTargets {
    public static final Codec<LootContext.ItemStackTarget> CODEC = StringRepresentable.fromEnum(LootContext.ItemStackTarget::values);

    private ItemStackTargets() {}
}
