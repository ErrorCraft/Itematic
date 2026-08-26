package net.errorcraft.itematic.mixin.world.entity.animal.equine;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Horse.class)
public abstract class HorseExtender extends MobExtender {
    protected HorseExtender(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
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
        return ItemIds.HORSE_SPAWN_EGG;
    }
}
