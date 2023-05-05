package net.errorcraft.itematic.mixin.client.render.model;

import com.google.common.collect.ImmutableMap;
import net.errorcraft.itematic.client.render.TexturedRenderLayersUtil;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(BakedModelManager.class)
public class BakedModelManagerExtender {
    @Shadow
    @Final
    @Mutable
    private static Map<Identifier, Identifier> LAYERS_TO_LOADERS;

    static {
        LAYERS_TO_LOADERS = new ImmutableMap.Builder<Identifier, Identifier>()
            .putAll(LAYERS_TO_LOADERS)
            .put(TexturedRenderLayersUtil.ARMOR_MATERIALS_ATLAS_TEXTURE, TexturedRenderLayersUtil.ARMOR_MATERIALS_ID)
            .build();
    }
}
