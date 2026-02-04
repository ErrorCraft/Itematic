package net.errorcraft.itematic.mixin.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeyedValue;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Item.class)
public interface ItemAccessor {
    @Invoker("raycast")
    @Contract
    static BlockHitResult raycast(World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling) {
        throw new AssertionError();
    }

    @Mixin(Item.Settings.class)
    interface SettingsAccessor {
        @Accessor("ITEM_PREFIXED_TRANSLATION_KEY")
        static RegistryKeyedValue<Item, String> itemNameSupplier() {
            throw new AssertionError();
        }

        @Accessor("BLOCK_PREFIXED_TRANSLATION_KEY")
        static RegistryKeyedValue<Item, String> blockNameSupplier() {
            throw new AssertionError();
        }
    }
}
