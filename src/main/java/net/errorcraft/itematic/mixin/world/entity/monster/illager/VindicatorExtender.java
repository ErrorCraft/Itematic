package net.errorcraft.itematic.mixin.world.entity.monster.illager;

import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.illager.AbstractIllager;
import net.minecraft.world.entity.monster.illager.Vindicator;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Vindicator.class)
public abstract class VindicatorExtender extends MobExtender {
    protected VindicatorExtender(EntityType<? extends AbstractIllager> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = {
            "populateDefaultEquipmentSlots",
            "applyRaidBuffs"
        },
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForIronAxeUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.IRON_AXE);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.VINDICATOR_SPAWN_EGG;
    }
}
