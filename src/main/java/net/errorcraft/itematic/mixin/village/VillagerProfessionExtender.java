package net.errorcraft.itematic.mixin.village;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.access.village.VillagerProfessionAccess;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.village.VillagerProfession;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VillagerProfession.class)
public class VillagerProfessionExtender implements VillagerProfessionAccess {
    @Unique
    private TagKey<Item> gatherableItems;

    @ModifyExpressionValue(
        method = "registerAndGetDefault",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/village/VillagerProfession;register(Lnet/minecraft/registry/Registry;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/RegistryKey;Lcom/google/common/collect/ImmutableSet;Lcom/google/common/collect/ImmutableSet;Lnet/minecraft/sound/SoundEvent;)Lnet/minecraft/village/VillagerProfession;"
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
