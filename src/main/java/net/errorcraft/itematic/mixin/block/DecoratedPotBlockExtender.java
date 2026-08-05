package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.block.AbstractBlockAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.PotDecorations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.List;

@Mixin(DecoratedPotBlock.class)
public class DecoratedPotBlockExtender implements AbstractBlockAccess {
    @Redirect(
        method = "method_49815",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/PotDecorations;ordered()Ljava/util/List;"
        )
    )
    @SuppressWarnings("DataFlowIssue")
    private static List<Holder<Item>> streamSherdsUseRegistryEntry(PotDecorations instance, DecoratedPotBlockEntity blockEntity) {
        return instance.itematic$entries(blockEntity.getLevel().registryAccess());
    }

    @Redirect(
        method = "method_49815",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Iterator;next()Ljava/lang/Object;"
        )
    )
    private static <E> E nextItemReturnNull(Iterator<E> instance) {
        return null;
    }

    @Redirect(
        method = "method_49815",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;getDefaultInstance()Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackUseRegistryEntry(Item instance, @Local Iterator<Holder<Item>> iterator) {
        return new ItemStack(iterator.next());
    }

    @Redirect(
        method = "getCloneItemStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/DecoratedPotBlockEntity;createDecoratedPotItem(Lnet/minecraft/world/level/block/entity/PotDecorations;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack getStackWithUseCreateStack(PotDecorations sherds, LevelReader world) {
        ItemStack stack = world.itematic$createStack(ItemKeys.DECORATED_POT);
        stack.set(DataComponents.POT_DECORATIONS, sherds);
        return stack;
    }

    @Override
    public void itematic$addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.POT_DECORATIONS, PotDecorations.EMPTY);
    }
}
