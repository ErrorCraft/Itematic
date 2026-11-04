package net.errorcraft.itematic.mixin.world.item.component;

import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.component.UseableListener;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SuspiciousStewEffects.class)
public abstract class SuspiciousStewEffectsExtender implements UseableListener.Finished {
    @Shadow
    public abstract void onConsume(Level level, LivingEntity user, ItemStack stack, @Nullable Consumable consumable);

    @Override
    public void itematic$onFinishedUsing(ItemStack stack, ItemStackExchanger stackExchanger, Level level, LivingEntity user) {
        this.onConsume(level, user, stack, null);
    }
}
