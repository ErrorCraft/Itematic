package net.errorcraft.itematic.item.component;

import net.errorcraft.itematic.item.component.components.*;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.registry.Registry;

public class ItemComponentTypes {
    public static final ItemComponentType<UseDurationItemComponent> USE_DURATION = register("use_duration", new ItemComponentType<>(UseDurationItemComponent.CODEC));
    public static final ItemComponentType<FoodItemComponent> FOOD = register("food", new ItemComponentType<>(FoodItemComponent.CODEC));
    public static final ItemComponentType<BlockItemComponent> BLOCK = register("block", new ItemComponentType<>(BlockItemComponent.CODEC));
    public static final ItemComponentType<DamageableItemComponent> DAMAGEABLE = register("damageable", new ItemComponentType<>(DamageableItemComponent.CODEC));
    public static final ItemComponentType<ToolItemComponent> TOOL = register("tool", new ItemComponentType<>(ToolItemComponent.CODEC));
    public static final ItemComponentType<EntityItemComponent> ENTITY = register("entity", new ItemComponentType<>(EntityItemComponent.CODEC));
    public static final ItemComponentType<WeaponItemComponent> WEAPON = register("weapon", new ItemComponentType<>(WeaponItemComponent.CODEC));
    public static final ItemComponentType<CompostableItemComponent> COMPOSTABLE = register("compostable", new ItemComponentType<>(CompostableItemComponent.CODEC));
    public static final ItemComponentType<EquipmentItemComponent> EQUIPMENT = register("equipment", new ItemComponentType<>(EquipmentItemComponent.CODEC));
    public static final ItemComponentType<ArmorItemComponent> ARMOR = register("armor", new ItemComponentType<>(ArmorItemComponent.CODEC));
    public static final ItemComponentType<FuelItemComponent> FUEL = register("fuel", new ItemComponentType<>(FuelItemComponent.CODEC));
    public static final ItemComponentType<EnchantableItemComponent> ENCHANTABLE = register("enchantable", new ItemComponentType<>(EnchantableItemComponent.CODEC));
    public static final ItemComponentType<EnchantmentHolderItemComponent> ENCHANTMENT_HOLDER = register("enchantment_holder", new ItemComponentType<>(EnchantmentHolderItemComponent.CODEC));
    public static final ItemComponentType<ForgeableItemComponent> FORGEABLE = register("forgeable", new ItemComponentType<>(ForgeableItemComponent.CODEC));
    public static final ItemComponentType<RepairableItemComponent> REPAIRABLE = register("repairable", new ItemComponentType<>(RepairableItemComponent.CODEC));
    public static final ItemComponentType<FoilItemComponent> FOIL = register("foil", new ItemComponentType<>(FoilItemComponent.CODEC));
    public static final ItemComponentType<ThrowableItemComponent> THROWABLE = register("throwable", new ItemComponentType<>(ThrowableItemComponent.CODEC));
    public static final ItemComponentType<ProjectileItemComponent> PROJECTILE = register("projectile", new ItemComponentType<>(ProjectileItemComponent.CODEC));
    public static final ItemComponentType<CooldownItemComponent> COOLDOWN = register("cooldown", new ItemComponentType<>(CooldownItemComponent.CODEC));
    public static final ItemComponentType<DyeItemComponent> DYE = register("dye", new ItemComponentType<>(DyeItemComponent.CODEC));
    public static final ItemComponentType<DyeableItemComponent> DYEABLE = register("dyeable", new ItemComponentType<>(DyeableItemComponent.CODEC));
    public static final ItemComponentType<TintedItemComponent> TINTED = register("tinted", new ItemComponentType<>(TintedItemComponent.CODEC));
    public static final ItemComponentType<CanPlaceOnFluidsItemComponent> CAN_PLACE_ON_FLUIDS = register("can_place_on_fluids", new ItemComponentType<>(CanPlaceOnFluidsItemComponent.CODEC));
    public static final ItemComponentType<SpawnEggItemComponent> SPAWN_EGG = register("spawn_egg", new ItemComponentType<>(SpawnEggItemComponent.CODEC));
    public static final ItemComponentType<DispensableItemComponent> DISPENSABLE = register("dispensable", new ItemComponentType<>(DispensableItemComponent.CODEC));
    public static final ItemComponentType<ShooterItemComponent> SHOOTER = register("shooter", new ItemComponentType<>(ShooterItemComponent.CODEC));
    public static final ItemComponentType<UseAnimationItemComponent> USE_ANIMATION = register("use_animation", new ItemComponentType<>(UseAnimationItemComponent.CODEC));
    public static final ItemComponentType<RecordItemComponent> RECORD = register("record", new ItemComponentType<>(RecordItemComponent.CODEC));
    public static final ItemComponentType<FireworkShapeModifierItemComponent> FIREWORK_SHAPE_MODIFIER = register("firework_shape_modifier", new ItemComponentType<>(FireworkShapeModifierItemComponent.CODEC));
    public static final ItemComponentType<FireworkExplosionHolderItemComponent> FIREWORK_EXPLOSION_HOLDER = register("firework_explosion_holder", new ItemComponentType<>(FireworkExplosionHolderItemComponent.CODEC));
    public static final ItemComponentType<FireworkItemComponent> FIREWORK = register("firework", new ItemComponentType<>(FireworkItemComponent.CODEC));
    public static final ItemComponentType<BucketItemComponent> BUCKET = register("bucket", new ItemComponentType<>(BucketItemComponent.CODEC));
    public static final ItemComponentType<ConsumableItemComponent> CONSUMABLE = register("consumable", new ItemComponentType<>(ConsumableItemComponent.CODEC));
    public static final ItemComponentType<PotionItemComponent> POTION = register("potion", new ItemComponentType<>(PotionItemComponent.CODEC));
    public static final ItemComponentType<PotionHolderItemComponent> POTION_HOLDER = register("potion_holder", new ItemComponentType<>(PotionHolderItemComponent.CODEC));

    private ItemComponentTypes() {}

    public static void init() {}

    private static <T extends ItemComponent> ItemComponentType<T> register(String id, ItemComponentType<T> type) {
        return Registry.register(ItematicRegistries.ITEM_COMPONENT_TYPE, id, type);
    }
}
