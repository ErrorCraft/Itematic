package errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import errorcraft.itematic.item.component.ItemComponent;
import errorcraft.itematic.item.component.ItemComponentType;
import errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.registry.tag.TagKey;

import java.util.Optional;

public record EnchantableItemComponent(int enchantability, Optional<TagKey<Enchantment>> enchantments, Optional<RegistryEntry<Item>> transformsInto) implements ItemComponent {
    public static final Codec<EnchantableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("enchantability").forGetter(EnchantableItemComponent::enchantability),
        TagKey.unprefixedCodec(RegistryKeys.ENCHANTMENT).optionalFieldOf("enchantments").forGetter(EnchantableItemComponent::enchantments),
        RegistryFixedCodec.of(RegistryKeys.ITEM).optionalFieldOf("transforms_into").forGetter(EnchantableItemComponent::transformsInto)
    ).apply(instance, EnchantableItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.ENCHANTABLE;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    public static EnchantableItemComponent enchants(ArmorMaterial material, TagKey<Enchantment> enchantments) {
        return enchants(material.getEnchantability(), enchantments);
    }

    public static EnchantableItemComponent enchants(ToolMaterial material, TagKey<Enchantment> enchantments) {
        return enchants(material.getEnchantability(), enchantments);
    }

    public static EnchantableItemComponent enchants(int enchantability, TagKey<Enchantment> enchantments) {
        return new EnchantableItemComponent(enchantability, Optional.of(enchantments), Optional.empty());
    }

    public static EnchantableItemComponent transforms(int enchantability, RegistryEntry<Item> item) {
        return new EnchantableItemComponent(enchantability, Optional.empty(), Optional.of(item));
    }
}
