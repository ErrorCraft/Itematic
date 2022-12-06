package errorcraft.itematic.mixin.client.render.item;

import errorcraft.itematic.access.client.render.item.ItemModelsAccess;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(ItemModels.class)
public abstract class ItemModelsExtender implements ItemModelsAccess {
    @Shadow
    @Final
    private Int2ObjectMap<BakedModel> models;

    @Shadow
    public abstract void putModel(Item item, ModelIdentifier modelId);

    @Nullable
    private Registry<Item> registry;

    @Redirect(
        method = { "getModel(Lnet/minecraft/item/Item;)Lnet/minecraft/client/render/model/BakedModel;", "putModel" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/item/ItemModels;getModelId(Lnet/minecraft/item/Item;)I"
        )
    )
    private int getModelIdUseDynamicRegistry(Item item) {
        return this.registry == null ? -1 : this.registry.getRawId(item);
    }

    @Override
    public void reloadModelIds(Registry<Item> registry) {
        this.registry = registry;
        this.models.clear();
        for (Identifier id : registry.getIds()) {
            this.putModel(registry.get(id), new ModelIdentifier(id, "inventory"));
        }
    }
}
