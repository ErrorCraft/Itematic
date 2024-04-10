package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.MilkBucketItem;
import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MilkBucketItem.class)
public interface MilkBucketItemAccessor {
    @Accessor("MAX_USE_TIME")
    @Contract
    static int getMaxUseTime() {
        throw new AssertionError();
    }
}
