package net.errorcraft.itematic.recipe.display.slot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.display.DisplayContentsFactory;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import java.util.stream.Stream;

public record PotionSlotDisplay(Holder<Potion> potion) implements SlotDisplay {
    public static final MapCodec<PotionSlotDisplay> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RegistryFixedCodec.create(Registries.POTION).fieldOf("potion").forGetter(PotionSlotDisplay::potion)
    ).apply(instance, PotionSlotDisplay::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, PotionSlotDisplay> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.holderRegistry(Registries.POTION), PotionSlotDisplay::potion,
        PotionSlotDisplay::new
    );

    @Override
    public <T> Stream<T> resolve(ContextMap parameters, DisplayContentsFactory<T> factory) {
        if (!(factory instanceof DisplayContentsFactory.ForStacks<T> fromStack)) {
            return Stream.empty();
        }

        HolderLookup.Provider lookup = parameters.getOptional(SlotDisplayContext.REGISTRIES);
        if (lookup == null) {
            return Stream.empty();
        }

        return lookup.lookupOrThrow(Registries.ITEM)
            .get(ItematicItemTags.BREWING_INPUTS)
            .stream()
            .flatMap(HolderSet.ListBacked::stream)
            .map(ItemStack::new)
            .map(stack -> PotionContentsComponentUtil.setPotion(stack, this.potion))
            .map(fromStack::forStack);
    }

    @Override
    public Type<? extends SlotDisplay> type() {
        return ItematicSlotDisplaySerializers.POTION;
    }
}
