package net.errorcraft.itematic.mixin.world.level.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.world.level.block.state.BlockBehaviourAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.PotDecorations;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.List;

@Mixin(DecoratedPotBlock.class)
public class DecoratedPotBlockExtender implements BlockBehaviourAccess {
    @Redirect(
        method = "lambda$getDrops$0",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/PotDecorations;ordered()Ljava/util/List;"
        )
    )
    @SuppressWarnings("DataFlowIssue")
    private static List<Holder<Item>> getDecorationsUseHolders(PotDecorations instance, DecoratedPotBlockEntity entity) {
        return instance.itematic$entries(entity.getLevel().registryAccess());
    }

    @Redirect(
        method = "lambda$getDrops$0",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Iterator;next()Ljava/lang/Object;"
        )
    )
    @Nullable
    private static <E> E nextItemReturnNull(Iterator<E> instance) {
        return null;
    }

    @Redirect(
        method = "lambda$getDrops$0",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;getDefaultInstance()Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackUseHolder(Item instance, @Local(name = "i$") Iterator<Holder<Item>> i$) {
        return new ItemStack(i$.next());
    }

    @Redirect(
        method = "getCloneItemStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/DecoratedPotBlockEntity;createDecoratedPotItem(Lnet/minecraft/world/level/block/entity/PotDecorations;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack getStackWithUseCreateStack(PotDecorations decorations, LevelReader level) {
        ItemStack stack = level.itematic$createStack(ItemIds.DECORATED_POT);
        stack.set(DataComponents.POT_DECORATIONS, decorations);
        return stack;
    }

    @Override
    public void itematic$addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.POT_DECORATIONS, PotDecorations.EMPTY);
    }
}
