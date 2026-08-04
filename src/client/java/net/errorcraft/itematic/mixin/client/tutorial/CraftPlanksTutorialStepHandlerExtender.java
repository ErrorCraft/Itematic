package net.errorcraft.itematic.mixin.client.tutorial;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.tutorial.CraftPlanksTutorialStep;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CraftPlanksTutorialStep.class)
public class CraftPlanksTutorialStepHandlerExtender {
    @Redirect(
        method = "hasCraftedPlanksPreviously",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/DefaultedRegistry;getTagOrEmpty(Lnet/minecraft/tags/TagKey;)Ljava/lang/Iterable;"
        )
    )
    private static Iterable<Holder<Item>> iterateEntriesUseDynamicRegistry(DefaultedRegistry<Item> instance, TagKey<Item> tag, LocalPlayer player) {
        return player.level()
            .itematic$getItemAccess()
            .iterateEntries(tag);
    }

    @Redirect(
        method = "hasCraftedPlanksPreviously",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;"
        )
    )
    private static <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key, @Local Holder<Item> entry) {
        return instance.itematic$getOrCreateStat(entry);
    }
}
