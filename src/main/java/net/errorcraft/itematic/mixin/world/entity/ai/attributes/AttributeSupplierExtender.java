package net.errorcraft.itematic.mixin.world.entity.ai.attributes;

import net.errorcraft.itematic.access.world.entity.ai.attributes.AttributeSupplierAccess;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AttributeSupplier.class)
public abstract class AttributeSupplierExtender implements AttributeSupplierAccess {
    @Shadow
    protected abstract AttributeInstance getAttributeInstance(Holder<Attribute> attribute);

    @Override
    public double itematic$getValue(Holder<Attribute> attribute, double base) {
        return this.getAttributeInstance(attribute).itematic$getValue(base);
    }
}
