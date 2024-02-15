package net.errorcraft.itematic.data.server.tag;

import net.errorcraft.itematic.enchantment.EnchantmentKeys;
import net.errorcraft.itematic.enchantment.EnchantmentTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class EnchantmentTagProvider extends FabricTagProvider<Enchantment> {
    public EnchantmentTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ENCHANTMENT, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        RegistryWrapper.Impl<Enchantment> registry = arg.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);

        this.getOrCreateTagBuilder(EnchantmentTags.CURSED)
            .add(getAll(registry, Enchantment::isCursed));
        this.getOrCreateTagBuilder(EnchantmentTags.ENCHANTING)
            .add(EnchantmentKeys.UNBREAKING);
        this.getOrCreateTagBuilder(EnchantmentTags.FORGING)
            .addTag(EnchantmentTags.CURSED)
            .add(EnchantmentKeys.MENDING);

        this.getOrCreateTagBuilder(EnchantmentTags.ARMOR_ENCHANTING)
            .addTag(EnchantmentTags.ENCHANTING)
            .add(EnchantmentKeys.PROTECTION, EnchantmentKeys.FIRE_PROTECTION, EnchantmentKeys.BLAST_PROTECTION, EnchantmentKeys.PROJECTILE_PROTECTION);
        this.getOrCreateTagBuilder(EnchantmentTags.BOOTS_ENCHANTING)
            .addTag(EnchantmentTags.ARMOR_ENCHANTING)
            .add(EnchantmentKeys.FEATHER_FALLING, EnchantmentKeys.DEPTH_STRIDER);
        this.getOrCreateTagBuilder(EnchantmentTags.LEGGINGS_ENCHANTING)
            .addTag(EnchantmentTags.ARMOR_ENCHANTING);
        this.getOrCreateTagBuilder(EnchantmentTags.CHESTPLATE_ENCHANTING)
            .addTag(EnchantmentTags.ARMOR_ENCHANTING)
            .add(EnchantmentKeys.THORNS);
        this.getOrCreateTagBuilder(EnchantmentTags.HELMET_ENCHANTING)
            .addTag(EnchantmentTags.ARMOR_ENCHANTING)
            .add(EnchantmentKeys.RESPIRATION, EnchantmentKeys.AQUA_AFFINITY);

        this.getOrCreateTagBuilder(EnchantmentTags.ARMOR_FORGING)
            .addTag(EnchantmentTags.FORGING);
        this.getOrCreateTagBuilder(EnchantmentTags.BOOTS_FORGING)
            .addTag(EnchantmentTags.ARMOR_FORGING)
            .add(EnchantmentKeys.THORNS, EnchantmentKeys.FROST_WALKER, EnchantmentKeys.SOUL_SPEED);
        this.getOrCreateTagBuilder(EnchantmentTags.LEGGINGS_FORGING)
            .addTag(EnchantmentTags.ARMOR_FORGING)
            .add(EnchantmentKeys.THORNS, EnchantmentKeys.SWIFT_SNEAK);
        this.getOrCreateTagBuilder(EnchantmentTags.CHESTPLATE_FORGING)
            .addTag(EnchantmentTags.ARMOR_FORGING);
        this.getOrCreateTagBuilder(EnchantmentTags.HELMET_FORGING)
            .addTag(EnchantmentTags.ARMOR_FORGING)
            .add(EnchantmentKeys.THORNS);

        this.getOrCreateTagBuilder(EnchantmentTags.TOOL_ENCHANTING)
            .addTag(EnchantmentTags.ENCHANTING)
            .add(EnchantmentKeys.EFFICIENCY, EnchantmentKeys.SILK_TOUCH, EnchantmentKeys.FORTUNE);
        this.getOrCreateTagBuilder(EnchantmentTags.SHOVEL_ENCHANTING)
            .addTag(EnchantmentTags.TOOL_ENCHANTING);
        this.getOrCreateTagBuilder(EnchantmentTags.PICKAXE_ENCHANTING)
            .addTag(EnchantmentTags.TOOL_ENCHANTING);
        this.getOrCreateTagBuilder(EnchantmentTags.AXE_ENCHANTING)
            .addTag(EnchantmentTags.TOOL_ENCHANTING);
        this.getOrCreateTagBuilder(EnchantmentTags.HOE_ENCHANTING)
            .addTag(EnchantmentTags.TOOL_ENCHANTING);

        this.getOrCreateTagBuilder(EnchantmentTags.TOOL_FORGING)
            .addTag(EnchantmentTags.FORGING);
        this.getOrCreateTagBuilder(EnchantmentTags.SHOVEL_FORGING)
            .addTag(EnchantmentTags.TOOL_FORGING)
            .addTag(EnchantmentTags.SHOVEL_ENCHANTING);
        this.getOrCreateTagBuilder(EnchantmentTags.PICKAXE_FORGING)
            .addTag(EnchantmentTags.TOOL_FORGING)
            .addTag(EnchantmentTags.PICKAXE_ENCHANTING);
        this.getOrCreateTagBuilder(EnchantmentTags.AXE_FORGING)
            .addTag(EnchantmentTags.TOOL_FORGING)
            .addTag(EnchantmentTags.AXE_ENCHANTING)
            .add(EnchantmentKeys.BANE_OF_ARTHROPODS, EnchantmentKeys.SHARPNESS, EnchantmentKeys.SMITE);
        this.getOrCreateTagBuilder(EnchantmentTags.HOE_FORGING)
            .addTag(EnchantmentTags.TOOL_FORGING)
            .addTag(EnchantmentTags.HOE_ENCHANTING);

        this.getOrCreateTagBuilder(EnchantmentTags.SWORD_ENCHANTING)
            .addTag(EnchantmentTags.ENCHANTING)
            .add(EnchantmentKeys.SHARPNESS, EnchantmentKeys.SMITE, EnchantmentKeys.BANE_OF_ARTHROPODS, EnchantmentKeys.KNOCKBACK, EnchantmentKeys.FIRE_ASPECT, EnchantmentKeys.LOOTING, EnchantmentKeys.SWEEPING_EDGE);

        this.getOrCreateTagBuilder(EnchantmentTags.SWORD_FORGING)
            .addTag(EnchantmentTags.FORGING)
            .addTag(EnchantmentTags.SWORD_ENCHANTING);

        this.getOrCreateTagBuilder(EnchantmentTags.BOW_ENCHANTING)
            .addTag(EnchantmentTags.ENCHANTING)
            .add(EnchantmentKeys.POWER, EnchantmentKeys.PUNCH, EnchantmentKeys.FLAME, EnchantmentKeys.INFINITY);
        this.getOrCreateTagBuilder(EnchantmentTags.CROSSBOW_ENCHANTING)
            .addTag(EnchantmentTags.ENCHANTING)
            .add(EnchantmentKeys.MULTISHOT, EnchantmentKeys.QUICK_CHARGE, EnchantmentKeys.PIERCING);
        this.getOrCreateTagBuilder(EnchantmentTags.TRIDENT_ENCHANTING)
            .addTag(EnchantmentTags.ENCHANTING)
            .add(EnchantmentKeys.CHANNELING, EnchantmentKeys.RIPTIDE, EnchantmentKeys.LOYALTY);

        this.getOrCreateTagBuilder(EnchantmentTags.BOW_FORGING)
            .addTag(EnchantmentTags.BOW_ENCHANTING)
            .addTag(EnchantmentTags.FORGING);
        this.getOrCreateTagBuilder(EnchantmentTags.CROSSBOW_FORGING)
            .addTag(EnchantmentTags.CROSSBOW_ENCHANTING)
            .addTag(EnchantmentTags.FORGING);
        this.getOrCreateTagBuilder(EnchantmentTags.TRIDENT_FORGING)
            .addTag(EnchantmentTags.TRIDENT_ENCHANTING)
            .addTag(EnchantmentTags.FORGING);

        this.getOrCreateTagBuilder(EnchantmentTags.STEERING_FORGING)
            .add(EnchantmentKeys.VANISHING_CURSE)
            .add(EnchantmentKeys.MENDING)
            .add(EnchantmentKeys.UNBREAKING);
        this.getOrCreateTagBuilder(EnchantmentTags.BRUSH_FORGING)
            .add(EnchantmentKeys.VANISHING_CURSE)
            .add(EnchantmentKeys.MENDING)
            .add(EnchantmentKeys.UNBREAKING);
    }

    private static Identifier[] getAll(RegistryWrapper.Impl<Enchantment> registry, Predicate<Enchantment> predicate) {
        return registry.streamEntries()
            .filter(entry -> predicate.test(entry.value()))
            .map(RegistryEntry.Reference::registryKey)
            .map(RegistryKey::getValue)
            .toArray(Identifier[]::new);
    }
}
