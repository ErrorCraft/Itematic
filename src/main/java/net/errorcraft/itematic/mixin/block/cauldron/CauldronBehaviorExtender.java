package net.errorcraft.itematic.mixin.block.cauldron;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BlockItemComponent;
import net.errorcraft.itematic.item.component.components.DyeableItemComponent;
import net.errorcraft.itematic.item.placement.block.picker.BlockPicker;
import net.minecraft.core.Holder;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(CauldronInteraction.class)
public interface CauldronBehaviorExtender {
    @Redirect(
        method = "shulkerBoxInteraction",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;byItem(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/level/block/Block;"
        )
    )
    private static Block getBlockFromItemUseItemComponent(Item item) {
        return item.itematic$getBehavior(ItemComponentTypes.BLOCK)
            .map(BlockItemComponent::block)
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
    private static ItemStack copyComponentsToNewStackForShulkerBoxUseRegistryEntry(ItemStack instance, ItemLike itemConvertible, int count, @Local(argsOnly = true) Level world) {
        return instance.itematic$copyComponentsToNewStack(world.itematic$getItem(ItemKeys.SHULKER_BOX), count);
    }

    @Redirect(
        method = "dyedItemIteration",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"
        )
    )
    private static boolean isInForDyeableItemUseItemComponentCheck(ItemStack instance, TagKey<Item> tag, @Share("dyeableItemComponent") LocalRef<DyeableItemComponent> dyeableItemComponent) {
        return instance.itematic$hasBehavior(ItemComponentTypes.DYEABLE);
    }

