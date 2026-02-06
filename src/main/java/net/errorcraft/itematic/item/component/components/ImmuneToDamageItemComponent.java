package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DamageResistantComponent;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public record ImmuneToDamageItemComponent(TagKey<DamageType> damage) implements ItemComponent<ImmuneToDamageItemComponent> {
    public static final Codec<ImmuneToDamageItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TagKey.unprefixedCodec(RegistryKeys.DAMAGE_TYPE).fieldOf("damage").forGetter(ImmuneToDamageItemComponent::damage)
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
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.DAMAGE_RESISTANT, new DamageResistantComponent(this.damage));
    }
}
