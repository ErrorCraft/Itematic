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
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.InstrumentComponent;
import net.minecraft.world.level.Level;
import java.util.Optional;

public record PlayableItemComponent(Holder<Instrument> defaultInstrument) implements ItemComponent<PlayableItemComponent> {
    public static final Codec<PlayableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.create(Registries.INSTRUMENT).fieldOf("default_instrument").forGetter(PlayableItemComponent::defaultInstrument)
    ).apply(instance, PlayableItemComponent::new));

    public static ItemComponent<?>[] of(Holder<Instrument> defaultInstrument) {
        return new ItemComponent<?>[] {
            UseableItemComponent.builder()
                .useFor(PlayableIntegerProvider.INSTANCE)
                .animation(ItemUseAnimation.TOOT_HORN)
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
    public ItemResult use(Level world, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        return this.instrument(stack, user.registryAccess())
            .map(Holder::value)
            .map(instrument -> {
                GoatHornItemAccessor.playSound(world, user, instrument);
                user.getCooldowns().addCooldown(stack, Mth.floor(instrument.useDuration() * SharedConstants.TICKS_PER_SECOND));
                user.awardStat(Stats.ITEM_USED.itematic$getOrCreateStat(stack.getItemHolder()));
                return ItemResult.CONSUME;
            }).orElse(ItemResult.PASS);
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.INSTRUMENT, new InstrumentComponent(this.defaultInstrument));
    }

    public Optional<Holder<Instrument>> instrument(ItemStack stack, HolderLookup.Provider lookup) {
        InstrumentComponent instrument = stack.get(DataComponents.INSTRUMENT);
        if (instrument == null) {
            return Optional.empty();
        }

        return instrument.unwrap(lookup);
    }
}
