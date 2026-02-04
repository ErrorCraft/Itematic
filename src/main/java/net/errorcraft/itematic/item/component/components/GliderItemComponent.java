package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.component.type.GliderDataComponent;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.item.ItemPredicate;

import java.util.Optional;

public record GliderItemComponent(GliderDataComponent glider) implements ItemComponent<GliderItemComponent> {
    public static final Codec<GliderItemComponent> CODEC = GliderDataComponent.CODEC.xmap(GliderItemComponent::new, GliderItemComponent::glider);

    public static GliderItemComponent of(ItemPredicate condition) {
        return new GliderItemComponent(new GliderDataComponent(Optional.of(condition)));
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
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(ItematicDataComponentTypes.GLIDER, this.glider);
    }

    public boolean canUse(ItemStack stack) {
        GliderDataComponent glider = stack.get(ItematicDataComponentTypes.GLIDER);
        if (glider == null) {
            return false;
        }

        return glider.canUse(stack);
    }
}
