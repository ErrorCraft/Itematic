package net.errorcraft.itematic.access.world;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public interface InteractionResultAccess {
    default InteractionResult itematic$exchanged(Consumer<ItemStack> stackExchanger) {
        return (InteractionResult) this;
    }
}
