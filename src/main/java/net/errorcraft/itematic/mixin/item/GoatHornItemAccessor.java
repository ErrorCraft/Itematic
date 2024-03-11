package net.errorcraft.itematic.mixin.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GoatHornItem;
import net.minecraft.item.Instrument;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GoatHornItem.class)
public interface GoatHornItemAccessor {
    @Invoker("playSound")
    static void playSound(World world, PlayerEntity player, Instrument instrument) {
        throw new AssertionError();
    }
}
