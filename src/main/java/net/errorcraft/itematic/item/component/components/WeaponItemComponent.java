package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentSet;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.Codecs;

public record WeaponItemComponent(int damagePerHit, double attackDamage, double attackSpeed) implements ItemComponent<WeaponItemComponent> {
    public static final Codec<WeaponItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.NONNEGATIVE_INT.optionalFieldOf("damage_per_hit", 1).forGetter(WeaponItemComponent::damagePerHit),
        Codec.DOUBLE.fieldOf("attack_damage").forGetter(WeaponItemComponent::attackDamage),
        Codec.DOUBLE.fieldOf("attack_speed").forGetter(WeaponItemComponent::attackSpeed)
    ).apply(instance, WeaponItemComponent::new));

    @Override
    public ItemComponentType<WeaponItemComponent> type() {
        return ItemComponentTypes.WEAPON;
    }

    @Override
    public Codec<WeaponItemComponent> codec() {
        return CODEC;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker, ItemStackConsumer resultStackConsumer) {
        if (!(attacker.getWorld() instanceof ServerWorld serverWorld)) {
            return true;
        }
        ActionContext context = ActionContext.builder(serverWorld, stack, resultStackConsumer, EquipmentSlot.MAINHAND)
            .entityPosition(ActionContextParameter.THIS, attacker)
            .entityPosition(ActionContextParameter.TARGET, target)
            .build();
        stack.itematic$invokeEvent(ItemEvents.USE_WEAPON, context);
        stack.itematic$damage(this.damagePerHit, context);
        return true;
    }

    @Override
    public void addAttributeModifiers(AttributeModifiersComponent.Builder builder, ItemComponentSet components) {
        builder.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(Item.ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.attackDamage, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
            .add(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(Item.ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", this.attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND);
    }

    public static WeaponItemComponent of(int damagePerHit, double attackDamage, double attackSpeed) {
        return new WeaponItemComponent(damagePerHit, attackDamage, attackSpeed);
    }
}
