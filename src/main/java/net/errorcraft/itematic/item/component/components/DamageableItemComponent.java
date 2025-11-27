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
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;
import java.util.Optional;

public record DamageableItemComponent(int durability, Optional<RegistryEntry<SoundEvent>> breakSound, boolean preserveItem) implements ItemComponent<DamageableItemComponent> {
    public static final Codec<DamageableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.POSITIVE_INT.fieldOf("durability").forGetter(DamageableItemComponent::durability),
        SoundEvent.ENTRY_CODEC.optionalFieldOf("break_sound").forGetter(DamageableItemComponent::breakSound),
        Codec.BOOL.optionalFieldOf("preserve_item", false).forGetter(DamageableItemComponent::preserveItem)
    ).apply(instance, DamageableItemComponent::new));

    public static DamageableItemComponent of(int durability) {
        return new DamageableItemComponent(durability, Optional.empty(), false);
    }

    public static DamageableItemComponent of(int durability, RegistryEntry<SoundEvent> breakSound) {
        return new DamageableItemComponent(durability, Optional.of(breakSound), false);
    }

    public static DamageableItemComponent ofPreserved(int durability) {
        return new DamageableItemComponent(durability, Optional.empty(), true);
    }

    public static ItemComponent<?>[] sword(ToolMaterial material, TagKey<Item> repairItemsTag) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            DamageableItemComponent.of(material.getDurability()),
            ToolItemComponent.builder(2)
                .rule(ToolComponent.Rule.ofAlwaysDropping(List.of(Blocks.COBWEB), 15.0f))
                .rule(ToolComponent.Rule.of(BlockTags.SWORD_EFFICIENT, 1.5f))
                .build(),
            WeaponItemComponent.of(1, 3.0d + material.getAttackDamage(), 0.4d),
            EnchantableItemComponent.enchants(material, EnchantmentTags.SWORD_ENCHANTING),
            ForgeableItemComponent.of(EnchantmentTags.SWORD_FORGING),
            RepairableItemComponent.of(repairItemsTag)
        };
    }

    public static ItemComponent<?>[] shovel(ToolMaterial material, TagKey<Item> repairItemsTag) {
        return tool(material, 1.5d, 0.25d, BlockTags.SHOVEL_MINEABLE, EnchantmentTags.SHOVEL_ENCHANTING, EnchantmentTags.SHOVEL_FORGING, repairItemsTag);
    }

    public static ItemComponent<?>[] pickaxe(ToolMaterial material, TagKey<Item> repairItemsTag) {
        return tool(material, 1.0d, 0.3d, BlockTags.PICKAXE_MINEABLE, EnchantmentTags.PICKAXE_ENCHANTING, EnchantmentTags.PICKAXE_FORGING, repairItemsTag);
    }

    public static ItemComponent<?>[] axe(ToolMaterial material, double attackDamage, double attackSpeed, TagKey<Item> repairItemsTag) {
        return tool(material, attackDamage, attackSpeed, BlockTags.AXE_MINEABLE, EnchantmentTags.AXE_ENCHANTING, EnchantmentTags.AXE_FORGING, repairItemsTag);
    }

    public static ItemComponent<?>[] hoe(ToolMaterial material, double attackDamage, double attackSpeed, TagKey<Item> repairItemsTag) {
        return tool(material, attackDamage, attackSpeed, BlockTags.HOE_MINEABLE, EnchantmentTags.HOE_ENCHANTING, EnchantmentTags.HOE_FORGING, repairItemsTag);
    }

    private static ItemComponent<?>[] tool(ToolMaterial material, double attackDamage, double attackSpeed, TagKey<Block> mineableBlocks, TagKey<Enchantment> toolEnchantingTag, TagKey<Enchantment> toolForgingTag, TagKey<Item> repairItemsTag) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            DamageableItemComponent.of(material.getDurability()),
            ToolItemComponent.of(material, mineableBlocks),
            WeaponItemComponent.of(2, attackDamage + material.getAttackDamage(), attackSpeed),
            EnchantableItemComponent.enchants(material, toolEnchantingTag),
            ForgeableItemComponent.of(toolForgingTag),
            RepairableItemComponent.of(repairItemsTag)
        };
    }

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
}
