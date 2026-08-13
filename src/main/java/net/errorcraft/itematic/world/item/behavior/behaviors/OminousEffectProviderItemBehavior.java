package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.OminousBottleAmplifier;
import net.minecraft.world.level.Level;

public class OminousEffectProviderItemBehavior implements ItemBehavior<OminousEffectProviderItemBehavior> {
    public static final OminousEffectProviderItemBehavior INSTANCE = new OminousEffectProviderItemBehavior();
    public static final Codec<OminousEffectProviderItemBehavior> CODEC = MapCodec.unitCodec(INSTANCE);

    private OminousEffectProviderItemBehavior() {}

    @Override
    public ItemBehaviorType<OminousEffectProviderItemBehavior> type() {
        return ItemBehaviorType.OMINOUS_EFFECT_PROVIDER;
    }

    @Override
    public Codec<OminousEffectProviderItemBehavior> codec() {
        return CODEC;
    }

    @Override
    public void finishUsing(Level world, LivingEntity user, ItemStack stack, int usedTicks, ItemStackExchanger stackExchanger) {
        OminousBottleAmplifier ominousAmplifier = stack.get(DataComponents.OMINOUS_BOTTLE_AMPLIFIER);
        if (ominousAmplifier != null) {
            ominousAmplifier.onConsume(world, user, stack, null);
        }
    }
}
