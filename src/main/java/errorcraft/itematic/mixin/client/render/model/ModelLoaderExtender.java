package errorcraft.itematic.mixin.client.render.model;

import errorcraft.itematic.registry.RegistryUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@Environment(EnvType.CLIENT)
@Mixin(ModelLoader.class)
public class ModelLoaderExtender {
    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/registry/DefaultedRegistry;getIds()Ljava/util/Set;"
        )
    )
    private Set<Identifier> useDynamicRegistry(DefaultedRegistry<Item> instance) {
        Registry<Item> registry = RegistryUtil.getRegistry(MinecraftClient.getInstance().world, Registry.ITEM_KEY);
        return registry == null ? Set.of() : registry.getIds();
    }
}
