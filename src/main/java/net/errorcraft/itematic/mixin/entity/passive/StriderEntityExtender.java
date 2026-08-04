package net.errorcraft.itematic.mixin.entity.passive;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Strider.class)
public abstract class StriderEntityExtender extends MobEntityExtender {
    protected StriderEntityExtender(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getControllingPassenger",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;isHolding(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isHoldingForWarpedFungusOnAStickUseRegistryKeyCheck(Player instance, Item item) {
        return instance.itematic$isHolding(ItemKeys.WARPED_FUNGUS_ON_A_STICK);
    }

    @Redirect(
        method = "finalizeSpawn",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForWarpedFungusOnAStickUseCreateStack(ItemLike item, @Local(argsOnly = true) ServerLevelAccessor world) {
        return world.itematic$createStack(ItemKeys.WARPED_FUNGUS_ON_A_STICK);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemKeys.STRIDER_SPAWN_EGG;
    }
}
