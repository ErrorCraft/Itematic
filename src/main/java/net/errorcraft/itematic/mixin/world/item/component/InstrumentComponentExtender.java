package net.errorcraft.itematic.mixin.world.item.component;

import net.errorcraft.itematic.mixin.world.item.InstrumentItemAccessor;
import net.errorcraft.itematic.world.item.component.UseableListener;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.component.InstrumentComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(InstrumentComponent.class)
public class InstrumentComponentExtender implements UseableListener.Started {
    @Shadow
    @Final
    private Holder<Instrument> instrument;

    @Override
    public void itematic$onStartedUsing(LivingEntity user) {
        if (user instanceof Player player) {
            InstrumentItemAccessor.play(user.level(), player, this.instrument.value());
        }
    }
}
