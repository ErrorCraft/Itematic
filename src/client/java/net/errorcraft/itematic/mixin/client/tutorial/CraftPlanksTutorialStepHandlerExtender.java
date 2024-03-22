package net.errorcraft.itematic.mixin.client.tutorial;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.tutorial.CraftPlanksTutorialStepHandler;
import net.minecraft.item.Item;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CraftPlanksTutorialStepHandler.class)
public class CraftPlanksTutorialStepHandlerExtender {
    @Redirect(
        method = "hasCrafted",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;iterateEntries(Lnet/minecraft/registry/tag/TagKey;)Ljava/lang/Iterable;"
        )
    )
    private static Iterable<RegistryEntry<Item>> iterateEntriesUseDynamicRegistry(DefaultedRegistry<Item> instance, TagKey<Item> tag, ClientPlayerEntity player) {
        return player.getWorld()
            .itematic$getItemAccess()
            .iterateEntries(tag);
    }

    @Redirect(
        method = "hasCrafted",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;"
        )
    )
    private static <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key, @Local RegistryEntry<Item> entry) {
        return instance.itematic$getOrCreateStat(entry);
    }
}
