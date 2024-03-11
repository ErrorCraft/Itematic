package net.errorcraft.itematic.mixin.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SpawnEggItem.class)
public interface SpawnEggItemAccessor {
    @Accessor("ENTITY_TYPE_MAP_CODEC")
    static MapCodec<EntityType<?>> entityTypeMapCodec() {
        throw new AssertionError();
    }
}
