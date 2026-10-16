package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;

public class OminousEffectProviderItemBehavior implements ItemBehavior<OminousEffectProviderItemBehavior> {
    public static final OminousEffectProviderItemBehavior INSTANCE = new OminousEffectProviderItemBehavior();
    public static final Codec<OminousEffectProviderItemBehavior> CODEC = MapCodec.unitCodec(INSTANCE);

    private OminousEffectProviderItemBehavior() {}

    @Override
    public ItemBehaviorType<OminousEffectProviderItemBehavior> type() {
        return ItemBehaviorType.OMINOUS_EFFECT_PROVIDER;
    }
}
