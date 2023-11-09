package net.errorcraft.itematic.mixin.loot.entry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.TagEntry;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

@Mixin(TagEntry.class)
public class TagEntryExtender {
    @Redirect(
        method = "generateLoot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;iterateEntries(Lnet/minecraft/registry/tag/TagKey;)Ljava/lang/Iterable;"
        )
    )
    private Iterable<RegistryEntry<Item>> generateLootUseDynamicRegistry(DefaultedRegistry<Item> instance, TagKey<Item> tagKey, Consumer<ItemStack> lootConsumer, LootContext context) {
        return context.getWorld().itematic$getItemAccess().iterateEntries(tagKey);
    }

    @Redirect(
        method = "grow",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;iterateEntries(Lnet/minecraft/registry/tag/TagKey;)Ljava/lang/Iterable;"
        )
    )
    private Iterable<RegistryEntry<Item>> growUseDynamicRegistry(DefaultedRegistry<Item> instance, TagKey<Item> tagKey, LootContext context) {
        return context.getWorld().itematic$getItemAccess().iterateEntries(tagKey);
    }
}
