package net.errorcraft.itematic.client.resources.item.smithing;

import net.errorcraft.itematic.mixin.world.item.SmithingTemplateItemAccessor;
import net.errorcraft.itematic.references.SmithingTemplateDisplayIds;
import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;

public class SmithingTemplateDisplays {
    private SmithingTemplateDisplays() {}

    public static void bootstrap(BiConsumer<Identifier, SmithingTemplateDisplay> provider) {
        provider.accept(
            SmithingTemplateDisplayIds.TRIM_PATTERN,
            new SmithingTemplateDisplay(
                new SmithingTemplateDisplay.Slot(
                    SmithingTemplateItemAccessor.trimPatternBaseSlotDescription(),
                    SmithingTemplateItemAccessor.trimPatternBaseSlotEmptyTextures()
                ),
                new SmithingTemplateDisplay.Slot(
                    SmithingTemplateItemAccessor.trimPatternAdditionsSlotDescription(),
                    SmithingTemplateItemAccessor.trimPatternAdditionsSlotEmptyTextures()
                )
            )
        );
        provider.accept(
            SmithingTemplateDisplayIds.NETHERITE_UPGRADE,
            new SmithingTemplateDisplay(
                new SmithingTemplateDisplay.Slot(
                    SmithingTemplateItemAccessor.netheriteUpgradeBaseSlotDescription(),
                    SmithingTemplateItemAccessor.netheriteUpgradeBaseSlotEmptyTextures()
                ),
                new SmithingTemplateDisplay.Slot(
                    SmithingTemplateItemAccessor.netheriteUpgradeAdditionsSlotDescription(),
                    SmithingTemplateItemAccessor.netheriteUpgradeAdditionsSlotEmptyTextures()
                )
            )
        );
    }
}
