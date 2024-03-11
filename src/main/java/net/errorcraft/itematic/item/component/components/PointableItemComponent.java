package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.pointer.Pointer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.util.Optional;

public record PointableItemComponent(RegistryEntry<Pointer> pointsTo, Optional<String> lodestoneTranslationKey) implements ItemComponent<PointableItemComponent> {
    public static final Codec<PointableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Pointer.ENTRY_CODEC.fieldOf("points_to").forGetter(PointableItemComponent::pointsTo),
        Codecs.createStrictOptionalFieldCodec(Codec.STRING,"lodestone_translation_key").forGetter(PointableItemComponent::lodestoneTranslationKey)
    ).apply(instance, PointableItemComponent::new));

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
        if (this.lodestoneTranslationKey.isEmpty()) {
            return;
        }
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }
        LodestoneTrackerComponent lodestoneTracker = stack.get(DataComponentTypes.LODESTONE_TRACKER);
        if (lodestoneTracker == null) {
            return;
        }
        LodestoneTrackerComponent lodestoneTrackerForCurrentWorld = lodestoneTracker.forWorld(serverWorld);
        if (lodestoneTrackerForCurrentWorld != lodestoneTracker) {
            stack.set(DataComponentTypes.LODESTONE_TRACKER, lodestoneTrackerForCurrentWorld);
        }
    }

    public Optional<String> lodestoneTranslationKey(ItemStack stack) {
        if (stack.contains(DataComponentTypes.LODESTONE_TRACKER)) {
            return this.lodestoneTranslationKey;
        }
        return Optional.empty();
    }

    public static PointableItemComponent of(RegistryEntry<Pointer> pointsTo) {
        return new PointableItemComponent(pointsTo, Optional.empty());
    }

    public static PointableItemComponent of(RegistryEntry<Pointer> pointsTo, String lodestoneTranslationKey) {
        return new PointableItemComponent(pointsTo, Optional.of(lodestoneTranslationKey));
    }
}
