package net.errorcraft.itematic.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextArg;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class FillMapItemModifier extends LootItemConditionalFunction {
    private static final Codec<LootContextArg<BlockPos>> BLOCK_POS_GETTER_CODEC = LootContextArg.createArgCodec(builder -> builder
        .anyEntity(EntitySource::new)
        .anyOf(PositionTarget.values(), positionTarget -> new PositionSource(positionTarget.contextParam()))
    );
    public static final MapCodec<FillMapItemModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(instance).and(instance.group(
        BLOCK_POS_GETTER_CODEC.fieldOf("position").forGetter(fillMap -> fillMap.position),
        Codec.BYTE.optionalFieldOf("zoom", ExplorationMapFunction.DEFAULT_ZOOM).forGetter(fillMap -> fillMap.zoom),
        Codec.BOOL.optionalFieldOf("track_position", true).forGetter(fillMap -> fillMap.trackPosition),
        Codec.BOOL.optionalFieldOf("unlimited_tracking", false).forGetter(fillMap -> fillMap.unlimitedTracking)
    )).apply(instance, FillMapItemModifier::new));

    private final LootContextArg<BlockPos> position;
    private final byte zoom;
    private final boolean trackPosition;
    private final boolean unlimitedTracking;

    private FillMapItemModifier(Optional<Holder<LootItemCondition>> condition, LootContextArg<BlockPos> position, byte zoom, boolean trackPosition, boolean unlimitedTracking) {
        super(condition);
        this.position = position;
        this.zoom = zoom;
        this.trackPosition = trackPosition;
        this.unlimitedTracking = unlimitedTracking;
    }

    public static Builder<?> builder(LootContext.EntityTarget sourceEntity, int zoom, boolean trackPosition, boolean unlimitedTracking) {
        return simpleBuilder(conditions -> new FillMapItemModifier(
            conditions,
            new EntitySource(sourceEntity.contextParam()),
            (byte) zoom,
            trackPosition,
            unlimitedTracking
        ));
    }

    @Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return CODEC;
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        BlockPos pos = this.position.get(context);
        if (pos == null) {
            return stack;
        }

        MapItem.applyNewSavedData(
            context.getLevel(),
            stack,
            pos.getX(),
            pos.getZ(),
            this.zoom,
            this.trackPosition,
            this.unlimitedTracking
        );
        return stack;
    }

    private record EntitySource(ContextKey<? extends Entity> contextParam) implements LootContextArg.Getter<Entity, BlockPos> {
        @Override
        public BlockPos get(Entity value) {
            return value.blockPosition();
        }
    }

    private record PositionSource(ContextKey<? extends Vec3> contextParam) implements LootContextArg.Getter<Vec3, BlockPos> {
        @Override
        public BlockPos get(Vec3 value) {
            return BlockPos.containing(value);
        }
    }
}
