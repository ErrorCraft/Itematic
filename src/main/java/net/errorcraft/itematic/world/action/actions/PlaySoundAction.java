package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

public record PlaySoundAction(ActionContextParameter position, RegistryEntry<SoundEvent> sound, SoundCategory category, float volume, float pitch) implements Action {
    public static final Codec<PlaySoundAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(PlaySoundAction::position),
        SoundEvent.ENTRY_CODEC.fieldOf("sound").forGetter(PlaySoundAction::sound),
        StringIdentifiable.createCodec(SoundCategory::values).fieldOf("category").forGetter(PlaySoundAction::category),
        Codec.FLOAT.fieldOf("volume").forGetter(PlaySoundAction::volume),
        Codec.FLOAT.fieldOf("pitch").forGetter(PlaySoundAction::pitch)
    ).apply(instance, PlaySoundAction::new));

    @Override
    public ActionType<?> type() {
        return ActionTypes.PLAY_SOUND;
    }

    @Override
    public boolean execute(ActionContext context) {
        Vec3d pos = context.position(this.position);
        ServerWorld world = context.world();
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), this.sound, this.category, this.volume, this.pitch, world.getRandom().nextLong());
        return true;
    }
}
