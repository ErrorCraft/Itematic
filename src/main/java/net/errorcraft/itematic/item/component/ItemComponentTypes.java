package net.errorcraft.itematic.item.component;

import net.errorcraft.itematic.item.component.components.*;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.registry.Registry;

public class ItemComponentTypes {
    public static final ItemComponentType<UseableItemComponent> USEABLE = register("useable", new ItemComponentType<>(UseableItemComponent.CODEC));
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
    public static final ItemComponentType<RepairableItemComponent> REPAIRABLE = register("repairable", new ItemComponentType<>(RepairableItemComponent.CODEC));
    public static final ItemComponentType<ThrowableItemComponent> THROWABLE = register("throwable", new ItemComponentType<>(ThrowableItemComponent.CODEC));
    public static final ItemComponentType<ProjectileItemComponent> PROJECTILE = register("projectile", new ItemComponentType<>(ProjectileItemComponent.CODEC));
    public static final ItemComponentType<CooldownItemComponent> COOLDOWN = register("cooldown", new ItemComponentType<>(CooldownItemComponent.CODEC));
    public static final ItemComponentType<DyeItemComponent> DYE = register("dye", new ItemComponentType<>(DyeItemComponent.CODEC));
    public static final ItemComponentType<DyeableItemComponent> DYEABLE = register("dyeable", new ItemComponentType<>(DyeableItemComponent.CODEC));
    public static final ItemComponentType<TintedItemComponent> TINTED = register("tinted", new ItemComponentType<>(TintedItemComponent.CODEC));
    public static final ItemComponentType<SpawnEggItemComponent> SPAWN_EGG = register("spawn_egg", new ItemComponentType<>(SpawnEggItemComponent.CODEC));
    public static final ItemComponentType<DispensableItemComponent> DISPENSABLE = register("dispensable", new ItemComponentType<>(DispensableItemComponent.CODEC));
    public static final ItemComponentType<ShooterItemComponent> SHOOTER = register("shooter", new ItemComponentType<>(ShooterItemComponent.CODEC));
    public static final ItemComponentType<PlayableSongItemComponent> PLAYABLE_SONG = register("playable_song", new ItemComponentType<>(PlayableSongItemComponent.CODEC));
    public static final ItemComponentType<FireworkShapeModifierItemComponent> FIREWORK_SHAPE_MODIFIER = register("firework_shape_modifier", new ItemComponentType<>(FireworkShapeModifierItemComponent.CODEC));
    public static final ItemComponentType<FireworkExplosionHolderItemComponent> FIREWORK_EXPLOSION_HOLDER = register("firework_explosion_holder", new ItemComponentType<>(FireworkExplosionHolderItemComponent.CODEC));
    public static final ItemComponentType<FireworkItemComponent> FIREWORK = register("firework", new ItemComponentType<>(FireworkItemComponent.CODEC));
    public static final ItemComponentType<BucketItemComponent> BUCKET = register("bucket", new ItemComponentType<>(BucketItemComponent.CODEC));
    public static final ItemComponentType<ConsumableItemComponent> CONSUMABLE = register("consumable", new ItemComponentType<>(ConsumableItemComponent.CODEC));
    public static final ItemComponentType<PotionItemComponent> POTION = register("potion", new ItemComponentType<>(PotionItemComponent.CODEC));
    public static final ItemComponentType<PotionHolderItemComponent> POTION_HOLDER = register("potion_holder", new ItemComponentType<>(PotionHolderItemComponent.CODEC));
    public static final ItemComponentType<SaddleItemComponent> SADDLE = register("saddle", new ItemComponentType<>(SaddleItemComponent.CODEC));
    public static final ItemComponentType<SteeringItemComponent> STEERING = register("steering", new ItemComponentType<>(SteeringItemComponent.CODEC));
    public static final ItemComponentType<PointableItemComponent> POINTABLE = register("pointable", new ItemComponentType<>(PointableItemComponent.CODEC));
    public static final ItemComponentType<PreventUseWhenUsedOnTargetItemComponent> PREVENT_USE_WHEN_USED_ON_TARGET = register("prevent_use_when_used_on_target", new ItemComponentType<>(PreventUseWhenUsedOnTargetItemComponent.CODEC));
    public static final ItemComponentType<SmithingTemplateItemComponent> SMITHING_TEMPLATE = register("smithing_template", new ItemComponentType<>(SmithingTemplateItemComponent.CODEC));
    public static final ItemComponentType<BannerPatternItemComponent> BANNER_PATTERN = register("banner_pattern", new ItemComponentType<>(BannerPatternItemComponent.CODEC));
    public static final ItemComponentType<BannerPatternHolderItemComponent> BANNER_PATTERN_HOLDER = register("banner_pattern_holder", new ItemComponentType<>(BannerPatternHolderItemComponent.CODEC));
    public static final ItemComponentType<WritableItemComponent> WRITABLE = register("writable", new ItemComponentType<>(WritableItemComponent.CODEC));
    public static final ItemComponentType<TextHolderItemComponent> TEXT_HOLDER = register("text_holder", new ItemComponentType<>(TextHolderItemComponent.CODEC));
    public static final ItemComponentType<PlayableItemComponent> PLAYABLE = register("playable", new ItemComponentType<>(PlayableItemComponent.CODEC));
    public static final ItemComponentType<DecoratedPotPatternItemComponent> DECORATED_POT_PATTERN = register("decorated_pot_pattern", new ItemComponentType<>(DecoratedPotPatternItemComponent.CODEC));
    public static final ItemComponentType<MappableItemComponent> MAPPABLE = register("mappable", new ItemComponentType<>(MappableItemComponent.CODEC));
    public static final ItemComponentType<MapHolderItemComponent> MAP_HOLDER = register("map_holder", new ItemComponentType<>(MapHolderItemComponent.CODEC));
    public static final ItemComponentType<BrushItemComponent> BRUSH = register("brush", new ItemComponentType<>(BrushItemComponent.CODEC));
    public static final ItemComponentType<ZoomItemComponent> ZOOM = register("zoom", new ItemComponentType<>(ZoomItemComponent.CODEC));
    public static final ItemComponentType<ItemHolderItemComponent> ITEM_HOLDER = register("item_holder", new ItemComponentType<>(ItemHolderItemComponent.CODEC));
    public static final ItemComponentType<ImmuneToDamageItemComponent> IMMUNE_TO_DAMAGE = register("immune_to_damage", new ItemComponentType<>(ImmuneToDamageItemComponent.CODEC));
    public static final ItemComponentType<UnlockRecipesItemComponent> UNLOCK_RECIPES = register("unlock_recipes", new ItemComponentType<>(UnlockRecipesItemComponent.CODEC));
    public static final ItemComponentType<DebugStickItemComponent> DEBUG_STICK = register("debug_stick", new ItemComponentType<>(DebugStickItemComponent.CODEC));
    public static final ItemComponentType<SuspiciousEffectIngredientItemComponent> SUSPICIOUS_EFFECT_INGREDIENT = register("suspicious_effect_ingredient", new ItemComponentType<>(SuspiciousEffectIngredientItemComponent.CODEC));
    public static final ItemComponentType<CastableItemComponent> CASTABLE = register("castable", new ItemComponentType<>(CastableItemComponent.CODEC));
    public static final ItemComponentType<StackableItemComponent> STACKABLE = register("stackable", new ItemComponentType<>(StackableItemComponent.CODEC));
    public static final ItemComponentType<AttributeModifiersItemComponent> ATTRIBUTE_MODIFIERS = register("attribute_modifiers", new ItemComponentType<>(AttributeModifiersItemComponent.CODEC));
    public static final ItemComponentType<OminousEffectProviderItemComponent> OMINOUS_EFFECT_PROVIDER = register("ominous_effect_provider", new ItemComponentType<>(OminousEffectProviderItemComponent.CODEC));

    private ItemComponentTypes() {}

    public static void init() {}

    private static <T extends ItemComponent<T>> ItemComponentType<T> register(String id, ItemComponentType<T> type) {
        return Registry.register(ItematicRegistries.ITEM_COMPONENT_TYPE, id, type);
    }
}
