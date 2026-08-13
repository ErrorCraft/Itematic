package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.equipment.Glider;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record GliderItemBehavior(Glider glider) implements ItemBehavior<GliderItemBehavior> {
    public static final Codec<GliderItemBehavior> CODEC = Glider.CODEC.xmap(GliderItemBehavior::new, GliderItemBehavior::glider);

    public static GliderItemBehavior of(ItemPredicate condition) {
        return new GliderItemBehavior(new Glider(Optional.of(condition)));
    }

    @Override
    public ItemBehaviorType<GliderItemBehavior> type() {
        return ItemBehaviorType.GLIDER;
    }

    @Override
    public Codec<GliderItemBehavior> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.GLIDER, this.glider);
    }

    public boolean canUse(ItemStack stack) {
        Glider glider = stack.get(ItematicDataComponents.GLIDER);
        if (glider == null) {
            return false;
        }

        return glider.canUse(stack);
    }
}
