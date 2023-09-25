package net.errorcraft.itematic.mixin.block.cauldron;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.DyeableItemComponent;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;

@Mixin(CauldronBehavior.class)
public interface CauldronBehaviorExtender {
    @ModifyConstant(
        method = "method_32209",
        constant = @Constant(
            classValue = DyeableItem.class,
            ordinal = 0
        )
    )
    private static boolean instanceOfDyeableItemUseItemComponentCheck(Object reference, Class<DyeableItem> clazz, @Local ItemStack stack, @Share("dyeableItemComponent") LocalRef<DyeableItemComponent> dyeableItemComponent) {
        Optional<DyeableItemComponent> optionalComponent = stack.getComponent(ItemComponentTypes.DYEABLE);
        optionalComponent.ifPresent(dyeableItemComponent::set);
        return optionalComponent.isPresent();
    }

    @ModifyVariable(
        method = "method_32209",
        at = @At("LOAD")
    )
    private static Item castToDyeableItemUseNull(Item value) {
        return null;
    }

    @ModifyVariable(
        method = "method_32209",
        at = @At("LOAD")
    )
    private static DyeableItem getDyeableItemUseItemComponent(DyeableItem value, @Share("dyeableItemComponent") LocalRef<DyeableItemComponent> dyeableItemComponent) {
        return dyeableItemComponent.get();
    }

    @Redirect(
        method = { "method_32219", "method_32222"},
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private static ItemStack newItemStackForGlassBottleUseRegistryEntry(ItemConvertible item, @Local World world) {
        return new ItemStack(world.getItem(ItemKeys.GLASS_BOTTLE));
    }

    @Redirect(
        method = "method_32220",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private static ItemStack newItemStackForPotionUseRegistryEntry(ItemConvertible item, @Local World world) {
        return new ItemStack(world.getItem(ItemKeys.POTION));
    }

    @Redirect(
        method = "method_32221",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private static ItemStack newItemStackForWaterBucketUseRegistryEntry(ItemConvertible item, @Local World world) {
        return new ItemStack(world.getItem(ItemKeys.WATER_BUCKET));
    }

    @Redirect(
        method = "method_32218",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private static ItemStack newItemStackForLavaBucketUseRegistryEntry(ItemConvertible item, @Local World world) {
        return new ItemStack(world.getItem(ItemKeys.LAVA_BUCKET));
    }

    @Redirect(
        method = "method_32698",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private static ItemStack newItemStackForPowderSnowBucketUseRegistryEntry(ItemConvertible item, @Local World world) {
        return new ItemStack(world.getItem(ItemKeys.POWDER_SNOW_BUCKET));
    }

    @Redirect(
        method = "fillCauldron",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private static ItemStack newItemStackForBucketUseRegistryEntry(ItemConvertible item, @Local World world) {
        return new ItemStack(world.getItem(ItemKeys.BUCKET));
    }

    @ModifyArg(
        method = "registerBucketBehavior",
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
        method = "registerBucketBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;WATER_BUCKET:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object waterBucketUseRegistryKey(Object key) {
        return ItemKeys.WATER_BUCKET;
    }

    @ModifyArg(
        method = "registerBucketBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;POWDER_SNOW_BUCKET:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object powderSnowBucketUseRegistryKey(Object key) {
        return ItemKeys.POWDER_SNOW_BUCKET;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;POTION:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object emptyCauldronPotionUseRegistryKey(Object key) {
        return ItemKeys.POTION;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;POTION:Lnet/minecraft/item/Item;",
                ordinal = 1
            )
        ),
        index = 0
    )
    private static Object waterCauldronPotionUseRegistryKey(Object key) {
        return ItemKeys.POTION;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;BUCKET:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object waterCauldronBucketUseRegistryKey(Object key) {
        return ItemKeys.BUCKET;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/block/cauldron/CauldronBehavior;LAVA_CAULDRON_BEHAVIOR:Ljava/util/Map;"
            )
        ),
        index = 0
    )
    private static Object lavaCauldronBucketUseRegistryKey(Object key) {
        return ItemKeys.BUCKET;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/block/cauldron/CauldronBehavior;POWDER_SNOW_CAULDRON_BEHAVIOR:Ljava/util/Map;"
            )
        ),
        index = 0
    )
    private static Object powderSnowCauldronBucketUseRegistryKey(Object key) {
        return ItemKeys.BUCKET;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;GLASS_BOTTLE:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object glassBottleUseRegistryKey(Object key) {
        return ItemKeys.GLASS_BOTTLE;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;LEATHER_BOOTS:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object leatherBootsUseRegistryKey(Object key) {
        return ItemKeys.LEATHER_BOOTS;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;LEATHER_LEGGINGS:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object leatherLeggingsUseRegistryKey(Object key) {
        return ItemKeys.LEATHER_LEGGINGS;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;LEATHER_CHESTPLATE:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object leatherChestplateUseRegistryKey(Object key) {
        return ItemKeys.LEATHER_CHESTPLATE;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;LEATHER_HELMET:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object leatherHelmetUseRegistryKey(Object key) {
        return ItemKeys.LEATHER_HELMET;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;WHITE_BANNER:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object whiteBannerUseRegistryKey(Object key) {
        return ItemKeys.WHITE_BANNER;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;GRAY_BANNER:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object grayBannerUseRegistryKey(Object key) {
        return ItemKeys.GRAY_BANNER;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;BLACK_BANNER:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object blackBannerUseRegistryKey(Object key) {
        return ItemKeys.BLACK_BANNER;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;BLUE_BANNER:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object blueBannerUseRegistryKey(Object key) {
        return ItemKeys.BLUE_BANNER;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;BROWN_BANNER:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object brownBannerUseRegistryKey(Object key) {
        return ItemKeys.BROWN_BANNER;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;CYAN_BANNER:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object cyanBannerUseRegistryKey(Object key) {
        return ItemKeys.CYAN_BANNER;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;GREEN_BANNER:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object greenBannerUseRegistryKey(Object key) {
        return ItemKeys.GREEN_BANNER;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;LIGHT_BLUE_BANNER:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object lightBlueBannerUseRegistryKey(Object key) {
        return ItemKeys.LIGHT_BLUE_BANNER;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;LIGHT_GRAY_BANNER:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object lightGrayBannerUseRegistryKey(Object key) {
        return ItemKeys.LIGHT_GRAY_BANNER;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;LIME_BANNER:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object limeBannerUseRegistryKey(Object key) {
        return ItemKeys.LIME_BANNER;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;MAGENTA_BANNER:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object magentaBannerUseRegistryKey(Object key) {
        return ItemKeys.MAGENTA_BANNER;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;ORANGE_BANNER:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object orangeBannerUseRegistryKey(Object key) {
        return ItemKeys.ORANGE_BANNER;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;PINK_BANNER:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object pinkBannerUseRegistryKey(Object key) {
        return ItemKeys.PINK_BANNER;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;PURPLE_BANNER:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object purpleBannerUseRegistryKey(Object key) {
        return ItemKeys.PURPLE_BANNER;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;RED_BANNER:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object redBannerUseRegistryKey(Object key) {
        return ItemKeys.RED_BANNER;
    }

    @ModifyArg(
        method = "registerBehavior",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;YELLOW_BANNER:Lnet/minecraft/item/Item;"
            )
        ),
        index = 0
    )
    private static Object yellowBannerUseRegistryKey(Object key) {
        return ItemKeys.YELLOW_BANNER;
    }
}
