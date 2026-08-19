package net.errorcraft.itematic.world.action.context;

import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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
    private ItemStack result;

    private ItemStackExchanger(Predicate<ItemStack> shouldDrop, Consumer<ItemStack> dropper, ItemStack initialStack) {
        this.shouldDrop = Objects.requireNonNull(shouldDrop);
        this.dropper = Objects.requireNonNull(dropper);
        this.result = Objects.requireNonNull(initialStack);
    }

    public static ItemStackExchanger forEntity(LivingEntity entity, ItemStack initialStack) {
        return new ItemStackExchanger(
            stack -> !entity.hasInfiniteMaterials() || !entity.itematic$hasStackInInventory(stack),
            entity::handleExtraItemsCreatedOnUse,
            initialStack
        );
    }

    public static ItemStackExchanger forDispenser(Level world, Direction side, Vec3 pos, ItemStack initialStack) {
        return new ItemStackExchanger(
            stack -> true,
            stack -> DefaultDispenseItemBehavior.spawnItem(world, stack, 6, side, pos),
            initialStack
        );
    }

    public ItemStack result() {
        return this.result;
    }

    public void exchange(ItemStack stack) {
        Objects.requireNonNull(stack);
        if (stack == this.result) {
            return;
        }

        if (!this.shouldDrop.test(stack)) {
            return;
        }

        if (this.result.isEmpty()) {
            this.result = stack;
        } else {
            this.dropper.accept(stack);
        }
    }
}
