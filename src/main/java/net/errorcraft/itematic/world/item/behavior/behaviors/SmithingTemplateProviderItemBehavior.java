package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.smithing.template.SmithingTemplate;

public record SmithingTemplateProviderItemBehavior(SmithingTemplate template) implements ItemBehavior<SmithingTemplateProviderItemBehavior> {
    public static final Codec<SmithingTemplateProviderItemBehavior> CODEC = SmithingTemplate.CODEC.xmap(
        SmithingTemplateProviderItemBehavior::new,
        SmithingTemplateProviderItemBehavior::template
    );

    public static SmithingTemplateProviderItemBehavior of(SmithingTemplate template) {
        return new SmithingTemplateProviderItemBehavior(template);
    }

    @Override
    public ItemBehaviorType<SmithingTemplateProviderItemBehavior> type() {
        return ItemBehaviorType.SMITHING_TEMPLATE_PROVIDER;
    }

    @Override
    public Codec<SmithingTemplateProviderItemBehavior> codec() {
        return CODEC;
    }
}
