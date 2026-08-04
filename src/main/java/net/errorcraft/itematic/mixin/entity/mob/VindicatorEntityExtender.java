package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.illager.AbstractIllager;
import net.minecraft.world.entity.monster.illager.Vindicator;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Vindicator.class)
public abstract class VindicatorEntityExtender extends MobEntityExtender {
    protected VindicatorEntityExtender(EntityType<? extends AbstractIllager> entityType, Level world) {
        super(entityType, world);
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
        return this.level().itematic$createStack(ItemKeys.IRON_AXE);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemKeys.VINDICATOR_SPAWN_EGG;
    }
}
