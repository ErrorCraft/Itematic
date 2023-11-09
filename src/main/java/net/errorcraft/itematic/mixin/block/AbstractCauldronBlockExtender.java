package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractCauldronBlock.class)
public abstract class AbstractCauldronBlockExtender {
    @ModifyArg(
        method = "onUse",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
        )
    )
    private Object useRegistryKey(Object key, @Local ItemStack stack) {
        return stack.itematic$key();
    }
}
