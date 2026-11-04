package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public record EnchantmentHolderItemBehavior(Holder<Item> grindResultItem) implements ItemBehavior<EnchantmentHolderItemBehavior> {
    public static final Codec<EnchantmentHolderItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Item.CODEC.fieldOf("grind_result_item").forGetter(EnchantmentHolderItemBehavior::grindResultItem)
    ).apply(instance, EnchantmentHolderItemBehavior::new));

    public static EnchantmentHolderItemBehavior of(Holder<Item> grindingTransformsInto) {
        return new EnchantmentHolderItemBehavior(grindingTransformsInto);
    }

    @Override
    public ItemBehaviorType<EnchantmentHolderItemBehavior> type() {
        return ItemBehaviorType.ENCHANTMENT_HOLDER;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        builder.set(ItematicDataComponents.GRIND_RESULT_ITEM, this.grindResultItem);
    }
}
