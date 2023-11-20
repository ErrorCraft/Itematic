package net.errorcraft.itematic.item.model.override;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ModelOverrideKeys {
    public static final RegistryKey<ModelOverride> LEFT_HANDED = of("lefthanded");
    public static final RegistryKey<ModelOverride> COOLDOWN = of("cooldown");
    public static final RegistryKey<ModelOverride> CUSTOM_MODEL_DATA = of("custom_model_data");
    public static final RegistryKey<ModelOverride> TRIM_TYPE = of("trim_type");
    public static final RegistryKey<ModelOverride> DAMAGED = of("damaged");
    public static final RegistryKey<ModelOverride> DAMAGE = of("damage");
    public static final RegistryKey<ModelOverride> PULL = of("pull");
    public static final RegistryKey<ModelOverride> PULLING = of("pulling");
    public static final RegistryKey<ModelOverride> CHARGED = of("charged");
    public static final RegistryKey<ModelOverride> FIREWORK = of("firework");
    public static final RegistryKey<ModelOverride> BROKEN = of("broken");
    public static final RegistryKey<ModelOverride> BLOCKING = of("blocking");
    public static final RegistryKey<ModelOverride> TIME = of("time");
    public static final RegistryKey<ModelOverride> ANGLE = of("angle");

    private static RegistryKey<ModelOverride> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.MODEL_OVERRIDE, new Identifier(id));
    }
}
