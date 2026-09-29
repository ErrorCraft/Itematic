package net.errorcraft.itematic.mixin.world.item.crafting;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.references.BlockItemIds;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ImbueRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.NormalCraftingRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ImbueRecipe.class)
public abstract class ImbueRecipeExtender extends NormalCraftingRecipe {
    @Shadow
    @Final
    private Ingredient source;

    @Shadow
    @Final
    private Ingredient material;

    @Shadow
    @Final
    private ItemStackTemplate result;

    protected ImbueRecipeExtender(CommonInfo commonInfo, CraftingBookInfo bookInfo) {
        super(commonInfo, bookInfo);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remainders = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int y = 0; y < input.height(); y++) {
            for (int x = 0; x < input.width(); x++) {
                int index = x + y * input.width();
                Ingredient ingredient = x == 1 && y == 1
                    ? this.source
                    : this.material;
                ingredient.itematic$remainder()
                    .map(ItemStackTemplate::create)
                    .ifPresent(remainder -> remainders.set(index, remainder));
            }
        }

        return remainders;
    }

    @Override
    public List<RecipeDisplay> itematic$display(HolderGetter<Item> items) {
        SlotDisplay material = this.material.display();
        SlotDisplay.WithAnyPotion source = new SlotDisplay.WithAnyPotion(this.source.display());
        return List.of(
            new ShapedCraftingRecipeDisplay(
                3,
                3,
                List.of(
                    material, material, material,
                    material, source,   material,
                    material, material, material
                ),
                new SlotDisplay.WithAnyPotion(new SlotDisplay.ItemStackSlotDisplay(this.result)),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(BlockItemIds.CRAFTING_TABLE.item()))
            )
        );
    }
}
