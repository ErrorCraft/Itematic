package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.component.type.WeaponAttackDamageDataComponent;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;

public record WeaponItemComponent(int itemDamagePerAttack, float disableBlockingForSeconds, boolean maySmash, WeaponAttackDamageDataComponent attackDamage, double attackSpeed) implements ItemComponent<WeaponItemComponent> {
    public static final Codec<WeaponItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.NON_NEGATIVE_INT.optionalFieldOf("item_damage_per_attack", 1).forGetter(WeaponItemComponent::itemDamagePerAttack),
        Codecs.NON_NEGATIVE_FLOAT.optionalFieldOf("disable_blocking_for_seconds", 0.0f).forGetter(WeaponItemComponent::disableBlockingForSeconds),
        Codec.BOOL.optionalFieldOf("may_smash", false).forGetter(WeaponItemComponent::maySmash),
        WeaponAttackDamageDataComponent.CODEC.fieldOf("attack_damage").forGetter(WeaponItemComponent::attackDamage),
        ItematicCodecs.NON_NEGATIVE_DOUBLE.fieldOf("attack_speed").forGetter(WeaponItemComponent::attackSpeed)
    ).apply(instance, WeaponItemComponent::new));
    private static final MaceItem DUMMY = new MaceItem(new Item.Settings());

    public static WeaponItemComponent of(int damagePerHit, float disableBlockingForSeconds, double attackDamage, double attackSpeed) {
        return new WeaponItemComponent(
            damagePerHit,
            disableBlockingForSeconds,
            false,
            new WeaponAttackDamageDataComponent(List.of(), attackDamage),
            attackSpeed
        );
    }

    public static WeaponItemComponent ofSmashing(int damagePerHit, double attackDamage, double attackSpeed) {
        return new WeaponItemComponent(
            damagePerHit,
            0.0f,
            true,
            new WeaponAttackDamageDataComponent(List.of(), attackDamage),
            attackSpeed
        );
    }

    @Override
    public ItemComponentType<WeaponItemComponent> type() {
        return ItemComponentTypes.WEAPON;
    }

    @Override
    public Codec<WeaponItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker, ItemStackExchanger stackExchanger) {
        if (this.maySmash) {
            DUMMY.postHit(stack, target, attacker);
        }

        if (!(attacker.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }

        ActionContext context = ActionContext.builder(serverWorld)
            .stackExchanger(stackExchanger)
            .add(LootContextParameters.THIS_ENTITY, attacker)
            .add(LootContextParameters.ORIGIN, attacker.getPos())
            .add(ItematicContextParameters.TARGET_ENTITY, target)
            .add(ItematicContextParameters.INTERACTED_POSITION, target.getPos())
            .add(LootContextParameters.TOOL, stack)
            .add(ItematicContextParameters.EQUIPMENT_SLOT, EquipmentSlot.MAINHAND)
            .build();
        stack.itematic$invokeEvent(ItemEvents.USE_WEAPON, context);
        WeaponComponent weapon = stack.get(DataComponentTypes.WEAPON);
        if (weapon != null) {
            stack.itematic$damage(weapon.itemDamagePerAttack(), context);
        }
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.WEAPON, new WeaponComponent(this.itemDamagePerAttack, this.disableBlockingForSeconds));
        builder.add(ItematicDataComponentTypes.WEAPON_ATTACK_DAMAGE, this.attackDamage);
        builder.add(ItematicDataComponentTypes.ATTACK_SPEED_MULTIPLIER, this.attackSpeed);
    }
}
