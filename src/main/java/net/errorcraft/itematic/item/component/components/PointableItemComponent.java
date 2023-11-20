package net.errorcraft.itematic.item.component.components;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.pointer.Pointer;
import net.errorcraft.itematic.mixin.item.CompassItemAccessor;
import net.errorcraft.itematic.util.BlockPosUtil;
import net.minecraft.entity.Entity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.slf4j.Logger;

import java.util.Optional;

public record PointableItemComponent(RegistryEntry<Pointer> pointsTo, Optional<String> lodestoneTranslationKey) implements ItemComponent {
    public static final Codec<PointableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Pointer.ENTRY_CODEC.fieldOf("points_to").forGetter(PointableItemComponent::pointsTo),
        Codecs.createStrictOptionalFieldCodec(Codec.STRING,"lodestone_translation_key").forGetter(PointableItemComponent::lodestoneTranslationKey)
    ).apply(instance, PointableItemComponent::new));
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.POINTABLE;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
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
        if (!CompassItem.hasLodestone(stack)) {
            return;
        }
        NbtCompound nbt = stack.getOrCreateNbt();
        if (!lodestoneExists(nbt, serverWorld)) {
            nbt.remove(CompassItem.LODESTONE_POS_KEY);
        }
    }

    public Optional<String> lodestoneTranslationKey(ItemStack stack) {
        if (CompassItem.hasLodestone(stack)) {
            return this.lodestoneTranslationKey;
        }
        return Optional.empty();
    }

    private static boolean lodestoneExists(NbtCompound nbt, ServerWorld world) {
        if (!inLodestoneWorld(nbt, world)) {
            return false;
        }
        if (nbt.contains(CompassItem.LODESTONE_TRACKED_KEY) && !nbt.getBoolean(CompassItem.LODESTONE_TRACKED_KEY)) {
            return true;
        }
        if (!nbt.contains(CompassItem.LODESTONE_POS_KEY)) {
            return false;
        }
        return BlockPosUtil.MAP_CODEC.parse(NbtOps.INSTANCE, nbt.getCompound(CompassItem.LODESTONE_POS_KEY))
            .resultOrPartial(LOGGER::error)
            .map(pos -> world.isInBuildLimit(pos) && world.getPointOfInterestStorage().hasTypeAt(PointOfInterestTypes.LODESTONE, pos))
            .orElse(false);
    }

    private static boolean inLodestoneWorld(NbtCompound nbt, ServerWorld world) {
        return CompassItemAccessor.getLodestoneDimension(nbt)
            .map(key -> key == world.getRegistryKey())
            .orElse(false);
    }

    public static PointableItemComponent of(RegistryEntry<Pointer> pointsTo) {
        return new PointableItemComponent(pointsTo, Optional.empty());
    }

    public static PointableItemComponent of(RegistryEntry<Pointer> pointsTo, String lodestoneTranslationKey) {
        return new PointableItemComponent(pointsTo, Optional.of(lodestoneTranslationKey));
    }
}
