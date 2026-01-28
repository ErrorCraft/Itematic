package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.OminousBottleAmplifierComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

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
        OminousBottleAmplifierComponent ominousAmplifier = stack.get(DataComponentTypes.OMINOUS_BOTTLE_AMPLIFIER);
        if (ominousAmplifier != null) {
            ominousAmplifier.onConsume(world, user, stack, null);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        OminousBottleAmplifierComponent ominousAmplifier = stack.get(DataComponentTypes.OMINOUS_BOTTLE_AMPLIFIER);
        if (ominousAmplifier != null) {
            ominousAmplifier.appendTooltip(context, tooltip::add, type);
        }
    }
}
