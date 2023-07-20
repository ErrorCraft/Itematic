package net.errorcraft.itematic.mixin.village;

import net.errorcraft.itematic.access.village.VillagerProfessionAccess;
import net.errorcraft.itematic.item.ItemTagsUtil;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerProfession.class)
public class VillagerProfessionExtender implements VillagerProfessionAccess {
    @Shadow
    @Final
    public static VillagerProfession FARMER;

    private TagKey<Item> gatherableItemsTag;

    @Inject(
        method = "<clinit>",
        at = @At("TAIL")
    )
    private static void staticSetGatherableItemsTag(CallbackInfo info) {
        ((VillagerProfessionExtender)(Object) FARMER).gatherableItemsTag = ItemTagsUtil.FARMER_VILLAGER_GATHERABLE_ITEMS;
    }

    @Override
    public TagKey<Item> gatherableItemsTag() {
        return this.gatherableItemsTag;
    }
}
