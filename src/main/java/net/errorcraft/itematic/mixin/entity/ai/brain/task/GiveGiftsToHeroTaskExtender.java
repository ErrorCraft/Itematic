package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.ai.brain.task.GiveGiftsToHeroTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(GiveGiftsToHeroTask.class)
public class GiveGiftsToHeroTaskExtender {
    @Redirect(
        method = "getGifts",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;POPPY:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack getGiftsNewItemStackForWheatSeedsUseRegistryEntry(ItemConvertible item, VillagerEntity villager) {
        return new ItemStack(villager.getWorld().itematic$getItem(ItemKeys.WHEAT_SEEDS));
    }
}