    @Redirect(
        method = {
            "method_32219",
            "method_32222"
        },
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForGlassBottleUseCreateStack(ItemLike item, @Local(argsOnly = true) Level world) {
        return world.itematic$createStack(ItemKeys.GLASS_BOTTLE);
    }

    @Redirect(
        method = "method_32220",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/alchemy/PotionContents;createItemStack(Lnet/minecraft/world/item/Item;Lnet/minecraft/core/Holder;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForPotionUseCreateStack(Item item, Holder<Potion> potion, @Local(argsOnly = true) Level world) {
        return PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), potion);
    }

    @Redirect(
        method = "method_32221",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForWaterBucketUseCreateStack(ItemLike item, @Local(argsOnly = true) Level world) {
        return world.itematic$createStack(ItemKeys.WATER_BUCKET);
    }

    @Redirect(
        method = "method_32218",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForLavaBucketUseCreateStack(ItemLike item, @Local(argsOnly = true) Level world) {
        return world.itematic$createStack(ItemKeys.LAVA_BUCKET);
    }

    @Redirect(
        method = "method_32698",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForPowderSnowBucketUseCreateStack(ItemLike item, @Local(argsOnly = true) Level world) {
        return world.itematic$createStack(ItemKeys.POWDER_SNOW_BUCKET);
    }

    @Redirect(
        method = "emptyBucket",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForBucketUseCreateStack(ItemLike item, @Local(argsOnly = true) Level world) {
        return world.itematic$createStack(ItemKeys.BUCKET);
    }

    @ModifyArg(
        method = "addDefaultInteractions",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        index = 0
    )
    private static Object lavaBucketUseRegistryKey(Object key) {
        return ItemKeys.LAVA_BUCKET;
    }

    @ModifyArg(
        method = "addDefaultInteractions",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;WATER_BUCKET:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object waterBucketUseRegistryKey(Object key) {
        return ItemKeys.WATER_BUCKET;
    }

    @ModifyArg(
        method = "addDefaultInteractions",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;POWDER_SNOW_BUCKET:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object powderSnowBucketUseRegistryKey(Object key) {
        return ItemKeys.POWDER_SNOW_BUCKET;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;POTION:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object emptyCauldronPotionUseRegistryKey(Object key) {
        return ItemKeys.POTION;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;POTION:Lnet/minecraft/world/item/Item;",
                ordinal = 1
            )
        ),
        index = 0
    )
    private static Object waterCauldronPotionUseRegistryKey(Object key) {
        return ItemKeys.POTION;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;BUCKET:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object waterCauldronBucketUseRegistryKey(Object key) {
        return ItemKeys.BUCKET;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/core/cauldron/CauldronInteraction;LAVA:Lnet/minecraft/core/cauldron/CauldronInteraction$InteractionMap;"
            )
        ),
        index = 0
    )
    private static Object lavaCauldronBucketUseRegistryKey(Object key) {
        return ItemKeys.BUCKET;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/core/cauldron/CauldronInteraction;POWDER_SNOW:Lnet/minecraft/core/cauldron/CauldronInteraction$InteractionMap;"
            )
        ),
        index = 0
    )
    private static Object powderSnowCauldronBucketUseRegistryKey(Object key) {
        return ItemKeys.BUCKET;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;GLASS_BOTTLE:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object glassBottleUseRegistryKey(Object key) {
        return ItemKeys.GLASS_BOTTLE;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;LEATHER_BOOTS:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object leatherBootsUseRegistryKey(Object key) {
        return ItemKeys.LEATHER_BOOTS;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;LEATHER_LEGGINGS:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object leatherLeggingsUseRegistryKey(Object key) {
        return ItemKeys.LEATHER_LEGGINGS;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;LEATHER_CHESTPLATE:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object leatherChestplateUseRegistryKey(Object key) {
        return ItemKeys.LEATHER_CHESTPLATE;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;LEATHER_HELMET:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object leatherHelmetUseRegistryKey(Object key) {
        return ItemKeys.LEATHER_HELMET;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;LEATHER_HORSE_ARMOR:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object leatherHorseArmorUseRegistryKey(Object key) {
        return ItemKeys.LEATHER_HORSE_ARMOR;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;WOLF_ARMOR:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object wolfArmorUseRegistryKey(Object key) {
        return ItemKeys.WOLF_ARMOR;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;WHITE_BANNER:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object whiteBannerUseRegistryKey(Object key) {
        return ItemKeys.WHITE_BANNER;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;GRAY_BANNER:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object grayBannerUseRegistryKey(Object key) {
        return ItemKeys.GRAY_BANNER;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;BLACK_BANNER:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object blackBannerUseRegistryKey(Object key) {
        return ItemKeys.BLACK_BANNER;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;BLUE_BANNER:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object blueBannerUseRegistryKey(Object key) {
        return ItemKeys.BLUE_BANNER;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;BROWN_BANNER:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object brownBannerUseRegistryKey(Object key) {
        return ItemKeys.BROWN_BANNER;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;CYAN_BANNER:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object cyanBannerUseRegistryKey(Object key) {
        return ItemKeys.CYAN_BANNER;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;GREEN_BANNER:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object greenBannerUseRegistryKey(Object key) {
        return ItemKeys.GREEN_BANNER;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;LIGHT_BLUE_BANNER:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object lightBlueBannerUseRegistryKey(Object key) {
        return ItemKeys.LIGHT_BLUE_BANNER;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;LIGHT_GRAY_BANNER:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object lightGrayBannerUseRegistryKey(Object key) {
        return ItemKeys.LIGHT_GRAY_BANNER;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;LIME_BANNER:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object limeBannerUseRegistryKey(Object key) {
        return ItemKeys.LIME_BANNER;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;MAGENTA_BANNER:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object magentaBannerUseRegistryKey(Object key) {
        return ItemKeys.MAGENTA_BANNER;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;ORANGE_BANNER:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object orangeBannerUseRegistryKey(Object key) {
        return ItemKeys.ORANGE_BANNER;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;PINK_BANNER:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object pinkBannerUseRegistryKey(Object key) {
        return ItemKeys.PINK_BANNER;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;PURPLE_BANNER:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object purpleBannerUseRegistryKey(Object key) {
        return ItemKeys.PURPLE_BANNER;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;RED_BANNER:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object redBannerUseRegistryKey(Object key) {
        return ItemKeys.RED_BANNER;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;YELLOW_BANNER:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object yellowBannerUseRegistryKey(Object key) {
        return ItemKeys.YELLOW_BANNER;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;WHITE_SHULKER_BOX:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object whiteShulkerBoxUseRegistryKey(Object key) {
        return ItemKeys.WHITE_SHULKER_BOX;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;GRAY_SHULKER_BOX:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object grayShulkerBoxUseRegistryKey(Object key) {
        return ItemKeys.GRAY_SHULKER_BOX;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;BLACK_SHULKER_BOX:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object blackShulkerBoxUseRegistryKey(Object key) {
        return ItemKeys.BLACK_SHULKER_BOX;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;BLUE_SHULKER_BOX:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object blueShulkerBoxUseRegistryKey(Object key) {
        return ItemKeys.BLUE_SHULKER_BOX;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;BROWN_SHULKER_BOX:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object brownShulkerBoxUseRegistryKey(Object key) {
        return ItemKeys.BROWN_SHULKER_BOX;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;CYAN_SHULKER_BOX:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object cyanShulkerBoxUseRegistryKey(Object key) {
        return ItemKeys.CYAN_SHULKER_BOX;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;GREEN_SHULKER_BOX:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object greenShulkerBoxUseRegistryKey(Object key) {
        return ItemKeys.GREEN_SHULKER_BOX;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;LIGHT_BLUE_SHULKER_BOX:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object lightBlueShulkerBoxUseRegistryKey(Object key) {
        return ItemKeys.LIGHT_BLUE_SHULKER_BOX;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;LIGHT_GRAY_SHULKER_BOX:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object lightGrayShulkerBoxUseRegistryKey(Object key) {
        return ItemKeys.LIGHT_GRAY_SHULKER_BOX;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;LIME_SHULKER_BOX:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object limeShulkerBoxUseRegistryKey(Object key) {
        return ItemKeys.LIME_SHULKER_BOX;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;MAGENTA_SHULKER_BOX:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object magentaShulkerBoxUseRegistryKey(Object key) {
        return ItemKeys.MAGENTA_SHULKER_BOX;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;ORANGE_SHULKER_BOX:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object orangeShulkerBoxUseRegistryKey(Object key) {
        return ItemKeys.ORANGE_SHULKER_BOX;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;PINK_SHULKER_BOX:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object pinkShulkerBoxUseRegistryKey(Object key) {
        return ItemKeys.PINK_SHULKER_BOX;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;PURPLE_SHULKER_BOX:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object purpleShulkerBoxUseRegistryKey(Object key) {
        return ItemKeys.PURPLE_SHULKER_BOX;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;RED_SHULKER_BOX:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object redShulkerBoxUseRegistryKey(Object key) {
        return ItemKeys.RED_SHULKER_BOX;
    }

    @ModifyArg(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;YELLOW_SHULKER_BOX:Lnet/minecraft/world/item/Item;"
            )
        ),
        index = 0
    )
    private static Object yellowShulkerBoxUseRegistryKey(Object key) {
        return ItemKeys.YELLOW_SHULKER_BOX;
    }

    @Redirect(
        method = {
            "method_32219",
            "method_32220",
            "method_32222",
            "fillBucket",
            "emptyBucket"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;"
        )
    )
    private static <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key, @Local(argsOnly = true, ordinal = 0) ItemStack stack) {
        return instance.itematic$getOrCreateStat(stack.getItemHolder());
    }
}
