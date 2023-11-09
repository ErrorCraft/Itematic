package net.errorcraft.itematic.mixin.block.entity;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.recipe.BrewingRecipeRegistryUtil;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(BrewingStandBlockEntity.class)
public class BrewingStandBlockEntityExtender {
    @Redirect(
        method = "isValid",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean isValidIsOfForBlazePowderUseRegistryKeyCheck(ItemStack instance, Item item) {
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
    private boolean isValidIsOfUseItemComponentCheck(ItemStack instance, Item item) {
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
    private boolean isValidIsOfRemainingItemChecksReturnFalse(ItemStack instance, Item item) {
        return false;
    }

    @Redirect(
        method = { "isValid", "canExtract" },
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
    private boolean isValidIsOfForGlassBottleUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.GLASS_BOTTLE);
    }

    @Redirect(
        method = "craft",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/BrewingRecipeRegistry;craft(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack craftCraftUseDynamicRegistryManager(ItemStack ingredient, ItemStack input, World world) {
        return BrewingRecipeRegistryUtil.INSTANCE.itematic$craft(ingredient, input, world);
    }

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private static boolean isOfForBlazePowderUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BLAZE_POWDER);
    }
}
