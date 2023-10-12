package net.errorcraft.itematic.mixin.client.render.model;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.item.Item;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderExtender {
    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getIds()Ljava/util/Set;"
        )
    )
    private Set<Identifier> getIdsUseDynamicRegistry(DefaultedRegistry<Item> instance, BlockColors blockColors) {
        Registry<Item> items = blockColors.itemRegistry();
        if (items == null) {
            return Set.of();
        }
        return items.getIds();
    }
}
