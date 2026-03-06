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
@SuppressWarnings("DataFlowIssue")
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
        ((DyeColorExtender)(Object) WHITE).itemKey = ItemKeys.WHITE_DYE;
        ((DyeColorExtender)(Object) ORANGE).itemKey = ItemKeys.ORANGE_DYE;
        ((DyeColorExtender)(Object) MAGENTA).itemKey = ItemKeys.MAGENTA_DYE;
        ((DyeColorExtender)(Object) LIGHT_BLUE).itemKey = ItemKeys.LIGHT_BLUE_DYE;
        ((DyeColorExtender)(Object) YELLOW).itemKey = ItemKeys.YELLOW_DYE;
        ((DyeColorExtender)(Object) LIME).itemKey = ItemKeys.LIME_DYE;
        ((DyeColorExtender)(Object) PINK).itemKey = ItemKeys.PINK_DYE;
        ((DyeColorExtender)(Object) GRAY).itemKey = ItemKeys.GRAY_DYE;
        ((DyeColorExtender)(Object) LIGHT_GRAY).itemKey = ItemKeys.LIGHT_GRAY_DYE;
        ((DyeColorExtender)(Object) CYAN).itemKey = ItemKeys.CYAN_DYE;
        ((DyeColorExtender)(Object) PURPLE).itemKey = ItemKeys.PURPLE_DYE;
        ((DyeColorExtender)(Object) BLUE).itemKey = ItemKeys.BLUE_DYE;
        ((DyeColorExtender)(Object) BROWN).itemKey = ItemKeys.BROWN_DYE;
        ((DyeColorExtender)(Object) GREEN).itemKey = ItemKeys.GREEN_DYE;
        ((DyeColorExtender)(Object) RED).itemKey = ItemKeys.RED_DYE;
        ((DyeColorExtender)(Object) BLACK).itemKey = ItemKeys.BLACK_DYE;
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
}
