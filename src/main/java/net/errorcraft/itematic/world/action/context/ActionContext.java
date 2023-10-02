package net.errorcraft.itematic.world.action.context;

import net.errorcraft.itematic.loot.context.ItematicLootContextTypes;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameters;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ActionContext {
    private final ServerWorld world;
    protected final Map<ActionContextParameter, Entity> entities = new HashMap<>();
    protected final Map<ActionContextParameter, Vec3d> positions = new HashMap<>();
    protected Direction side = Direction.UP;
    protected ItemStack stack = ItemStack.EMPTY;
    protected Hand hand = null;

    public ActionContext(ServerWorld world) {
        this.world = world;
    }

    public LootContext createLootContext(ActionContextParameters parameters) {
        LootContextParameterSet set = new LootContextParameterSet.Builder(this.world)
            .add(LootContextParameters.THIS_ENTITY, this.entities.get(parameters.entity()))
            .add(LootContextParameters.ORIGIN, this.position(parameters.position()))
            .add(LootContextParameters.TOOL, this.stack)
            .build(ItematicLootContextTypes.ACTION);
        return new LootContext.Builder(set).build(Optional.empty());
    }

    public ServerCommandSource createCommandSource(ActionContextParameters parameters, CommandFunctionManager functionManager) {
        return functionManager.getScheduledCommandSource()
            .withEntity(this.entities.get(parameters.entity()))
            .withPosition(this.positions.get(parameters.position()));
    }

    public ServerWorld world() {
        return this.world;
    }

    public Optional<Entity> entity(ActionContextParameter parameter) {
        return Optional.ofNullable(this.entities.get(parameter));
    }

    public Optional<PlayerEntity> player(ActionContextParameter parameter) {
        return this.entity(parameter).map(entity -> {
            if (entity instanceof PlayerEntity player) {
                return player;
            }
            return null;
        });
    }

    public Vec3d position(ActionContextParameter parameter) {
        Vec3d position = this.positions.get(parameter);
        if (position == null) {
            return this.world.getSpawnPos().toCenterPos();
        }
        return position;
    }

    public BlockPos blockPos(ActionContextParameter parameter) {
        return BlockPos.ofFloored(this.position(parameter));
    }

    public Direction side() {
        return this.side;
    }

    public ItemStack stack() {
        return this.stack;
    }

    public Optional<Hand> hand() {
        return Optional.ofNullable(this.hand);
    }
}
