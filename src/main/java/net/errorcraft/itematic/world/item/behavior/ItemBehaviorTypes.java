package net.errorcraft.itematic.world.item.behavior;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.behavior.behaviors.AttackBlockingItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.BannerPatternHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.BannerPatternItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.BlockItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.BlockTransformerItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.BrewingFuelItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.BrushItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.BucketItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.CastableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.CompostableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ConsumableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.CookingFuelItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.DamageableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.DebugStickItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.DecoratedPotPatternItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.DispensableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.DyeItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.EnchantableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.EnchantmentHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.EntityItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.EquipmentItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.FireworkExplosionHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.FireworkItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.FoodItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.GliderFuelItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.GliderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ImmuneToDamageItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ItemHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.MapDecorationHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.PlayableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.PlayableSongItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.PotionHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ProjectileItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.RepairableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ShooterItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.SignModifierItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.SmithingTemplateDisplayItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.StackableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.SteeringItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.SuspiciousEffectIngredientItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ThrowableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ToolItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.TrimMaterialProviderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.UseableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.VillagerFoodItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.WeaponItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.WritableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ZoomItemBehavior;
import net.minecraft.core.Registry;

public class ItemBehaviorTypes {
    private ItemBehaviorTypes() {}

    public static void bootstrap(Registry<Codec<? extends ItemBehavior>> registry) {
        // Common behavior
        Registry.register(registry, "stackable", StackableItemBehavior.CODEC);
        Registry.register(registry, "damageable", DamageableItemBehavior.CODEC);
        Registry.register(registry, "tool", ToolItemBehavior.CODEC);
        Registry.register(registry, "weapon", WeaponItemBehavior.CODEC);
        Registry.register(registry, "equipment", EquipmentItemBehavior.CODEC);
        Registry.register(registry, "repairable", RepairableItemBehavior.CODEC);
        Registry.register(registry, "enchantable", EnchantableItemBehavior.CODEC);
        Registry.register(registry, "dispensable", DispensableItemBehavior.CODEC);
        Registry.register(registry, "useable", UseableItemBehavior.CODEC);
        Registry.register(registry, "consumable", ConsumableItemBehavior.CODEC);

        // Block, entity and fluid behavior
        Registry.register(registry, "block", BlockItemBehavior.CODEC);
        Registry.register(registry, "block_transformer", BlockTransformerItemBehavior.CODEC);
        Registry.register(registry, "sign_modifier", SignModifierItemBehavior.CODEC);
        Registry.register(registry, "debug_stick", DebugStickItemBehavior.CODEC);
        Registry.register(registry, "compostable", CompostableItemBehavior.CODEC);
        Registry.register(registry, "brush", BrushItemBehavior.CODEC);
        Registry.register(registry, "entity", EntityItemBehavior.CODEC);
        Registry.register(registry, "bucket", BucketItemBehavior.CODEC);
        Registry.register(registry, "projectile", ProjectileItemBehavior.CODEC);
        Registry.register(registry, "throwable", ThrowableItemBehavior.CODEC);
        Registry.register(registry, "shooter", ShooterItemBehavior.CODEC);
        Registry.register(registry, "steering", SteeringItemBehavior.CODEC);

        // Player behavior
        Registry.register(registry, "food", FoodItemBehavior.CODEC);
        Registry.register(registry, "potion_holder", PotionHolderItemBehavior.CODEC);
        Registry.register(registry, "attack_blocking", AttackBlockingItemBehavior.CODEC);
        Registry.register(registry, "glider", GliderItemBehavior.CODEC);
        Registry.register(registry, "glider_fuel", GliderFuelItemBehavior.CODEC);
        Registry.register(registry, "map_decoration_holder", MapDecorationHolderItemBehavior.CODEC);
        Registry.register(registry, "item_holder", ItemHolderItemBehavior.CODEC);
        Registry.register(registry, "castable", CastableItemBehavior.CODEC);
        Registry.register(registry, "playable", PlayableItemBehavior.CODEC);
        Registry.register(registry, "playable_song", PlayableSongItemBehavior.CODEC);
        Registry.register(registry, "zoom", ZoomItemBehavior.CODEC);
        Registry.register(registry, "writable", WritableItemBehavior.CODEC);

        // Interactive behavior
        Registry.register(registry, "dye", DyeItemBehavior.CODEC);
        Registry.register(registry, "cooking_fuel", CookingFuelItemBehavior.CODEC);
        Registry.register(registry, "brewing_fuel", BrewingFuelItemBehavior.CODEC);
        Registry.register(registry, "immune_to_damage", ImmuneToDamageItemBehavior.CODEC);
        Registry.register(registry, "enchantment_holder", EnchantmentHolderItemBehavior.CODEC);
        Registry.register(registry, "villager_food", VillagerFoodItemBehavior.CODEC);
        Registry.register(registry, "firework", FireworkItemBehavior.CODEC);
        Registry.register(registry, "firework_explosion_holder", FireworkExplosionHolderItemBehavior.CODEC);
        Registry.register(registry, "banner_pattern", BannerPatternItemBehavior.CODEC);
        Registry.register(registry, "banner_pattern_holder", BannerPatternHolderItemBehavior.CODEC);
        Registry.register(registry, "smithing_template_display", SmithingTemplateDisplayItemBehavior.CODEC);
        Registry.register(registry, "trim_material_provider", TrimMaterialProviderItemBehavior.CODEC);
        Registry.register(registry, "decorated_pot_pattern", DecoratedPotPatternItemBehavior.CODEC);
        Registry.register(registry, "suspicious_effect_ingredient", SuspiciousEffectIngredientItemBehavior.CODEC);
    }
}
