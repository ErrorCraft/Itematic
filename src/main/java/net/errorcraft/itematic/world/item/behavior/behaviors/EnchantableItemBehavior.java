package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.equipment.ArmorMaterial;

import java.util.Optional;

public record EnchantableItemBehavior(int enchantability, Optional<Holder<Item>> enchantResultItem) implements ItemBehavior<EnchantableItemBehavior> {
    public static final Codec<EnchantableItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("enchantability").forGetter(EnchantableItemBehavior::enchantability),
        Item.CODEC.optionalFieldOf("enchant_result_item").forGetter(EnchantableItemBehavior::enchantResultItem)
    ).apply(instance, EnchantableItemBehavior::new));

    public static EnchantableItemBehavior of(ArmorMaterial material) {
        return of(material.enchantmentValue());
    }

    public static EnchantableItemBehavior of(ToolMaterial material) {
        return of(material.enchantmentValue());
    }

    public static EnchantableItemBehavior of(int enchantability) {
        return new EnchantableItemBehavior(enchantability, Optional.empty());
    }

    public static EnchantableItemBehavior of(int enchantability, Holder<Item> transformsInto) {
        return new EnchantableItemBehavior(enchantability, Optional.of(transformsInto));
    }

    @Override
    public ItemBehaviorType<EnchantableItemBehavior> type() {
        return ItemBehaviorType.ENCHANTABLE;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.ENCHANTABLE, new Enchantable(this.enchantability));
        this.enchantResultItem.ifPresent(enchantResultItem -> builder.set(ItematicDataComponents.ENCHANT_RESULT_ITEM, enchantResultItem));
    }
}
