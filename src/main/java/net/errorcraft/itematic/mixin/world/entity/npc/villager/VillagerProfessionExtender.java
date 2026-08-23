package net.errorcraft.itematic.mixin.world.entity.npc.villager;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.access.world.entity.npc.villager.VillagerProfessionAccess;
import net.errorcraft.itematic.tags.ItematicItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VillagerProfession.class)
public class VillagerProfessionExtender implements VillagerProfessionAccess {
    @Unique
    @Nullable
    private TagKey<Item> gatherableItems;

    @ModifyExpressionValue(
        method = "bootstrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/npc/villager/VillagerProfession;register(Lnet/minecraft/core/Registry;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/resources/ResourceKey;Lcom/google/common/collect/ImmutableSet;Lcom/google/common/collect/ImmutableSet;Lnet/minecraft/sounds/SoundEvent;Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;)Lnet/minecraft/world/entity/npc/villager/VillagerProfession;"
        )
    )
    private static VillagerProfession setGatherableItemsTag(VillagerProfession original) {
        original.itematic$setGatherableItems(ItematicItemTags.FARMER_VILLAGER_GATHERABLE_ITEMS);
        return original;
    }

    @Override
    public @Nullable TagKey<Item> itematic$gatherableItems() {
        return this.gatherableItems;
    }

    @Override
    public void itematic$setGatherableItems(TagKey<Item> gatherableItems) {
        this.gatherableItems = gatherableItems;
    }
}
