package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.Identifier;

public record SmithingTemplateDisplayItemBehavior(Identifier smithingTemplateDisplay) implements ItemBehavior<SmithingTemplateDisplayItemBehavior> {
    public static final Codec<SmithingTemplateDisplayItemBehavior> CODEC = Identifier.CODEC.xmap(
        SmithingTemplateDisplayItemBehavior::new,
        SmithingTemplateDisplayItemBehavior::smithingTemplateDisplay
    );

    public static SmithingTemplateDisplayItemBehavior of(Identifier smithingTemplateDisplay) {
        return new SmithingTemplateDisplayItemBehavior(smithingTemplateDisplay);
    }

    @Override
    public ItemBehaviorType<SmithingTemplateDisplayItemBehavior> type() {
        return ItemBehaviorType.SMITHING_TEMPLATE_DISPLAY;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.SMITHING_TEMPLATE_DISPLAY, this.smithingTemplateDisplay);
    }
}
