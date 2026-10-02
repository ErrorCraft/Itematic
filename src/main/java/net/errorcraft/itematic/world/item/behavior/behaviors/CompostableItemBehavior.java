package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.component.Compostable;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;

public record CompostableItemBehavior(ResolvableInt layers) implements ItemBehavior<CompostableItemBehavior> {
    public static final Codec<CompostableItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResolvableInt.CODEC.fieldOf("layers").forGetter(CompostableItemBehavior::layers)
    ).apply(instance, CompostableItemBehavior::new));

    public static CompostableItemBehavior of(ResourceKey<ContextIntProvider> layers) {
        return new CompostableItemBehavior(ResolvableInt.fromKey(layers));
    }

    @Override
    public ItemBehaviorType<CompostableItemBehavior> type() {
        return ItemBehaviorType.COMPOSTABLE;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.COMPOSTABLE, new Compostable(this.layers));
    }
}
