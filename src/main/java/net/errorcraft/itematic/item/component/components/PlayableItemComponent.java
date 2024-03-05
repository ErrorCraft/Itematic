package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.mixin.item.GoatHornItemAccessor;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Instrument;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public record PlayableItemComponent(TagKey<Instrument> instruments) implements ItemComponent<PlayableItemComponent> {
    public static final Codec<PlayableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TagKey.unprefixedCodec(RegistryKeys.INSTRUMENT).fieldOf("instruments").forGetter(PlayableItemComponent::instruments)
    ).apply(instance, PlayableItemComponent::new));
    private static final String INSTRUMENT_KEY = GoatHornItemAccessor.instrumentKey();

    @Override
    public ItemComponentType<PlayableItemComponent> type() {
        return ItemComponentTypes.PLAYABLE;
    }

    @Override
    public Codec<PlayableItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        return this.instrument(stack)
            .map(RegistryEntry::value)
            .map(instrument -> {
                user.itematic$startUsingHand(hand, instrument.useDuration());
                GoatHornItemAccessor.playSound(world, user, instrument);
                user.getItemCooldownManager().set(stack.getItem(), instrument.useDuration());
                user.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
                return ActionResult.CONSUME;
            }).orElse(ActionResult.PASS);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        this.instrument(stack)
            .flatMap(RegistryEntry::getKey)
            .map(RegistryKey::getValue)
            .ifPresent(id -> tooltip.add(Text.translatable(Util.createTranslationKey(INSTRUMENT_KEY, id)).formatted(Formatting.GRAY)));
    }

    public static PlayableItemComponent of(TagKey<Instrument> instruments) {
        return new PlayableItemComponent(instruments);
    }

    private Optional<? extends RegistryEntry<Instrument>> instrument(ItemStack stack) {
        return this.instrumentFromNbt(stack.getNbt())
            .or(() -> Registries.INSTRUMENT.getEntryList(this.instruments)
                .map(RegistryEntryList::stream)
                .flatMap(Stream::findFirst)
            );
    }

    private Optional<RegistryEntry<Instrument>> instrumentFromNbt(NbtCompound nbt) {
        if (nbt == null) {
            return Optional.empty();
        }
        if (!nbt.contains(INSTRUMENT_KEY, NbtElement.STRING_TYPE)) {
            return Optional.empty();
        }
        Identifier instrument = Identifier.tryParse(nbt.getString(INSTRUMENT_KEY));
        if (instrument == null) {
            return Optional.empty();
        }
        return Registries.INSTRUMENT.getEntry(RegistryKey.of(RegistryKeys.INSTRUMENT, instrument))
            .map(Function.identity());
    }
}
