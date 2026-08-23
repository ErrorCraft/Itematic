package net.errorcraft.itematic.mixin.world.level.block.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class VaultBlockEntityExtender {
    @Mixin(VaultBlockEntity.Server.class)
    public static class ServerExtender {
        @Redirect(
            method = "tryInsertKey",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;"
            )
        )
        private static <T> Stat<Item> getStatUseHolder(StatType<Item> instance, T argument, @Local(name = "stackToInsert", argsOnly = true) ItemStack stackToInsert) {
            return instance.itematic$get(stackToInsert.typeHolder());
        }
    }
}
