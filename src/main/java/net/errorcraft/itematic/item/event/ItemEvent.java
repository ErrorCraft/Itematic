package net.errorcraft.itematic.item.event;

import net.minecraft.registry.RegistryKey;

public record ItemEvent(RegistryKey<ItemEvent> id) {
    @Override
    public String toString() {
        return this.id.getValue().toString();
    }
}
