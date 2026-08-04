package net.errorcraft.itematic.mixin.loot.context;

import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

public class LootContextExtender {
    @Mixin(LootContext.EntityTarget.class)
    public enum EntityReferenceExtender {
        ITEMATIC_SPAWNED_ENTITY("spawned_entity", ItematicContextParameters.SPAWNED_ENTITY);

        @Shadow
        EntityReferenceExtender(String type, ContextKey<? extends Entity> parameter) {}
    }
}
