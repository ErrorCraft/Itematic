package net.errorcraft.itematic.item.dispense.behavior;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class DispenseBehaviorKeys {
    public static final RegistryKey<DispenserBehavior> ITEM = of("item");
    public static final RegistryKey<DispenserBehavior> ENTITY = of("entity");
    public static final RegistryKey<DispenserBehavior> PROJECTILE = of("projectile");
    public static final RegistryKey<DispenserBehavior> EQUIPMENT = of("equipment");
    public static final RegistryKey<DispenserBehavior> FIREWORK = of("firework");
    public static final RegistryKey<DispenserBehavior> BUCKET = of("bucket");
    public static final RegistryKey<DispenserBehavior> POTION = of("potion");
    public static final RegistryKey<DispenserBehavior> BOTTLE = of("bottle");
    public static final RegistryKey<DispenserBehavior> FERTILIZE = of("fertilize");
    public static final RegistryKey<DispenserBehavior> SADDLE = of("saddle");
    public static final RegistryKey<DispenserBehavior> USE_ON_BLOCK = of("use_on_block");
    public static final RegistryKey<DispenserBehavior> WAX_BLOCK = of("wax_block");
    public static final RegistryKey<DispenserBehavior> PLACE_BLOCK_FROM_ITEM = of("place_block_from_item");
    public static final RegistryKey<DispenserBehavior> BRUSH = of("brush");
    public static final RegistryKey<DispenserBehavior> CHEST_EQUIPMENT = of("chest_equipment");
    public static final RegistryKey<DispenserBehavior> TNT = of("tnt");
    public static final RegistryKey<DispenserBehavior> CARVED_PUMPKIN = of("carved_pumpkin");
    public static final RegistryKey<DispenserBehavior> CHARGE_RESPAWN_ANCHOR = of("charge_respawn_anchor");
    public static final RegistryKey<DispenserBehavior> SHEAR = of("shear");

    private DispenseBehaviorKeys() {}

    private static RegistryKey<DispenserBehavior> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.DISPENSE_BEHAVIOR, new Identifier(id));
    }
}
