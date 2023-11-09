package net.errorcraft.itematic.world.action.context;

import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class MutableActionContext extends ActionContext {
    public MutableActionContext(ServerWorld world) {
        super(world);
    }

    public MutableActionContext entity(ActionContextParameter parameter, Entity entity) {
        this.entities.put(parameter, entity);
        return this;
    }

    public MutableActionContext entityPosition(ActionContextParameter parameter, Entity entity) {
        this.entities.put(parameter, entity);
        if (entity != null) {
            this.positions.put(parameter, entity.getPos());
        }
        return this;
    }

    public MutableActionContext position(ActionContextParameter parameter, BlockPos position) {
        return this.position(parameter, Vec3d.ofBottomCenter(position));
    }

    public MutableActionContext position(ActionContextParameter parameter, Vec3d position) {
        if (position == null) {
            throw new IllegalArgumentException("Position was null");
        }
        this.positions.put(parameter, position);
        return this;
    }

    public MutableActionContext side(Direction side) {
        if (side == null) {
            throw new IllegalArgumentException("Side was null");
        }
        this.side = side;
        return this;
    }

    public MutableActionContext stack(ItemStack stack) {
        if (stack == null) {
            throw new IllegalArgumentException("Item stack was null");
        }
        this.stack = stack;
        return this;
    }

    public MutableActionContext resultStackConsumer(ItemStackConsumer resultStackConsumer) {
        this.resultStackConsumer = resultStackConsumer;
        return this;
    }

    public MutableActionContext slot(EquipmentSlot slot) {
        this.slot = slot;
        return this;
    }

    public static MutableActionContext stackUsage(ServerWorld world, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        return new MutableActionContext(world)
            .stack(stack)
            .resultStackConsumer(resultStackConsumer);
    }

    public static MutableActionContext stackUsage(ServerWorld world, ItemStack stack, ItemStackConsumer resultStackConsumer, EquipmentSlot slot) {
        return stackUsage(world, stack, resultStackConsumer)
            .slot(slot);
    }

    public static MutableActionContext stackUsage(ServerWorld world, ItemStack stack, ItemStackConsumer resultStackConsumer, Hand hand) {
        if (hand == null) {
            return stackUsage(world, stack, resultStackConsumer);
        }
        return stackUsage(world, stack, resultStackConsumer, hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
    }
}
