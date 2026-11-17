package net.errorcraft.itematic.mixin.world.item.alchemy;

import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.component.UseableListener;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PotionContents.class)
public abstract class PotionContentsExtender implements UseableListener.Finished {
    @Shadow
    public abstract void onConsume(Level level, LivingEntity user, ItemStack stack, @Nullable Consumable consumable);

    @Override
    public void itematic$onFinishedUsing(ItemStack stack, ItemStackExchanger stackExchanger, Level level, LivingEntity user) {
        this.onConsume(level, user, stack, null);
    }
}
