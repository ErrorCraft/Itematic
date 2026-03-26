package net.errorcraft.itematic.world.action.context;

import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ItemStackExchanger {
    public static final ItemStackExchanger EMPTY = new ItemStackExchanger(
        stack -> true,
        stack -> {},
        ItemStack.EMPTY
    );

    private final Predicate<ItemStack> shouldDrop;
    private final Consumer<ItemStack> dropper;
    private ItemStack currentStack;

    private ItemStackExchanger(Predicate<ItemStack> shouldDrop, Consumer<ItemStack> dropper, ItemStack initialStack) {
        this.shouldDrop = Objects.requireNonNull(shouldDrop);
        this.dropper = Objects.requireNonNull(dropper);
        this.currentStack = Objects.requireNonNull(initialStack);
    }

    public static ItemStackExchanger forEntity(LivingEntity entity, ItemStack initialStack) {
        return new ItemStackExchanger(
            stack -> !entity.isInCreativeMode() || !entity.itematic$hasStackInInventory(stack),
            entity::giveOrDropStack,
            initialStack
        );
    }

    public static ItemStackExchanger forDispenser(ServerWorld world, Direction side, Vec3d pos, ItemStack initialStack) {
        return new ItemStackExchanger(
            stack -> true,
            stack -> ItemDispenserBehavior.spawnItem(world, stack, 6, side, pos),
            initialStack
        );
    }

    public ItemStack result() {
        return this.currentStack;
    }

    public void exchange(ItemStack stack) {
        Objects.requireNonNull(stack);
        if (stack == this.currentStack) {
            return;
        }

        if (!this.currentStack.isEmpty() && this.shouldDrop.test(this.currentStack)) {
            this.dropper.accept(this.currentStack);
        }

        this.currentStack = stack;
    }
}
