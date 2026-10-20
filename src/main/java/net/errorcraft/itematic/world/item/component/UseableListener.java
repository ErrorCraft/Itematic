package net.errorcraft.itematic.world.item.component;

import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface UseableListener {
    interface Started {
        void onStartedUsing(LivingEntity user);
    }

    interface Stopped {
        void onStoppedUsing(ItemStack stack, ItemStackExchanger stackExchanger, Level level, LivingEntity user);
    }

    interface Finished {
        void itematic$onFinishedUsing(ItemStack stack, ItemStackExchanger stackExchanger, Level level, LivingEntity user);
    }
}
