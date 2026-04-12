package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(SmithingTemplateItem.class)
public interface SmithingTemplateItemAccessor {
    @Accessor("SMITHING_TEMPLATE_TEXT")
    static Text smithingTemplateTitle() {
        throw new AssertionError();
    }

    @Accessor("APPLIES_TO_TEXT")
    static Text appliesToTitle() {
        throw new AssertionError();
    }

    @Accessor("ARMOR_TRIM_APPLIES_TO_TEXT")
    static Text trimPatternAppliesToLabel() {
        throw new AssertionError();
    }

    @Accessor("INGREDIENTS_TEXT")
    static Text ingredientsTitle() {
        throw new AssertionError();
    }

    @Accessor("ARMOR_TRIM_INGREDIENTS_TEXT")
    static Text trimPatternIngredients() {
        throw new AssertionError();
    }

    @Accessor("ARMOR_TRIM_BASE_SLOT_DESCRIPTION_TEXT")
    static Text trimPatternBaseSlotDescription() {
        throw new AssertionError();
    }

    @Accessor("ARMOR_TRIM_ADDITIONS_SLOT_DESCRIPTION_TEXT")
    static Text trimPatternAdditionsSlotDescription() {
        throw new AssertionError();
    }

    @Accessor("NETHERITE_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT")
    static Text itemUpgradeBaseSlotDescription() {
        throw new AssertionError();
    }

    @Accessor("NETHERITE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT")
    static Text itemUpgradeAdditionsSlotDescription() {
        throw new AssertionError();
    }

    @Invoker("getArmorTrimEmptyBaseSlotTextures")
    static List<Identifier> trimPatternEmptyBaseSlotTextures() {
        throw new AssertionError();
    }

    @Invoker("getArmorTrimEmptyAdditionsSlotTextures")
    static List<Identifier> trimPatternEmptyAdditionsSlotTextures() {
        throw new AssertionError();
    }

    @Invoker("getNetheriteUpgradeEmptyBaseSlotTextures")
    static List<Identifier> itemUpgradeEmptyBaseSlotTextures() {
        throw new AssertionError();
    }

    @Invoker("getNetheriteUpgradeEmptyAdditionsSlotTextures")
    static List<Identifier> itemUpgradeEmptyAdditionsSlotTextures() {
        throw new AssertionError();
    }
}
