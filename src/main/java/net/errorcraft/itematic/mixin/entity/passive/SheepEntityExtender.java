package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.DyeItemComponent;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(SheepEntity.class)
public abstract class SheepEntityExtender extends MobEntityExtender {
    @Shadow
    public abstract DyeColor getColor();

    protected SheepEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "interactMob",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForShearsUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.SHEARS);
    }

    @Redirect(
        method = "getChildColor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/SheepEntity;createChildColorRecipeInput(Lnet/minecraft/util/DyeColor;Lnet/minecraft/util/DyeColor;)Lnet/minecraft/recipe/input/CraftingRecipeInput;"
        )
    )
    private CraftingRecipeInput createChildColorRecipeInput(DyeColor firstColor, DyeColor secondColor) {
        return CraftingRecipeInput.create(
            2,
            1,
            List.of(
                this.getWorld().itematic$createStack(firstColor.itematic$itemKey()),
                this.getWorld().itematic$createStack(secondColor.itematic$itemKey())
            )
        );
    }

    @Redirect(
        method = "getChildColor",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;filter(Ljava/util/function/Predicate;)Ljava/util/Optional;"
        )
    )
    private Optional<DyeItemComponent> instanceOfDyeItemUseItemComponent(Optional<Item> instance, Predicate<? super Object> predicate) {
        return instance.flatMap(item -> item.itematic$getBehavior(ItemComponentTypes.DYE));
    }

    @Redirect(
        method = "getChildColor",
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
    private Optional<DyeItemComponent> castToDyeItemDoNothing(Optional<DyeItemComponent> instance, Function<? super Item, ? extends DyeItem> mapper) {
        return instance;
    }

    @ModifyArg(
        method = "getChildColor",
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
    private Function<? super DyeItemComponent, ? extends DyeColor> getColorUseItemComponent(Function<? super DyeItem, ? extends DyeColor> mapper) {
        return DyeItemComponent::color;
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.SHEEP_SPAWN_EGG;
    }
}
