package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.util.UseActionUtil;
import net.minecraft.util.UseAction;

public record UseAnimationItemComponent(UseAction animation) implements ItemComponent<UseAnimationItemComponent> {
    public static final Codec<UseAnimationItemComponent> CODEC = UseActionUtil.CODEC.xmap(UseAnimationItemComponent::new, UseAnimationItemComponent::animation);

    public static UseAnimationItemComponent of(UseAction animation) {
        return new UseAnimationItemComponent(animation);
    }

    @Override
    public ItemComponentType<UseAnimationItemComponent> type() {
        return ItemComponentTypes.USE_ANIMATION;
    }

    @Override
    public Codec<UseAnimationItemComponent> codec() {
        return CODEC;
    }
}
