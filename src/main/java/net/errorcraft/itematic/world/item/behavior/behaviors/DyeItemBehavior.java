package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;

public record DyeItemBehavior(DyeColor color) implements ItemBehavior<DyeItemBehavior> {
    public static final Codec<DyeItemBehavior> CODEC = DyeColor.CODEC.xmap(DyeItemBehavior::new, DyeItemBehavior::color);

    public static DyeItemBehavior of(DyeColor color) {
        return new DyeItemBehavior(color);
    }

    @Override
    public ItemBehaviorType<DyeItemBehavior> type() {
        return ItemBehaviorType.DYE;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.DYE, this.color);
    }
}
