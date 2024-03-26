package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.enchantment.EnchantmentTags;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

import java.util.List;

public record DamageableItemComponent(int durability, boolean preserveItem) implements ItemComponent<DamageableItemComponent> {
    public static final Codec<DamageableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("durability").forGetter(DamageableItemComponent::durability),
        Codec.BOOL.optionalFieldOf("preserve_item", false).forGetter(DamageableItemComponent::preserveItem)
    ).apply(instance, DamageableItemComponent::new));

    @Override
    public ItemComponentType<DamageableItemComponent> type() {
        return ItemComponentTypes.DAMAGEABLE;
    }

    @Override
    public Codec<DamageableItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.MAX_DAMAGE, this.durability);
        builder.add(DataComponentTypes.DAMAGE, 0);
    }

    public int maximumDamage(ItemStack stack) {
        return stack.getMaxDamage() - (this.preserveItem ? 1 : 0);
    }

    public boolean isUsable(ItemStack stack) {
        return stack.getDamage() < this.maximumDamage(stack);
    }

    public static DamageableItemComponent of(int durability) {
        return new DamageableItemComponent(durability, false);
    }

    public static DamageableItemComponent of(int durability, boolean preserveItem) {
        return new DamageableItemComponent(durability, preserveItem);
    }

    public static ItemComponent<?>[] sword(ToolMaterial material, TagKey<Item> repairItemsTag) {
        return new ItemComponent<?>[] {
            MaxStackSizeItemComponent.of(1),
            DamageableItemComponent.of(material.getDurability()),
            ToolItemComponent.builder(2)
                .rule(ToolComponent.Rule.ofAlwaysDropping(List.of(Blocks.COBWEB), 15.0f))
                .rule(ToolComponent.Rule.of(BlockTags.SWORD_EFFICIENT, 1.5f))
                .build(),
            WeaponItemComponent.of(1, 3.0d + material.getAttackDamage(), -2.4d),
            EnchantableItemComponent.enchants(material, EnchantmentTags.SWORD_ENCHANTING),
            ForgeableItemComponent.of(EnchantmentTags.SWORD_FORGING),
            RepairableItemComponent.of(repairItemsTag)
        };
    }

    public static ItemComponent<?>[] shovel(ToolMaterial material, TagKey<Item> repairItemsTag) {
        return tool(material, 1.5d, -3.0d, BlockTags.SHOVEL_MINEABLE, EnchantmentTags.SHOVEL_ENCHANTING, EnchantmentTags.SHOVEL_FORGING, repairItemsTag);
    }

    public static ItemComponent<?>[] pickaxe(ToolMaterial material, TagKey<Item> repairItemsTag) {
        return tool(material, 1.0d, -2.8d, BlockTags.PICKAXE_MINEABLE, EnchantmentTags.PICKAXE_ENCHANTING, EnchantmentTags.PICKAXE_FORGING, repairItemsTag);
    }

    public static ItemComponent<?>[] axe(ToolMaterial material, double attackDamage, double attackSpeed, TagKey<Item> repairItemsTag) {
        return tool(material, attackDamage, attackSpeed, BlockTags.AXE_MINEABLE, EnchantmentTags.AXE_ENCHANTING, EnchantmentTags.AXE_FORGING, repairItemsTag);
    }

    public static ItemComponent<?>[] hoe(ToolMaterial material, double attackDamage, double attackSpeed, TagKey<Item> repairItemsTag) {
        return tool(material, attackDamage, attackSpeed, BlockTags.HOE_MINEABLE, EnchantmentTags.HOE_ENCHANTING, EnchantmentTags.HOE_FORGING, repairItemsTag);
    }

    private static ItemComponent<?>[] tool(ToolMaterial material, double attackDamage, double attackSpeed, TagKey<Block> mineableBlocks, TagKey<Enchantment> toolEnchantingTag, TagKey<Enchantment> toolForgingTag, TagKey<Item> repairItemsTag) {
        return new ItemComponent<?>[] {
            MaxStackSizeItemComponent.of(1),
            DamageableItemComponent.of(material.getDurability()),
            ToolItemComponent.of(material, mineableBlocks),
            WeaponItemComponent.of(2, attackDamage + material.getAttackDamage(), attackSpeed),
            EnchantableItemComponent.enchants(material, toolEnchantingTag),
            ForgeableItemComponent.of(toolForgingTag),
            RepairableItemComponent.of(repairItemsTag)
        };
    }
}
