package net.errorcraft.itematic.mixin.world.level.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractCauldronBlock.class)
public abstract class AbstractCauldronBlockExtender {
    @ModifyArg(
        method = "useItemOn",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
        )
    )
    private Object useItemId(Object key, @Local(name = "itemStack", argsOnly = true) ItemStack itemStack) {
        return itemStack.itematic$key();
    }
}
