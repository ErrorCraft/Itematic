package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(SmithingTemplateItem.class)
public interface SmithingTemplateItemAccessor {
    @Accessor("TITLE_FORMATTING")
    static Formatting getTitleFormatting() {
        throw new AssertionError();
    }

    @Accessor("DESCRIPTION_FORMATTING")
    static Formatting getDescriptionFormatting() {
        throw new AssertionError();
    }

    @Accessor("SMITHING_TEMPLATE_TEXT")
    static Text getSmithingTemplateText() {
        throw new AssertionError();
    }

    @Accessor("APPLIES_TO_TEXT")
    static Text getAppliesToText() {
        throw new AssertionError();
    }

    @Accessor("ARMOR_TRIM_APPLIES_TO_TEXT")
    static Text getTrimPatternAppliesToText() {
        throw new AssertionError();
    }

    @Accessor("INGREDIENTS_TEXT")
    static Text getIngredientsText() {
        throw new AssertionError();
    }

    @Accessor("ARMOR_TRIM_INGREDIENTS_TEXT")
    static Text getTrimPatternIngredientsText() {
        throw new AssertionError();
    }

    @Accessor("ARMOR_TRIM_BASE_SLOT_DESCRIPTION_TEXT")
    static Text getTrimPatternBaseSlotDescriptionText() {
        throw new AssertionError();
    }

    @Accessor("ARMOR_TRIM_ADDITIONS_SLOT_DESCRIPTION_TEXT")
    static Text getTrimPatternAdditionsSlotDescriptionText() {
        throw new AssertionError();
    }

    @Accessor("NETHERITE_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT")
    static Text getItemUpgradeBaseSlotDescriptionText() {
        throw new AssertionError();
    }

    @Accessor("NETHERITE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT")
    static Text getItemUpgradeAdditionsSlotDescriptionText() {
        throw new AssertionError();
    }

    @Invoker("getArmorTrimEmptyBaseSlotTextures")
    static List<Identifier> getTrimPatternEmptyBaseSlotTextures() {
        throw new AssertionError();
    }

    @Invoker("getArmorTrimEmptyAdditionsSlotTextures")
    static List<Identifier> getTrimPatternEmptyAdditionsSlotTextures() {
        throw new AssertionError();
    }

    @Invoker("getNetheriteUpgradeEmptyBaseSlotTextures")
    static List<Identifier> getItemUpgradeEmptyBaseSlotTextures() {
        throw new AssertionError();
    }

    @Invoker("getNetheriteUpgradeEmptyAdditionsSlotTextures")
    static List<Identifier> getItemUpgradeEmptyAdditionsSlotTextures() {
        throw new AssertionError();
    }
}
