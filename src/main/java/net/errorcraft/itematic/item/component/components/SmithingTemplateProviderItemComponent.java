package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplate;

public record SmithingTemplateProviderItemComponent(SmithingTemplate template) implements ItemComponent<SmithingTemplateProviderItemComponent> {
    public static final Codec<SmithingTemplateProviderItemComponent> CODEC = SmithingTemplate.CODEC.xmap(
        SmithingTemplateProviderItemComponent::new,
        SmithingTemplateProviderItemComponent::template
    );

    public static SmithingTemplateProviderItemComponent of(SmithingTemplate template) {
        return new SmithingTemplateProviderItemComponent(template);
    }

    @Override
    public ItemComponentType<SmithingTemplateProviderItemComponent> type() {
        return ItemComponentTypes.SMITHING_TEMPLATE_PROVIDER;
    }

    @Override
    public Codec<SmithingTemplateProviderItemComponent> codec() {
        return CODEC;
    }
}
