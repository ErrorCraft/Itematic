package net.errorcraft.itematic.mixin.entity.attribute;

import net.errorcraft.itematic.access.entity.attribute.EntityAttributeInstanceAccess;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityAttributeInstance.class)
public abstract class EntityAttributeInstanceExtender implements EntityAttributeInstanceAccess {
    @Shadow
    private double baseValue;

    @Shadow
    public abstract double getValue();

    @Shadow
    protected abstract double computeValue();

    @Override
    public double itematic$getValue(double base) {
        if (this.baseValue == base) {
            return this.getValue();
        }

        // Temporarily update the base value without sending any updates by directly modifying the field
        double originalBase = this.baseValue;
        this.baseValue = base;
        double value = this.computeValue();
        this.baseValue = originalBase;
        return value;
    }
}
