package net.errorcraft.itematic.mixin.world.entity.ai.behavior;

import com.google.common.collect.ImmutableSet;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.ItemIds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.behavior.TradeWithVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TradeWithVillager.class)
public class TradeWithVillagerExtender {
    @WrapOperation(
        method = "figureOutWhatIAmWillingToTrade",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/npc/villager/VillagerProfession;requestedItems()Lcom/google/common/collect/ImmutableSet;"
        )
    )
    private static ImmutableSet<Item> requestedItemsUseDynamicRegistry(VillagerProfession instance, Operation<ImmutableSet<Item>> original, Villager myBody) {
        TagKey<Item> gatherableItems = instance.itematic$gatherableItems();
        if (gatherableItems == null) {
            return ImmutableSet.of();
        }

        return myBody.registryAccess()
            .lookupOrThrow(Registries.ITEM)
            .get(gatherableItems)
            .stream()
            .flatMap(HolderSet::stream)
            .map(Holder::value)
            .collect(ImmutableSet.toImmutableSet());
    }

    @WrapOperation(
        method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/npc/villager/Villager;J)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/Items;WHEAT:Lnet/minecraft/world/item/Item;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private Item getWheatUseDynamicRegistry(Operation<Item> original, ServerLevel level) {
        return level.itematic$getItem(ItemIds.WHEAT).value();
    }

    @WrapOperation(
        method = "lambda$tick$1",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private static boolean isWheatCheckId(ItemStack instance, Object o, Operation<Boolean> original) {
        return instance.is(ItemIds.WHEAT);
    }
}
