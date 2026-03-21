package net.errorcraft.itematic.world.action.context;

import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;
import java.util.function.Consumer;

public class ItemStackExchanger {
    public static final ItemStackExchanger EMPTY = new ItemStackExchanger(stack -> {}, ItemStack.EMPTY);

    private final Consumer<ItemStack> dropper;
    private ItemStack currentStack;

    public static ItemStackExchanger forEntity(LivingEntity entity, ItemStack initialStack) {
        return new ItemStackExchanger(entity::giveOrDropStack, initialStack);
    }

    public static ItemStackExchanger forDispenser(ServerWorld world, Direction side, Vec3d pos, ItemStack initialStack) {
        Consumer<ItemStack> dropper = stack -> ItemDispenserBehavior.spawnItem(world, stack, 6, side, pos);
        return new ItemStackExchanger(dropper, initialStack);
    }

    public static ItemStackExchanger forStackReference(StackReference stackReference) {
        return new ItemStackExchanger(stackReference::set, stackReference.get());
    }

    public ItemStackExchanger(Consumer<ItemStack> dropper, ItemStack initialStack) {
        this.dropper = Objects.requireNonNull(dropper);
        this.currentStack = Objects.requireNonNull(initialStack);
    }

    public ItemStack result() {
        return this.currentStack;
    }

    public void exchange(ItemStack stack) {
        Objects.requireNonNull(stack);
        if (stack == this.currentStack) {
            return;
        }

        // TODO: Add predicate field that checks whether the current stack should be dropped
        if (!this.currentStack.isEmpty()) {
            this.dropper.accept(this.currentStack);
        }

        this.currentStack = stack;
    }
}
