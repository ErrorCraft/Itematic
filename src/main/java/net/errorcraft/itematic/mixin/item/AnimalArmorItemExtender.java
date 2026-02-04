package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.access.item.AnimalArmorItemTypeAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

public class AnimalArmorItemExtender {
    @Mixin(AnimalArmorItem.Type.class)
    public static class TypeExtender implements AnimalArmorItemTypeAccess {
        @Shadow
        @Final
        SoundEvent breakSound;

        @Shadow
        @Final
        RegistryEntryList<EntityType<?>> allowedEntities;

        @Override
        public RegistryEntry<SoundEvent> itematic$breakSound() {
            return Registries.SOUND_EVENT.getEntry(this.breakSound);
        }

        @Override
        public RegistryEntryList<EntityType<?>> itematic$allowedEntities() {
            return this.allowedEntities;
        }
    }
}
