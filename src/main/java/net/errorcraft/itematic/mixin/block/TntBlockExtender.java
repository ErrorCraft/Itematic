package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(TntBlock.class)
public class TntBlockExtender {
    @Redirect(
        method = "onUseWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"
            )
        )
    )
    private boolean isOfForFlintAndSteelUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.FLINT_AND_STEEL);
    }

    @ModifyExpressionValue(
        method = "primeTnt(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/LivingEntity;)Z",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/World;DDDLnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/TntEntity;"
        )
    )
    private static TntEntity setBlockState(TntEntity original, World world, BlockPos pos) {
        original.setBlockState(world.getBlockState(pos));
        return original;
    }

    @Redirect(
        method = "primeTnt(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/LivingEntity;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"
        )
    )
    private static void doNotPlaySound(World instance, Entity source, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {}
}
