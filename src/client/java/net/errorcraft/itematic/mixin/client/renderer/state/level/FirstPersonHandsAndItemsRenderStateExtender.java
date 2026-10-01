package net.errorcraft.itematic.mixin.client.renderer.state.level;

import net.errorcraft.itematic.access.client.renderer.state.level.FirstPersonHandsAndItemsRenderStateAccess;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(FirstPersonHandsAndItemsRenderState.class)
public class FirstPersonHandsAndItemsRenderStateExtender implements FirstPersonHandsAndItemsRenderStateAccess {
    @Unique
    private int usedItemTicks;

    @Override
    public int itematic$usedItemTicks() {
        return this.usedItemTicks;
    }

    @Override
    public void itematic$setUsedItemTicks(int usedTicks) {
        this.usedItemTicks = usedTicks;
    }
}
