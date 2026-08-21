package net.errorcraft.itematic.world.item.behavior;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.world.item.behavior.behaviors.AttackBlockingItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.BannerPatternHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.BannerPatternItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.BlockItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.BrushItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.BucketItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.CastableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.CompostableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ConsumableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.CooldownItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.DamageableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.DebugStickItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.DecoratedPotPatternItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.DispensableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.DyeItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.DyeableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.EnchantableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.EnchantmentHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.EntityItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.EquipmentItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.FireworkExplosionHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.FireworkItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.FireworkShapeModifierItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.FoodItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.FuelItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.GliderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ImmuneToDamageItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ItemHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.MapHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.MappableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.OminousEffectProviderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.PlayableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.PlayableSongItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.PotionHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.PreventUseWhenUsedOnTargetItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ProjectileItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.RepairableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ShooterItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.SmithingTemplateProviderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.SpawnEggItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.StackableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.SteeringItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.SuspiciousEffectIngredientItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.TextHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ThrowableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ToolItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.TrimMaterialProviderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.UnlockRecipesItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.UseableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.WeaponItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.WritableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ZoomItemBehavior;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ItemBehaviorType<T extends ItemBehavior<T>>(Codec<T> codec) {
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemBehaviorType<?>> STREAM_CODEC = ByteBufCodecs.registry(ItematicRegistries.ITEM_BEHAVIOR_TYPE);
    public static final ItemBehaviorType<UseableItemBehavior> USEABLE = register(
        "useable",
        UseableItemBehavior.CODEC
    );
    public static final ItemBehaviorType<FoodItemBehavior> FOOD = register(
        "food",
        FoodItemBehavior.CODEC
    );
    public static final ItemBehaviorType<BlockItemBehavior> BLOCK = register(
        "block",
        BlockItemBehavior.CODEC
    );
    public static final ItemBehaviorType<DamageableItemBehavior> DAMAGEABLE = register(
        "damageable",
        DamageableItemBehavior.CODEC
    );
    public static final ItemBehaviorType<ToolItemBehavior> TOOL = register(
        "tool",
        ToolItemBehavior.CODEC
    );
    public static final ItemBehaviorType<EntityItemBehavior> ENTITY = register(
        "entity",
        EntityItemBehavior.CODEC
    );
    public static final ItemBehaviorType<WeaponItemBehavior> WEAPON = register(
        "weapon",
        WeaponItemBehavior.CODEC
    );
    public static final ItemBehaviorType<CompostableItemBehavior> COMPOSTABLE = register(
        "compostable",
        CompostableItemBehavior.CODEC
    );
    public static final ItemBehaviorType<EquipmentItemBehavior> EQUIPMENT = register(
        "equipment",
        EquipmentItemBehavior.CODEC
    );
    public static final ItemBehaviorType<FuelItemBehavior> FUEL = register(
        "fuel",
        FuelItemBehavior.CODEC
    );
    public static final ItemBehaviorType<EnchantableItemBehavior> ENCHANTABLE = register(
        "enchantable",
        EnchantableItemBehavior.CODEC
    );
    public static final ItemBehaviorType<EnchantmentHolderItemBehavior> ENCHANTMENT_HOLDER = register(
        "enchantment_holder",
        EnchantmentHolderItemBehavior.CODEC
    );
    public static final ItemBehaviorType<RepairableItemBehavior> REPAIRABLE = register(
        "repairable",
        RepairableItemBehavior.CODEC
    );
    public static final ItemBehaviorType<ThrowableItemBehavior> THROWABLE = register(
        "throwable",
        ThrowableItemBehavior.CODEC
    );
    public static final ItemBehaviorType<ProjectileItemBehavior> PROJECTILE = register(
        "projectile",
        ProjectileItemBehavior.CODEC
    );
    public static final ItemBehaviorType<CooldownItemBehavior> COOLDOWN = register(
        "cooldown",
        CooldownItemBehavior.CODEC
    );
    public static final ItemBehaviorType<DyeItemBehavior> DYE = register(
        "dye",
        DyeItemBehavior.CODEC
    );
    public static final ItemBehaviorType<DyeableItemBehavior> DYEABLE = register(
        "dyeable",
        DyeableItemBehavior.CODEC
    );
    public static final ItemBehaviorType<SpawnEggItemBehavior> SPAWN_EGG = register(
        "spawn_egg",
        SpawnEggItemBehavior.CODEC
    );
    public static final ItemBehaviorType<DispensableItemBehavior> DISPENSABLE = register(
        "dispensable",
        DispensableItemBehavior.CODEC
    );
    public static final ItemBehaviorType<ShooterItemBehavior> SHOOTER = register(
        "shooter",
        ShooterItemBehavior.CODEC
    );
    public static final ItemBehaviorType<PlayableSongItemBehavior> PLAYABLE_SONG = register(
        "playable_song",
        PlayableSongItemBehavior.CODEC
    );
    public static final ItemBehaviorType<FireworkShapeModifierItemBehavior> FIREWORK_SHAPE_MODIFIER = register(
        "firework_shape_modifier",
        FireworkShapeModifierItemBehavior.CODEC
    );
    public static final ItemBehaviorType<FireworkExplosionHolderItemBehavior> FIREWORK_EXPLOSION_HOLDER = register(
        "firework_explosion_holder",
        FireworkExplosionHolderItemBehavior.CODEC
    );
    public static final ItemBehaviorType<FireworkItemBehavior> FIREWORK = register(
        "firework",
        FireworkItemBehavior.CODEC
    );
    public static final ItemBehaviorType<BucketItemBehavior> BUCKET = register(
        "bucket",
        BucketItemBehavior.CODEC
    );
    public static final ItemBehaviorType<ConsumableItemBehavior> CONSUMABLE = register(
        "consumable",
        ConsumableItemBehavior.CODEC
    );
    public static final ItemBehaviorType<PotionHolderItemBehavior> POTION_HOLDER = register(
        "potion_holder",
        PotionHolderItemBehavior.CODEC
    );
    public static final ItemBehaviorType<SteeringItemBehavior> STEERING = register(
        "steering",
        SteeringItemBehavior.CODEC
    );
    public static final ItemBehaviorType<PreventUseWhenUsedOnTargetItemBehavior> PREVENT_USE_WHEN_USED_ON_TARGET = register(
        "prevent_use_when_used_on_target",
        PreventUseWhenUsedOnTargetItemBehavior.CODEC
    );
    public static final ItemBehaviorType<SmithingTemplateProviderItemBehavior> SMITHING_TEMPLATE_PROVIDER = register(
        "smithing_template_provider",
        SmithingTemplateProviderItemBehavior.CODEC
    );
    public static final ItemBehaviorType<BannerPatternItemBehavior> BANNER_PATTERN = register(
        "banner_pattern",
        BannerPatternItemBehavior.CODEC
    );
    public static final ItemBehaviorType<BannerPatternHolderItemBehavior> BANNER_PATTERN_HOLDER = register(
        "banner_pattern_holder",
        BannerPatternHolderItemBehavior.CODEC
    );
    public static final ItemBehaviorType<WritableItemBehavior> WRITABLE = register(
        "writable",
        WritableItemBehavior.CODEC
    );
    public static final ItemBehaviorType<TextHolderItemBehavior> TEXT_HOLDER = register(
        "text_holder",
        TextHolderItemBehavior.CODEC
    );
    public static final ItemBehaviorType<PlayableItemBehavior> PLAYABLE = register(
        "playable",
        PlayableItemBehavior.CODEC
    );
    public static final ItemBehaviorType<DecoratedPotPatternItemBehavior> DECORATED_POT_PATTERN = register(
        "decorated_pot_pattern",
        DecoratedPotPatternItemBehavior.CODEC
    );
    public static final ItemBehaviorType<MappableItemBehavior> MAPPABLE = register(
        "mappable",
        MappableItemBehavior.CODEC
    );
    public static final ItemBehaviorType<MapHolderItemBehavior> MAP_HOLDER = register(
        "map_holder",
        MapHolderItemBehavior.CODEC
    );
    public static final ItemBehaviorType<BrushItemBehavior> BRUSH = register(
        "brush",
        BrushItemBehavior.CODEC
    );
    public static final ItemBehaviorType<ZoomItemBehavior> ZOOM = register(
        "zoom",
        ZoomItemBehavior.CODEC
    );
    public static final ItemBehaviorType<ItemHolderItemBehavior> ITEM_HOLDER = register(
        "item_holder",
        ItemHolderItemBehavior.CODEC
    );
    public static final ItemBehaviorType<ImmuneToDamageItemBehavior> IMMUNE_TO_DAMAGE = register(
        "immune_to_damage",
        ImmuneToDamageItemBehavior.CODEC
    );
    public static final ItemBehaviorType<UnlockRecipesItemBehavior> UNLOCK_RECIPES = register(
        "unlock_recipes",
        UnlockRecipesItemBehavior.CODEC
    );
    public static final ItemBehaviorType<DebugStickItemBehavior> DEBUG_STICK = register(
        "debug_stick",
        DebugStickItemBehavior.CODEC
    );
    public static final ItemBehaviorType<SuspiciousEffectIngredientItemBehavior> SUSPICIOUS_EFFECT_INGREDIENT = register(
        "suspicious_effect_ingredient",
        SuspiciousEffectIngredientItemBehavior.CODEC
    );
    public static final ItemBehaviorType<CastableItemBehavior> CASTABLE = register(
        "castable",
        CastableItemBehavior.CODEC
    );
    public static final ItemBehaviorType<StackableItemBehavior> STACKABLE = register(
        "stackable",
        StackableItemBehavior.CODEC
    );
    public static final ItemBehaviorType<OminousEffectProviderItemBehavior> OMINOUS_EFFECT_PROVIDER = register(
        "ominous_effect_provider",
        OminousEffectProviderItemBehavior.CODEC
    );
    public static final ItemBehaviorType<GliderItemBehavior> GLIDER = register(
        "glider",
        GliderItemBehavior.CODEC
    );
    public static final ItemBehaviorType<AttackBlockingItemBehavior> ATTACK_BLOCKING = register(
        "attack_blocking",
        AttackBlockingItemBehavior.CODEC
    );
    public static final ItemBehaviorType<TrimMaterialProviderItemBehavior> TRIM_MATERIAL_PROVIDER = register(
        "trim_material_provider",
        TrimMaterialProviderItemBehavior.CODEC
    );

    public static void init() {}

    private static <T extends ItemBehavior<T>> ItemBehaviorType<T> register(String id, Codec<T> type) {
        return Registry.register(ItematicBuiltInRegistries.ITEM_BEHAVIOR_TYPE, id, new ItemBehaviorType<>(type));
    }
}
