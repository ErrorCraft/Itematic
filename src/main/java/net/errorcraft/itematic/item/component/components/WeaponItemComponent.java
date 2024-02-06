package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.MutableActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public record WeaponItemComponent(int damage, double attackDamage, double attackSpeed) implements ItemComponent {
    public static final Codec<WeaponItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("damage").forGetter(WeaponItemComponent::damage),
        Codec.DOUBLE.fieldOf("attack_damage").forGetter(WeaponItemComponent::attackDamage),
        Codec.DOUBLE.fieldOf("attack_speed").forGetter(WeaponItemComponent::attackSpeed)
    ).apply(instance, WeaponItemComponent::new));

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.WEAPON;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
        return CODEC;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker, ItemStackConsumer resultStackConsumer) {
        if (!(attacker.getWorld() instanceof ServerWorld serverWorld)) {
            return true;
        }
        ActionContext context = MutableActionContext.stackUsage(serverWorld, stack, resultStackConsumer, EquipmentSlot.MAINHAND)
            .entityPosition(ActionContextParameter.THIS, attacker)
            .entityPosition(ActionContextParameter.TARGET, target);
        stack.itematic$invokeEvent(ItemEvents.USE_WEAPON, context);
        stack.itematic$damage(this.damage, context);
        return true;
    }

    public static WeaponItemComponent of(int damage, double attackDamage, double attackSpeed) {
        return new WeaponItemComponent(damage, attackDamage, attackSpeed);
    }
}
