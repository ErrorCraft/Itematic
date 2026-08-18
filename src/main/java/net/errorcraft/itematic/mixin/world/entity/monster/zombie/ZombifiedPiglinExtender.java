package net.errorcraft.itematic.mixin.world.entity.monster.zombie;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ZombifiedPiglin.class)
public abstract class ZombifiedPiglinExtender extends MobExtender {
    public ZombifiedPiglinExtender(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
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
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForGoldenWeaponUseCreateStack(ItemLike item, @Share("spearChance") LocalIntRef spearChance) {
        return this.level().itematic$createStack(
            spearChance.get() == 0
                ? ItemIds.GOLDEN_SPEAR
                : ItemIds.GOLDEN_SWORD
        );
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.ZOMBIFIED_PIGLIN_SPAWN_EGG;
    }
}
