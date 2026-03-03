package net.errorcraft.itematic.mixin.block.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.block.entity.BrewingStandBlockEntityAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.recipe.ItematicRecipeTypes;
import net.errorcraft.itematic.recipe.brewing.BrewingRecipe;
import net.errorcraft.itematic.recipe.input.BrewingRecipeInput;
import net.errorcraft.itematic.screen.BrewingStandMenuDelegate;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
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
public abstract class BrewingStandBlockEntityExtender extends LockableContainerBlockEntity implements RecipeInputProvider, BrewingStandBlockEntityAccess {
    @Shadow
    @Final
    private static int INPUT_SLOT_INDEX;

    @Shadow
    private DefaultedList<ItemStack> inventory;

    @Unique
    private final RecipeManager.MatchGetter<BrewingRecipeInput, BrewingRecipe<?>> matcher = RecipeManager.createCachedMatchGetter(ItematicRecipeTypes.BREWING);

    @Unique
    private int maxBrewingTime;

    protected BrewingStandBlockEntityExtender(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/BrewingStandBlockEntity;canCraft(Lnet/minecraft/recipe/BrewingRecipeRegistry;Lnet/minecraft/util/collection/DefaultedList;)Z"
        )
    )
    @SuppressWarnings("DataFlowIssue")
    private static boolean useRecipe(BrewingRecipeRegistry brewingRecipeRegistry, DefaultedList<ItemStack> slots, World world, @Local(argsOnly = true) BrewingStandBlockEntity blockEntity) {
        BrewingStandBlockEntityExtender blockEntityExtender = (BrewingStandBlockEntityExtender)(Object) blockEntity;
        if (!blockEntityExtender.acceptsRecipes()) {
            return false;
        }

        if (!(world instanceof ServerWorld serverWorld)) {
            return false;
        }

        ItemStack reagent = slots.get(INPUT_SLOT_INDEX);
        for (int i = 0; i < 3; i++) {
            ItemStack base = slots.get(i);
            BrewingRecipeInput input = new BrewingRecipeInput(base, reagent);
            if (blockEntityExtender.matcher.getFirstMatch(input, serverWorld).isPresent()) {
                return true;
            }
        }

        return false;
    }

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/BrewingStandBlockEntity;craft(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/collection/DefaultedList;)V"
        )
    )
    @SuppressWarnings("DataFlowIssue")
    private static void useRecipe(World world, BlockPos pos, DefaultedList<ItemStack> slots, @Local(argsOnly = true) BrewingStandBlockEntity blockEntity) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }

        BrewingStandBlockEntityExtender blockEntityExtender = (BrewingStandBlockEntityExtender)(Object) blockEntity;
        BrewingRecipe<?> recipe = null;
        ItemStack reagent = slots.get(INPUT_SLOT_INDEX);
        for (int i = 0; i < 3; i++) {
            BrewingRecipeInput input = new BrewingRecipeInput(slots.get(i), reagent);
            if (recipe != null && recipe.matches(input, world)) {
                ItemStack result = recipe.craft(input, world.getRegistryManager());
                slots.set(i, result);
                continue;
            }

            Optional<RecipeEntry<BrewingRecipe<?>>> optionalRecipe = blockEntityExtender.matcher.getFirstMatch(input, serverWorld);
            if (optionalRecipe.isPresent()) {
                recipe = optionalRecipe.get().value();
                ItemStack result = recipe.craft(input, world.getRegistryManager());
                slots.set(i, result);
            }
        }

        reagent.decrement(1);
        if (recipe != null) {
            recipe.reagentRemainder().ifPresent(remainder -> {
                if (reagent.isEmpty()) {
                    slots.set(INPUT_SLOT_INDEX, remainder);
                } else {
                    ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), remainder);
                }
            });
        }

        world.syncWorldEvent(WorldEvents.BREWING_STAND_BREWS, pos, 0);
    }

    @ModifyConstant(
        method = "tick",
        constant = @Constant(
            intValue = 400
        )
    )
    @SuppressWarnings("DataFlowIssue")
    private static int useRecipeForBrewingTime(int original, World world, @Local(argsOnly = true) BrewingStandBlockEntity blockEntity) {
        if (world instanceof ServerWorld serverWorld) {
            BrewingStandBlockEntityExtender blockEntityExtender = (BrewingStandBlockEntityExtender)(Object) blockEntity;
            return blockEntityExtender.maxBrewingTime = blockEntityExtender.brewTime(serverWorld);
        }

        return BrewingRecipe.DEFAULT_BREWING_TIME;
    }

    @Redirect(
        method = "isValid",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/BrewingRecipeRegistry;isValidIngredient(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean acceptAllItemsForInput(BrewingRecipeRegistry instance, ItemStack stack) {
        return true;
    }

    @Redirect(
        method = "isValid",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean isOfForBlazePowderUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BLAZE_POWDER);
    }

    @Redirect(
        method = "isValid",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;POTION:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForPotionUseItemComponentCheck(ItemStack instance, Item item) {
        return instance.itematic$hasComponent(ItemComponentTypes.POTION_HOLDER);
    }

    @Redirect(
        method = "isValid",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;SPLASH_POTION:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            ),
            to = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;GLASS_BOTTLE:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfRemainingItemChecksReturnFalse(ItemStack instance, Item item) {
        return false;
    }

    @Redirect(
        method = {
            "isValid",
            "canExtract"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;GLASS_BOTTLE:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForGlassBottleUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.GLASS_BOTTLE);
    }

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private static boolean isOfForBlazePowderUseRegistryKeyCheckStatic(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BLAZE_POWDER);
    }

    @ModifyReturnValue(
        method = "createScreenHandler",
        at = @At("TAIL")
    )
    private ScreenHandler useDelegate(ScreenHandler original) {
        return new BrewingStandMenuDelegate((BrewingStandScreenHandler) original, this.getWorld());
    }

    @Override
    public void provideRecipeInputs(RecipeMatcher finder) {
        finder.addInput(this.inventory.get(INPUT_SLOT_INDEX));
        for (int i = 0; i < 3; i++) {
            finder.addInput(this.inventory.get(i));
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
        ItemStack reagent = this.inventory.get(INPUT_SLOT_INDEX);
        if (reagent.isEmpty()) {
            return false;
        }

        for (int i = 0; i < 3; i++) {
            if (!this.inventory.get(i).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    @Unique
    private int brewTime(ServerWorld world) {
        ItemStack reagent = this.inventory.get(INPUT_SLOT_INDEX);
        for (int i = 0; i < 3; i++) {
            BrewingRecipeInput input = new BrewingRecipeInput(this.inventory.get(i), reagent);
            Optional<RecipeEntry<BrewingRecipe<?>>> optionalRecipe = this.matcher.getFirstMatch(input, world);
            if (optionalRecipe.isPresent()) {
                return optionalRecipe.get()
                    .value()
                    .brewingTime();
            }
        }

        return BrewingRecipe.DEFAULT_BREWING_TIME;
    }

    @Mixin(targets = "net/minecraft/block/entity/BrewingStandBlockEntity$1")
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
            method = "size",
            at = @At("TAIL")
        )
        private int addMaxFuelTimeProperty(int original) {
            return original + 1;
        }
    }
}
