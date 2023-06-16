package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.FireworkRocketItem;
import net.minecraft.util.StringIdentifiable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

public class FireworkRocketItemExtender {
    @Mixin(FireworkRocketItem.Type.class)
    public static class TypeExtender implements StringIdentifiable {
        @Shadow
        @Final
        private String name;

        @Override
        public String asString() {
            return this.name;
        }
    }
}
