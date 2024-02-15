package net.errorcraft.itematic.item.model.override;

import net.errorcraft.itematic.item.model.override.overrides.*;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class ModelOverrides {
    public static final ModelOverride LEFT_HANDED = register(ModelOverrideKeys.LEFT_HANDED, new LeftHandedModelOverride());
    public static final ModelOverride COOLDOWN = register(ModelOverrideKeys.COOLDOWN, new CooldownModelOverride());
    public static final ModelOverride CUSTOM_MODEL_DATA = register(ModelOverrideKeys.CUSTOM_MODEL_DATA, new CustomModelDataModelOverride());
    public static final ModelOverride TRIM_TYPE = register(ModelOverrideKeys.TRIM_TYPE, new TrimTypeModelOverride());
    public static final ModelOverride DAMAGED = register(ModelOverrideKeys.DAMAGED, new DamagedModelOverride());
    public static final ModelOverride DAMAGE = register(ModelOverrideKeys.DAMAGE, new DamageModelOverride());
    public static final ModelOverride PULL = register(ModelOverrideKeys.PULL, new PullModelOverride());
    public static final ModelOverride PULLING = register(ModelOverrideKeys.PULLING, new PullingModelOverride());
    public static final ModelOverride CHARGED = register(ModelOverrideKeys.CHARGED, new ChargedModelOverride());
    public static final ModelOverride FIREWORK = register(ModelOverrideKeys.FIREWORK, new FireworkModelOverride());
    public static final ModelOverride BROKEN = register(ModelOverrideKeys.BROKEN, new BrokenModelOverride());
    public static final ModelOverride CAST = register(ModelOverrideKeys.CAST, new CastModelOverride());
    public static final ModelOverride BLOCKING = register(ModelOverrideKeys.BLOCKING, new BlockingModelOverride());
    public static final ModelOverride TIME = register(ModelOverrideKeys.TIME, new TimeModelOverride());
    public static final ModelOverride ANGLE = register(ModelOverrideKeys.ANGLE, new AngleModelOverride());
    public static final ModelOverride TOOTING = register(ModelOverrideKeys.TOOTING, new TootingModelOverride());
    public static final ModelOverride THROWING = register(ModelOverrideKeys.THROWING, new ThrowingModelOverride());
    public static final ModelOverride BRUSHING = register(ModelOverrideKeys.BRUSHING, new BrushingModelOverride());

    private ModelOverrides() {}

    public static void init() {}

    private static ModelOverride register(RegistryKey<ModelOverride> id, ModelOverride override) {
        return Registry.register(ItematicRegistries.MODEL_OVERRIDE, id, override);
    }
}
