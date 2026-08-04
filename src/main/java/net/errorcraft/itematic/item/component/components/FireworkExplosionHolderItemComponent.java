package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.FireworkExplosion;

public class FireworkExplosionHolderItemComponent implements ItemComponent<FireworkExplosionHolderItemComponent> {
    public static final FireworkExplosionHolderItemComponent INSTANCE = new FireworkExplosionHolderItemComponent();
    public static final Codec<FireworkExplosionHolderItemComponent> CODEC = MapCodec.unitCodec(INSTANCE);

    private FireworkExplosionHolderItemComponent() {}

    @Override
    public ItemComponentType<FireworkExplosionHolderItemComponent> type() {
        return ItemComponentTypes.FIREWORK_EXPLOSION_HOLDER;
    }

    @Override
    public Codec<FireworkExplosionHolderItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.FIREWORK_EXPLOSION, FireworkExplosion.DEFAULT);
    }
}
