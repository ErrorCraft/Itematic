package net.errorcraft.itematic.mixin.world.inventory;

import net.minecraft.world.inventory.RecipeBookType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RecipeBookType.class)
public enum RecipeBookTypeExtender {
    ITEMATIC_BREWING;

    @Shadow
    RecipeBookTypeExtender() {}
}
