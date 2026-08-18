package net.errorcraft.itematic.mixin.world.entity.ai.attributes;

import net.errorcraft.itematic.access.world.entity.ai.attributes.AttributeMapAccess;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(AttributeMap.class)
public abstract class AttributeMapExtender implements AttributeMapAccess {
    @Shadow
    @Final
    private AttributeSupplier supplier;

    @Shadow
    @Final
    private Map<Holder<Attribute>, AttributeInstance> attributes;

    @Shadow
    public abstract double getValue(Holder<Attribute> attribute);

    @Override
    public double itematic$getValue(Holder<Attribute> attribute, @Nullable Double possibleBase) {
        if (possibleBase == null) {
            return this.getValue(attribute);
        }

        AttributeInstance instance = this.attributes.get(attribute);
        return instance != null ?
            instance.itematic$getValue(possibleBase) :
            this.supplier.itematic$getValue(attribute, possibleBase);
    }
}
