package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record ZoomItemBehavior(float fieldOfViewMultiplier, Holder<SoundEvent> startUsingSound, Holder<SoundEvent> stopUsingSound) implements ItemBehavior<ZoomItemBehavior> {
    public static final Codec<ZoomItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItematicCodecs.positiveFloat(1.0f).fieldOf("field_of_view_multiplier").forGetter(ZoomItemBehavior::fieldOfViewMultiplier),
        SoundEvent.CODEC.fieldOf("start_using_sound").forGetter(ZoomItemBehavior::startUsingSound),
        SoundEvent.CODEC.fieldOf("stop_using_sound").forGetter(ZoomItemBehavior::stopUsingSound)
    ).apply(instance, ZoomItemBehavior::new));

    public static ZoomItemBehavior of(float fieldOfViewMultiplier, Holder<SoundEvent> startUsingSound, Holder<SoundEvent> stopUsingSound) {
        return new ZoomItemBehavior(fieldOfViewMultiplier, startUsingSound, stopUsingSound);
    }

    @Override
    public ItemBehaviorType<ZoomItemBehavior> type() {
        return ItemBehaviorType.ZOOM;
    }

    @Override
    public Codec<ZoomItemBehavior> codec() {
        return CODEC;
    }

    @Override
    public ItemResult use(Level world, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        user.playSound(this.startUsingSound.value(), 1.0f, 1.0f);
        user.awardStat(Stats.ITEM_USED.itematic$get(stack.getItemHolder()));
        return ItemResult.PASS;
    }

    @Override
    public boolean stopUsing(ItemStack stack, Level world, LivingEntity user, int usedTicks, int remainingUseTicks, ItemStackExchanger stackExchanger) {
        this.playStopSound(user);
        return true;
    }

    @Override
    public void finishUsing(Level world, LivingEntity user, ItemStack stack, int usedTicks, ItemStackExchanger stackExchanger) {
        this.playStopSound(user);
    }

    private void playStopSound(LivingEntity target) {
        target.playSound(this.stopUsingSound.value(), 1.0f, 1.0f);
    }
}
