package net.errorcraft.itematic.mixin.world.entity.animal.panda;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.panda.Panda;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Panda.class)
public abstract class PandaExtender extends MobExtender {
    protected PandaExtender(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
        method = "mobInteract",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;I)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseHolder(ItemLike item, int count, Operation<ItemStack> original, @Local(name = "interactionItemStack") ItemStack interactionItemStack) {
        return new ItemStack(interactionItemStack.typeHolder(), count);
    }

    @WrapOperation(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;isHolding(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isHoldingGoldenDandelionCheckId(Player instance, Item item, Operation<Boolean> original) {
        return instance.itematic$isHolding(ItemIds.GOLDEN_DANDELION);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.PANDA_SPAWN_EGG;
    }
}
