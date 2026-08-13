package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.UseBonemeal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(UseBonemeal.class)
public class BoneMealTaskExtender {
    @Redirect(
        method = "checkExtraStartConditions(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/npc/villager/Villager;)Z",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/Items;BONE_MEAL:Lnet/minecraft/world/item/Item;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private Item getBoneMealUseDynamicRegistry(ServerLevel world) {
        return world.itematic$getItem(ItemIds.BONE_MEAL).value();
    }

    @Redirect(
        method = "start(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/npc/villager/Villager;J)V",
        at = @At(
            value = "NEW",
            target = "net/minecraft/world/item/ItemStack"
        )
    )
    private ItemStack newItemStackForBoneMealUseCreateStack(ItemLike item, ServerLevel world) {
        return world.itematic$createStack(ItemIds.BONE_MEAL);
    }

    @Redirect(
        method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/npc/villager/Villager;J)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfForBoneMealUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemIds.BONE_MEAL);
    }
}
