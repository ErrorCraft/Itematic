package net.errorcraft.itematic.mixin.block.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class VaultBlockEntityExtender {
    @Mixin(VaultBlockEntity.Server.class)
    public static class ServerExtender {
        @Redirect(
            method = "tryUnlock",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;"
            )
        )
        @SuppressWarnings("unchecked")
        private static <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key, @Local(argsOnly = true) ItemStack stack) {
            return instance.itematic$getOrCreateStat(stack.getRegistryEntry());
        }
    }
}
