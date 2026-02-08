package net.errorcraft.itematic.mixin.loot.entry;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.Item;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.TagEntry;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TagEntry.class)
public class TagEntryExtender {
    @Redirect(
        method = {
            "generateLoot",
            "grow"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;iterateEntries(Lnet/minecraft/registry/tag/TagKey;)Ljava/lang/Iterable;"
        )
    )
    private Iterable<RegistryEntry<Item>> iterateEntriesUseDynamicRegistry(DefaultedRegistry<Item> instance, TagKey<Item> tagKey, @Local(argsOnly = true) LootContext context) {
        return context.getWorld().itematic$getItemAccess().iterateEntries(tagKey);
    }
}
