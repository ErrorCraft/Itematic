package net.errorcraft.itematic.access.client;

import net.errorcraft.itematic.client.resources.item.bar.ItemBarStyleManager;
import net.errorcraft.itematic.client.resources.item.smithing.SmithingTemplateDisplayManager;

public interface MinecraftAccess {
    default ItemBarStyleManager itematic$itemBarStyles() {
        throw new AssertionError("Implemented via mixin");
    }
    default SmithingTemplateDisplayManager itematic$smithingTemplateDisplays() {
        throw new AssertionError("Implemented via mixin");
    }
}
