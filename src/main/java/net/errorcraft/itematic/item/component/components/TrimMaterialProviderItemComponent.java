package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ProvidesTrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

public record TrimMaterialProviderItemComponent(Holder<TrimMaterial> trimMaterial) implements ItemComponent<TrimMaterialProviderItemComponent> {
    public static final Codec<TrimMaterialProviderItemComponent> CODEC = TrimMaterial.CODEC.xmap(
        TrimMaterialProviderItemComponent::new,
        TrimMaterialProviderItemComponent::trimMaterial
    );

    public static TrimMaterialProviderItemComponent of(Holder<TrimMaterial> trimMaterial) {
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
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(this.trimMaterial));
    }
}
