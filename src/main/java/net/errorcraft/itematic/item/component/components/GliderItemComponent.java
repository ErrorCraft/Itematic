package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.item.equipment.Glider;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record GliderItemComponent(Glider glider) implements ItemComponent<GliderItemComponent> {
    public static final Codec<GliderItemComponent> CODEC = Glider.CODEC.xmap(GliderItemComponent::new, GliderItemComponent::glider);

    public static GliderItemComponent of(ItemPredicate condition) {
        return new GliderItemComponent(new Glider(Optional.of(condition)));
    }

    @Override
    public ItemComponentType<GliderItemComponent> type() {
        return ItemComponentTypes.GLIDER;
    }

    @Override
    public Codec<GliderItemComponent> codec() {
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
