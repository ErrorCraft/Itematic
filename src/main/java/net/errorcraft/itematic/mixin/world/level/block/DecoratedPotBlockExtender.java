package net.errorcraft.itematic.mixin.world.level.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.access.world.level.block.state.BlockBehaviourAccess;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.references.BlockItemIds;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.entity.PotDecorations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DecoratedPotBlock.class)
public class DecoratedPotBlockExtender implements BlockBehaviourAccess {
    @WrapOperation(
        method = "getCloneItemStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/DecoratedPotBlockEntity;createDecoratedPotInstance(Lnet/minecraft/world/level/block/entity/PotDecorations;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack createDecoratedPotInstanceUseCreateStack(PotDecorations decorations, Operation<ItemStack> original, LevelReader level) {
        ItemStack stack = level.itematic$createStack(BlockItemIds.DECORATED_POT.item());
        stack.set(DataComponents.POT_DECORATIONS, decorations);
        return stack;
    }

    @Override
    public void itematic$addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.POT_DECORATIONS, PotDecorations.EMPTY);
    }
}
