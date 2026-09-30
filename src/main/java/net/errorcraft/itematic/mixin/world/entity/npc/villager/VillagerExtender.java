package net.errorcraft.itematic.mixin.world.entity.npc.villager;

import com.google.common.collect.ImmutableSet;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.minecraft.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Villager.class)
public abstract class VillagerExtender extends MobExtender {
    @Shadow
    public abstract VillagerData getVillagerData();

    protected VillagerExtender(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "wantsToPickUp",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableSet;contains(Ljava/lang/Object;)Z"
        )
    )
    private boolean isRequestedItemCheckTag(ImmutableSet<Item> instance, Object o, @Local(name = "itemStack", argsOnly = true) ItemStack itemStack) {
        TagKey<Item> requestedItems = this.getVillagerData().profession().value().itematic$gatherableItems();
        if (requestedItems == null) {
            return false;
        }

        return itemStack.is(requestedItems);
    }

    @Redirect(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isVillagerSpawnEggCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.VILLAGER_SPAWN_EGG);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.VILLAGER_SPAWN_EGG;
    }
}
