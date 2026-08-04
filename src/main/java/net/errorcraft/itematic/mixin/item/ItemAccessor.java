package net.errorcraft.itematic.mixin.item;

import net.minecraft.resources.DependantName;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Item.class)
public interface ItemAccessor {
    @Invoker("getPlayerPOVHitResult")
    @Contract
    static BlockHitResult raycast(Level world, Player player, ClipContext.Fluid fluidHandling) {
        throw new AssertionError();
    }

    @Mixin(Item.Properties.class)
    interface SettingsAccessor {
        @Accessor("ITEM_DESCRIPTION_ID")
        static DependantName<Item, String> itemNameSupplier() {
            throw new AssertionError();
        }

        @Accessor("BLOCK_DESCRIPTION_ID")
        static DependantName<Item, String> blockNameSupplier() {
            throw new AssertionError();
        }
    }
}
