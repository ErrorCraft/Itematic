package net.errorcraft.itematic.mixin.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(InstrumentItem.class)
public interface GoatHornItemAccessor {
    @Invoker("play")
    static void playSound(Level world, Player player, Instrument instrument) {
        throw new AssertionError();
    }
}
