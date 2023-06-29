package net.errorcraft.itematic.mixin.entity.passive;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItemTagsUtil;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(AbstractHorseEntity.class)
public class AbstractHorseEntityExtender {
    @Redirect(
        method = "isBreedingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean isBreedingItemTestUseItemTagCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.isIn(ItemTagsUtil.HORSE_BREEDING_ITEMS);
    }

    @Redirect(
        method = "receiveFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean receiveFoodIsOfForWheatUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.WHEAT);
    }

    @Redirect(
        method = "receiveFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;APPLE:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean receiveFoodIsOfForAppleUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.APPLE);
    }

    @Redirect(
        method = "receiveFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;GOLDEN_CARROT:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean receiveFoodIsOfForGoldenCarrotUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.GOLDEN_CARROT);
    }

    @Redirect(
        method = "receiveFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;GOLDEN_APPLE:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean receiveFoodIsOfForGoldenAppleUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.GOLDEN_APPLE);
    }

    @Redirect(
        method = "receiveFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;ENCHANTED_GOLDEN_APPLE:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean receiveFoodIsOfForEnchantedGoldenAppleUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.ENCHANTED_GOLDEN_APPLE);
    }

    @ModifyExpressionValue(
        method = "initCustomGoals",
        at = @At(
            value = "NEW",
            target = "net/minecraft/entity/ai/goal/TemptGoal"
        )
    )
    private TemptGoal initCustomGoalsNewTemptGoalSetFoodTag(TemptGoal original) {
        original.setFoodTag(ItemTagsUtil.HORSE_TEMPTING_ITEMS);
        return original;
    }
}
