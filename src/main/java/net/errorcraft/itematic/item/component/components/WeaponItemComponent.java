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
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;

public record WeaponItemComponent(int itemDamagePerAttack, boolean canDisableBlocking, boolean maySmash, WeaponAttackDamageDataComponent attackDamage, double attackSpeed) implements ItemComponent<WeaponItemComponent> {
    public static final Codec<WeaponItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.NON_NEGATIVE_INT.optionalFieldOf("item_damage_per_attack", 1).forGetter(WeaponItemComponent::itemDamagePerAttack),
        Codec.BOOL.optionalFieldOf("can_disable_blocking", false).forGetter(WeaponItemComponent::canDisableBlocking),
        Codec.BOOL.optionalFieldOf("may_smash", false).forGetter(WeaponItemComponent::maySmash),
        WeaponAttackDamageDataComponent.CODEC.fieldOf("attack_damage").forGetter(WeaponItemComponent::attackDamage),
        ItematicCodecs.NON_NEGATIVE_DOUBLE.fieldOf("attack_speed").forGetter(WeaponItemComponent::attackSpeed)
    ).apply(instance, WeaponItemComponent::new));
    private static final MaceItem DUMMY = new MaceItem(new Item.Settings());

    public static WeaponItemComponent of(int damagePerHit, boolean canDisableBlocking, double attackDamage, double attackSpeed) {
        return new WeaponItemComponent(
            damagePerHit,
            canDisableBlocking,
            false,
            new WeaponAttackDamageDataComponent(List.of(), attackDamage),
            attackSpeed
        );
    }

    public static WeaponItemComponent ofSmashing(int damagePerHit, double attackDamage, double attackSpeed) {
        return new WeaponItemComponent(
            damagePerHit,
            false,
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
        builder.add(DataComponentTypes.WEAPON, new WeaponComponent(this.itemDamagePerAttack, this.canDisableBlocking));
        builder.add(ItematicDataComponentTypes.WEAPON_ATTACK_DAMAGE, this.attackDamage);
        builder.add(ItematicDataComponentTypes.ATTACK_SPEED_MULTIPLIER, this.attackSpeed);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        WeaponAttackDamageDataComponent weaponAttackDamage = stack.get(ItematicDataComponentTypes.WEAPON_ATTACK_DAMAGE);
        if (weaponAttackDamage != null) {
            tooltip.add(
                Text.translatable(
                    "attribute.modifier.equals.0",
                    AttributeModifiersComponent.DECIMAL_FORMAT.format(weaponAttackDamage.defaultDamage()),
                    Text.translatable(EntityAttributes.ATTACK_DAMAGE.value().getTranslationKey())
                ).formatted(Formatting.DARK_GREEN)
            );
        }
        tooltip.add(
            Text.translatable(
                "attribute.modifier.equals.0",
                AttributeModifiersComponent.DECIMAL_FORMAT.format(this.attackSpeed),
                Text.translatable(EntityAttributes.ATTACK_SPEED.value().getTranslationKey())
            ).formatted(Formatting.DARK_GREEN)
        );
    }
}
