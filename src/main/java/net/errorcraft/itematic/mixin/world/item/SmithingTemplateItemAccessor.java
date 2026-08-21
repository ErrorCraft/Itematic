package net.errorcraft.itematic.mixin.world.item;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.SmithingTemplateItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(SmithingTemplateItem.class)
public interface SmithingTemplateItemAccessor {
    @Accessor("SMITHING_TEMPLATE_SUFFIX")
    static Component smithingTemplateTitle() {
        throw new AssertionError();
    }

    @Accessor("APPLIES_TO_TITLE")
    static Component appliesToTitle() {
        throw new AssertionError();
    }

    @Accessor("ARMOR_TRIM_APPLIES_TO")
    static Component trimPatternAppliesToLabel() {
        throw new AssertionError();
    }

    @Accessor("INGREDIENTS_TITLE")
    static Component ingredientsTitle() {
        throw new AssertionError();
    }

    @Accessor("ARMOR_TRIM_INGREDIENTS")
    static Component trimPatternIngredients() {
        throw new AssertionError();
    }

    @Accessor("ARMOR_TRIM_BASE_SLOT_DESCRIPTION")
    static Component trimPatternBaseSlotDescription() {
        throw new AssertionError();
    }

    @Accessor("ARMOR_TRIM_ADDITIONS_SLOT_DESCRIPTION")
    static Component trimPatternAdditionsSlotDescription() {
        throw new AssertionError();
    }

    @Accessor("NETHERITE_UPGRADE_BASE_SLOT_DESCRIPTION")
    static Component itemUpgradeBaseSlotDescription() {
        throw new AssertionError();
    }

    @Accessor("NETHERITE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION")
    static Component itemUpgradeAdditionsSlotDescription() {
        throw new AssertionError();
    }

    @Invoker("createTrimmableArmorIconList")
    static List<Identifier> trimPatternEmptyBaseSlotTextures() {
        throw new AssertionError();
    }

    @Invoker("createTrimmableMaterialIconList")
    static List<Identifier> trimPatternEmptyAdditionsSlotTextures() {
        throw new AssertionError();
    }

    @Invoker("createNetheriteUpgradeIconList")
    static List<Identifier> itemUpgradeEmptyBaseSlotTextures() {
        throw new AssertionError();
    }

    @Invoker("createNetheriteUpgradeMaterialList")
    static List<Identifier> itemUpgradeEmptyAdditionsSlotTextures() {
        throw new AssertionError();
    }
}
