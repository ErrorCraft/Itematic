package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.component.InstrumentComponent;

public record PlayableItemBehavior(Holder<Instrument> defaultInstrument) implements ItemBehavior<PlayableItemBehavior> {
    public static final Codec<PlayableItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.holder(Registries.INSTRUMENT).fieldOf("default_instrument").forGetter(PlayableItemBehavior::defaultInstrument)
    ).apply(instance, PlayableItemBehavior::new));

    public static PlayableItemBehavior of(Holder<Instrument> defaultInstrument) {
        return new PlayableItemBehavior(defaultInstrument);
    }

    @Override
    public ItemBehaviorType<PlayableItemBehavior> type() {
        return ItemBehaviorType.PLAYABLE;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.INSTRUMENT, new InstrumentComponent(this.defaultInstrument));
    }
}
