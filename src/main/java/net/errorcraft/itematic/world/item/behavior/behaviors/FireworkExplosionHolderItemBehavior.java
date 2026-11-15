package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.FireworkExplosion;

public class FireworkExplosionHolderItemBehavior implements ItemBehavior {
    public static final FireworkExplosionHolderItemBehavior INSTANCE = new FireworkExplosionHolderItemBehavior();
    public static final Codec<FireworkExplosionHolderItemBehavior> CODEC = MapCodec.unitCodec(INSTANCE);

    private FireworkExplosionHolderItemBehavior() {}

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.FIREWORK_EXPLOSION, FireworkExplosion.DEFAULT);
    }
}
