package net.errorcraft.itematic.item.dispense.behavior;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class DispenseBehaviorKeys {
    public static final RegistryKey<DispenserBehavior> ITEM = of("item");
    public static final RegistryKey<DispenserBehavior> ENTITY = of("entity");
    public static final RegistryKey<DispenserBehavior> PROJECTILE = of("projectile");
    public static final RegistryKey<DispenserBehavior> ARMOR = of("armor");
    public static final RegistryKey<DispenserBehavior> FIREWORK = of("firework");
    public static final RegistryKey<DispenserBehavior> BUCKET = of("bucket");

    private DispenseBehaviorKeys() {}

    private static RegistryKey<DispenserBehavior> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.DISPENSE_BEHAVIOR, new Identifier(id));
    }
}
