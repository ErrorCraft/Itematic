package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public record ZoomItemComponent(float fieldOfViewMultiplier, RegistryEntry<SoundEvent> startUsingSound, RegistryEntry<SoundEvent> stopUsingSound) implements ItemComponent<ZoomItemComponent> {
    public static final Codec<ZoomItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItematicCodecs.positiveFloat(1.0f).fieldOf("field_of_view_multiplier").forGetter(ZoomItemComponent::fieldOfViewMultiplier),
        SoundEvent.ENTRY_CODEC.fieldOf("start_using_sound").forGetter(ZoomItemComponent::startUsingSound),
        SoundEvent.ENTRY_CODEC.fieldOf("stop_using_sound").forGetter(ZoomItemComponent::stopUsingSound)
    ).apply(instance, ZoomItemComponent::new));

    @Override
    public ItemComponentType<ZoomItemComponent> type() {
        return ItemComponentTypes.ZOOM;
    }

    @Override
    public Codec<ZoomItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        user.playSound(this.startUsingSound.value(), 1.0f, 1.0f);
        user.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
        return ItemUsage.consumeHeldItem(world, user, hand).getResult();
    }

    @Override
    public void stopUsing(ItemStack stack, World world, LivingEntity user, int usedTicks, int remainingUseTicks, ItemStackConsumer resultStackConsumer) {
        this.playStopSound(user);
    }

    @Override
    public void finishUsing(World world, LivingEntity user, ItemStack stack, int usedTicks, ItemStackConsumer resultStackConsumer) {
        this.playStopSound(user);
    }

    public static ZoomItemComponent of(float fieldOfViewMultiplier, RegistryEntry<SoundEvent> startUsingSound, RegistryEntry<SoundEvent> stopUsingSound) {
        return new ZoomItemComponent(fieldOfViewMultiplier, startUsingSound, stopUsingSound);
    }

    private void playStopSound(LivingEntity target) {
        target.playSound(this.stopUsingSound.value(), 1.0f, 1.0f);
    }
}
