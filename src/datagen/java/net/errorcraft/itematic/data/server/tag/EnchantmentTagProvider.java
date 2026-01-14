package net.errorcraft.itematic.data.server.tag;

import net.errorcraft.itematic.enchantment.EnchantmentKeys;
import net.errorcraft.itematic.enchantment.ItematicEnchantmentTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;

import java.util.concurrent.CompletableFuture;

public class EnchantmentTagProvider extends FabricTagProvider<Enchantment> {
    public EnchantmentTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ENCHANTMENT, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.ENCHANTING)
            .add(EnchantmentKeys.UNBREAKING);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.FORGING)
            .forceAddTag(EnchantmentTags.CURSE)
            .add(EnchantmentKeys.MENDING);

        this.getOrCreateTagBuilder(ItematicEnchantmentTags.ARMOR_ENCHANTING)
            .addTag(ItematicEnchantmentTags.ENCHANTING)
            .add(EnchantmentKeys.PROTECTION, EnchantmentKeys.FIRE_PROTECTION, EnchantmentKeys.BLAST_PROTECTION, EnchantmentKeys.PROJECTILE_PROTECTION);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.BOOTS_ENCHANTING)
            .addTag(ItematicEnchantmentTags.ARMOR_ENCHANTING)
            .add(EnchantmentKeys.FEATHER_FALLING, EnchantmentKeys.DEPTH_STRIDER);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.LEGGINGS_ENCHANTING)
            .addTag(ItematicEnchantmentTags.ARMOR_ENCHANTING);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.CHESTPLATE_ENCHANTING)
            .addTag(ItematicEnchantmentTags.ARMOR_ENCHANTING)
            .add(EnchantmentKeys.THORNS);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.HELMET_ENCHANTING)
            .addTag(ItematicEnchantmentTags.ARMOR_ENCHANTING)
            .add(EnchantmentKeys.RESPIRATION, EnchantmentKeys.AQUA_AFFINITY);

        this.getOrCreateTagBuilder(ItematicEnchantmentTags.ARMOR_FORGING)
            .addTag(ItematicEnchantmentTags.FORGING);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.BOOTS_FORGING)
            .addTag(ItematicEnchantmentTags.ARMOR_FORGING)
            .add(EnchantmentKeys.THORNS, EnchantmentKeys.FROST_WALKER, EnchantmentKeys.SOUL_SPEED);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.LEGGINGS_FORGING)
            .addTag(ItematicEnchantmentTags.ARMOR_FORGING)
            .add(EnchantmentKeys.THORNS, EnchantmentKeys.SWIFT_SNEAK);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.CHESTPLATE_FORGING)
            .addTag(ItematicEnchantmentTags.ARMOR_FORGING);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.HELMET_FORGING)
            .addTag(ItematicEnchantmentTags.ARMOR_FORGING)
            .add(EnchantmentKeys.THORNS);

        this.getOrCreateTagBuilder(ItematicEnchantmentTags.TOOL_ENCHANTING)
            .addTag(ItematicEnchantmentTags.ENCHANTING)
            .add(EnchantmentKeys.EFFICIENCY, EnchantmentKeys.SILK_TOUCH, EnchantmentKeys.FORTUNE);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.SHOVEL_ENCHANTING)
            .addTag(ItematicEnchantmentTags.TOOL_ENCHANTING);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.PICKAXE_ENCHANTING)
            .addTag(ItematicEnchantmentTags.TOOL_ENCHANTING);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.AXE_ENCHANTING)
            .addTag(ItematicEnchantmentTags.TOOL_ENCHANTING);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.HOE_ENCHANTING)
            .addTag(ItematicEnchantmentTags.TOOL_ENCHANTING);

        this.getOrCreateTagBuilder(ItematicEnchantmentTags.TOOL_FORGING)
            .addTag(ItematicEnchantmentTags.FORGING);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.SHOVEL_FORGING)
            .addTag(ItematicEnchantmentTags.TOOL_FORGING)
            .addTag(ItematicEnchantmentTags.SHOVEL_ENCHANTING);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.PICKAXE_FORGING)
            .addTag(ItematicEnchantmentTags.TOOL_FORGING)
            .addTag(ItematicEnchantmentTags.PICKAXE_ENCHANTING);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.AXE_FORGING)
            .addTag(ItematicEnchantmentTags.TOOL_FORGING)
            .addTag(ItematicEnchantmentTags.AXE_ENCHANTING)
            .add(EnchantmentKeys.BANE_OF_ARTHROPODS, EnchantmentKeys.SHARPNESS, EnchantmentKeys.SMITE);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.HOE_FORGING)
            .addTag(ItematicEnchantmentTags.TOOL_FORGING)
            .addTag(ItematicEnchantmentTags.HOE_ENCHANTING);

        this.getOrCreateTagBuilder(ItematicEnchantmentTags.SWORD_ENCHANTING)
            .addTag(ItematicEnchantmentTags.ENCHANTING)
            .add(EnchantmentKeys.SHARPNESS, EnchantmentKeys.SMITE, EnchantmentKeys.BANE_OF_ARTHROPODS, EnchantmentKeys.KNOCKBACK, EnchantmentKeys.FIRE_ASPECT, EnchantmentKeys.LOOTING, EnchantmentKeys.SWEEPING_EDGE);

        this.getOrCreateTagBuilder(ItematicEnchantmentTags.SWORD_FORGING)
            .addTag(ItematicEnchantmentTags.FORGING)
            .addTag(ItematicEnchantmentTags.SWORD_ENCHANTING);

        this.getOrCreateTagBuilder(ItematicEnchantmentTags.BOW_ENCHANTING)
            .addTag(ItematicEnchantmentTags.ENCHANTING)
            .add(EnchantmentKeys.POWER, EnchantmentKeys.PUNCH, EnchantmentKeys.FLAME, EnchantmentKeys.INFINITY);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.CROSSBOW_ENCHANTING)
            .addTag(ItematicEnchantmentTags.ENCHANTING)
            .add(EnchantmentKeys.MULTISHOT, EnchantmentKeys.QUICK_CHARGE, EnchantmentKeys.PIERCING);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.TRIDENT_ENCHANTING)
            .addTag(ItematicEnchantmentTags.ENCHANTING)
            .add(EnchantmentKeys.CHANNELING, EnchantmentKeys.RIPTIDE, EnchantmentKeys.LOYALTY);

        this.getOrCreateTagBuilder(ItematicEnchantmentTags.BOW_FORGING)
            .addTag(ItematicEnchantmentTags.BOW_ENCHANTING)
            .addTag(ItematicEnchantmentTags.FORGING);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.CROSSBOW_FORGING)
            .addTag(ItematicEnchantmentTags.CROSSBOW_ENCHANTING)
            .addTag(ItematicEnchantmentTags.FORGING);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.TRIDENT_FORGING)
            .addTag(ItematicEnchantmentTags.TRIDENT_ENCHANTING)
            .addTag(ItematicEnchantmentTags.FORGING);

        this.getOrCreateTagBuilder(ItematicEnchantmentTags.STEERING_FORGING)
            .add(EnchantmentKeys.VANISHING_CURSE)
            .add(EnchantmentKeys.MENDING)
            .add(EnchantmentKeys.UNBREAKING);
        this.getOrCreateTagBuilder(ItematicEnchantmentTags.BRUSH_FORGING)
            .add(EnchantmentKeys.VANISHING_CURSE)
            .add(EnchantmentKeys.MENDING)
            .add(EnchantmentKeys.UNBREAKING);
    }
}
