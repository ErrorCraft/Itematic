package net.errorcraft.itematic.item.event;

import net.minecraft.resources.ResourceKey;

public record ItemEvent(ResourceKey<ItemEvent> id) {
    @Override
    public String toString() {
        return this.id.identifier().toString();
    }
}
