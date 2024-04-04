package net.errorcraft.itematic.entity.initializer.initializers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record BoatEntityInitializer(BoatEntity.Type variant) implements EntityInitializer<BoatEntity> {
    public static final MapCodec<BoatEntityInitializer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        StringIdentifiable.createCodec(BoatEntity.Type::values).fieldOf("variant").forGetter(BoatEntityInitializer::variant)
    ).apply(instance, BoatEntityInitializer::new));

    @Override
    public EntityType<?> type() {
        return EntityType.BOAT;
    }

    @Override
    public BoatEntity create(ActionContext context) {
        return this.create(context.world(), context.blockPos(ActionContextParameter.TARGET));
    }

    private BoatEntity create(World world, BlockPos pos) {
        BoatEntity entity = new BoatEntity(world, pos.getX() + 0.5d, pos.getY(), pos.getZ() + 0.5d);
        entity.setVariant(this.variant);
        return entity;
    }
}
