package errorcraft.itematic.mixin.registry;

import com.google.common.collect.ImmutableList;
import errorcraft.itematic.item.ItemUtil;
import errorcraft.itematic.item.armor.ArmorMaterial;
import errorcraft.itematic.item.armor.ArmorMaterials;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(RegistryLoader.class)
public class RegistryLoaderExtender {
    @Shadow
    @Final
    @Mutable
    public static List<RegistryLoader.Entry<?>> DYNAMIC_REGISTRIES;

    static {
        DYNAMIC_REGISTRIES = new ImmutableList.Builder<RegistryLoader.Entry<?>>()
            .addAll(DYNAMIC_REGISTRIES)
            .add(new RegistryLoader.Entry<>(RegistryKeys.ITEM, ItemUtil.CODEC))
            .add(new RegistryLoader.Entry<>(ArmorMaterials.ARMOR_MATERIAL_KEY, ArmorMaterial.CODEC))
            .build();
    }
}
