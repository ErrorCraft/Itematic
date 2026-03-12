package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.Optional;

public record PointableItemComponent(String lodestoneTranslationKey) implements ItemComponent<PointableItemComponent> {
    public static final Codec<PointableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("lodestone_translation_key").forGetter(PointableItemComponent::lodestoneTranslationKey)
    ).apply(instance, PointableItemComponent::new));

    public static PointableItemComponent of(String lodestoneTranslationKey) {
        return new PointableItemComponent(lodestoneTranslationKey);
    }

    @Override
    public ItemComponentType<PointableItemComponent> type() {
        return ItemComponentTypes.POINTABLE;
    }

    @Override
    public Codec<PointableItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity holder, int slot, boolean selected) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }

        LodestoneTrackerComponent currentTracker = stack.get(DataComponentTypes.LODESTONE_TRACKER);
        if (currentTracker == null) {
            return;
        }

        LodestoneTrackerComponent newTracker = currentTracker.forWorld(serverWorld);
        if (newTracker != currentTracker) {
            stack.set(DataComponentTypes.LODESTONE_TRACKER, newTracker);
        }
    }

    public Optional<String> lodestoneTranslationKey(ItemStack stack) {
        if (stack.contains(DataComponentTypes.LODESTONE_TRACKER)) {
            return Optional.of(this.lodestoneTranslationKey);
        }

        return Optional.empty();
    }
}
