package net.errorcraft.itematic.mixin.world.entity.animal.equine;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.Llama;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Llama.class)
public abstract class LlamaExtender extends MobExtender {
    protected LlamaExtender(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
        method = "handleEating",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
            ordinal = 0
        )
    )
    private boolean isWheatCheckId(ItemStack instance, Object o, Operation<Boolean> original) {
        return instance.is(ItemIds.WHEAT);
    }

    @WrapOperation(
        method = "handleEating",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;HAY_BLOCK:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isHayBlockCheckId(ItemStack instance, Object o, Operation<Boolean> original) {
        return instance.is(ItemIds.HAY_BLOCK);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.LLAMA_SPAWN_EGG;
    }
}
