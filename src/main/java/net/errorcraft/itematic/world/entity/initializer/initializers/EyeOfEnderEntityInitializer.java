package net.errorcraft.itematic.world.entity.initializer.initializers;

import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.entity.initializer.EntityInitializer;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class EyeOfEnderEntityInitializer implements EntityInitializer<EyeOfEnder> {
    public static final EyeOfEnderEntityInitializer INSTANCE = new EyeOfEnderEntityInitializer();

    private EyeOfEnderEntityInitializer() {}

    @Override
    public @Nullable EyeOfEnder create(ActionContext context, EntitySpawnReason reason) {
        if (!(context.world() instanceof ServerLevel level)) {
            return null;
        }

        BlockPos blockPos = this.getBlockPos(context);
        if (blockPos == null) {
            return null;
        }

        BlockPos strongholdPos = level.findNearestMapStructure(
            StructureTags.EYE_OF_ENDER_LOCATED,
            blockPos,
            100,
            false
        );
        if (strongholdPos == null) {
            return null;
        }

        Vec3 pos = this.getPosition(context);
        EyeOfEnder entity = this.createEntity(level, pos, context.get(LootContextParams.TOOL), strongholdPos);
        Entity user = context.get(LootContextParams.THIS_ENTITY);
        level.gameEvent(GameEvent.PROJECTILE_SHOOT, pos, GameEvent.Context.of(user));
        if (user instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.USED_ENDER_EYE.trigger(serverPlayer, strongholdPos);
        }

        return entity;
    }

    private BlockPos getBlockPos(ActionContext context) {
        Entity entity = context.get(LootContextParams.THIS_ENTITY);
        if (entity != null) {
            return entity.blockPosition();
        }

        return context.get(ItematicContextKeys.INTERACTED_POSITION, BlockPos::containing);
    }

    private Vec3 getPosition(ActionContext context) {
        Entity entity = context.get(LootContextParams.THIS_ENTITY);
        if (entity != null) {
            return new Vec3(entity.getX(), entity.getY(0.5d), entity.getZ());
        }

        return context.get(ItematicContextKeys.INTERACTED_POSITION);
    }

    private EyeOfEnder createEntity(ServerLevel level, Vec3 pos, ItemStack stack, BlockPos strongholdPos) {
        EyeOfEnder entity = new EyeOfEnder(level, pos.x(), pos.y(), pos.z());
        if (stack != null) {
            entity.setItem(stack);
        }

        entity.signalTo(Vec3.atLowerCornerOf(strongholdPos));
        return entity;
    }
}
