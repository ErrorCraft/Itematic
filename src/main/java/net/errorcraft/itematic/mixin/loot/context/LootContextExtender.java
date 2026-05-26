package net.errorcraft.itematic.mixin.loot.context;

import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.context.ContextParameter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

public class LootContextExtender {
    @Mixin(LootContext.EntityTarget.class)
    public enum EntityTargetExtender {
        ITEMATIC_TARGET_ENTITY("target_entity", ItematicContextParameters.TARGET_ENTITY);

        @Shadow
        EntityTargetExtender(String type, ContextParameter<? extends Entity> parameter) {}
    }
}
