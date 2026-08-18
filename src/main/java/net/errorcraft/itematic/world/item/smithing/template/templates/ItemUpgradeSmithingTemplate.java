package net.errorcraft.itematic.world.item.smithing.template.templates;

import net.errorcraft.itematic.mixin.world.item.SmithingTemplateItemAccessor;
import net.errorcraft.itematic.world.item.smithing.template.SmithingTemplate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;

public class ItemUpgradeSmithingTemplate implements SmithingTemplate {
    private static final List<Identifier> EMPTY_BASE_SLOT_TEXTURES = SmithingTemplateItemAccessor.itemUpgradeEmptyBaseSlotTextures();
    private static final List<Identifier> EMPTY_ADDITIONS_SLOT_TEXTURES = SmithingTemplateItemAccessor.itemUpgradeEmptyAdditionsSlotTextures();
    private static final Component BASE_SLOT_DESCRIPTION = SmithingTemplateItemAccessor.itemUpgradeBaseSlotDescription();
    private static final Component ADDITIONS_SLOT_DESCRIPTION = SmithingTemplateItemAccessor.itemUpgradeAdditionsSlotDescription();

    @Override
    public List<Identifier> emptyBaseSlotTextures() {
        return EMPTY_BASE_SLOT_TEXTURES;
    }

    @Override
    public List<Identifier> emptyAdditionsSlotTextures() {
        return EMPTY_ADDITIONS_SLOT_TEXTURES;
    }

    @Override
    public Component baseSlotDescription() {
        return BASE_SLOT_DESCRIPTION;
    }

    @Override
    public Component additionsSlotDescription() {
        return ADDITIONS_SLOT_DESCRIPTION;
    }
}
