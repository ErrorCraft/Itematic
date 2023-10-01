package net.errorcraft.itematic.mixin.entity.passive;

import com.google.common.collect.ImmutableSet;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.VillagerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityExtender {
    @Shadow
    public abstract VillagerData getVillagerData();

    @Redirect(
        method = "canGather",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z"
        )
    )
    private boolean canGatherContainsUseItemTag(Set<Item> instance, Object o, ItemStack stack) {
        return stack.isIn(ItematicItemTags.VILLAGER_GATHERABLE_ITEMS);
    }

    @Redirect(
        method = "canGather",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableSet;contains(Ljava/lang/Object;)Z",
            remap = false
        )
    )
    private boolean canGatherContainsUseItemTag(ImmutableSet<Item> instance, Object o, ItemStack stack) {
        return stack.isIn(this.getVillagerData().getProfession().gatherableItemsTag());
    }
}
