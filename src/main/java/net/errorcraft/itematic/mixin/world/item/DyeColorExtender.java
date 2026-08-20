package net.errorcraft.itematic.mixin.world.item;

import net.errorcraft.itematic.access.world.item.DyeColorAccess;
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
    private ResourceKey<Item> item;

    static {
        WHITE.itematic$setItemId(ItemIds.WHITE_DYE);
        ORANGE.itematic$setItemId(ItemIds.ORANGE_DYE);
        MAGENTA.itematic$setItemId(ItemIds.MAGENTA_DYE);
        LIGHT_BLUE.itematic$setItemId(ItemIds.LIGHT_BLUE_DYE);
        YELLOW.itematic$setItemId(ItemIds.YELLOW_DYE);
        LIME.itematic$setItemId(ItemIds.LIME_DYE);
        PINK.itematic$setItemId(ItemIds.PINK_DYE);
        GRAY.itematic$setItemId(ItemIds.GRAY_DYE);
        LIGHT_GRAY.itematic$setItemId(ItemIds.LIGHT_GRAY_DYE);
        CYAN.itematic$setItemId(ItemIds.CYAN_DYE);
        PURPLE.itematic$setItemId(ItemIds.PURPLE_DYE);
        BLUE.itematic$setItemId(ItemIds.BLUE_DYE);
        BROWN.itematic$setItemId(ItemIds.BROWN_DYE);
        GREEN.itematic$setItemId(ItemIds.GREEN_DYE);
        RED.itematic$setItemId(ItemIds.RED_DYE);
        BLACK.itematic$setItemId(ItemIds.BLACK_DYE);
    }

    @Redirect(
        method = "getMixedColor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/DyeColor;makeCraftColorInput(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/DyeColor;)Lnet/minecraft/world/item/crafting/CraftingInput;"
        )
    )
    private static CraftingInput newItemStackForCraftingInputUseCreateStack(DyeColor firstColor, DyeColor secondColor, ServerLevel level) {
        return CraftingInput.of(
            2,
            1,
            List.of(
                level.itematic$createStack(firstColor.itematic$itemId()),
                level.itematic$createStack(secondColor.itematic$itemId())
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
    public ResourceKey<Item> itematic$itemId() {
        return this.item;
    }

    @Override
    public void itematic$setItemId(ResourceKey<Item> item) {
        this.item = item;
    }
}
