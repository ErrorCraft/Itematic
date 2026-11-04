package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.component.ZoomFieldOfViewMultiplier;
import net.errorcraft.itematic.world.item.component.ZoomSounds;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.sounds.SoundEvent;

public record ZoomItemBehavior(ZoomFieldOfViewMultiplier fieldOfViewMultiplier, Holder<SoundEvent> startUsingSound, Holder<SoundEvent> stopUsingSound) implements ItemBehavior<ZoomItemBehavior> {
    public static final Codec<ZoomItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ZoomFieldOfViewMultiplier.CODEC.fieldOf("field_of_view_multiplier").forGetter(ZoomItemBehavior::fieldOfViewMultiplier),
        SoundEvent.CODEC.fieldOf("start_using_sound").forGetter(ZoomItemBehavior::startUsingSound),
        SoundEvent.CODEC.fieldOf("stop_using_sound").forGetter(ZoomItemBehavior::stopUsingSound)
    ).apply(instance, ZoomItemBehavior::new));

    public static ZoomItemBehavior of(float fieldOfViewMultiplier, Holder<SoundEvent> startUsingSound, Holder<SoundEvent> stopUsingSound) {
        return new ZoomItemBehavior(
            new ZoomFieldOfViewMultiplier(fieldOfViewMultiplier),
            startUsingSound,
            stopUsingSound
        );
    }

    @Override
    public ItemBehaviorType<ZoomItemBehavior> type() {
        return ItemBehaviorType.ZOOM;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.ZOOM_FIELD_OF_VIEW_MULTIPLIER, this.fieldOfViewMultiplier);
        builder.set(ItematicDataComponents.ZOOM_SOUNDS, new ZoomSounds(this.startUsingSound, this.stopUsingSound));
    }
}
