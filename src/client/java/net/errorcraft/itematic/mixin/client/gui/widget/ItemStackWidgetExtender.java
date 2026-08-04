package net.errorcraft.itematic.mixin.client.gui.widget;

import net.errorcraft.itematic.access.client.gui.widget.ItemStackWidgetAccess;
import net.minecraft.client.gui.components.ItemDisplayWidget;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemDisplayWidget.class)
public class ItemStackWidgetExtender implements ItemStackWidgetAccess {
    @Shadow
    @Final
    @Mutable
    private ItemStack itemStack;

    @Override
    public void itematic$setStack(ItemStack stack) {
        this.itemStack = stack;
    }
}
