package errorcraft.itematic.mixin.util.registry;

import net.minecraft.item.Item;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Registry.class)
public interface RegistryAccessor {
    @Accessor("ITEM")
    @Mutable
    static void setItemRegistry(DefaultedRegistry<Item> registry) {}
}
