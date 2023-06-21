package net.errorcraft.itematic.item.dispense.behavior;

import net.errorcraft.itematic.item.dispense.behavior.behaviors.BucketItemComponentDispenserBehavior;
import net.errorcraft.itematic.item.dispense.behavior.behaviors.EntityItemComponentDispenserBehavior;
import net.errorcraft.itematic.item.dispense.behavior.behaviors.FireworkRocketDispenserBehavior;
import net.errorcraft.itematic.item.dispense.behavior.behaviors.ProjectileItemComponentDispenserBehavior;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ArmorItem;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class DispenseBehaviors {
    public static final DispenserBehavior ITEM = register(DispenseBehaviorKeys.ITEM, new ItemDispenserBehavior());
    public static final DispenserBehavior ENTITY = register(DispenseBehaviorKeys.ENTITY, new EntityItemComponentDispenserBehavior());
    public static final DispenserBehavior PROJECTILE = register(DispenseBehaviorKeys.PROJECTILE, new ProjectileItemComponentDispenserBehavior());
    public static final DispenserBehavior ARMOR = register(DispenseBehaviorKeys.ARMOR, ArmorItem.DISPENSER_BEHAVIOR);
    public static final DispenserBehavior FIREWORK = register(DispenseBehaviorKeys.FIREWORK, new FireworkRocketDispenserBehavior());
    public static final DispenserBehavior BUCKET = register(DispenseBehaviorKeys.BUCKET, new BucketItemComponentDispenserBehavior());

    private DispenseBehaviors() {}

    public static void init() {}

    private static DispenserBehavior register(RegistryKey<DispenserBehavior> id, DispenserBehavior behavior) {
        return Registry.register(ItematicRegistries.DISPENSE_BEHAVIOR, id, behavior);
    }
}
