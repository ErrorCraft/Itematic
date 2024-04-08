package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;

public record GlintItemComponent(boolean glint) implements ItemComponent<GlintItemComponent> {
    public static final Codec<GlintItemComponent> CODEC = Codec.BOOL.xmap(GlintItemComponent::new, GlintItemComponent::glint);

    @Override
    public ItemComponentType<GlintItemComponent> type() {
        return ItemComponentTypes.GLINT;
    }

    @Override
    public Codec<GlintItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, this.glint);
    }

    public static GlintItemComponent of(boolean glint) {
        return new GlintItemComponent(glint);
    }
}
