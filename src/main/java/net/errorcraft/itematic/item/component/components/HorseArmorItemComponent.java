package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.armor.ArmorMaterial;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;

public record HorseArmorItemComponent(RegistryEntry<ArmorMaterial> material) implements ItemComponent {
    public static final Codec<HorseArmorItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(ItematicRegistryKeys.ARMOR_MATERIAL).fieldOf("material").forGetter(HorseArmorItemComponent::material)
    ).apply(instance, HorseArmorItemComponent::new));

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.HORSE_ARMOR;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
        return CODEC;
    }
}
