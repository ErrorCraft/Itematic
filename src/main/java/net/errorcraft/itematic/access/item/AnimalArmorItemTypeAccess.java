package net.errorcraft.itematic.access.item;

import net.errorcraft.itematic.item.armor.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

public interface AnimalArmorItemTypeAccess extends StringIdentifiable {
    @Override
    default String asString() {
        return null;
    }
    default Identifier itematic$textureId(RegistryEntry<ArmorMaterial> armorMaterial) {
        return null;
    }
}
