package net.errorcraft.itematic.mixin.entity.mob;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ZombifiedPiglinEntity.class)
public abstract class ZombifiedPiglinEntityExtender extends MobEntityExtender {
    public ZombifiedPiglinEntityExtender(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(
        method = "initEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"
        )
    )
    private int storeSpearChance(int original, @Share("spearChance") LocalIntRef spearChance) {
        spearChance.set(original);
        return original;
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackForGoldenWeaponUseCreateStack(ItemConvertible item, @Share("spearChance") LocalIntRef spearChance) {
        return this.getEntityWorld().itematic$createStack(spearChance.get() == 0
            ? ItemKeys.GOLDEN_SPEAR
            : ItemKeys.GOLDEN_SWORD
        );
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.ZOMBIFIED_PIGLIN_SPAWN_EGG;
    }
}
