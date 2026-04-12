package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;

public record FireworkExplosionHolderItemComponent() implements ItemComponent<FireworkExplosionHolderItemComponent> {
    public static final FireworkExplosionHolderItemComponent INSTANCE = new FireworkExplosionHolderItemComponent();
    public static final Codec<FireworkExplosionHolderItemComponent> CODEC = Codec.unit(INSTANCE);

    @Override
    public ItemComponentType<FireworkExplosionHolderItemComponent> type() {
        return ItemComponentTypes.FIREWORK_EXPLOSION_HOLDER;
    }

    @Override
    public Codec<FireworkExplosionHolderItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.FIREWORK_EXPLOSION, FireworkExplosionComponent.DEFAULT);
    }
}
