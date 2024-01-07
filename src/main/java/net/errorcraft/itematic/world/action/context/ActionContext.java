package net.errorcraft.itematic.world.action.context;

import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.loot.context.ItematicLootContextParameters;
import net.errorcraft.itematic.loot.context.ItematicLootContextTypes;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameters;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
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
    protected ItemStackConsumer resultStackConsumer;
    protected EquipmentSlot slot;

    public ActionContext(ServerWorld world) {
        this.world = world;
    }

    public LootContext createLootContext(ActionContextParameters parameters) {
        LootContextParameterSet set = new LootContextParameterSet.Builder(this.world)
            .add(LootContextParameters.THIS_ENTITY, this.entities.get(parameters.entity()))
            .add(LootContextParameters.ORIGIN, this.position(parameters.position()))
            .add(LootContextParameters.TOOL, this.stack)
            .add(ItematicLootContextParameters.SIDE, this.side)
            .build(ItematicLootContextTypes.ACTION);
        return new LootContext.Builder(set).build(Optional.empty());
    }

    public ServerCommandSource createCommandSource(ActionContextParameters parameters, CommandFunctionManager functionManager) {
        return functionManager.getScheduledCommandSource()
            .withEntity(this.entities.get(parameters.entity()))
            .withPosition(this.positions.get(parameters.position()));
    }

    public ItemUsageContext createItemUsageContext(ActionContextParameter position) {
        return new ItemUsageContext(
            this.world,
            this.player(ActionContextParameter.THIS).orElse(null),
            this.hand(),
            this.stack(),
            new BlockHitResult(
                this.position(position),
                this.side(),
                this.blockPos(position),
                false
            )
        );
    }

    public ItemPlacementContext createItemPlacementContext(ActionContextParameter position, RegistryEntry<Block> block) {
        if (this.entity(ActionContextParameter.THIS).isPresent()) {
            ItemPlacementContext placementContext = new ItemPlacementContext(this.createItemUsageContext(position));
            return block.value().itematic$placementContext(placementContext);
        }
        BlockPos pos = this.blockPos(position);
        Direction useSide = this.world.isAir(pos.down()) ? this.side : Direction.UP;
        return new AutomaticItemPlacementContext(this.world, pos, this.side, this.stack, useSide);
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

    public ItemStackConsumer resultStackConsumer() {
        return this.resultStackConsumer;
    }

    public void setResultStack(ItemStack stack) {
        if (this.resultStackConsumer == null) {
            return;
        }
        this.resultStackConsumer.set(stack);
    }

    public Optional<EquipmentSlot> slot() {
        return Optional.ofNullable(this.slot);
    }

    public Hand hand() {
        if (this.slot == null) {
            return null;
        }
        return switch (this.slot) {
            case MAINHAND -> Hand.MAIN_HAND;
            case OFFHAND -> Hand.OFF_HAND;
            default -> null;
        };
    }
}
