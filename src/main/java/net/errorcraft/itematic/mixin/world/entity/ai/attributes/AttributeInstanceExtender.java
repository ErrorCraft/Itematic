package net.errorcraft.itematic.mixin.world.entity.ai.attributes;

import net.errorcraft.itematic.access.world.entity.ai.attributes.AttributeInstanceAccess;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AttributeInstance.class)
public abstract class AttributeInstanceExtender implements AttributeInstanceAccess {
    @Shadow
    private double baseValue;

    @Shadow
    public abstract double getValue();

    @Shadow
    protected abstract double calculateValue();

    @Override
    public double itematic$getValue(double base) {
        if (this.baseValue == base) {
            return this.getValue();
        }

        // Temporarily update the base value without sending any updates by directly modifying the field
        double originalBase = this.baseValue;
        this.baseValue = base;
        double value = this.calculateValue();
        this.baseValue = originalBase;
        return value;
    }
}
