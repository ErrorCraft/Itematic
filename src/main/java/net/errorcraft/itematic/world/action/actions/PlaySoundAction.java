package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.RandomRange;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public record PlaySoundAction(PositionTarget position, Holder<SoundEvent> sound, Optional<SoundSource> category, RandomRange.Floats volume, RandomRange.Floats pitch, boolean fromEntity) implements Action<PlaySoundAction> {
    public static final MapCodec<PlaySoundAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(PlaySoundAction::position),
        SoundEvent.CODEC.fieldOf("sound").forGetter(PlaySoundAction::sound),
        StringRepresentable.fromEnum(SoundSource::values).optionalFieldOf("category").forGetter(PlaySoundAction::category),
        RandomRange.Floats.CODEC.fieldOf("volume").forGetter(PlaySoundAction::volume),
        RandomRange.Floats.CODEC.fieldOf("pitch").forGetter(PlaySoundAction::pitch),
        Codec.BOOL.optionalFieldOf("from_entity", false).forGetter(PlaySoundAction::fromEntity)
    ).apply(instance, PlaySoundAction::new));

    public static Builder builder(PositionTarget position, Holder<SoundEvent> sound, SoundSource category) {
        return new Builder(position, sound, category);
    }

    public static PlaySoundAction of(PositionTarget position, Holder<SoundEvent> sound) {
        return new PlaySoundAction(
            position,
            sound,
            Optional.empty(),
            RandomRange.Floats.exactly(1.0f),
            RandomRange.Floats.exactly(1.0f),
            false
        );
    }

    public static PlaySoundAction of(PositionTarget position, Holder<SoundEvent> sound, SoundSource category) {
        return new PlaySoundAction(
            position,
            sound,
            Optional.of(category),
            RandomRange.Floats.exactly(1.0f),
            RandomRange.Floats.exactly(1.0f),
            false
        );
    }

    @Override
    public ActionType<PlaySoundAction> type() {
        return ActionType.PLAY_SOUND;
    }

    @Override
    public boolean execute(ActionContext context) {
        Entity entity = context.get(LootContextParams.THIS_ENTITY);
        SoundSource category = this.category(entity);
        if (category == null) {
            return false;
        }

        Level level = context.level();
        RandomSource random = level.getRandom();
        float volume = this.volume.get(random);
        float pitch = this.pitch.get(random);
        long seed = random.nextLong();
        if (this.fromEntity && entity != null) {
            level.playSeededSound(null, entity, this.sound, category, volume, pitch, seed);
            return true;
        }

        Vec3 pos = context.get(this.position.contextParam());
        if (pos == null) {
            return false;
        }

        level.playSeededSound(null, pos.x(), pos.y(), pos.z(), this.sound, category, volume, pitch, seed);
        return true;
    }

    @Nullable
    private SoundSource category(@Nullable Entity entity) {
        return this.category.orElseGet(() -> {
            if (entity == null) {
                return null;
            }

            return entity.getSoundSource();
        });
    }

    public static class Builder {
        private final PositionTarget position;
        private final Holder<SoundEvent> sound;
        private final SoundSource category;
        private RandomRange.Floats volume = RandomRange.Floats.exactly(1.0f);
        private RandomRange.Floats pitch = RandomRange.Floats.exactly(1.0f);

        private Builder(PositionTarget position, Holder<SoundEvent> sound, SoundSource category) {
            this.position = position;
            this.sound = sound;
            this.category = category;
        }

        public PlaySoundAction build() {
            return new PlaySoundAction(this.position, this.sound, Optional.of(this.category), this.volume, this.pitch, false);
        }

        public Builder volume(float volume) {
            this.volume = RandomRange.Floats.exactly(volume);
            return this;
        }

        public Builder volume(float min, float max) {
            this.volume = RandomRange.Floats.of(min, max);
            return this;
        }

        public Builder pitch(float pitch) {
            this.pitch = RandomRange.Floats.exactly(pitch);
            return this;
        }

        public Builder pitch(float min, float max) {
            this.pitch = RandomRange.Floats.of(min, max);
            return this;
        }
    }
}
