package net.errorcraft.itematic.mixin.loot.context;

import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.context.ContextParameter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

public class LootContextExtender {
    @Mixin(LootContext.EntityReference.class)
    public enum EntityReferenceExtender {
        ITEMATIC_SPAWNED_ENTITY("spawned_entity", ItematicContextParameters.SPAWNED_ENTITY);

        @Shadow
        EntityReferenceExtender(String type, ContextParameter<? extends Entity> parameter) {}
    }
}
