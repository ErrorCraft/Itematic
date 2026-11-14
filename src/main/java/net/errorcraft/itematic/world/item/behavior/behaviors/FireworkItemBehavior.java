package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.Fireworks;

public record FireworkItemBehavior(Fireworks defaultFireworks) implements ItemBehavior<FireworkItemBehavior> {
    public static final Codec<FireworkItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Fireworks.CODEC.fieldOf("default").forGetter(FireworkItemBehavior::defaultFireworks)
    ).apply(instance, FireworkItemBehavior::new));

    public static FireworkItemBehavior of(Fireworks defaultFireworks) {
        return new FireworkItemBehavior(defaultFireworks);
    }

    @Override
    public ItemBehaviorType<FireworkItemBehavior> type() {
        return ItemBehaviorType.FIREWORK;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.FIREWORKS, this.defaultFireworks);
    }
}
