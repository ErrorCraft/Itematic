package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.OminousBottleItem;
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
    public void finishUsing(World world, LivingEntity user, ItemStack stack, int usedTicks, ItemStackConsumer resultStackConsumer) {
        if (world.isClient()) {
            return;
        }

        user.removeStatusEffect(StatusEffects.BAD_OMEN);
        int ominousAmplifier = stack.getOrDefault(DataComponentTypes.OMINOUS_BOTTLE_AMPLIFIER, 0);
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.BAD_OMEN, OminousBottleItem.BAD_OMEN_LENGTH, ominousAmplifier, false, false, true));
    }
}
