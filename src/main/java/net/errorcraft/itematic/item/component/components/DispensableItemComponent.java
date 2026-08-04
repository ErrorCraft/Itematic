package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehavior;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;

public record DispensableItemComponent(Holder<DispenseBehavior> behavior) implements ItemComponent<DispensableItemComponent> {
    public static final Codec<DispensableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.create(ItematicRegistryKeys.DISPENSE_BEHAVIOR).fieldOf("behavior").forGetter(DispensableItemComponent::behavior)
    ).apply(instance, DispensableItemComponent::new));

    @Override
    public ItemComponentType<DispensableItemComponent> type() {
        return ItemComponentTypes.DISPENSABLE;
    }

    @Override
    public Codec<DispensableItemComponent> codec() {
        return CODEC;
    }

    public static DispensableItemComponent of(Holder<DispenseBehavior> behavior) {
        return new DispensableItemComponent(behavior);
    }
}
