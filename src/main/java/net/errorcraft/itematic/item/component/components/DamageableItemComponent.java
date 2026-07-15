package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.block.BlockKeys;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.weapon.melee.MeleeWeaponComponents;
import net.errorcraft.itematic.item.weapon.melee.component.KineticMeleeWeapon;
import net.errorcraft.itematic.item.weapon.melee.component.PiercingMeleeWeapon;
import net.errorcraft.itematic.sound.SoundEventKeys;
import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.*;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.consume.UseAction;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.SwingAnimationType;
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
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            DamageableItemComponent.of(material.durability()),
            ToolItemComponent.builder(2)
                .preventCreativeDestruction()
                .rule(ToolComponent.Rule.ofAlwaysDropping(RegistryEntryList.of(blocks.getOrThrow(BlockKeys.COBWEB)), 15.0f))
                .rule(ToolComponent.Rule.of(blocks.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5f))
                .build(),
            WeaponItemComponent.builder(1, 4.0d + material.attackDamageBonus(), 0.4d)
                .build(),
            EnchantableItemComponent.of(material),
            RepairableItemComponent.of(repairItems)
        };
    }

    public static ItemComponent<?>[] spear(ToolMaterial material, RegistryEntryLookup<DamageType> damageTypes, float attackDuration, float damageMultiplier, float delay, float dismountTime, float dismountSpeedThreshold, float knockbackTime, float knockbackSpeedThreshold, float damageTime, float damageSpeedThreshold, RegistryEntryList<Item> repairItems, RegistryEntryLookup<SoundEvent> soundEvents) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            DamageableItemComponent.of(material.durability()),
            UseableItemComponent.builder()
                .useIndefinitely()
                .animation(UseAction.SPEAR)
                .effects(new UseEffectsComponent(true, 1.0f))
                .build(),
            WeaponItemComponent.builder(1, material.attackDamageBonus(), 1 / (4 * attackDuration))
                .type(
                    MeleeWeaponComponents.KINETIC,
                    KineticMeleeWeapon.of(new KineticWeaponComponent(
                        2.0f,
                        4.5f,
                        0.25f,
                        10,
                        (int)(delay * SharedConstants.TICKS_PER_SECOND),
                        KineticWeaponComponent.Condition.ofMinSpeed(
                            (int)(dismountTime * SharedConstants.TICKS_PER_SECOND),
                            dismountSpeedThreshold
                        ),
                        KineticWeaponComponent.Condition.ofMinSpeed(
                            (int)(knockbackTime * SharedConstants.TICKS_PER_SECOND),
                            knockbackSpeedThreshold
                        ),
                        KineticWeaponComponent.Condition.ofMinRelativeSpeed(
                            (int)(damageTime * SharedConstants.TICKS_PER_SECOND),
                            damageSpeedThreshold
                        ),
                        0.38f,
                        damageMultiplier,
                        Optional.of(material == ToolMaterial.WOOD
                            ? soundEvents.getOrThrow(SoundEventKeys.SPEAR_WOOD_USE)
                            : soundEvents.getOrThrow(SoundEventKeys.SPEAR_USE)
                        ),
                        Optional.of(material == ToolMaterial.WOOD
                            ? soundEvents.getOrThrow(SoundEventKeys.SPEAR_WOOD_HIT)
                            : soundEvents.getOrThrow(SoundEventKeys.SPEAR_HIT)
                        )
                    ))
                )
                .type(
                    MeleeWeaponComponents.PIERCING,
                    PiercingMeleeWeapon.of(new PiercingWeaponComponent(
                        2.0F,
                        4.5F,
                        0.25F,
                        true,
                        false,
                        Optional.of(material == ToolMaterial.WOOD
                            ? soundEvents.getOrThrow(SoundEventKeys.SPEAR_WOOD_ATTACK)
                            : soundEvents.getOrThrow(SoundEventKeys.SPEAR_ATTACK)
                        ),
                        Optional.of(material == ToolMaterial.WOOD
                            ? soundEvents.getOrThrow(SoundEventKeys.SPEAR_WOOD_HIT)
                            : soundEvents.getOrThrow(SoundEventKeys.SPEAR_HIT)
                        )
                    ))
                )
                .damageType(damageTypes.getOrThrow(DamageTypes.SPEAR))
                .swingAnimation(new SwingAnimationComponent(SwingAnimationType.STAB, (int)(attackDuration * SharedConstants.TICKS_PER_SECOND)))
                .minimumAttackCharge(1.0f)
                .build(),
            EnchantableItemComponent.of(material),
            RepairableItemComponent.of(repairItems)
        };
    }

    public static ItemComponent<?>[] shovel(RegistryEntryLookup<Block> blocks, ToolMaterial material, RegistryEntryList<Item> repairItems) {
        return tool(blocks, material, 0.0f, 2.5d, 0.25d, BlockTags.SHOVEL_MINEABLE, repairItems);
    }

    public static ItemComponent<?>[] pickaxe(RegistryEntryLookup<Block> blocks, ToolMaterial material, RegistryEntryList<Item> repairItems) {
        return tool(blocks, material, 0.0f, 2.0d, 0.3d, BlockTags.PICKAXE_MINEABLE, repairItems);
    }

    public static ItemComponent<?>[] axe(RegistryEntryLookup<Block> blocks, ToolMaterial material, double attackDamage, double attackSpeed, RegistryEntryList<Item> repairItems) {
        return tool(blocks, material, 5.0f, attackDamage, attackSpeed, BlockTags.AXE_MINEABLE, repairItems);
    }

    public static ItemComponent<?>[] hoe(RegistryEntryLookup<Block> blocks, ToolMaterial material, double attackDamage, double attackSpeed, RegistryEntryList<Item> repairItems) {
        return tool(blocks, material, 0.0f, attackDamage, attackSpeed, BlockTags.HOE_MINEABLE, repairItems);
    }

    private static ItemComponent<?>[] tool(RegistryEntryLookup<Block> blocks, ToolMaterial material, float disableBlockingForSeconds, double baseAttackDamage, double attackSpeed, TagKey<Block> mineableBlocks, RegistryEntryList<Item> repairItems) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            DamageableItemComponent.of(material.durability()),
            ToolItemComponent.of(blocks, material, mineableBlocks),
            WeaponItemComponent.builder(2, baseAttackDamage + material.attackDamageBonus(), attackSpeed)
                .disableBlockingForSeconds(disableBlockingForSeconds)
                .build(),
            EnchantableItemComponent.of(material),
            RepairableItemComponent.of(repairItems)
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
        this.breakSound.ifPresent(breakSound -> builder.add(DataComponentTypes.BREAK_SOUND, breakSound));
    }

    public int maximumDamage(ItemStack stack) {
        return stack.getMaxDamage() - (this.preserveItem ? 1 : 0);
    }

    public boolean isUsable(ItemStack stack) {
        return stack.getDamage() < this.maximumDamage(stack);
    }
}
