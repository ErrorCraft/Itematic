package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.access.item.AnimalArmorItemTypeAccess;
import net.errorcraft.itematic.item.armor.ArmorMaterial;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;
import java.util.function.Function;

public class AnimalArmorItemExtender {
    @Mixin(AnimalArmorItem.Type.class)
    public static class TypeExtender implements AnimalArmorItemTypeAccess {
        @Unique
        private String name;

        @Unique
        private Function<RegistryEntry<ArmorMaterial>, Identifier> materialToTextureIdMapper;

        @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                value = "NEW",
                target = "(Ljava/lang/String;ILjava/util/function/Function;Lnet/minecraft/sound/SoundEvent;)Lnet/minecraft/item/AnimalArmorItem$Type;",
                ordinal = 0
            )
        )
        private static AnimalArmorItem.Type equestrianSetMaterialToTextureIdMapper(AnimalArmorItem.Type original) {
            ((TypeExtender)(Object) original).materialToTextureIdMapper = entry -> entry.value().assetId().withPath(path -> "entity/horse/armor/horse_armor_" + path);
            return original;
        }

        @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                value = "NEW",
                target = "(Ljava/lang/String;ILjava/util/function/Function;Lnet/minecraft/sound/SoundEvent;)Lnet/minecraft/item/AnimalArmorItem$Type;",
                ordinal = 0
            ),
            slice = @Slice(
                from = @At(
                    value = "CONSTANT",
                    args = "stringValue=EQUESTRIAN"
                )
            )
        )
        private static AnimalArmorItem.Type canineSetMaterialToTextureIdMapper(AnimalArmorItem.Type original) {
            ((TypeExtender)(Object) original).materialToTextureIdMapper = entry -> entry.value().assetId().withPath("textures/entity/wolf/wolf_armor");
            return original;
        }

        @Inject(
            method = "<init>",
            at = @At("TAIL")
        )
        private void initSetNameField(String string, int i, Function<String, Identifier> textureIdFunction, SoundEvent breakSound, CallbackInfo info) {
            this.name = string.toLowerCase(Locale.ROOT);
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
