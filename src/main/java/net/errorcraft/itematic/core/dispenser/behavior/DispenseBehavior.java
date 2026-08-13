package net.errorcraft.itematic.core.dispenser.behavior;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.actions.SequenceAction;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandler;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import java.util.Optional;

public class DispenseBehavior extends OptionalDispenseItemBehavior {
    public static final Codec<DispenseBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionEntry.REGISTRY_CODEC.fieldOf("entry").forGetter(dispenseBehavior -> dispenseBehavior.entry),
        Codec.BOOL.optionalFieldOf("dispense_as_item_on_failure", true).forGetter(dispenseBehavior -> dispenseBehavior.dispenseAsItemOnFailure),
        Offset.CODEC.optionalFieldOf("offset", Offset.DEFAULT).forGetter(dispenseBehavior -> dispenseBehavior.offset)
    ).apply(instance, DispenseBehavior::new));

    private final Holder<ActionEntry> entry;
    private final boolean dispenseAsItemOnFailure;
    private final Offset offset;

    public static Builder builder(Holder<ActionEntry> entry) {
        return new Builder(entry);
    }

    public static Builder builder(ActionEntry entry) {
        return builder(Holder.direct(entry));
    }

    public static Builder builder(Action<?> action) {
        return builder(ActionEntry.of(action));
    }

    public static Builder builder(SequenceHandler.Builder<?, ?> builder) {
        return builder(SequenceAction.of(builder));
    }

    public DispenseBehavior(Holder<ActionEntry> entry, boolean dispenseAsItemOnFailure, Offset offset) {
        this.entry = entry;
        this.dispenseAsItemOnFailure = dispenseAsItemOnFailure;
        this.offset = offset;
    }

    @Override
    protected ItemStack execute(BlockSource pointer, ItemStack stack) {
        Direction side = pointer.state().getValue(DispenserBlock.FACING);
        Vec3 outputPos = this.offset.position(pointer);
        ActionContext context = ActionContext.builder(pointer.level())
            .stackExchanger(side, outputPos, stack)
            .add(LootContextParams.ORIGIN, pointer.center())
            .add(ItematicContextParameters.INTERACTED_POSITION, outputPos)
            .add(ItematicContextParameters.SIDE, side)
            .add(LootContextParams.TOOL, stack)
            .add(LootContextParams.BLOCK_ENTITY, pointer.blockEntity())
            .build();
        Optional<Boolean> result = this.entry.value().execute(context);
        if (result.isEmpty()) {
            return super.execute(pointer, stack);
        }

        if (result.get()) {
            return this.succeed(pointer, stack, context.resultStack());
        }

        return this.fail(pointer, stack);
    }

    private ItemStack succeed(BlockSource pointer, ItemStack oldStack, ItemStack newStack) {
        if (oldStack == newStack) {
            return oldStack;
        }

        if (newStack.isEmpty()) {
            return super.execute(pointer, oldStack);
        }

        if (oldStack.isEmpty()) {
            return newStack;
        }

        ItemStack remainingStack = pointer.blockEntity().insertItem(newStack);
        if (!remainingStack.isEmpty()) {
            super.execute(pointer, remainingStack);
        }

        return oldStack;
    }

    private ItemStack fail(BlockSource pointer, ItemStack stack) {
        if (this.dispenseAsItemOnFailure) {
            return super.execute(pointer, stack);
        }

        this.setSuccess(false);
        return stack;
    }

    public record Offset(Vec3 sideFactor, Vec3 constant) {
        private static final Vec3 DEFAULT_SIDE_FACTOR = new Vec3(1.0d, 1.0d, 1.0d);
        public static final Codec<Offset> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Vec3.CODEC.optionalFieldOf("side_factor", DEFAULT_SIDE_FACTOR).forGetter(Offset::sideFactor),
            Vec3.CODEC.optionalFieldOf("constant", Vec3.ZERO).forGetter(Offset::constant)
        ).apply(instance, Offset::new));
        public static final Offset DEFAULT = new Offset(DEFAULT_SIDE_FACTOR, Vec3.ZERO);

        public static Offset ofSide(double sideX, double sideY, double sideZ) {
            return new Offset(new Vec3(sideX, sideY, sideZ), Vec3.ZERO);
        }

        public static Offset of(double side, double constantX, double constantY, double constantZ) {
            return new Offset(new Vec3(side, side, side), new Vec3(constantX, constantY, constantZ));
        }

        public Vec3 position(BlockSource pointer) {
            Direction side = pointer.state().getValue(DispenserBlock.FACING);
            double offsetX = this.sideFactor.x() * side.getStepX() + this.constant.x();
            double offsetY = this.sideFactor.y() * side.getStepY() + this.constant.y();
            double offsetZ = this.sideFactor.z() * side.getStepZ() + this.constant.z();
            return pointer.center().add(offsetX, offsetY, offsetZ);
        }
    }

    public static class Builder {
        private final Holder<ActionEntry> entry;
        private boolean dispenseAsItemOnFailure = true;
        private Offset offset = Offset.DEFAULT;

        private Builder(Holder<ActionEntry> entry) {
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
