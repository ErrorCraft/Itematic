package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.references.BlockIds;
import net.errorcraft.itematic.sound.SoundEventKeys;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.MeleeWeaponComponents;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.component.KineticMeleeWeapon;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.component.PiercingMeleeWeapon;
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

public record DamageableItemBehavior(int durability, Optional<Holder<SoundEvent>> breakSound, boolean preserveItem) implements ItemBehavior<DamageableItemBehavior> {
    public static final Codec<DamageableItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("durability").forGetter(DamageableItemBehavior::durability),
        SoundEvent.CODEC.optionalFieldOf("break_sound").forGetter(DamageableItemBehavior::breakSound),
        Codec.BOOL.optionalFieldOf("preserve_item", false).forGetter(DamageableItemBehavior::preserveItem)
    ).apply(instance, DamageableItemBehavior::new));

    public static DamageableItemBehavior of(int durability) {
        return new DamageableItemBehavior(durability, Optional.empty(), false);
    }

    public static DamageableItemBehavior of(int durability, Holder<SoundEvent> breakSound) {
        return new DamageableItemBehavior(durability, Optional.of(breakSound), false);
    }

    public static DamageableItemBehavior ofPreserved(int durability) {
        return new DamageableItemBehavior(durability, Optional.empty(), true);
    }

    public static ItemBehavior<?>[] sword(HolderGetter<Block> blocks, ToolMaterial material, HolderSet<Item> repairItems) {
        return new ItemBehavior<?>[] {
            StackableItemBehavior.of(1),
            DamageableItemBehavior.of(material.durability()),
            ToolItemBehavior.builder(2)
                .preventCreativeDestruction()
                .rule(Tool.Rule.minesAndDrops(HolderSet.direct(blocks.getOrThrow(BlockIds.COBWEB)), 15.0f))
                .rule(Tool.Rule.overrideSpeed(blocks.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5f))
                .build(),
            WeaponItemBehavior.builder(1, 4.0d + material.attackDamageBonus(), 0.4d)
                .build(),
            EnchantableItemBehavior.of(material),
            RepairableItemBehavior.of(repairItems)
        };
    }

    public static ItemBehavior<?>[] spear(ToolMaterial material, HolderGetter<DamageType> damageTypes, float attackDuration, float damageMultiplier, float delay, float dismountTime, float dismountSpeedThreshold, float knockbackTime, float knockbackSpeedThreshold, float damageTime, float damageSpeedThreshold, HolderSet<Item> repairItems, HolderGetter<SoundEvent> soundEvents) {
        return new ItemBehavior<?>[] {
            StackableItemBehavior.of(1),
            DamageableItemBehavior.of(material.durability()),
            UseableItemBehavior.builder()
                .useIndefinitely()
                .animation(ItemUseAnimation.SPEAR)
                .effects(new UseEffects(
                    true,
                    false,
                    1.0f
                ))
                .build(),
            WeaponItemBehavior.builder(1, material.attackDamageBonus(), 1 / (4 * attackDuration))
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
            EnchantableItemBehavior.of(material),
            RepairableItemBehavior.of(repairItems)
        };
    }

    public static ItemBehavior<?>[] shovel(HolderGetter<Block> blocks, ToolMaterial material, HolderSet<Item> repairItems) {
        return tool(blocks, material, 0.0f, 2.5d, 0.25d, BlockTags.MINEABLE_WITH_SHOVEL, repairItems);
    }

    public static ItemBehavior<?>[] pickaxe(HolderGetter<Block> blocks, ToolMaterial material, HolderSet<Item> repairItems) {
        return tool(blocks, material, 0.0f, 2.0d, 0.3d, BlockTags.MINEABLE_WITH_PICKAXE, repairItems);
    }

    public static ItemBehavior<?>[] axe(HolderGetter<Block> blocks, ToolMaterial material, double attackDamage, double attackSpeed, HolderSet<Item> repairItems) {
        return tool(blocks, material, 5.0f, attackDamage, attackSpeed, BlockTags.MINEABLE_WITH_AXE, repairItems);
    }

    public static ItemBehavior<?>[] hoe(HolderGetter<Block> blocks, ToolMaterial material, double attackDamage, double attackSpeed, HolderSet<Item> repairItems) {
        return tool(blocks, material, 0.0f, attackDamage, attackSpeed, BlockTags.MINEABLE_WITH_HOE, repairItems);
    }

    private static ItemBehavior<?>[] tool(HolderGetter<Block> blocks, ToolMaterial material, float disableBlockingForSeconds, double baseAttackDamage, double attackSpeed, TagKey<Block> mineableBlocks, HolderSet<Item> repairItems) {
        return new ItemBehavior<?>[] {
            StackableItemBehavior.of(1),
            DamageableItemBehavior.of(material.durability()),
            ToolItemBehavior.of(blocks, material, mineableBlocks),
            WeaponItemBehavior.builder(2, baseAttackDamage + material.attackDamageBonus(), attackSpeed)
                .disableBlockingForSeconds(disableBlockingForSeconds)
                .build(),
            EnchantableItemBehavior.of(material),
            RepairableItemBehavior.of(repairItems)
        };
    }

    @Override
    public ItemBehaviorType<DamageableItemBehavior> type() {
        return ItemBehaviorType.DAMAGEABLE;
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
