package net.errorcraft.itematic.mixin.block.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.block.entity.BrewingStandBlockEntityAccess;
import net.errorcraft.itematic.recipe.ItematicRecipeTypes;
import net.errorcraft.itematic.recipe.brewing.BrewingRecipe;
import net.errorcraft.itematic.recipe.input.BrewingRecipeInput;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.screen.BrewingStandMenuDelegate;
import net.errorcraft.itematic.tags.ItematicItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(BrewingStandBlockEntity.class)
public class BrewingStandBlockEntityExtender implements StackedContentsCompatible, BrewingStandBlockEntityAccess {
    @Shadow
    @Final
    private static int INGREDIENT_SLOT;

    @Shadow
    private NonNullList<ItemStack> items;

    @Unique
    private final RecipeManager.CachedCheck<BrewingRecipeInput, BrewingRecipe<?>> matcher = RecipeManager.createCheck(ItematicRecipeTypes.BREWING);

    @Unique
    private int maxBrewingTime;

    @Redirect(
        method = "serverTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/BrewingStandBlockEntity;isBrewable(Lnet/minecraft/world/item/alchemy/PotionBrewing;Lnet/minecraft/core/NonNullList;)Z"
        )
    )
    @SuppressWarnings("DataFlowIssue")
    private static boolean useRecipe(PotionBrewing brewingRecipeRegistry, NonNullList<ItemStack> slots, Level world, @Local(argsOnly = true) BrewingStandBlockEntity blockEntity) {
        BrewingStandBlockEntityExtender blockEntityExtender = (BrewingStandBlockEntityExtender)(Object) blockEntity;
        if (!blockEntityExtender.acceptsRecipes()) {
            return false;
        }

        if (!(world instanceof ServerLevel serverWorld)) {
            return false;
        }

        ItemStack reagent = slots.get(INGREDIENT_SLOT);
        for (int i = 0; i < 3; i++) {
            ItemStack base = slots.get(i);
            BrewingRecipeInput input = new BrewingRecipeInput(base, reagent);
            if (blockEntityExtender.matcher.getRecipeFor(input, serverWorld).isPresent()) {
                return true;
            }
        }

        return false;
    }

    @Redirect(
        method = "serverTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/BrewingStandBlockEntity;doBrew(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/NonNullList;)V"
        )
    )
    @SuppressWarnings("DataFlowIssue")
    private static void useRecipe(Level world, BlockPos pos, NonNullList<ItemStack> slots, @Local(argsOnly = true) BrewingStandBlockEntity blockEntity) {
        if (!(world instanceof ServerLevel serverWorld)) {
            return;
        }

        BrewingStandBlockEntityExtender blockEntityExtender = (BrewingStandBlockEntityExtender)(Object) blockEntity;
        BrewingRecipe<?> recipe = null;
        ItemStack reagent = slots.get(INGREDIENT_SLOT);
        for (int i = 0; i < 3; i++) {
            BrewingRecipeInput input = new BrewingRecipeInput(slots.get(i), reagent);
            if (recipe != null && recipe.matches(input, world)) {
                ItemStack result = recipe.assemble(input, world.registryAccess());
                slots.set(i, result);
                continue;
            }

            Optional<RecipeHolder<BrewingRecipe<?>>> optionalRecipe = blockEntityExtender.matcher.getRecipeFor(input, serverWorld);
            if (optionalRecipe.isPresent()) {
                recipe = optionalRecipe.get().value();
                ItemStack result = recipe.assemble(input, world.registryAccess());
                slots.set(i, result);
            }
        }

        reagent.shrink(1);
        if (recipe != null) {
            recipe.reagentRemainder().ifPresent(remainder -> {
                if (reagent.isEmpty()) {
                    slots.set(INGREDIENT_SLOT, remainder);
                } else {
                    Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), remainder);
                }
            });
        }

        world.levelEvent(LevelEvent.SOUND_BREWING_STAND_BREW, pos, 0);
    }

    @ModifyConstant(
        method = "serverTick",
        constant = @Constant(
            intValue = 400
        )
    )
    @SuppressWarnings("DataFlowIssue")
    private static int useRecipeForBrewingTime(int original, Level world, @Local(argsOnly = true) BrewingStandBlockEntity blockEntity) {
        if (world instanceof ServerLevel serverWorld) {
            BrewingStandBlockEntityExtender blockEntityExtender = (BrewingStandBlockEntityExtender)(Object) blockEntity;
            return blockEntityExtender.maxBrewingTime = blockEntityExtender.brewTime(serverWorld);
        }

        return BrewingRecipe.DEFAULT_BREWING_TIME;
    }

    @Redirect(
        method = "canPlaceItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/alchemy/PotionBrewing;isIngredient(Lnet/minecraft/world/item/ItemStack;)Z"
        )
    )
    private boolean acceptAllItemsForInput(PotionBrewing instance, ItemStack stack) {
        return true;
    }

    @Redirect(
        method = "canPlaceItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;POTION:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForPotionUseItemTagCheck(ItemStack instance, Item item) {
        return instance.is(ItematicItemTags.BREWING_INPUTS);
    }

    @Redirect(
        method = "canPlaceItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;SPLASH_POTION:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            ),
            to = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;GLASS_BOTTLE:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfRemainingItemChecksReturnFalse(ItemStack instance, Item item) {
        return false;
    }

    @Redirect(
        method = {
            "canPlaceItem",
            "canTakeItemThroughFace"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;GLASS_BOTTLE:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForGlassBottleUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemIds.GLASS_BOTTLE);
    }

    @ModifyReturnValue(
        method = "createMenu",
        at = @At("TAIL")
    )
    private AbstractContainerMenu useDelegate(AbstractContainerMenu original) {
        return new BrewingStandMenuDelegate((BrewingStandMenu) original);
    }

    @Override
    public void fillStackedContents(StackedItemContents finder) {
        finder.accountStack(this.items.get(INGREDIENT_SLOT));
        for (int i = 0; i < 3; i++) {
            finder.accountStack(this.items.get(i));
        }
    }

    @Override
    public int itematic$maxBrewingTime() {
        return this.maxBrewingTime;
    }

    @Override
    public void itematic$setMaxBrewingTime(int maxBrewingTime) {
        this.maxBrewingTime = maxBrewingTime;
    }

    @Unique
    private boolean acceptsRecipes() {
        ItemStack reagent = this.items.get(INGREDIENT_SLOT);
        if (reagent.isEmpty()) {
            return false;
        }

        for (int i = 0; i < 3; i++) {
            if (!this.items.get(i).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    @Unique
    private int brewTime(ServerLevel world) {
        ItemStack reagent = this.items.get(INGREDIENT_SLOT);
        for (int i = 0; i < 3; i++) {
            BrewingRecipeInput input = new BrewingRecipeInput(this.items.get(i), reagent);
            Optional<RecipeHolder<BrewingRecipe<?>>> optionalRecipe = this.matcher.getRecipeFor(input, world);
            if (optionalRecipe.isPresent()) {
                return optionalRecipe.get()
                    .value()
                    .brewingTime();
            }
        }

        return BrewingRecipe.DEFAULT_BREWING_TIME;
    }

    @Mixin(targets = "net/minecraft/world/level/block/entity/BrewingStandBlockEntity$1")
    public static class PropertyDelegateExtender {
        @Shadow
        @Final
        BrewingStandBlockEntity field_17382;

        @Inject(
            method = "set",
            at = @At("HEAD"),
            cancellable = true
        )
        private void setMaxFuelTimeProperty(int index, int value, CallbackInfo info) {
            if (index == 2) {
                ((BrewingStandBlockEntityAccess) this.field_17382).itematic$setMaxBrewingTime(value);
                info.cancel();
            }
        }

        @Inject(
            method = "get",
            at = @At("HEAD"),
            cancellable = true
        )
        private void getMaxFuelTimeProperty(int index, CallbackInfoReturnable<Integer> info) {
            if (index == 2) {
                int maxBrewingTime = ((BrewingStandBlockEntityAccess) this.field_17382).itematic$maxBrewingTime();
                info.setReturnValue(maxBrewingTime);
            }
        }

        @ModifyReturnValue(
            method = "getCount",
            at = @At("TAIL")
        )
        private int addMaxFuelTimeProperty(int original) {
            return original + 1;
        }
    }
}
