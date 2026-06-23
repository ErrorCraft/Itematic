package net.errorcraft.itematic.mixin.util;

import net.errorcraft.itematic.access.util.DyeColorAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.DyeItemComponent;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
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
    private RegistryKey<Item> itemKey;

    static {
        WHITE.itematic$setItemKey(ItemKeys.WHITE_DYE);
        ORANGE.itematic$setItemKey(ItemKeys.ORANGE_DYE);
        MAGENTA.itematic$setItemKey(ItemKeys.MAGENTA_DYE);
        LIGHT_BLUE.itematic$setItemKey(ItemKeys.LIGHT_BLUE_DYE);
        YELLOW.itematic$setItemKey(ItemKeys.YELLOW_DYE);
        LIME.itematic$setItemKey(ItemKeys.LIME_DYE);
        PINK.itematic$setItemKey(ItemKeys.PINK_DYE);
        GRAY.itematic$setItemKey(ItemKeys.GRAY_DYE);
        LIGHT_GRAY.itematic$setItemKey(ItemKeys.LIGHT_GRAY_DYE);
        CYAN.itematic$setItemKey(ItemKeys.CYAN_DYE);
        PURPLE.itematic$setItemKey(ItemKeys.PURPLE_DYE);
        BLUE.itematic$setItemKey(ItemKeys.BLUE_DYE);
        BROWN.itematic$setItemKey(ItemKeys.BROWN_DYE);
        GREEN.itematic$setItemKey(ItemKeys.GREEN_DYE);
        RED.itematic$setItemKey(ItemKeys.RED_DYE);
        BLACK.itematic$setItemKey(ItemKeys.BLACK_DYE);
    }

    @Redirect(
        method = "mixColors",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/DyeColor;createColorMixingRecipeInput(Lnet/minecraft/util/DyeColor;Lnet/minecraft/util/DyeColor;)Lnet/minecraft/recipe/input/CraftingRecipeInput;"
        )
    )
    private static CraftingRecipeInput newItemStackForRecipeInputUseCreateStack(DyeColor firstColor, DyeColor secondColor, ServerWorld world) {
        return CraftingRecipeInput.create(
            2,
            1,
            List.of(
                world.itematic$createStack(firstColor.itematic$itemKey()),
                world.itematic$createStack(secondColor.itematic$itemKey())
            )
        );
    }

    @Redirect(
        method = "mixColors",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;filter(Ljava/util/function/Predicate;)Ljava/util/Optional;"
        )
    )
    private static Optional<DyeItemComponent> instanceOfDyeItemUseItemComponent(Optional<Item> instance, Predicate<? super Object> predicate) {
        return instance.flatMap(item -> item.itematic$getBehavior(ItemComponentTypes.DYE));
    }

    @Redirect(
        method = "mixColors",
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
    private static Optional<DyeItemComponent> castToDyeItemDoNothing(Optional<DyeItemComponent> instance, Function<? super Item, ? extends DyeItem> mapper) {
        return instance;
    }

    @ModifyArg(
        method = "mixColors",
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
    private static Function<? super DyeItemComponent, ? extends DyeColor> getColorUseItemComponent(Function<? super DyeItem, ? extends DyeColor> mapper) {
        return DyeItemComponent::color;
    }

    @Override
    public RegistryKey<Item> itematic$itemKey() {
        return this.itemKey;
    }

    @Override
    public void itematic$setItemKey(RegistryKey<Item> item) {
        this.itemKey = item;
    }
}
