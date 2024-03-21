package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ArmorItemComponent;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(HorseEntity.class)
public abstract class HorseEntityExtender extends MobEntityExtender {
    protected HorseEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean isHorseArmor(ItemStack item) {
        return item.itematic$getComponent(ItemComponentTypes.ARMOR)
            .flatMap(ArmorItemComponent::armorType)
            .map(type -> type == AnimalArmorItem.Type.EQUESTRIAN)
            .orElse(false);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.HORSE_SPAWN_EGG;
    }
}
