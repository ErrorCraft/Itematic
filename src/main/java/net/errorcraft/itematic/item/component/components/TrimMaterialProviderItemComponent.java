package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProvidesTrimMaterialComponent;
import net.minecraft.item.equipment.trim.ArmorTrimMaterial;
import net.minecraft.registry.entry.RegistryEntry;

public record TrimMaterialProviderItemComponent(RegistryEntry<ArmorTrimMaterial> trimMaterial) implements ItemComponent<TrimMaterialProviderItemComponent> {
    public static final Codec<TrimMaterialProviderItemComponent> CODEC = ArmorTrimMaterial.ENTRY_CODEC.xmap(
        TrimMaterialProviderItemComponent::new,
        TrimMaterialProviderItemComponent::trimMaterial
    );

    public static TrimMaterialProviderItemComponent of(RegistryEntry<ArmorTrimMaterial> trimMaterial) {
        return new TrimMaterialProviderItemComponent(trimMaterial);
    }

    @Override
    public ItemComponentType<TrimMaterialProviderItemComponent> type() {
        return ItemComponentTypes.TRIM_MATERIAL_PROVIDER;
    }

    @Override
    public Codec<TrimMaterialProviderItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterialComponent(this.trimMaterial));
    }
}
