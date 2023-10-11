package net.errorcraft.itematic.data.server.tag;

import net.errorcraft.itematic.enchantment.EnchantmentKeys;
import net.errorcraft.itematic.enchantment.EnchantmentTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
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

        this.getOrCreateTagBuilder(EnchantmentTags.ARMOR)
            .add(getAll(registry, enchantment -> enchantment.target == EnchantmentTarget.ARMOR));
        this.getOrCreateTagBuilder(EnchantmentTags.CURSED)
            .add(getAll(registry, Enchantment::isCursed));
        this.getOrCreateTagBuilder(EnchantmentTags.BOOTS_ENCHANTING)
            .addTag(EnchantmentTags.ARMOR)
            .add(getAll(registry, EnchantmentTarget.ARMOR_FEET, false));
        this.forgingTag(registry, EnchantmentTags.BOOTS_FORGING, EnchantmentTags.BOOTS_ENCHANTING, EnchantmentTarget.ARMOR_FEET)
            .add(Enchantments.THORNS);
        this.getOrCreateTagBuilder(EnchantmentTags.LEGGINGS_ENCHANTING)
            .addTag(EnchantmentTags.ARMOR)
            .add(getAll(registry, EnchantmentTarget.ARMOR_LEGS, false));
        this.forgingTag(registry, EnchantmentTags.LEGGINGS_FORGING, EnchantmentTags.LEGGINGS_ENCHANTING, EnchantmentTarget.ARMOR_LEGS)
            .add(Enchantments.THORNS);
        this.getOrCreateTagBuilder(EnchantmentTags.CHESTPLATE_ENCHANTING)
            .addTag(EnchantmentTags.ARMOR)
            .add(getAll(registry, EnchantmentTarget.ARMOR_CHEST, false));
        this.forgingTag(registry, EnchantmentTags.CHESTPLATE_FORGING, EnchantmentTags.CHESTPLATE_ENCHANTING, EnchantmentTarget.ARMOR_CHEST);
        this.getOrCreateTagBuilder(EnchantmentTags.HELMET_ENCHANTING)
            .addTag(EnchantmentTags.ARMOR)
            .add(getAll(registry, EnchantmentTarget.ARMOR_HEAD, false));
        this.forgingTag(registry, EnchantmentTags.HELMET_FORGING, EnchantmentTags.HELMET_ENCHANTING, EnchantmentTarget.ARMOR_HEAD)
            .add(Enchantments.THORNS);
        this.getOrCreateTagBuilder(EnchantmentTags.SWORD_ENCHANTING)
            .add(getAll(registry, EnchantmentTarget.WEAPON, false));
        this.forgingTag(registry, EnchantmentTags.SWORD_FORGING, EnchantmentTags.SWORD_ENCHANTING, EnchantmentTarget.WEAPON);
        this.getOrCreateTagBuilder(EnchantmentTags.SHOVEL_ENCHANTING)
            .add(getAll(registry, EnchantmentTarget.DIGGER, false));
        this.forgingTag(registry, EnchantmentTags.SHOVEL_FORGING, EnchantmentTags.SHOVEL_ENCHANTING, EnchantmentTarget.DIGGER);
        this.getOrCreateTagBuilder(EnchantmentTags.PICKAXE_ENCHANTING)
            .add(getAll(registry, EnchantmentTarget.DIGGER, false));
        this.forgingTag(registry, EnchantmentTags.PICKAXE_FORGING, EnchantmentTags.PICKAXE_ENCHANTING, EnchantmentTarget.DIGGER);
        this.getOrCreateTagBuilder(EnchantmentTags.AXE_ENCHANTING)
            .add(getAll(registry, EnchantmentTarget.DIGGER, false));
        this.forgingTag(registry, EnchantmentTags.AXE_FORGING, EnchantmentTags.AXE_ENCHANTING, EnchantmentTarget.DIGGER)
            .add(Enchantments.BANE_OF_ARTHROPODS, Enchantments.SHARPNESS, Enchantments.SMITE);
        this.getOrCreateTagBuilder(EnchantmentTags.HOE_ENCHANTING)
            .add(getAll(registry, EnchantmentTarget.DIGGER, false));
        this.forgingTag(registry, EnchantmentTags.HOE_FORGING, EnchantmentTags.HOE_ENCHANTING, EnchantmentTarget.DIGGER);
        this.getOrCreateTagBuilder(EnchantmentTags.BOW_ENCHANTING)
            .add(getAll(registry, EnchantmentTarget.BOW, false));
        this.forgingTag(registry, EnchantmentTags.BOW_FORGING, EnchantmentTags.BOW_ENCHANTING, EnchantmentTarget.BOW);
        this.getOrCreateTagBuilder(EnchantmentTags.CROSSBOW_ENCHANTING)
            .add(getAll(registry, EnchantmentTarget.CROSSBOW, false));
        this.forgingTag(registry, EnchantmentTags.CROSSBOW_FORGING, EnchantmentTags.CROSSBOW_ENCHANTING, EnchantmentTarget.CROSSBOW);
        this.getOrCreateTagBuilder(EnchantmentTags.STEERING_FORGING)
            .add(EnchantmentKeys.VANISHING_CURSE)
            .add(EnchantmentKeys.MENDING)
            .add(EnchantmentKeys.UNBREAKING);
    }

    private FabricTagProvider<Enchantment>.FabricTagBuilder forgingTag(RegistryWrapper.Impl<Enchantment> registry, TagKey<Enchantment> tag, TagKey<Enchantment> enchantingTag, EnchantmentTarget enchantmentTarget) {
        return this.getOrCreateTagBuilder(tag)
            .addTag(enchantingTag)
            .addTag(EnchantmentTags.CURSED)
            .add(getAll(registry, enchantmentTarget, true));
    }

    private static Identifier[] getAll(RegistryWrapper.Impl<Enchantment> registry, EnchantmentTarget enchantmentTarget, boolean treasure) {
        return getAll(registry, enchantment -> isTargetable(enchantment, enchantmentTarget) && enchantment.isTreasure() == treasure);
    }

    private static Identifier[] getAll(RegistryWrapper.Impl<Enchantment> registry, Predicate<Enchantment> predicate) {
        return registry.streamEntries()
            .filter(entry -> predicate.test(entry.value()))
            .map(RegistryEntry.Reference::registryKey)
            .map(RegistryKey::getValue)
            .toArray(Identifier[]::new);
    }

    private static boolean isTargetable(Enchantment enchantment, EnchantmentTarget enchantmentTarget) {
        return enchantment.target == enchantmentTarget || enchantment.target == EnchantmentTarget.BREAKABLE;
    }
}
