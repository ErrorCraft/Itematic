package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.ArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.EnumMap;
import java.util.UUID;

@Mixin(ArmorItem.class)
public interface ArmorItemAccessor {
    @Accessor("MODIFIERS")
    static EnumMap<ArmorItem.Type, UUID> attributeModifiers() {
        throw new AssertionError();
    }
}
