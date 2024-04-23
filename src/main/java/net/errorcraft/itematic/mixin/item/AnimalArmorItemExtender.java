package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.access.item.AnimalArmorItemTypeAccess;
import net.errorcraft.itematic.item.armor.ArmorMaterial;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;
import java.util.function.Function;

public class AnimalArmorItemExtender {
    @Mixin(AnimalArmorItem.Type.class)
    public static class TypeExtender implements AnimalArmorItemTypeAccess {
        @Shadow
        @Final
        public static AnimalArmorItem.Type EQUESTRIAN;

        @Shadow
        @Final
        public static AnimalArmorItem.Type CANINE;

        @Unique
        private String name;

        @Unique
        private Function<RegistryEntry<ArmorMaterial>, Identifier> materialToTextureIdMapper;

        static {
            ((TypeExtender)(Object) EQUESTRIAN).materialToTextureIdMapper = entry -> entry.value().assetId().withPath(path -> "entity/horse/armor/horse_armor_" + path);
            ((TypeExtender)(Object) CANINE).materialToTextureIdMapper = entry -> entry.value().assetId().withPath("entity/wolf/wolf_armor");
        }

        @Inject(
            method = "<init>",
            at = @At("TAIL")
        )
        private void setName(String name, int ordinal, Function<String, Identifier> textureIdFunction, SoundEvent breakSound, CallbackInfo info) {
            this.name = name.toLowerCase(Locale.ROOT);
        }

        @Override
        public String asString() {
            return this.name;
        }

        @Override
        public Identifier itematic$textureId(RegistryEntry<ArmorMaterial> armorMaterial) {
            return this.materialToTextureIdMapper.apply(armorMaterial);
        }
    }
}
