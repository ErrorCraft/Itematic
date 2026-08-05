package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.weapon.melee.MeleeWeaponComponents;
import net.errorcraft.itematic.item.weapon.melee.component.KineticMeleeWeapon;
import net.errorcraft.itematic.item.weapon.melee.component.PiercingMeleeWeapon;
import net.errorcraft.itematic.references.BlockKeys;
import net.errorcraft.itematic.sound.SoundEventKeys;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record DamageableItemComponent(int durability, Optional<Holder<SoundEvent>> breakSound, boolean preserveItem) implements ItemComponent<DamageableItemComponent> {
    public static final Codec<DamageableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("durability").forGetter(DamageableItemComponent::durability),
        SoundEvent.CODEC.optionalFieldOf("break_sound").forGetter(DamageableItemComponent::breakSound),
        Codec.BOOL.optionalFieldOf("preserve_item", false).forGetter(DamageableItemComponent::preserveItem)
    ).apply(instance, DamageableItemComponent::new));

    public static DamageableItemComponent of(int durability) {
        return new DamageableItemComponent(durability, Optional.empty(), false);
    }

    public static DamageableItemComponent of(int durability, Holder<SoundEvent> breakSound) {
        return new DamageableItemComponent(durability, Optional.of(breakSound), false);
    }

    public static DamageableItemComponent ofPreserved(int durability) {
        return new DamageableItemComponent(durability, Optional.empty(), true);
    }

    public static ItemComponent<?>[] sword(HolderGetter<Block> blocks, ToolMaterial material, HolderSet<Item> repairItems) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            DamageableItemComponent.of(material.durability()),
            ToolItemComponent.builder(2)
                .preventCreativeDestruction()
                .rule(Tool.Rule.minesAndDrops(HolderSet.direct(blocks.getOrThrow(BlockKeys.COBWEB)), 15.0f))
                .rule(Tool.Rule.overrideSpeed(blocks.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5f))
                .build(),
            WeaponItemComponent.builder(1, 4.0d + material.attackDamageBonus(), 0.4d)
                .build(),
            EnchantableItemComponent.of(material),
            RepairableItemComponent.of(repairItems)
        };
    }

    public static ItemComponent<?>[] spear(ToolMaterial material, HolderGetter<DamageType> damageTypes, float attackDuration, float damageMultiplier, float delay, float dismountTime, float dismountSpeedThreshold, float knockbackTime, float knockbackSpeedThreshold, float damageTime, float damageSpeedThreshold, HolderSet<Item> repairItems, HolderGetter<SoundEvent> soundEvents) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            DamageableItemComponent.of(material.durability()),
            UseableItemComponent.builder()
                .useIndefinitely()
                .animation(ItemUseAnimation.SPEAR)
                .effects(new UseEffects(
                    true,
                    false,
                    1.0f
                ))
                .build(),
            WeaponItemComponent.builder(1, material.attackDamageBonus(), 1 / (4 * attackDuration))
                .type(
                    MeleeWeaponComponents.KINETIC,
                    KineticMeleeWeapon.of(new KineticWeapon(
                        10,
                        (int)(delay * SharedConstants.TICKS_PER_SECOND),
                        KineticWeapon.Condition.ofAttackerSpeed(
                            (int)(dismountTime * SharedConstants.TICKS_PER_SECOND),
                            dismountSpeedThreshold
                        ),
                        KineticWeapon.Condition.ofAttackerSpeed(
                            (int)(knockbackTime * SharedConstants.TICKS_PER_SECOND),
                            knockbackSpeedThreshold
                        ),
                        KineticWeapon.Condition.ofRelativeSpeed(
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
                    PiercingMeleeWeapon.of(new PiercingWeapon(
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
                .swingAnimation(new SwingAnimation(
                    SwingAnimationType.STAB,
                    (int)(attackDuration * SharedConstants.TICKS_PER_SECOND)
                ))
                .attackRange(new AttackRange(
                    2.0f,
                    4.5f,
                    2.0f,
                    6.5f,
                    0.125f,
                    0.5f
                ))
                .minimumAttackCharge(1.0f)
                .build(),
            EnchantableItemComponent.of(material),
            RepairableItemComponent.of(repairItems)
        };
    }

    public static ItemComponent<?>[] shovel(HolderGetter<Block> blocks, ToolMaterial material, HolderSet<Item> repairItems) {
        return tool(blocks, material, 0.0f, 2.5d, 0.25d, BlockTags.MINEABLE_WITH_SHOVEL, repairItems);
    }

    public static ItemComponent<?>[] pickaxe(HolderGetter<Block> blocks, ToolMaterial material, HolderSet<Item> repairItems) {
        return tool(blocks, material, 0.0f, 2.0d, 0.3d, BlockTags.MINEABLE_WITH_PICKAXE, repairItems);
    }

    public static ItemComponent<?>[] axe(HolderGetter<Block> blocks, ToolMaterial material, double attackDamage, double attackSpeed, HolderSet<Item> repairItems) {
        return tool(blocks, material, 5.0f, attackDamage, attackSpeed, BlockTags.MINEABLE_WITH_AXE, repairItems);
    }

    public static ItemComponent<?>[] hoe(HolderGetter<Block> blocks, ToolMaterial material, double attackDamage, double attackSpeed, HolderSet<Item> repairItems) {
        return tool(blocks, material, 0.0f, attackDamage, attackSpeed, BlockTags.MINEABLE_WITH_HOE, repairItems);
    }

    private static ItemComponent<?>[] tool(HolderGetter<Block> blocks, ToolMaterial material, float disableBlockingForSeconds, double baseAttackDamage, double attackSpeed, TagKey<Block> mineableBlocks, HolderSet<Item> repairItems) {
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
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.MAX_DAMAGE, this.durability);
        builder.set(DataComponents.DAMAGE, 0);
        this.breakSound.ifPresent(breakSound -> builder.set(DataComponents.BREAK_SOUND, breakSound));
    }

    public int maximumDamage(ItemStack stack) {
        return stack.getMaxDamage() - (this.preserveItem ? 1 : 0);
    }

    public boolean isUsable(ItemStack stack) {
        return stack.getDamageValue() < this.maximumDamage(stack);
    }
}
