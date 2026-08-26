package net.errorcraft.itematic.mixin.core.cauldron;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.alchemy.PotionContentsUtil;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.BlockItemBehavior;
import net.errorcraft.itematic.world.item.placement.block.picker.BlockPicker;
import net.minecraft.core.Holder;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CauldronInteractions.class)
public class CauldronInteractionsExtender {
    @Redirect(
        method = "shulkerBoxInteraction",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;byItem(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/level/block/Block;"
        )
    )
    private static @Nullable Block getBlockFromItemUseItemBehavior(Item item) {
        return item.itematic$getBehavior(ItemBehaviorType.BLOCK)
            .map(BlockItemBehavior::block)
            .map(BlockPicker::defaultBlock)
            .map(Holder::value)
            .orElse(null);
    }

    @Redirect(
        method = "shulkerBoxInteraction",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;transmuteCopy(Lnet/minecraft/world/level/ItemLike;I)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack transmuteCopyForShulkerBoxUseHolder(ItemStack instance, ItemLike newItem, int newCount, @Local(name = "level", argsOnly = true) Level level) {
        return instance.itematic$transmuteCopy(
            level.itematic$getItem(ItemIds.SHULKER_BOX),
            newCount
        );
    }

    @Redirect(
        method = {
            "lambda$bootStrap$0",
            "lambda$bootStrap$4"
        },
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForGlassBottleUseCreateStack(ItemLike item, @Local(name = "level", argsOnly = true) Level level) {
        return level.itematic$createStack(ItemIds.GLASS_BOTTLE);
    }

    @Redirect(
        method = "lambda$bootStrap$3",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/alchemy/PotionContents;createItemStack(Lnet/minecraft/world/item/Item;Lnet/minecraft/core/Holder;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForPotionUseCreateStack(Item item, Holder<Potion> potion, @Local(name = "level", argsOnly = true) Level level) {
        return PotionContentsUtil.setPotion(
            level.itematic$createStack(ItemIds.POTION),
            potion
        );
    }

    @Redirect(
        method = "lambda$bootStrap$1",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForWaterBucketUseCreateStack(ItemLike item, @Local(name = "level", argsOnly = true) Level level) {
        return level.itematic$createStack(ItemIds.WATER_BUCKET);
    }

    @Redirect(
        method = "lambda$bootStrap$5",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForLavaBucketUseCreateStack(ItemLike item, @Local(name = "level", argsOnly = true) Level level) {
        return level.itematic$createStack(ItemIds.LAVA_BUCKET);
    }

    @Redirect(
        method = "lambda$bootStrap$7",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForPowderSnowBucketUseCreateStack(ItemLike item, @Local(name = "level", argsOnly = true) Level level) {
        return level.itematic$createStack(ItemIds.POWDER_SNOW_BUCKET);
    }

    @Redirect(
        method = "emptyBucket",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForBucketUseCreateStack(ItemLike item, @Local(name = "level", argsOnly = true) Level level) {
        return level.itematic$createStack(ItemIds.BUCKET);
    }

    @Redirect(
        method = {
            "lambda$bootStrap$0",
            "lambda$bootStrap$3",
            "lambda$bootStrap$4",
            "fillBucket",
            "emptyBucket"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;"
        )
    )
    private static <T> Stat<Item> getStatUseHolder(StatType<Item> instance, T argument, @Local(name = "itemInHand", argsOnly = true) ItemStack itemInHand) {
        return instance.itematic$get(itemInHand.typeHolder());
    }
}
