package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;

public record DyeItemBehavior(DyeColor color) implements ItemBehavior {
    public static final Codec<DyeItemBehavior> CODEC = DyeColor.CODEC.xmap(DyeItemBehavior::new, DyeItemBehavior::color);

    public static DyeItemBehavior of(DyeColor color) {
        return new DyeItemBehavior(color);
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.DYE, this.color);
    }
}
