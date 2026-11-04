package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.MobVisibility;
import net.minecraft.world.item.equipment.Equippable;

import java.util.Optional;

public record EquipmentItemBehavior(Equippable equippable, Optional<MobVisibility> mobVisibility) implements ItemBehavior<EquipmentItemBehavior> {
    public static final Codec<EquipmentItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        MapCodec.assumeMapUnsafe(Equippable.CODEC).forGetter(EquipmentItemBehavior::equippable),
        MobVisibility.CODEC.optionalFieldOf("mob_visibility").forGetter(EquipmentItemBehavior::mobVisibility)
    ).apply(instance, EquipmentItemBehavior::new));
    public static final ScopedValue<ActionContext> ON_EQUIP_ACTION_CONTEXT = ScopedValue.newInstance();

    public static EquipmentItemBehavior of(Equippable equippable) {
        return new EquipmentItemBehavior(equippable, Optional.empty());
    }

    public static EquipmentItemBehavior of(Equippable equippable, MobVisibility mobVisibility) {
        return new EquipmentItemBehavior(equippable, Optional.of(mobVisibility));
    }

    @Override
    public ItemBehaviorType<EquipmentItemBehavior> type() {
        return ItemBehaviorType.EQUIPMENT;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.EQUIPPABLE, this.equippable);
        this.mobVisibility.ifPresent(mobVisibility -> builder.set(DataComponents.MOB_VISIBILITY, mobVisibility));
    }
}
