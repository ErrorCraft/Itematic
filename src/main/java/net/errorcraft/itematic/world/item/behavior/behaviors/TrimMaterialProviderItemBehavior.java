package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ProvidesTrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

public record TrimMaterialProviderItemBehavior(Holder<TrimMaterial> trimMaterial) implements ItemBehavior<TrimMaterialProviderItemBehavior> {
    public static final Codec<TrimMaterialProviderItemBehavior> CODEC = TrimMaterial.CODEC.xmap(
        TrimMaterialProviderItemBehavior::new,
        TrimMaterialProviderItemBehavior::trimMaterial
    );

    public static TrimMaterialProviderItemBehavior of(Holder<TrimMaterial> trimMaterial) {
        return new TrimMaterialProviderItemBehavior(trimMaterial);
    }

    @Override
    public ItemBehaviorType<TrimMaterialProviderItemBehavior> type() {
        return ItemBehaviorType.TRIM_MATERIAL_PROVIDER;
    }

    @Override
    public Codec<TrimMaterialProviderItemBehavior> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(this.trimMaterial));
    }
}
