package net.errorcraft.itematic.mixin.entity.attribute;

import net.errorcraft.itematic.access.entity.attribute.DefaultAttributeContainerAccess;
import net.errorcraft.itematic.access.entity.attribute.EntityAttributeInstanceAccess;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DefaultAttributeContainer.class)
public abstract class DefaultAttributeContainerExtender implements DefaultAttributeContainerAccess {
    @Shadow
    protected abstract EntityAttributeInstance require(RegistryEntry<EntityAttribute> attribute);

    @Override
    public double itematic$getValue(RegistryEntry<EntityAttribute> attribute, double base) {
        return ((EntityAttributeInstanceAccess) this.require(attribute)).itematic$getValue(base);
    }
}
