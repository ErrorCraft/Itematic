package net.errorcraft.itematic.item.smithing.template.templates;

import net.errorcraft.itematic.item.smithing.template.SmithingTemplate;
import net.errorcraft.itematic.mixin.item.SmithingTemplateItemAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import java.util.List;

public class TrimPatternSmithingTemplate implements SmithingTemplate {
    private static final List<Identifier> EMPTY_BASE_SLOT_TEXTURES = SmithingTemplateItemAccessor.trimPatternEmptyBaseSlotTextures();
    private static final List<Identifier> EMPTY_ADDITIONS_SLOT_TEXTURES = SmithingTemplateItemAccessor.trimPatternEmptyAdditionsSlotTextures();
    private static final Component BASE_SLOT_DESCRIPTION = SmithingTemplateItemAccessor.trimPatternBaseSlotDescription();
    private static final Component ADDITIONS_SLOT_DESCRIPTION = SmithingTemplateItemAccessor.trimPatternAdditionsSlotDescription();

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
