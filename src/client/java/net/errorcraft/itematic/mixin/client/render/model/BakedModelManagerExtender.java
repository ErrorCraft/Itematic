package net.errorcraft.itematic.mixin.client.render.model;

import com.google.common.collect.ImmutableMap;
import net.errorcraft.itematic.access.client.render.model.BakedModelManagerAccess;
import net.errorcraft.itematic.client.render.ItematicTexturedRenderLayers;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(BakedModelManager.class)
public class BakedModelManagerExtender implements BakedModelManagerAccess {
    @Shadow
    @Final
    @Mutable
    private static Map<Identifier, Identifier> LAYERS_TO_LOADERS;

    @Shadow
    @Final
    private BlockColors colorMap;

    @Inject(
        method = "<clinit>",
        at = @At("TAIL")
    )
    private static void addCustomLayersToLoaders(CallbackInfo info) {
        LAYERS_TO_LOADERS = new ImmutableMap.Builder<Identifier, Identifier>()
            .putAll(LAYERS_TO_LOADERS)
            .put(ItematicTexturedRenderLayers.ARMOR_MATERIALS_ATLAS_TEXTURE, ItematicTexturedRenderLayers.ARMOR_MATERIALS_ID)
            .build();
    }

    @Override
    public void setItemRegistry(Registry<Item> itemRegistry) {
        this.colorMap.setItemRegistry(itemRegistry);
    }
}
