package net.errorcraft.itematic.mixin.loot.entry;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TagEntry.class)
public class TagEntryExtender {
    @Redirect(
        method = {
            "createItemStack",
            "expandTag(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)Z"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/DefaultedRegistry;getTagOrEmpty(Lnet/minecraft/tags/TagKey;)Ljava/lang/Iterable;"
        )
    )
    private Iterable<Holder<Item>> iterateEntriesUseDynamicRegistry(DefaultedRegistry<Item> instance, TagKey<Item> tagKey, @Local(argsOnly = true) LootContext context) {
        return context.getLevel().itematic$getItemAccess().iterateTag(tagKey);
    }
}
