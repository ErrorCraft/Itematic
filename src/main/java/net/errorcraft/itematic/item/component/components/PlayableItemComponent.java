package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.use.provider.providers.PlayableIntegerProvider;
import net.errorcraft.itematic.mixin.item.GoatHornItemAccessor;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.SharedConstants;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.InstrumentComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Instrument;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Optional;

public record PlayableItemComponent(RegistryEntry<Instrument> defaultInstrument) implements ItemComponent<PlayableItemComponent> {
    public static final Codec<PlayableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.INSTRUMENT).fieldOf("default_instrument").forGetter(PlayableItemComponent::defaultInstrument)
    ).apply(instance, PlayableItemComponent::new));

    public static ItemComponent<?>[] of(RegistryEntry<Instrument> defaultInstrument) {
        return new ItemComponent<?>[] {
            UseableItemComponent.builder()
                .useFor(PlayableIntegerProvider.INSTANCE)
                .animation(UseAction.TOOT_HORN)
                .build(),
            new PlayableItemComponent(defaultInstrument)
        };
    }

    @Override
    public ItemComponentType<PlayableItemComponent> type() {
        return ItemComponentTypes.PLAYABLE;
    }

    @Override
    public Codec<PlayableItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        return this.instrument(stack, user.getRegistryManager())
            .map(RegistryEntry::value)
            .map(instrument -> {
                GoatHornItemAccessor.playSound(world, user, instrument);
                user.getItemCooldownManager().set(stack, MathHelper.floor(instrument.useDuration() * SharedConstants.TICKS_PER_SECOND));
                user.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
                return ItemResult.CONSUME;
            }).orElse(ItemResult.PASS);
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.INSTRUMENT, new InstrumentComponent(this.defaultInstrument));
    }

    public Optional<RegistryEntry<Instrument>> instrument(ItemStack stack, RegistryWrapper.WrapperLookup lookup) {
        InstrumentComponent instrument = stack.get(DataComponentTypes.INSTRUMENT);
        if (instrument == null) {
            return Optional.empty();
        }

        return instrument.getInstrument(lookup);
    }
}
