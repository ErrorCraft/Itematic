package errorcraft.itematic.data;

import errorcraft.itematic.client.render.TexturedRenderLayersUtil;
import errorcraft.itematic.enchantment.EnchantmentTags;
import errorcraft.itematic.item.armor.ArmorMaterials;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.client.texture.atlas.AtlasSourceManager;
import net.minecraft.data.DataOutput;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

@SuppressWarnings("UnstableApiUsage")
public class ItematicData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ItemProvider::new);
        pack.addProvider(ArmorMaterialProvider::new);
        pack.addProvider(AtlasProvider::new);
        pack.addProvider(EnchantmentTagProvider::new);
    }

    private static <T> void addAll(FabricDynamicRegistryProvider.Entries entries, RegistryWrapper.Impl<T> registry) {
        registry.streamKeys().forEach(key -> entries.add(registry, key));
    }

    private static class ItemProvider extends FabricDynamicRegistryProvider {
        public ItemProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
            addAll(entries, registries.getWrapperOrThrow(RegistryKeys.ITEM));
        }

        @Override
        public String getName() {
            return "Items";
        }
    }

    private static class ArmorMaterialProvider extends FabricDynamicRegistryProvider {
        public ArmorMaterialProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
            addAll(entries, registries.getWrapperOrThrow(ArmorMaterials.ARMOR_MATERIAL_KEY));
        }

        @Override
        public String getName() {
            return "Armor Materials";
        }
    }

    private static class AtlasProvider extends FabricCodecDataProvider<List<AtlasSource>> {
        protected AtlasProvider(FabricDataOutput dataOutput) {
            super(dataOutput, DataOutput.OutputType.RESOURCE_PACK, "atlases", AtlasSourceManager.LIST_CODEC);
        }

        @Override
        protected void configure(BiConsumer<Identifier, List<AtlasSource>> provider) {
            TexturedRenderLayersUtil.bootstrap(provider);
        }

        @Override
        public String getName() {
            return "Atlases";
        }
    }

    private static class EnchantmentTagProvider extends FabricTagProvider<Enchantment> {
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
}
