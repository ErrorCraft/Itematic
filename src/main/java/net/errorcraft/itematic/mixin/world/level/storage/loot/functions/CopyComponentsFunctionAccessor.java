package net.errorcraft.itematic.mixin.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.world.level.storage.loot.LootContextArg;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CopyComponentsFunction.class)
public interface CopyComponentsFunctionAccessor {
    @Accessor("GETTER_CODEC")
    static Codec<LootContextArg<DataComponentGetter>> getterCodec() {
        throw new AssertionError();
    }
}
