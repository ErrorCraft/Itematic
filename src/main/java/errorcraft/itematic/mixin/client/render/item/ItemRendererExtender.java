package errorcraft.itematic.mixin.client.render.item;

import errorcraft.itematic.access.DynamicRegistryAccess;
import errorcraft.itematic.access.client.render.item.ItemModelsAccess;
import errorcraft.itematic.registry.RegistryUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public class ItemRendererExtender implements DynamicRegistryAccess {
    @Shadow
    @Final
    private ItemModels models;

    @Redirect(
        method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/tag/TagKey;)Z"
        )
    )
    private boolean useDynamicTagCheck(ItemStack instance, TagKey<Item> tag) {
        return RegistryUtil.tagContains(MinecraftClient.getInstance().world, tag, instance.getItem());
    }

    public void loadDynamicEntries(DynamicRegistryManager registryManager) {
        Registry<Item> registry = registryManager.get(RegistryKeys.ITEM);
        if (registry == null) {
            return;
        }
        ((ItemModelsAccess) this.models).clearModels();
        for (Identifier id : registry.getIds()) {
            Item item = registry.get(id);
            this.models.putModel(item, new ModelIdentifier(id, "inventory"));
        }
        this.models.reloadModels();
    }
}
