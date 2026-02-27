package net.errorcraft.itematic.mixin.client.recipebook;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.access.client.recipebook.RecipeBookGroupAccess;
import net.errorcraft.itematic.client.recipebook.ItematicRecipeBookGroups;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.recipe.book.ItematicRecipeBookCategories;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(RecipeBookGroup.class)
@SuppressWarnings("DataFlowIssue")
public class RecipeBookGroupExtender implements RecipeBookGroupAccess {
    @Shadow
    @Final
    public static RecipeBookGroup CRAFTING_SEARCH;

    @Shadow
    @Final
    public static RecipeBookGroup CRAFTING_BUILDING_BLOCKS;

    @Shadow
    @Final
    public static RecipeBookGroup CRAFTING_REDSTONE;

    @Shadow
    @Final
    public static RecipeBookGroup CRAFTING_EQUIPMENT;

    @Shadow
    @Final
    public static RecipeBookGroup CRAFTING_MISC;

    @Shadow
    @Final
    public static RecipeBookGroup FURNACE_SEARCH;

    @Shadow
    @Final
    public static RecipeBookGroup FURNACE_FOOD;

    @Shadow
    @Final
    public static RecipeBookGroup FURNACE_BLOCKS;

    @Shadow
    @Final
    public static RecipeBookGroup FURNACE_MISC;

    @Shadow
    @Final
    public static RecipeBookGroup BLAST_FURNACE_SEARCH;

    @Shadow
    @Final
    public static RecipeBookGroup BLAST_FURNACE_BLOCKS;

    @Shadow
    @Final
    public static RecipeBookGroup BLAST_FURNACE_MISC;

    @Shadow
    @Final
    public static RecipeBookGroup SMOKER_SEARCH;

    @Shadow
    @Final
    public static RecipeBookGroup SMOKER_FOOD;

    @Shadow
    @Final
    public static RecipeBookGroup STONECUTTER;

    @Shadow
    @Final
    public static RecipeBookGroup SMITHING;

    @Shadow
    @Final
    public static RecipeBookGroup CAMPFIRE;

    @Shadow
    @Final
    public static RecipeBookGroup UNKNOWN;

    @Unique
    private List<RegistryKey<Item>> iconKeys;

    static {
        ((RecipeBookGroupExtender)(Object) CRAFTING_SEARCH).iconKeys = List.of(ItemKeys.COMPASS);
        ((RecipeBookGroupExtender)(Object) CRAFTING_BUILDING_BLOCKS).iconKeys = List.of(ItemKeys.BRICKS);
        ((RecipeBookGroupExtender)(Object) CRAFTING_REDSTONE).iconKeys = List.of(ItemKeys.REDSTONE);
        ((RecipeBookGroupExtender)(Object) CRAFTING_EQUIPMENT).iconKeys = List.of(ItemKeys.IRON_AXE, ItemKeys.GOLDEN_SWORD);
        ((RecipeBookGroupExtender)(Object) CRAFTING_MISC).iconKeys = List.of(ItemKeys.LAVA_BUCKET, ItemKeys.APPLE);
        ((RecipeBookGroupExtender)(Object) FURNACE_SEARCH).iconKeys = List.of(ItemKeys.COMPASS);
        ((RecipeBookGroupExtender)(Object) FURNACE_FOOD).iconKeys = List.of(ItemKeys.PORKCHOP);
        ((RecipeBookGroupExtender)(Object) FURNACE_BLOCKS).iconKeys = List.of(ItemKeys.STONE);
        ((RecipeBookGroupExtender)(Object) FURNACE_MISC).iconKeys = List.of(ItemKeys.LAVA_BUCKET, ItemKeys.EMERALD);
        ((RecipeBookGroupExtender)(Object) BLAST_FURNACE_SEARCH).iconKeys = List.of(ItemKeys.COMPASS);
        ((RecipeBookGroupExtender)(Object) BLAST_FURNACE_BLOCKS).iconKeys = List.of(ItemKeys.REDSTONE_ORE);
        ((RecipeBookGroupExtender)(Object) BLAST_FURNACE_MISC).iconKeys = List.of(ItemKeys.IRON_SHOVEL, ItemKeys.GOLDEN_LEGGINGS);
        ((RecipeBookGroupExtender)(Object) SMOKER_SEARCH).iconKeys = List.of(ItemKeys.COMPASS);
        ((RecipeBookGroupExtender)(Object) SMOKER_FOOD).iconKeys = List.of(ItemKeys.PORKCHOP);
        ((RecipeBookGroupExtender)(Object) STONECUTTER).iconKeys = List.of(ItemKeys.CHISELED_STONE_BRICKS);
        ((RecipeBookGroupExtender)(Object) SMITHING).iconKeys = List.of(ItemKeys.NETHERITE_CHESTPLATE);
        ((RecipeBookGroupExtender)(Object) CAMPFIRE).iconKeys = List.of(ItemKeys.PORKCHOP);
        ((RecipeBookGroupExtender)(Object) UNKNOWN).iconKeys = List.of(ItemKeys.BARRIER);
        ((RecipeBookGroupExtender)(Object) ItematicRecipeBookGroups.BREWING_SEARCH).iconKeys = List.of(ItemKeys.COMPASS);
        ((RecipeBookGroupExtender)(Object) ItematicRecipeBookGroups.BREWING_MODIFY).iconKeys = List.of(ItemKeys.NETHER_WART, ItemKeys.MAGMA_CREAM);
        ((RecipeBookGroupExtender)(Object) ItematicRecipeBookGroups.BREWING_AMPLIFY).iconKeys = List.of(ItemKeys.SPLASH_POTION, ItemKeys.LINGERING_POTION);
    }

    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackReturnEmptyStack(ItemConvertible item) {
        return ItemStack.EMPTY;
    }

    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableMap;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;",
            remap = false
        )
    )
    private static ImmutableMap<RecipeBookGroup, List<RecipeBookGroup>> addCustomEntries(ImmutableMap<RecipeBookGroup, List<RecipeBookGroup>> original) {
        return ImmutableMap.<RecipeBookGroup, List<RecipeBookGroup>>builder()
            .putAll(original)
            .put(
                ItematicRecipeBookGroups.BREWING_SEARCH,
                ImmutableList.of(ItematicRecipeBookGroups.BREWING_MODIFY, ItematicRecipeBookGroups.BREWING_AMPLIFY)
            )
            .build();
    }

    @Inject(
        method = "getGroups",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void checkForCustomEntries(RecipeBookCategory category, CallbackInfoReturnable<List<RecipeBookGroup>> info) {
        if (category == ItematicRecipeBookCategories.BREWING) {
            info.setReturnValue(ItematicRecipeBookGroups.BREWING);
        }
    }

    @Override
    public List<ItemStack> itematic$icons(Registry<Item> registry) {
        List<ItemStack> stacks = new ArrayList<>();
        for (RegistryKey<Item> item : this.iconKeys) {
            registry.getEntry(item)
                .map(ItemStack::new)
                .ifPresent(stacks::add);
        }

        return stacks;
    }
}
