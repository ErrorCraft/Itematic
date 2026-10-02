package net.errorcraft.itematic.mixin.world.item.crafting;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.references.BlockItemIds;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.DyeRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.NormalCraftingRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(DyeRecipe.class)
public abstract class DyeRecipeExtender extends NormalCraftingRecipe {
    @Shadow
    @Final
    private Ingredient target;

    @Shadow
    @Final
    private Ingredient dye;

    protected DyeRecipeExtender(CommonInfo commonInfo, CraftingBookInfo bookInfo) {
        super(commonInfo, bookInfo);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remainders = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        boolean foundTarget = false;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }

            final int index = i;
            if (!foundTarget && this.target.test(stack)) {
                foundTarget = true;
                this.target.itematic$remainder()
                    .map(ItemStackTemplate::create)
                    .ifPresent(remainder -> remainders.set(index, remainder));
                continue;
            }

            this.dye.itematic$remainder()
                .map(ItemStackTemplate::create)
                .ifPresent(remainder -> remainders.set(index, remainder));
        }

        return remainders;
    }

    @Override
    public List<RecipeDisplay> itematic$display(HolderGetter<Item> items) {
        SlotDisplay.OnlyWithComponent dyeDisplay = new SlotDisplay.OnlyWithComponent(
            this.dye.display(),
            DataComponents.DYE
        );
        SlotDisplay targetDisplay = this.target.display();
        return List.of(
            new ShapelessCraftingRecipeDisplay(
                List.of(targetDisplay, dyeDisplay),
                new SlotDisplay.DyedSlotDemo(dyeDisplay, targetDisplay),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(BlockItemIds.CRAFTING_TABLE.item()))
            )
        );
    }
}
