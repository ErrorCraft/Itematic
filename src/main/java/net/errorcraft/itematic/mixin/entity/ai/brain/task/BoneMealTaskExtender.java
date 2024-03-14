package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.ai.brain.task.BoneMealTask;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BoneMealTask.class)
public class BoneMealTaskExtender {
    @Redirect(
        method = "shouldRun(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;)Z",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/item/Items;BONE_MEAL:Lnet/minecraft/item/Item;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private Item getBoneMealUseDynamicRegistry(ServerWorld world) {
        return world.itematic$getItem(ItemKeys.BONE_MEAL).value();
    }

    @Redirect(
        method = "run(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackForBoneMealUseCreateStack(ItemConvertible item, ServerWorld world) {
        return world.itematic$createStack(ItemKeys.BONE_MEAL);
    }

    @Redirect(
        method = "keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForBoneMealUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BONE_MEAL);
    }
}
