package net.errorcraft.itematic.item.dispense.behavior;

import net.errorcraft.itematic.block.dispenser.DispenserBehaviorUtil;
import net.errorcraft.itematic.item.dispense.behavior.behaviors.*;
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
    public static final DispenserBehavior POTION = register(DispenseBehaviorKeys.POTION, DispenserBehaviorUtil.POTION_DISPENSER_BEHAVIOR);
    public static final DispenserBehavior BOTTLE = register(DispenseBehaviorKeys.BOTTLE, DispenserBehaviorUtil.BOTTLE_DISPENSER_BEHAVIOR);
    public static final DispenserBehavior FERTILIZE = register(DispenseBehaviorKeys.FERTILIZE, new FertilizeDispenserBehavior());
    public static final DispenserBehavior SADDLE = register(DispenseBehaviorKeys.SADDLE, new SaddleItemComponentDispenserBehavior());
    public static final DispenserBehavior HORSE_ARMOR = register(DispenseBehaviorKeys.HORSE_ARMOR, DispenserBehaviorUtil.HORSE_ARMOR_DISPENSER_BEHAVIOR);
    public static final DispenserBehavior USE_ON_BLOCK = register(DispenseBehaviorKeys.USE_ON_BLOCK, new UseOnBlockDispenserBehavior());
    public static final DispenserBehavior WAX_BLOCK = register(DispenseBehaviorKeys.WAX_BLOCK, new WaxBlockDispenserBehavior());
    public static final DispenserBehavior PLACE_BLOCK_FROM_ITEM = register(DispenseBehaviorKeys.PLACE_BLOCK_FROM_ITEM, new PlaceBlockFromItemDispenserBehavior());

    private DispenseBehaviors() {}

    public static void init() {}

    private static DispenserBehavior register(RegistryKey<DispenserBehavior> id, DispenserBehavior behavior) {
        return Registry.register(ItematicRegistries.DISPENSE_BEHAVIOR, id, behavior);
    }
}
