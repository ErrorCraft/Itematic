package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record ZoomItemComponent(float fieldOfViewMultiplier, Holder<SoundEvent> startUsingSound, Holder<SoundEvent> stopUsingSound) implements ItemComponent<ZoomItemComponent> {
    public static final Codec<ZoomItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItematicCodecs.positiveFloat(1.0f).fieldOf("field_of_view_multiplier").forGetter(ZoomItemComponent::fieldOfViewMultiplier),
        SoundEvent.CODEC.fieldOf("start_using_sound").forGetter(ZoomItemComponent::startUsingSound),
        SoundEvent.CODEC.fieldOf("stop_using_sound").forGetter(ZoomItemComponent::stopUsingSound)
    ).apply(instance, ZoomItemComponent::new));

    public static ZoomItemComponent of(float fieldOfViewMultiplier, Holder<SoundEvent> startUsingSound, Holder<SoundEvent> stopUsingSound) {
        return new ZoomItemComponent(fieldOfViewMultiplier, startUsingSound, stopUsingSound);
    }

    @Override
    public ItemComponentType<ZoomItemComponent> type() {
        return ItemComponentTypes.ZOOM;
    }

    @Override
    public Codec<ZoomItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ItemResult use(Level world, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        user.playSound(this.startUsingSound.value(), 1.0f, 1.0f);
        user.awardStat(Stats.ITEM_USED.itematic$getOrCreateStat(stack.getItemHolder()));
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
