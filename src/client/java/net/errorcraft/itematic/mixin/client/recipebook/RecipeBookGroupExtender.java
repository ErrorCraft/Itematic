package net.errorcraft.itematic.mixin.client.recipebook;

import net.errorcraft.itematic.access.client.recipebook.RecipeBookGroupAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(RecipeBookGroup.class)
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
