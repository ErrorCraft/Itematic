package net.errorcraft.itematic.item.dispense.behavior;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.actions.SequenceAction;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandler;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class DispenseBehavior extends FallibleItemDispenserBehavior {
    public static final Codec<DispenseBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionEntry.REGISTRY_CODEC.fieldOf("entry").forGetter(dispenseBehavior -> dispenseBehavior.entry),
        Codec.BOOL.optionalFieldOf("dispense_as_item_on_failure", true).forGetter(dispenseBehavior -> dispenseBehavior.dispenseAsItemOnFailure),
        Offset.CODEC.optionalFieldOf("offset", Offset.DEFAULT).forGetter(dispenseBehavior -> dispenseBehavior.offset)
    ).apply(instance, DispenseBehavior::new));

    private final RegistryEntry<ActionEntry> entry;
    private final boolean dispenseAsItemOnFailure;
    private final Offset offset;

    public static Builder builder(RegistryEntry<ActionEntry> entry) {
        return new Builder(entry);
    }

    public static Builder builder(ActionEntry entry) {
        return builder(RegistryEntry.of(entry));
    }

    public static Builder builder(Action<?> action) {
        return builder(ActionEntry.of(action));
    }

    public static Builder builder(SequenceHandler.Builder<?, ?> builder) {
        return builder(SequenceAction.of(builder));
    }

    public DispenseBehavior(RegistryEntry<ActionEntry> entry, boolean dispenseAsItemOnFailure, Offset offset) {
        this.entry = entry;
        this.dispenseAsItemOnFailure = dispenseAsItemOnFailure;
        this.offset = offset;
    }

    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        Direction side = pointer.state().get(DispenserBlock.FACING);
        Vec3d outputPos = this.offset.position(pointer);
        ActionContext context = ActionContext.builder(pointer.world())
            .stackExchanger(side, outputPos, stack)
            .add(LootContextParameters.ORIGIN, pointer.centerPos())
            .add(ItematicContextParameters.INTERACTED_POSITION, outputPos)
            .add(ItematicContextParameters.SIDE, side)
            .add(LootContextParameters.TOOL, stack)
            .add(LootContextParameters.BLOCK_ENTITY, pointer.blockEntity())
            .build();
        Optional<Boolean> result = this.entry.value().execute(context);
        if (result.isEmpty()) {
            return super.dispenseSilently(pointer, stack);
        }

        if (result.get()) {
            return this.succeed(pointer, stack, context.resultStack());
        }

        return this.fail(pointer, stack);
    }

    private ItemStack succeed(BlockPointer pointer, ItemStack oldStack, ItemStack newStack) {
        if (oldStack == newStack) {
            return oldStack;
        }

        if (newStack.isEmpty()) {
            return super.dispenseSilently(pointer, oldStack);
        }

        if (oldStack.isEmpty()) {
            return newStack;
        }

        ItemStack remainingStack = pointer.blockEntity().addToFirstFreeSlot(newStack);
        if (!remainingStack.isEmpty()) {
            super.dispenseSilently(pointer, remainingStack);
        }

        return oldStack;
    }

    private ItemStack fail(BlockPointer pointer, ItemStack stack) {
        if (this.dispenseAsItemOnFailure) {
            return super.dispenseSilently(pointer, stack);
        }

        this.setSuccess(false);
        return stack;
    }

    public record Offset(Vec3d sideFactor, Vec3d constant) {
        private static final Vec3d DEFAULT_SIDE_FACTOR = new Vec3d(1.0d, 1.0d, 1.0d);
        public static final Codec<Offset> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Vec3d.CODEC.optionalFieldOf("side_factor", DEFAULT_SIDE_FACTOR).forGetter(Offset::sideFactor),
            Vec3d.CODEC.optionalFieldOf("constant", Vec3d.ZERO).forGetter(Offset::constant)
        ).apply(instance, Offset::new));
        public static final Offset DEFAULT = new Offset(DEFAULT_SIDE_FACTOR, Vec3d.ZERO);

        public static Offset ofSide(double sideX, double sideY, double sideZ) {
            return new Offset(new Vec3d(sideX, sideY, sideZ), Vec3d.ZERO);
        }

        public static Offset of(double side, double constantX, double constantY, double constantZ) {
            return new Offset(new Vec3d(side, side, side), new Vec3d(constantX, constantY, constantZ));
        }

        public Vec3d position(BlockPointer pointer) {
            Direction side = pointer.state().get(DispenserBlock.FACING);
            double offsetX = this.sideFactor.getX() * side.getOffsetX() + this.constant.getX();
            double offsetY = this.sideFactor.getY() * side.getOffsetY() + this.constant.getY();
            double offsetZ = this.sideFactor.getZ() * side.getOffsetZ() + this.constant.getZ();
            return pointer.centerPos().add(offsetX, offsetY, offsetZ);
        }
    }

    public static class Builder {
        private final RegistryEntry<ActionEntry> entry;
        private boolean dispenseAsItemOnFailure = true;
        private Offset offset = Offset.DEFAULT;

        private Builder(RegistryEntry<ActionEntry> entry) {
            this.entry = entry;
        }

        public DispenseBehavior build() {
            return new DispenseBehavior(this.entry, this.dispenseAsItemOnFailure, this.offset);
        }

        public Builder doNotDispenseOnFailure() {
            this.dispenseAsItemOnFailure = false;
            return this;
        }

        public Builder offset(Offset offset) {
            this.offset = offset;
            return this;
        }
    }
}
