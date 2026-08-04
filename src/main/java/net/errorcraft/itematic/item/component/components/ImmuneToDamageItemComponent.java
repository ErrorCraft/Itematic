package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.component.DamageResistant;

public record ImmuneToDamageItemComponent(TagKey<DamageType> damage) implements ItemComponent<ImmuneToDamageItemComponent> {
    public static final Codec<ImmuneToDamageItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TagKey.codec(Registries.DAMAGE_TYPE).fieldOf("damage").forGetter(ImmuneToDamageItemComponent::damage)
    ).apply(instance, ImmuneToDamageItemComponent::new));

    public static ImmuneToDamageItemComponent of(TagKey<DamageType> damage) {
        return new ImmuneToDamageItemComponent(damage);
    }

    @Override
    public ItemComponentType<ImmuneToDamageItemComponent> type() {
        return ItemComponentTypes.IMMUNE_TO_DAMAGE;
    }

    @Override
    public Codec<ImmuneToDamageItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.DAMAGE_RESISTANT, new DamageResistant(this.damage));
    }
}
