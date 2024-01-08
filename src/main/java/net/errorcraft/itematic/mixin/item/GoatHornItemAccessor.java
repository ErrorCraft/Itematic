package net.errorcraft.itematic.mixin.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GoatHornItem;
import net.minecraft.item.Instrument;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GoatHornItem.class)
public interface GoatHornItemAccessor {
    @Accessor("INSTRUMENT_KEY")
    static String instrumentKey() {
        throw new AssertionError();
    }

    @Invoker("playSound")
    static void playSound(World world, PlayerEntity player, Instrument instrument) {
        throw new AssertionError();
    }

    @Invoker("setInstrument")
    static void setInstrument(ItemStack stack, RegistryEntry<Instrument> instrument) {}
}
