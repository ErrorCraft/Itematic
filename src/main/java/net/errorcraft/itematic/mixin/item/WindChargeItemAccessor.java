package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.WindChargeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WindChargeItem.class)
public interface WindChargeItemAccessor {
    @Accessor("COOLDOWN")
    static int cooldown() {
        throw new AssertionError();
    }
}
