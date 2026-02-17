package net.errorcraft.itematic.recipe.display.slot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.display.DisplayedItemFactory;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.display.SlotDisplayContexts;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.context.ContextParameterMap;

import java.util.stream.Stream;

public record PotionSlotDisplay(RegistryEntry<Potion> potion) implements SlotDisplay {
    public static final MapCodec<PotionSlotDisplay> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.POTION).fieldOf("potion").forGetter(PotionSlotDisplay::potion)
    ).apply(instance, PotionSlotDisplay::new));
    public static final PacketCodec<RegistryByteBuf, PotionSlotDisplay> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.registryEntry(RegistryKeys.POTION), PotionSlotDisplay::potion,
        PotionSlotDisplay::new
    );

    @Override
    public <T> Stream<T> appendStacks(ContextParameterMap parameters, DisplayedItemFactory<T> factory) {
        if (!(factory instanceof DisplayedItemFactory.FromStack<T> fromStack)) {
            return Stream.empty();
        }

        RegistryWrapper.WrapperLookup lookup = parameters.getNullable(SlotDisplayContexts.REGISTRIES);
        if (lookup == null) {
            return Stream.empty();
        }

        return lookup.getOrThrow(RegistryKeys.ITEM)
            .getOptional(ItemKeys.POTION)
            .map(ItemStack::new)
            .map(stack -> PotionContentsComponentUtil.setPotion(stack, this.potion))
            .map(fromStack::toDisplayed)
            .stream();
    }

    @Override
    public Serializer<? extends SlotDisplay> serializer() {
        return ItematicSlotDisplaySerializers.POTION;
    }
}
