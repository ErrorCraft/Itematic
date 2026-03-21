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
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.type.AttributeModifiersComponent;
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

public record WeaponItemComponent(int damagePerHit, WeaponAttackDamageDataComponent attackDamage, double attackSpeed, boolean maySmash) implements ItemComponent<WeaponItemComponent> {
    public static final Codec<WeaponItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.NON_NEGATIVE_INT.optionalFieldOf("damage_per_hit", 1).forGetter(WeaponItemComponent::damagePerHit),
        WeaponAttackDamageDataComponent.CODEC.fieldOf("attack_damage").forGetter(WeaponItemComponent::attackDamage),
        ItematicCodecs.NON_NEGATIVE_DOUBLE.fieldOf("attack_speed").forGetter(WeaponItemComponent::attackSpeed),
        Codec.BOOL.optionalFieldOf("may_smash", false).forGetter(WeaponItemComponent::maySmash)
    ).apply(instance, WeaponItemComponent::new));
    private static final MaceItem DUMMY = new MaceItem(new Item.Settings());

    public static WeaponItemComponent of(int damagePerHit, double attackDamage, double attackSpeed) {
        return new WeaponItemComponent(
            damagePerHit,
            new WeaponAttackDamageDataComponent(List.of(), attackDamage),
            attackSpeed,
            false
        );
    }

    public static WeaponItemComponent ofSmashing(int damagePerHit, double attackDamage, double attackSpeed) {
        return new WeaponItemComponent(
            damagePerHit,
            new WeaponAttackDamageDataComponent(List.of(), attackDamage),
            attackSpeed,
            true
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
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker, ItemStackExchanger stackExchanger) {
        if (this.maySmash && !DUMMY.postHit(stack, target, attacker)) {
            return false;
        }

        if (!(attacker.getWorld() instanceof ServerWorld serverWorld)) {
            return true;
        }


        NewActionContext context = NewActionContext.builder(serverWorld)
            .stackExchanger(stackExchanger)
            .add(LootContextParameters.THIS_ENTITY, attacker)
            .add(LootContextParameters.ORIGIN, attacker.getPos())
            .add(ItematicContextParameters.TARGET_ENTITY, target)
            .add(ItematicContextParameters.INTERACTED_POSITION, target.getPos())
            .add(LootContextParameters.TOOL, stack)
            .add(ItematicContextParameters.EQUIPMENT_SLOT, EquipmentSlot.MAINHAND)
            .build();
        stack.itematic$invokeEvent(ItemEvents.USE_WEAPON, context);
        stack.itematic$damage(this.damagePerHit, context);
        return true;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
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
