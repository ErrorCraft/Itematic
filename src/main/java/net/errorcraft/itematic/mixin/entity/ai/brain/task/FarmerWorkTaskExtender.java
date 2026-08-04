package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.inventory.SimpleInventoryAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.behavior.WorkAtComposter;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(WorkAtComposter.class)
public class FarmerWorkTaskExtender {
    @Unique
    private static final List<ResourceKey<Item>> COMPOSTABLE_KEYS = List.of(ItemKeys.WHEAT_SEEDS, ItemKeys.BEETROOT_SEEDS);

    @Redirect(
        method = "makeBread",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/SimpleContainer;removeItemType(Lnet/minecraft/world/item/Item;I)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack removeItemUseRegistryKey(SimpleContainer instance, Item item, int count) {
        ((SimpleInventoryAccess) instance).itematic$removeItem(ItemKeys.WHEAT, count);
        return ItemStack.EMPTY;
    }

    @Redirect(
        method = "makeBread",
        at = @At(
            value = "NEW",
            target = "net/minecraft/world/item/ItemStack"
        )
    )
    private ItemStack newItemStackForBreadUseCreateStack(ItemLike item, int count, @Local(argsOnly = true) Villager villager) {
        return villager.level().itematic$createStack(ItemKeys.BREAD, count);
    }

    @Redirect(
        method = "compostItems",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;indexOf(Ljava/lang/Object;)I"
        )
    )
    private int compostSeedsSizeUseRegistryKeys(List<Item> instance, Object o, @Local ItemStack stack) {
        return COMPOSTABLE_KEYS.indexOf(stack.itematic$key());
    }

    @Redirect(
        method = "makeBread",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/Items;WHEAT:Lnet/minecraft/world/item/Item;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private Item getWheatUseDynamicRegistry(@Local(argsOnly = true) Villager entity) {
        return entity.level().itematic$getItem(ItemKeys.WHEAT).value();
    }
}
