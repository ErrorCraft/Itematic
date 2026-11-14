package net.errorcraft.itematic.world.level.block.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignApplicator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.entity.SignTextSlot;

import java.util.Optional;

public record SignModifier(Optional<DyeColor> color, Optional<Boolean> glow, Optional<Boolean> wax, Holder<SoundEvent> applySound, boolean canApplyWithoutText) implements SignApplicator {
    public static final Codec<SignModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        DyeColor.CODEC.optionalFieldOf("color").forGetter(SignModifier::color),
        Codec.BOOL.optionalFieldOf("glow").forGetter(SignModifier::glow),
        Codec.BOOL.optionalFieldOf("wax").forGetter(SignModifier::wax),
        SoundEvent.CODEC.fieldOf("apply_sound").forGetter(SignModifier::applySound),
        Codec.BOOL.optionalFieldOf("can_apply_without_text", false).forGetter(SignModifier::canApplyWithoutText)
    ).apply(instance, SignModifier::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SignModifier> STREAM_CODEC = StreamCodec.composite(
        DyeColor.STREAM_CODEC.apply(ByteBufCodecs::optional), SignModifier::color,
        ByteBufCodecs.BOOL.apply(ByteBufCodecs::optional), SignModifier::glow,
        ByteBufCodecs.BOOL.apply(ByteBufCodecs::optional), SignModifier::wax,
        SoundEvent.STREAM_CODEC, SignModifier::applySound,
        ByteBufCodecs.BOOL, SignModifier::canApplyWithoutText,
        SignModifier::new
    );

    @Override
    public boolean tryApplyToSign(Level level, SignBlockEntity sign, SignTextSlot slot, ItemStack item, Player player) {
        boolean result = false;
        result |= this.glow.map(glow -> sign.updateText(text -> text.withGlowingText(glow), slot))
            .orElse(false);
        result |= this.wax.map(sign::setWaxed)
            .orElse(false);
        result |= this.color.map(color -> sign.updateText(text -> text.withColor(color), slot))
            .orElse(false);
        if (result) {
            this.applied(level, sign.getBlockPos());
        }

        return result;
    }

    @Override
    public boolean canApplyToSign(SignText text, ItemStack item, Player player) {
        return this.canApplyWithoutText || SignApplicator.super.canApplyToSign(text, item, player);
    }

    private void applied(Level level, BlockPos pos) {
        level.playSound(null, pos, this.applySound.value(), SoundSource.BLOCKS, 1.0f, 1.0f);
        if (this.wax.orElse(false)) {
            level.levelEvent(null, LevelEvent.PARTICLES_WAX_ON, pos, 0);
        }
    }
}
