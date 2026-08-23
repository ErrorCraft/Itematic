package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.mixin.world.item.InstrumentItemAccessor;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.PlayableUseDurationProvider;
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

public record PlayableItemBehavior(Holder<Instrument> defaultInstrument) implements ItemBehavior<PlayableItemBehavior> {
    public static final Codec<PlayableItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.create(Registries.INSTRUMENT).fieldOf("default_instrument").forGetter(PlayableItemBehavior::defaultInstrument)
    ).apply(instance, PlayableItemBehavior::new));

    public static ItemBehavior<?>[] of(Holder<Instrument> defaultInstrument) {
        return new ItemBehavior<?>[] {
            UseableItemBehavior.builder()
                .useFor(PlayableUseDurationProvider.INSTANCE)
                .animation(ItemUseAnimation.TOOT_HORN)
                .build(),
            new PlayableItemBehavior(defaultInstrument)
        };
    }

    @Override
    public ItemBehaviorType<PlayableItemBehavior> type() {
        return ItemBehaviorType.PLAYABLE;
    }

    @Override
    public ItemResult use(Level level, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        return this.instrument(stack, user.registryAccess())
            .map(Holder::value)
            .map(instrument -> {
                InstrumentItemAccessor.playSound(level, user, instrument);
                user.getCooldowns().addCooldown(stack, Mth.floor(instrument.useDuration() * SharedConstants.TICKS_PER_SECOND));
                user.awardStat(Stats.ITEM_USED.itematic$get(stack.typeHolder()));
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
