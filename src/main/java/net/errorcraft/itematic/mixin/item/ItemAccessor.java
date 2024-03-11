package net.errorcraft.itematic.mixin.item;

import com.google.common.collect.Interner;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Item.class)
public interface ItemAccessor {
    @Invoker("raycast")
    static BlockHitResult raycast(World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling) {
        throw new AssertionError();
    }

    @Mixin(Item.Settings.class)
    interface SettingsAccessor {
        @Accessor("COMPONENT_MAP_INTERNER")
        static Interner<ComponentMap> componentInterner() {
            throw new AssertionError();
        }
    }
}
