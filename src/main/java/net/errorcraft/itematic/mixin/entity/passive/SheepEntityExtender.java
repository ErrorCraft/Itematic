package net.errorcraft.itematic.mixin.entity.passive;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.util.DyeColorAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.DyeItemComponent;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(SheepEntity.class)
public abstract class SheepEntityExtender extends MobEntityExtender {
    @Shadow
    protected abstract ItemStack method_17689(RecipeInputInventory inventory, RecipeEntry<CraftingRecipe> entry);

    @Shadow
    protected abstract DyeColor method_17691(DyeColor par1, DyeColor par2);

    @Shadow
    public abstract DyeColor getColor();

    @Unique
    private static final Map<DyeColor, RegistryKey<Item>> DYE_TO_WOOL = new ImmutableMap.Builder<DyeColor, RegistryKey<Item>>()
        .put(DyeColor.WHITE, ItemKeys.WHITE_WOOL)
        .put(DyeColor.ORANGE, ItemKeys.ORANGE_WOOL)
        .put(DyeColor.MAGENTA, ItemKeys.MAGENTA_WOOL)
        .put(DyeColor.LIGHT_BLUE, ItemKeys.LIGHT_BLUE_WOOL)
        .put(DyeColor.YELLOW, ItemKeys.YELLOW_WOOL)
        .put(DyeColor.LIME, ItemKeys.LIME_WOOL)
        .put(DyeColor.PINK, ItemKeys.PINK_WOOL)
        .put(DyeColor.GRAY, ItemKeys.GRAY_WOOL)
        .put(DyeColor.LIGHT_GRAY, ItemKeys.LIGHT_GRAY_WOOL)
        .put(DyeColor.CYAN, ItemKeys.CYAN_WOOL)
        .put(DyeColor.PURPLE, ItemKeys.PURPLE_WOOL)
        .put(DyeColor.BLUE, ItemKeys.BLUE_WOOL)
        .put(DyeColor.BROWN, ItemKeys.BROWN_WOOL)
        .put(DyeColor.GREEN, ItemKeys.GREEN_WOOL)
        .put(DyeColor.RED, ItemKeys.RED_WOOL)
        .put(DyeColor.BLACK, ItemKeys.BLACK_WOOL)
        .build();

    @Unique
    private static World world;

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
        method = "sheared",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/SheepEntity;dropItem(Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/entity/ItemEntity;"
        )
    )
    private ItemEntity dropItemUseRegistryKey(SheepEntity instance, ItemConvertible item, int yOffset) {
        return this.itematic$dropItem(DYE_TO_WOOL.get(this.getColor()), yOffset);
    }

    @WrapOperation(
        method = "getChildColor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/SheepEntity;createDyeMixingCraftingInventory(Lnet/minecraft/util/DyeColor;Lnet/minecraft/util/DyeColor;)Lnet/minecraft/inventory/RecipeInputInventory;"
        )
    )
    private RecipeInputInventory createDyeMixingCraftingInventorySetRegistryManager(DyeColor firstColor, DyeColor secondColor, Operation<RecipeInputInventory> original) {
        world = this.getWorld();
        RecipeInputInventory inventory = original.call(firstColor, secondColor);
        world = null;
        return inventory;
    }

    @Inject(
        method = "getChildColor",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/entity/passive/SheepEntity;createDyeMixingCraftingInventory(Lnet/minecraft/util/DyeColor;Lnet/minecraft/util/DyeColor;)Lnet/minecraft/inventory/RecipeInputInventory;",
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    private void getColorUseItemComponent(AnimalEntity firstParent, AnimalEntity secondParent, CallbackInfoReturnable<DyeColor> info, @Local(ordinal = 0) DyeColor firstColor, @Local(ordinal = 1) DyeColor secondColor, @Local RecipeInputInventory inventory) {
        World world = this.getWorld();
        DyeColor color = world.getRecipeManager()
            .getFirstMatch(RecipeType.CRAFTING, inventory, world)
            .map(entry -> this.method_17689(inventory, entry))
            .flatMap(stack -> stack.itematic$getComponent(ItemComponentTypes.DYE))
            .map(DyeItemComponent::color)
            .orElseGet(() -> this.method_17691(firstColor, secondColor));
        info.setReturnValue(color);
    }

    @Redirect(
        method = "createDyeMixingCraftingInventory",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/DyeItem;byColor(Lnet/minecraft/util/DyeColor;)Lnet/minecraft/item/DyeItem;"
        )
    )
    private static DyeItem dyeItemByColorReturnNull(DyeColor color) {
        return null;
    }

    @Redirect(
        method = "createDyeMixingCraftingInventory",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;",
            ordinal = 0
        )
    )
    private static ItemStack newItemStackForFirstColorUseCreateStack(ItemConvertible item, DyeColor firstColor) {
        return world.itematic$createStack(((DyeColorAccess)(Object) firstColor).itematic$itemKey());
    }

    @Redirect(
        method = "createDyeMixingCraftingInventory",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;",
            ordinal = 1
        )
    )
    private static ItemStack newItemStackForSecondColorUseCreateStack(ItemConvertible item, @Local(ordinal = 1, argsOnly = true) DyeColor secondColor) {
        return world.itematic$createStack(((DyeColorAccess)(Object) secondColor).itematic$itemKey());
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.SHEEP_SPAWN_EGG;
    }
}
