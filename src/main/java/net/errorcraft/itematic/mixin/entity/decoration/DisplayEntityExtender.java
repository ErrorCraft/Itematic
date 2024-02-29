package net.errorcraft.itematic.mixin.entity.decoration;

import net.errorcraft.itematic.text.TextUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class DisplayEntityExtender {
    @Mixin(DisplayEntity.TextDisplayEntity.class)
    public static abstract class TextDisplayEntityExtender extends DisplayEntity {
        public TextDisplayEntityExtender(EntityType<?> entityType, World world) {
            super(entityType, world);
        }

        @Redirect(
            method = "writeCustomDataToNbt",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/text/Text$Serialization;toJsonString(Lnet/minecraft/text/Text;)Ljava/lang/String;"
            )
        )
        private String toJsonStringUseDynamicRegistry(Text text) {
            return TextUtil.toJsonString(text, this.getWorld().getRegistryManager());
        }

        @Redirect(
            method = "readCustomDataFromNbt",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/text/Text$Serialization;fromJson(Ljava/lang/String;)Lnet/minecraft/text/MutableText;"
            )
        )
        private MutableText fromJsonUseDynamicRegistry(String json) {
            return TextUtil.fromJsonString(json, this.getWorld().getRegistryManager());
        }
    }
}
