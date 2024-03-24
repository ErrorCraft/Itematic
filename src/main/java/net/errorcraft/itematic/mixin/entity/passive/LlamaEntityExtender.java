package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LlamaEntity.class)
public abstract class LlamaEntityExtender extends MobEntityExtender {
    protected LlamaEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "receiveFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean isOfForWheatUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.WHEAT);
    }

    @Redirect(
        method = "receiveFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/block/Blocks;HAY_BLOCK:Lnet/minecraft/block/Block;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForHayBlockUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.HAY_BLOCK);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.LLAMA_SPAWN_EGG;
    }
}
