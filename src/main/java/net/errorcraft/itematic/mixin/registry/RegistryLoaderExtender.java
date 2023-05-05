package net.errorcraft.itematic.mixin.registry;

import com.google.common.collect.ImmutableList;
import net.errorcraft.itematic.item.ItemUtil;
import net.errorcraft.itematic.item.armor.ArmorMaterial;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
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
            .add(new RegistryLoader.Entry<>(ItematicRegistryKeys.ARMOR_MATERIAL, ArmorMaterial.CODEC))
            .build();
    }
}
