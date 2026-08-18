package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.dispenser.behavior.DispenseBehavior;
import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;

public record DispensableItemBehavior(Holder<DispenseBehavior> behavior) implements ItemBehavior<DispensableItemBehavior> {
    public static final Codec<DispensableItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.create(ItematicRegistries.DISPENSE_BEHAVIOR).fieldOf("behavior").forGetter(DispensableItemBehavior::behavior)
    ).apply(instance, DispensableItemBehavior::new));

    public static DispensableItemBehavior of(Holder<DispenseBehavior> behavior) {
        return new DispensableItemBehavior(behavior);
    }

    @Override
    public ItemBehaviorType<DispensableItemBehavior> type() {
        return ItemBehaviorType.DISPENSABLE;
    }

}
