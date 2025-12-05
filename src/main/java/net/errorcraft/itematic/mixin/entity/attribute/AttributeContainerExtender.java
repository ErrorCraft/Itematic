package net.errorcraft.itematic.mixin.entity.attribute;

import net.errorcraft.itematic.access.entity.attribute.AttributeContainerAccess;
import net.errorcraft.itematic.access.entity.attribute.DefaultAttributeContainerAccess;
import net.errorcraft.itematic.access.entity.attribute.EntityAttributeInstanceAccess;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(AttributeContainer.class)
public abstract class AttributeContainerExtender implements AttributeContainerAccess {
    @Shadow
    @Final
    private DefaultAttributeContainer fallback;

    @Shadow
    @Final
    private Map<RegistryEntry<EntityAttribute>, EntityAttributeInstance> custom;

    @Shadow
    public abstract double getValue(RegistryEntry<EntityAttribute> attribute);

    @Override
    public double itematic$getTrueBaseValue(RegistryEntry<EntityAttribute> attribute) {
        return this.fallback.getBaseValue(attribute);
    }

    @Override
    public double itematic$getValue(RegistryEntry<EntityAttribute> attribute, @Nullable Double possibleBase) {
        if (possibleBase == null) {
            return this.getValue(attribute);
        }

        EntityAttributeInstance instance = this.custom.get(attribute);
        return instance != null ?
            ((EntityAttributeInstanceAccess) instance).itematic$getValue(possibleBase) :
            ((DefaultAttributeContainerAccess) this.fallback).itematic$getValue(attribute, possibleBase);
    }
}
