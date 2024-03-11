package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.BlockAccess;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShulkerBoxBlock.class)
public class ShulkerBoxBlockExtender implements BlockAccess {
    @Override
    public void itematic$addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT);
    }
}
