package net.errorcraft.itematic.mixin.loot.function;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.functions.FillPlayerHead;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FillPlayerHead.class)
public class FillPlayerHeadLootFunctionExtender {
    @Redirect(
        method = "run",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfForPlayerHeadUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemIds.PLAYER_HEAD);
    }
}
