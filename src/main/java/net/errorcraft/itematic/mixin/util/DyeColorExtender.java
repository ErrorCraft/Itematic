package net.errorcraft.itematic.mixin.util;

import net.errorcraft.itematic.access.util.DyeColorAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.DyeItemBehavior;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(DyeColor.class)
public class DyeColorExtender implements DyeColorAccess {
    @Shadow
    @Final
    public static DyeColor WHITE;

    @Shadow
    @Final
    public static DyeColor ORANGE;

    @Shadow
    @Final
    public static DyeColor MAGENTA;

    @Shadow
    @Final
    public static DyeColor LIGHT_BLUE;

    @Shadow
    @Final
    public static DyeColor YELLOW;

    @Shadow
    @Final
    public static DyeColor LIME;

    @Shadow
    @Final
    public static DyeColor PINK;

    @Shadow
    @Final
    public static DyeColor GRAY;

    @Shadow
    @Final
    public static DyeColor LIGHT_GRAY;

    @Shadow
    @Final
    public static DyeColor CYAN;

    @Shadow
    @Final
    public static DyeColor PURPLE;

    @Shadow
    @Final
    public static DyeColor BLUE;

    @Shadow
    @Final
    public static DyeColor BROWN;

    @Shadow
    @Final
    public static DyeColor GREEN;

    @Shadow
    @Final
    public static DyeColor RED;

    @Shadow
    @Final
    public static DyeColor BLACK;

    @Unique
    private ResourceKey<Item> itemKey;

    static {
        WHITE.itematic$setItemKey(ItemIds.WHITE_DYE);
        ORANGE.itematic$setItemKey(ItemIds.ORANGE_DYE);
        MAGENTA.itematic$setItemKey(ItemIds.MAGENTA_DYE);
        LIGHT_BLUE.itematic$setItemKey(ItemIds.LIGHT_BLUE_DYE);
        YELLOW.itematic$setItemKey(ItemIds.YELLOW_DYE);
        LIME.itematic$setItemKey(ItemIds.LIME_DYE);
        PINK.itematic$setItemKey(ItemIds.PINK_DYE);
        GRAY.itematic$setItemKey(ItemIds.GRAY_DYE);
        LIGHT_GRAY.itematic$setItemKey(ItemIds.LIGHT_GRAY_DYE);
        CYAN.itematic$setItemKey(ItemIds.CYAN_DYE);
        PURPLE.itematic$setItemKey(ItemIds.PURPLE_DYE);
        BLUE.itematic$setItemKey(ItemIds.BLUE_DYE);
        BROWN.itematic$setItemKey(ItemIds.BROWN_DYE);
        GREEN.itematic$setItemKey(ItemIds.GREEN_DYE);
        RED.itematic$setItemKey(ItemIds.RED_DYE);
        BLACK.itematic$setItemKey(ItemIds.BLACK_DYE);
    }

    @Redirect(
        method = "getMixedColor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/DyeColor;makeCraftColorInput(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/DyeColor;)Lnet/minecraft/world/item/crafting/CraftingInput;"
        )
    )
    private static CraftingInput newItemStackForRecipeInputUseCreateStack(DyeColor firstColor, DyeColor secondColor, ServerLevel world) {
        return CraftingInput.of(
            2,
            1,
            List.of(
                world.itematic$createStack(firstColor.itematic$itemKey()),
                world.itematic$createStack(secondColor.itematic$itemKey())
            )
        );
    }

    @Redirect(
        method = "getMixedColor",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;filter(Ljava/util/function/Predicate;)Ljava/util/Optional;"
        )
    )
    private static Optional<DyeItemBehavior> instanceOfDyeItemUseItemBehavior(Optional<Item> instance, Predicate<? super Object> predicate) {
        return instance.flatMap(item -> item.itematic$getBehavior(ItemBehaviorType.DYE));
    }

    @Redirect(
        method = "getMixedColor",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;map(Ljava/util/function/Function;)Ljava/util/Optional;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Ljava/util/Optional;filter(Ljava/util/function/Predicate;)Ljava/util/Optional;"
            )
        )
    )
    private static Optional<DyeItemBehavior> castToDyeItemDoNothing(Optional<DyeItemBehavior> instance, Function<? super Item, ? extends DyeItem> mapper) {
        return instance;
    }

    @ModifyArg(
        method = "getMixedColor",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;map(Ljava/util/function/Function;)Ljava/util/Optional;",
            ordinal = 1
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Ljava/util/Optional;filter(Ljava/util/function/Predicate;)Ljava/util/Optional;"
            )
        )
    )
    private static Function<? super DyeItemBehavior, ? extends DyeColor> getColorUseItemBehavior(Function<? super DyeItem, ? extends DyeColor> mapper) {
        return DyeItemBehavior::color;
    }

    @Override
    public ResourceKey<Item> itematic$itemKey() {
        return this.itemKey;
    }

    @Override
    public void itematic$setItemKey(ResourceKey<Item> item) {
        this.itemKey = item;
    }
}
