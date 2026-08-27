package net.errorcraft.itematic.mixin.world.entity.ai.behavior;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.references.ItemIds;
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
public class WorkAtComposterExtender {
    @Unique
    private static final List<ResourceKey<Item>> COMPOSTABLE_KEYS = List.of(
        ItemIds.WHEAT_SEEDS,
        ItemIds.BEETROOT_SEEDS
    );

    @Redirect(
        method = "makeBread",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/SimpleContainer;removeItemType(Lnet/minecraft/world/item/Item;I)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack removeItemUseId(SimpleContainer instance, Item itemType, int count) {
        instance.itematic$removeItem(ItemIds.WHEAT, count);
        return ItemStack.EMPTY;
    }

    @Redirect(
        method = "makeBread",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;I)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForBreadUseCreateStack(ItemLike item, int count, @Local(name = "body", argsOnly = true) Villager body) {
        return body.level().itematic$createStack(ItemIds.BREAD, count);
    }

    @Redirect(
        method = "compostItems",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;indexOf(Ljava/lang/Object;)I"
        )
    )
    private int indexOfItemUseId(List<Item> instance, Object o, @Local(name = "itemStack") ItemStack itemStack) {
        return COMPOSTABLE_KEYS.indexOf(itemStack.itematic$key());
    }

    @Redirect(
        method = "makeBread",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/Items;WHEAT:Lnet/minecraft/world/item/Item;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private Item getWheatUseDynamicRegistry(@Local(name = "body", argsOnly = true) Villager body) {
        return body.level().itematic$getItem(ItemIds.WHEAT).value();
    }
}
