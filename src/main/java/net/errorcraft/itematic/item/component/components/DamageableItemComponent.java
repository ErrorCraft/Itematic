package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.block.ItematicBlockTags;
import net.errorcraft.itematic.enchantment.EnchantmentTags;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

public record DamageableItemComponent(int durability, boolean preserveItem) implements ItemComponent {
    public static Codec<DamageableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("durability").forGetter(DamageableItemComponent::durability),
        Codec.BOOL.optionalFieldOf("preserve_item", false).forGetter(DamageableItemComponent::preserveItem)
    ).apply(instance, DamageableItemComponent::new));

    public DamageableItemComponent(int durability) {
        this(durability, false);
    }

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.DAMAGEABLE;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
        return CODEC;
    }

    public int maximumDamage() {
        return this.durability - (this.preserveItem ? 1 : 0);
    }

    public boolean isUsable(ItemStack stack) {
        return stack.getDamage() < this.maximumDamage();
    }

    public static ItemComponent[] sword(ToolMaterial material, TagKey<Item> repairItemsTag) {
        return new ItemComponent[] {
            new DamageableItemComponent(material.getDurability()),
            ToolItemComponent.builder(2)
                .miningSpeed(15.0f, ItematicBlockTags.SWORD_SUPER_EFFICIENT, true)
                .miningSpeed(1.5f, BlockTags.SWORD_EFFICIENT, true)
                .build(),
            new WeaponItemComponent(1),
            EnchantableItemComponent.enchants(material, EnchantmentTags.SWORD_ENCHANTING),
            ForgeableItemComponent.of(EnchantmentTags.SWORD_FORGING),
            new RepairableItemComponent(repairItemsTag)
        };
    }

    public static ItemComponent[] shovel(ToolMaterial material, TagKey<Item> repairItemsTag) {
        return tool(material, BlockTags.SHOVEL_MINEABLE, EnchantmentTags.SHOVEL_ENCHANTING, EnchantmentTags.SHOVEL_FORGING, repairItemsTag);
    }

    public static ItemComponent[] pickaxe(ToolMaterial material, TagKey<Item> repairItemsTag) {
        return tool(material, BlockTags.PICKAXE_MINEABLE, EnchantmentTags.PICKAXE_ENCHANTING, EnchantmentTags.PICKAXE_FORGING, repairItemsTag);
    }

    public static ItemComponent[] axe(ToolMaterial material, TagKey<Item> repairItemsTag) {
        return tool(material, BlockTags.AXE_MINEABLE, EnchantmentTags.AXE_ENCHANTING, EnchantmentTags.AXE_FORGING, repairItemsTag);
    }

    public static ItemComponent[] hoe(ToolMaterial material, TagKey<Item> repairItemsTag) {
        return tool(material, BlockTags.HOE_MINEABLE, EnchantmentTags.HOE_ENCHANTING, EnchantmentTags.HOE_FORGING, repairItemsTag);
    }

    private static ItemComponent[] tool(ToolMaterial material, TagKey<Block> miningSpeedTag, TagKey<Enchantment> toolEnchantingTag, TagKey<Enchantment> toolForgingTag, TagKey<Item> repairItemsTag) {
        return new ItemComponent[] {
            new DamageableItemComponent(material.getDurability()),
            ToolItemComponent.builder(1)
                .miningSpeed(material.getMiningSpeedMultiplier(), miningSpeedTag, true)
                .build(),
            new WeaponItemComponent(2),
            EnchantableItemComponent.enchants(material, toolEnchantingTag),
            ForgeableItemComponent.of(toolForgingTag),
            new RepairableItemComponent(repairItemsTag)
        };
    }
}
