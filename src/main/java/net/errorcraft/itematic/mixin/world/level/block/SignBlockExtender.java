package net.errorcraft.itematic.mixin.world.level.block;

import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignApplicator;
import net.minecraft.world.level.block.SignBlock;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SignBlock.class)
public class SignBlockExtender {
    @ModifyVariable(
        method = "useItemOn",
        at = @At("STORE"),
        name = "signApplicator"
    )
    @Nullable
    private static SignApplicator useDataComponent(SignApplicator signApplicator, ItemStack itemStack) {
        return itemStack.get(ItematicDataComponents.SIGN_MODIFIER);
    }
}
