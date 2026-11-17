package net.errorcraft.itematic.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record ZoomSounds(Holder<SoundEvent> start, Holder<SoundEvent> stop) implements UseableListener.Started, UseableListener.Stopped, UseableListener.Finished {
    public static final Codec<ZoomSounds> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        SoundEvent.CODEC.fieldOf("start").forGetter(ZoomSounds::start),
        SoundEvent.CODEC.fieldOf("stop").forGetter(ZoomSounds::stop)
    ).apply(instance, ZoomSounds::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ZoomSounds> STREAM_CODEC = StreamCodec.composite(
        SoundEvent.STREAM_CODEC, ZoomSounds::start,
        SoundEvent.STREAM_CODEC, ZoomSounds::stop,
        ZoomSounds::new
    );

    @Override
    public void itematic$onStartedUsing(LivingEntity user) {
        user.playSound(this.start.value(), 1.0f, 1.0f);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, ItemStackExchanger stackExchanger, Level level, LivingEntity user) {
        this.playStopSound(user);
    }

    @Override
    public void itematic$onFinishedUsing(ItemStack stack, ItemStackExchanger stackExchanger, Level level, LivingEntity user) {
        this.playStopSound(user);
    }

    private void playStopSound(LivingEntity user) {
        user.playSound(this.stop.value(), 1.0f, 1.0f);
    }
}
