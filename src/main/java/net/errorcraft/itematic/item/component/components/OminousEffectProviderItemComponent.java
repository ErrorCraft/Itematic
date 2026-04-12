package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.OminousBottleAmplifierComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class OminousEffectProviderItemComponent implements ItemComponent<OminousEffectProviderItemComponent> {
    public static final OminousEffectProviderItemComponent INSTANCE = new OminousEffectProviderItemComponent();
    public static final Codec<OminousEffectProviderItemComponent> CODEC = Codec.unit(INSTANCE);

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
    public void finishUsing(World world, LivingEntity user, ItemStack stack, int usedTicks, ItemStackExchanger stackExchanger) {
        OminousBottleAmplifierComponent ominousAmplifier = stack.get(DataComponentTypes.OMINOUS_BOTTLE_AMPLIFIER);
        if (ominousAmplifier != null) {
            ominousAmplifier.onConsume(world, user, stack, null);
        }
    }
}
