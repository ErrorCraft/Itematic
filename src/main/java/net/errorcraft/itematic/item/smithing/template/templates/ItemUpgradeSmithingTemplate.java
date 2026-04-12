package net.errorcraft.itematic.item.smithing.template.templates;

import net.errorcraft.itematic.item.smithing.template.SmithingTemplate;
import net.errorcraft.itematic.mixin.item.SmithingTemplateItemAccessor;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class ItemUpgradeSmithingTemplate implements SmithingTemplate {
    private static final List<Identifier> EMPTY_BASE_SLOT_TEXTURES = SmithingTemplateItemAccessor.itemUpgradeEmptyBaseSlotTextures();
    private static final List<Identifier> EMPTY_ADDITIONS_SLOT_TEXTURES = SmithingTemplateItemAccessor.itemUpgradeEmptyAdditionsSlotTextures();
    private static final Text BASE_SLOT_DESCRIPTION = SmithingTemplateItemAccessor.itemUpgradeBaseSlotDescription();
    private static final Text ADDITIONS_SLOT_DESCRIPTION = SmithingTemplateItemAccessor.itemUpgradeAdditionsSlotDescription();

    @Override
    public List<Identifier> emptyBaseSlotTextures() {
        return EMPTY_BASE_SLOT_TEXTURES;
    }

    @Override
    public List<Identifier> emptyAdditionsSlotTextures() {
        return EMPTY_ADDITIONS_SLOT_TEXTURES;
    }

    @Override
    public Text baseSlotDescription() {
        return BASE_SLOT_DESCRIPTION;
    }

    @Override
    public Text additionsSlotDescription() {
        return ADDITIONS_SLOT_DESCRIPTION;
    }
}
