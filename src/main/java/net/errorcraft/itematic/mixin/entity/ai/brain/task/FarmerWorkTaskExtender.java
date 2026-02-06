package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.inventory.SimpleInventoryAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.ai.brain.task.FarmerWorkTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(FarmerWorkTask.class)
public class FarmerWorkTaskExtender {
    @Unique
    private static final List<RegistryKey<Item>> COMPOSTABLE_KEYS = List.of(ItemKeys.WHEAT_SEEDS, ItemKeys.BEETROOT_SEEDS);

    @Redirect(
        method = "craftAndDropBread",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/inventory/SimpleInventory;removeItem(Lnet/minecraft/item/Item;I)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack removeItemUseRegistryKey(SimpleInventory instance, Item item, int count) {
        ((SimpleInventoryAccess) instance).itematic$removeItem(ItemKeys.WHEAT, count);
        return ItemStack.EMPTY;
    }

    @Redirect(
        method = "craftAndDropBread",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackForBreadUseCreateStack(ItemConvertible item, int count, @Local(argsOnly = true) VillagerEntity villager) {
        return villager.getWorld().itematic$createStack(ItemKeys.BREAD, count);
    }

    @Redirect(
        method = "compostSeeds",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;indexOf(Ljava/lang/Object;)I"
        )
    )
    private int compostSeedsSizeUseRegistryKeys(List<Item> instance, Object o, @Local ItemStack stack) {
        return COMPOSTABLE_KEYS.indexOf(stack.itematic$key());
    }

    @Redirect(
        method = "craftAndDropBread",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/item/Items;WHEAT:Lnet/minecraft/item/Item;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private Item getWheatUseDynamicRegistry(@Local(argsOnly = true) VillagerEntity entity) {
        return entity.getWorld().itematic$getItem(ItemKeys.WHEAT).value();
    }
}
