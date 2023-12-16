package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.Range;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public record PlaySoundAction(ActionContextParameter position, RegistryEntry<SoundEvent> sound, SoundCategory category, Range.FloatRange volume, Range.FloatRange pitch) implements Action {
    public static final Codec<PlaySoundAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(PlaySoundAction::position),
        SoundEvent.ENTRY_CODEC.fieldOf("sound").forGetter(PlaySoundAction::sound),
        StringIdentifiable.createCodec(SoundCategory::values).fieldOf("category").forGetter(PlaySoundAction::category),
        Range.FLOAT_CODEC.fieldOf("volume").forGetter(PlaySoundAction::volume),
        Range.FLOAT_CODEC.fieldOf("pitch").forGetter(PlaySoundAction::pitch)
    ).apply(instance, PlaySoundAction::new));

    @Override
    public ActionType<?> type() {
        return ActionTypes.PLAY_SOUND;
    }

    @Override
    public boolean execute(ActionContext context) {
        Vec3d pos = context.position(this.position);
        ServerWorld world = context.world();
        Random random = world.getRandom();
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), this.sound, this.category, this.volume.get(random), this.pitch.get(random), world.getRandom().nextLong());
        return true;
    }

    public static Builder builder(ActionContextParameter position, RegistryEntry<SoundEvent> sound, SoundCategory category) {
        return new Builder(position, sound, category);
    }

    public static PlaySoundAction of(ActionContextParameter position, RegistryEntry<SoundEvent> sound, SoundCategory category) {
        return new PlaySoundAction(position, sound, category, Range.FloatRange.of(1.0f), Range.FloatRange.of(1.0f));
    }

    public static PlaySoundAction of(ActionContextParameter position, RegistryEntry<SoundEvent> sound, SoundCategory category, float volume, float pitch) {
        return new PlaySoundAction(position, sound, category, Range.FloatRange.of(volume), Range.FloatRange.of(pitch));
    }

    public static class Builder {
        private final ActionContextParameter position;
        private final RegistryEntry<SoundEvent> sound;
        private final SoundCategory category;
        private Range.FloatRange volume = Range.FloatRange.of(1.0f);
        private Range.FloatRange pitch = Range.FloatRange.of(1.0f);

        private Builder(ActionContextParameter position, RegistryEntry<SoundEvent> sound, SoundCategory category) {
            this.position = position;
            this.sound = sound;
            this.category = category;
        }

        public PlaySoundAction build() {
            return new PlaySoundAction(this.position, this.sound, this.category, this.volume, this.pitch);
        }

        public Builder volume(float volume) {
            this.volume = Range.FloatRange.of(volume);
            return this;
        }

        public Builder volume(float min, float max) {
            this.volume = Range.FloatRange.of(min, max);
            return this;
        }

        public Builder pitch(float pitch) {
            this.pitch = Range.FloatRange.of(pitch);
            return this;
        }

        public Builder pitch(float min, float max) {
            this.pitch = Range.FloatRange.of(min, max);
            return this;
        }
    }
}
