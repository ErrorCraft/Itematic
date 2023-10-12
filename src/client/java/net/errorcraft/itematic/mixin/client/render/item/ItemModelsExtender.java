package net.errorcraft.itematic.mixin.client.render.item;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.errorcraft.itematic.access.client.render.item.ItemModelsAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.render.item.ItemModels;
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

@Mixin(ItemModels.class)
public abstract class ItemModelsExtender implements ItemModelsAccess {
    @Shadow
    @Final
    public Int2ObjectMap<ModelIdentifier> modelIds;

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
        this.modelIds.clear();
        Identifier airKey = ItemKeys.AIR.getValue();
        for (Identifier id : registry.getIds()) {
            if (id.equals(airKey)) {
                continue;
            }
            this.putModel(registry.get(id), new ModelIdentifier(id, "inventory"));
        }
    }
}
