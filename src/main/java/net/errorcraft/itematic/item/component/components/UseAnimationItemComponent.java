package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.util.UseActionUtil;
import net.minecraft.util.UseAction;

public record UseAnimationItemComponent(UseAction animation) implements ItemComponent {
    public static final Codec<UseAnimationItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        UseActionUtil.CODEC.fieldOf("animation").forGetter(UseAnimationItemComponent::animation)
    ).apply(instance, UseAnimationItemComponent::new));

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.USE_ANIMATION;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
        return CODEC;
    }
}
