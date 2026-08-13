package net.errorcraft.itematic.mixin.entity.mob;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ZombifiedPiglin.class)
public abstract class ZombifiedPiglinEntityExtender extends MobEntityExtender {
    public ZombifiedPiglinEntityExtender(EntityType<? extends Zombie> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"
        )
    )
    private int storeSpearChance(int original, @Share("spearChance") LocalIntRef spearChance) {
        spearChance.set(original);
        return original;
    }

    @Redirect(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "net/minecraft/world/item/ItemStack"
        )
    )
    private ItemStack newItemStackForGoldenWeaponUseCreateStack(ItemLike item, @Share("spearChance") LocalIntRef spearChance) {
        return this.level().itematic$createStack(spearChance.get() == 0
            ? ItemIds.GOLDEN_SPEAR
            : ItemIds.GOLDEN_SWORD
        );
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.ZOMBIFIED_PIGLIN_SPAWN_EGG;
    }
}
