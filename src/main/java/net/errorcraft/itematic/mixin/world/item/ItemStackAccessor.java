package net.errorcraft.itematic.mixin.world.item;

import com.mojang.serialization.DataResult;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemStack.class)
public interface ItemStackAccessor {
    @Invoker("validateComponents")
    static DataResult<?> validateComponents(final DataComponentMap components) {
        throw new AssertionError();
    }
}
