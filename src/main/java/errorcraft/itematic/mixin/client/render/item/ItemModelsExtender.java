package errorcraft.itematic.mixin.client.render.item;

import errorcraft.itematic.access.client.render.item.ItemModelsAccess;
import errorcraft.itematic.registry.RegistryUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ItemModels.class)
public abstract class ItemModelsExtender implements ItemModelsAccess {
    @Shadow
    @Final
    private Int2ObjectMap<BakedModel> models;

    @Inject(
        method = "getModelId",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void useDynamicRegistry(Item item, CallbackInfoReturnable<Integer> info) {
        Registry<Item> registry = RegistryUtil.getRegistry(MinecraftClient.getInstance().world, RegistryKeys.ITEM);
        info.setReturnValue(registry == null ? -1 : registry.getRawId(item));
    }

    @Override
    public void clearModels() {
        this.models.clear();
    }
}
