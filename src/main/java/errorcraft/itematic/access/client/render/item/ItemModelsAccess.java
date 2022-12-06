package errorcraft.itematic.access.client.render.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;

@Environment(EnvType.CLIENT)
public interface ItemModelsAccess {
    default void reloadModelIds(Registry<Item> registry) {}
}
