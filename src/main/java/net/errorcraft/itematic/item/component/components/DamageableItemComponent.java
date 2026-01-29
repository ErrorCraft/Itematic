package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.block.BlockKeys;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.block.Block;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.dynamic.Codecs;

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

    public static ItemComponent<?>[] sword(RegistryEntryLookup<Block> blocks, ToolMaterial material, RegistryEntryList<Item> repairItems) {
        double attackDamage = 4.0d + material.attackDamageBonus();
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            DamageableItemComponent.of(material.durability()),
            ToolItemComponent.builder(2)
                .rule(ToolComponent.Rule.ofAlwaysDropping(RegistryEntryList.of(blocks.getOrThrow(BlockKeys.COBWEB)), 15.0f))
                .rule(ToolComponent.Rule.of(blocks.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5f))
                .build(),
            WeaponItemComponent.of(
                1,
                attackDamage,
                0.4d
            ),
            EnchantableItemComponent.of(material),
            RepairableItemComponent.of(repairItems)
        };
    }

    public static ItemComponent<?>[] shovel(RegistryEntryLookup<Block> blocks, ToolMaterial material, RegistryEntryList<Item> repairItems) {
        return tool(blocks, material, 2.5d, 0.25d, BlockTags.SHOVEL_MINEABLE, repairItems);
    }

    public static ItemComponent<?>[] pickaxe(RegistryEntryLookup<Block> blocks, ToolMaterial material, RegistryEntryList<Item> repairItems) {
        return tool(blocks, material, 2.0d, 0.3d, BlockTags.PICKAXE_MINEABLE, repairItems);
    }

    public static ItemComponent<?>[] axe(RegistryEntryLookup<Block> blocks, ToolMaterial material, double attackDamage, double attackSpeed, RegistryEntryList<Item> repairItems) {
        return tool(blocks, material, attackDamage, attackSpeed, BlockTags.AXE_MINEABLE, repairItems);
    }

    public static ItemComponent<?>[] hoe(RegistryEntryLookup<Block> blocks, ToolMaterial material, double attackDamage, double attackSpeed, RegistryEntryList<Item> repairItems) {
        return tool(blocks, material, attackDamage, attackSpeed, BlockTags.HOE_MINEABLE, repairItems);
    }

    @Override
    public ItemComponentType<DamageableItemComponent> type() {
        return ItemComponentTypes.DAMAGEABLE;
    }

    private static ItemComponent<?>[] tool(RegistryEntryLookup<Block> blocks, ToolMaterial material, double attackDamage, double attackSpeed, TagKey<Block> mineableBlocks, RegistryEntryList<Item> repairItems) {
        double realAttackDamage = attackDamage + material.attackDamageBonus();
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            DamageableItemComponent.of(material.durability()),
            ToolItemComponent.of(blocks, material, mineableBlocks),
            WeaponItemComponent.of(
                2,
                realAttackDamage,
                attackSpeed
            ),
            EnchantableItemComponent.of(material),
            RepairableItemComponent.of(repairItems)
        };
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
