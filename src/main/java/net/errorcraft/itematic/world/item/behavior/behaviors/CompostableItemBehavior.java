package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;

public record CompostableItemBehavior(float levelIncreaseChance) implements ItemBehavior<CompostableItemBehavior> {
    public static final Codec<CompostableItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.floatRange(0.0f, 1.0f).fieldOf("level_increase_chance").forGetter(CompostableItemBehavior::levelIncreaseChance)
    ).apply(instance, CompostableItemBehavior::new));

    public static CompostableItemBehavior of(float levelIncreaseChance) {
        return new CompostableItemBehavior(levelIncreaseChance);
    }

    @Override
    public ItemBehaviorType<CompostableItemBehavior> type() {
        return ItemBehaviorType.COMPOSTABLE;
    }

}
