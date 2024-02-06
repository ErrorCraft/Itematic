package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.text.Text;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record EnchantmentHolderItemComponent(Optional<RegistryEntry<Item>> grindingTransformsInto) implements ItemComponent<EnchantmentHolderItemComponent> {
    public static final Codec<EnchantmentHolderItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.createStrictOptionalFieldCodec(RegistryFixedCodec.of(RegistryKeys.ITEM), "grinding_transforms_into").forGetter(EnchantmentHolderItemComponent::grindingTransformsInto)
    ).apply(instance, EnchantmentHolderItemComponent::new));

    @Override
    public ItemComponentType<EnchantmentHolderItemComponent> type() {
        return ItemComponentTypes.ENCHANTMENT_HOLDER;
    }

    @Override
    public Codec<EnchantmentHolderItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        ItemStack.appendEnchantments(tooltip, EnchantedBookItem.getEnchantmentNbt(stack));
    }

    public static EnchantmentHolderItemComponent of() {
        return new EnchantmentHolderItemComponent(Optional.empty());
    }

    public static EnchantmentHolderItemComponent of(RegistryEntry<Item> grindingTransformsInto) {
        return new EnchantmentHolderItemComponent(Optional.of(grindingTransformsInto));
    }
}
