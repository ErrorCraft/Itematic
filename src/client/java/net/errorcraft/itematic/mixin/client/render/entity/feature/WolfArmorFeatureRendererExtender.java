package net.errorcraft.itematic.mixin.client.render.entity.feature;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.render.entity.feature.WolfArmorFeatureRenderer;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WolfArmorFeatureRenderer.class)
public class WolfArmorFeatureRendererExtender {
    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/WolfEntity;FFFFFF)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/item/Items;WOLF_ARMOR:Lnet/minecraft/item/Item;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private Item getWolfArmorReturnNull() {
        return null;
    }

    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/WolfEntity;FFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/AnimalArmorItem;getEntityTexture()Lnet/minecraft/util/Identifier;"
        )
    )
    private Identifier getEntityTextureUseRegistryEntry(AnimalArmorItem instance, @Local(argsOnly = true) WolfEntity wolfEntity) {
        return wolfEntity.getBodyArmor()
            .itematic$getComponent(ItemComponentTypes.ARMOR)
            .orElseThrow()
            .textureId();
    }
}
