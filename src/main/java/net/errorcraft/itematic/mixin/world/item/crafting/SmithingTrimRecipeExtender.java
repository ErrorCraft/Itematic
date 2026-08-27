package net.errorcraft.itematic.mixin.world.item.crafting;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SimpleSmithingRecipe;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SmithingRecipeDisplay;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(SmithingTrimRecipe.class)
public abstract class SmithingTrimRecipeExtender extends SimpleSmithingRecipe {
    @Shadow
    @Final
    private Ingredient template;

    @Shadow
    @Final
    private Ingredient base;

    @Shadow
    @Final
    private Ingredient addition;

    @Shadow
    @Final
    private Holder<TrimPattern> pattern;

    protected SmithingTrimRecipeExtender(CommonInfo commonInfo) {
        super(commonInfo);
    }

    @Override
    public List<RecipeDisplay> itematic$display(HolderGetter<Item> items) {
        SlotDisplay base = this.base.display();
        SlotDisplay material = this.addition.display();
        SlotDisplay pattern = this.template.display();
        return List.of(
            new SmithingRecipeDisplay(
                pattern,
                base,
                material,
                new SlotDisplay.SmithingTrimDemoSlotDisplay(base, material, this.pattern),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemIds.SMITHING_TABLE))
            )
        );
    }
}
