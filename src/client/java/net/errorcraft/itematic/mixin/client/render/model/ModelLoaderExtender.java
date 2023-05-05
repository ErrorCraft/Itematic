package net.errorcraft.itematic.mixin.client.render.model;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.item.Item;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@Mixin(ModelLoader.class)
public class ModelLoaderExtender {
    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getIds()Ljava/util/Set;"
        )
    )
    private Set<Identifier> constructorGetIdsUseDynamicRegistry(DefaultedRegistry<Item> instance) {
        World world = MinecraftClient.getInstance().world;
        if (world == null) {
            return Set.of();
        }
        return world.getItemAccess().getIds();
    }
}
