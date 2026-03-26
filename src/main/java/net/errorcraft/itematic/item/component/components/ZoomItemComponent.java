package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public record ZoomItemComponent(float fieldOfViewMultiplier, RegistryEntry<SoundEvent> startUsingSound, RegistryEntry<SoundEvent> stopUsingSound) implements ItemComponent<ZoomItemComponent> {
    public static final Codec<ZoomItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItematicCodecs.positiveFloat(1.0f).fieldOf("field_of_view_multiplier").forGetter(ZoomItemComponent::fieldOfViewMultiplier),
        SoundEvent.ENTRY_CODEC.fieldOf("start_using_sound").forGetter(ZoomItemComponent::startUsingSound),
        SoundEvent.ENTRY_CODEC.fieldOf("stop_using_sound").forGetter(ZoomItemComponent::stopUsingSound)
    ).apply(instance, ZoomItemComponent::new));

    public static ZoomItemComponent of(float fieldOfViewMultiplier, RegistryEntry<SoundEvent> startUsingSound, RegistryEntry<SoundEvent> stopUsingSound) {
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
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        user.playSound(this.startUsingSound.value(), 1.0f, 1.0f);
        user.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
        return ItemResult.PASS;
    }

    @Override
    public boolean stopUsing(ItemStack stack, World world, LivingEntity user, int usedTicks, int remainingUseTicks, ItemStackExchanger stackExchanger) {
        this.playStopSound(user);
        return true;
    }

    @Override
    public void finishUsing(World world, LivingEntity user, ItemStack stack, int usedTicks, ItemStackExchanger stackExchanger) {
        this.playStopSound(user);
    }

    private void playStopSound(LivingEntity target) {
        target.playSound(this.stopUsingSound.value(), 1.0f, 1.0f);
    }
}
