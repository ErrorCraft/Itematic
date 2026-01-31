package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.use.provider.providers.PlayableIntegerProvider;
import net.errorcraft.itematic.mixin.item.GoatHornItemAccessor;
import net.minecraft.SharedConstants;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Instrument;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public record PlayableItemComponent(TagKey<Instrument> instruments) implements ItemComponent<PlayableItemComponent> {
    public static final Codec<PlayableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TagKey.unprefixedCodec(RegistryKeys.INSTRUMENT).fieldOf("instruments").forGetter(PlayableItemComponent::instruments)
    ).apply(instance, PlayableItemComponent::new));

    public static ItemComponent<?>[] of(TagKey<Instrument> instruments) {
        return new ItemComponent<?>[] {
            UseableItemComponent.builder()
                .useFor(PlayableIntegerProvider.INSTANCE)
                .animation(UseAction.TOOT_HORN)
                .build(),
            new PlayableItemComponent(instruments)
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
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
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
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        RegistryWrapper.WrapperLookup lookup = context.getRegistryLookup();
        if (lookup == null) {
            return;
        }

        this.instrument(stack, lookup)
            .flatMap(RegistryEntry::getKey)
            .map(RegistryKey::getValue)
            .ifPresent(id -> tooltip.add(Text.translatable(Util.createTranslationKey("instrument", id)).formatted(Formatting.GRAY)));
    }

    public Optional<? extends RegistryEntry<Instrument>> instrument(ItemStack stack, RegistryWrapper.WrapperLookup lookup) {
        RegistryEntry<Instrument> instrument = stack.get(DataComponentTypes.INSTRUMENT);
        if (instrument != null) {
            return Optional.of(instrument);
        }

        return lookup.getWrapperOrThrow(RegistryKeys.INSTRUMENT)
            .getOptional(this.instruments)
            .map(RegistryEntryList::stream)
            .flatMap(Stream::findFirst);
    }
}
