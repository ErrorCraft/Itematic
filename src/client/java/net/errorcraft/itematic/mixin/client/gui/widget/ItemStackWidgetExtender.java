package net.errorcraft.itematic.mixin.client.gui.widget;

import net.errorcraft.itematic.access.client.gui.widget.ItemStackWidgetAccess;
import net.minecraft.client.gui.widget.ItemStackWidget;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStackWidget.class)
public class ItemStackWidgetExtender implements ItemStackWidgetAccess {
    @Shadow
    @Final
    @Mutable
    private ItemStack stack;

    @Override
    public void itematic$setStack(ItemStack stack) {
        this.stack = stack;
    }
}
