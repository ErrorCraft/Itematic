package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawner;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.component.Bucket;
import net.errorcraft.itematic.world.item.placement.block.picker.pickers.SimpleBlockPicker;
import net.errorcraft.itematic.world.level.modification.modifications.DrainFluid;
import net.errorcraft.itematic.world.level.modification.modifications.None;
import net.errorcraft.itematic.world.level.modification.modifications.PlaceBlock;
import net.errorcraft.itematic.world.level.modification.modifications.PlaceFluid;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.references.ItemIds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.Optional;

public record BucketItemBehavior(Bucket bucket) implements ItemBehavior<BucketItemBehavior> {
    public static final Codec<BucketItemBehavior> CODEC = Bucket.CODEC.xmap(
        BucketItemBehavior::new,
        BucketItemBehavior::bucket
    );

    public static BucketItemBehavior drainFluid() {
        return new BucketItemBehavior(
            new Bucket(
                DrainFluid.INSTANCE,
                Optional.empty()
            )
        );
    }

    public static BucketItemBehavior placeFluid(Holder<Fluid> fluid, Holder<SoundEvent> placeSound, HolderGetter<Item> items) {
        return new BucketItemBehavior(
            new Bucket(
                new PlaceFluid(fluid, placeSound, items.getOrThrow(ItemIds.BUCKET)),
                Optional.empty()
            )
        );
    }

    public static BucketItemBehavior placeFluidWithEntity(Holder<Fluid> fluid, Holder<EntityType<?>> entity, Holder<SoundEvent> placeSound, HolderGetter<Item> items) {
        return new BucketItemBehavior(
            new Bucket(
                new PlaceFluid(fluid, placeSound, items.getOrThrow(ItemIds.BUCKET)),
                Optional.of(EntitySpawner.of(entity))
            )
        );
    }

    public static BucketItemBehavior placeEntity(Holder<EntityType<?>> entity, HolderGetter<Item> items) {
        return new BucketItemBehavior(
            new Bucket(
                new None(items.getOrThrow(ItemIds.BUCKET)),
                Optional.of(EntitySpawner.of(entity))
            )
        );
    }

    public static BucketItemBehavior placeBlock(Holder<Block> block, Holder<SoundEvent> placeSound, HolderGetter<Item> items) {
        return new BucketItemBehavior(
            new Bucket(
                new PlaceBlock(new SimpleBlockPicker(block), placeSound, items.getOrThrow(ItemIds.BUCKET)),
                Optional.empty()
            )
        );
    }

    @Override
    public ItemBehaviorType<BucketItemBehavior> type() {
        return ItemBehaviorType.BUCKET;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.BUCKET, this.bucket);
        this.bucket.addComponents(builder);
    }
}
