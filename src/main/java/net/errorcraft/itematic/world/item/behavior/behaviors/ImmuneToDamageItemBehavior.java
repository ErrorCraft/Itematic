package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.component.DamageResistant;

public record ImmuneToDamageItemBehavior(TagKey<DamageType> damage) implements ItemBehavior<ImmuneToDamageItemBehavior> {
    public static final Codec<ImmuneToDamageItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TagKey.codec(Registries.DAMAGE_TYPE).fieldOf("damage").forGetter(ImmuneToDamageItemBehavior::damage)
    ).apply(instance, ImmuneToDamageItemBehavior::new));

    public static ImmuneToDamageItemBehavior of(TagKey<DamageType> damage) {
        return new ImmuneToDamageItemBehavior(damage);
    }

    @Override
    public ItemBehaviorType<ImmuneToDamageItemBehavior> type() {
        return ItemBehaviorType.IMMUNE_TO_DAMAGE;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.DAMAGE_RESISTANT, new DamageResistant(this.damage));
    }
}
