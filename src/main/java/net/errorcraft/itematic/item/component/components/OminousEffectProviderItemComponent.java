package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.OminousBottleAmplifier;
import net.minecraft.world.level.Level;

public class OminousEffectProviderItemComponent implements ItemComponent<OminousEffectProviderItemComponent> {
    public static final OminousEffectProviderItemComponent INSTANCE = new OminousEffectProviderItemComponent();
    public static final Codec<OminousEffectProviderItemComponent> CODEC = MapCodec.unitCodec(INSTANCE);

    private OminousEffectProviderItemComponent() {}

    @Override
    public ItemComponentType<OminousEffectProviderItemComponent> type() {
        return ItemComponentTypes.OMINOUS_EFFECT_PROVIDER;
    }

    @Override
    public Codec<OminousEffectProviderItemComponent> codec() {
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
