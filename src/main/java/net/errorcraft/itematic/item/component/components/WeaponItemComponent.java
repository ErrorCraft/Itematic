package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.MutableActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

public record WeaponItemComponent(int damage) implements ItemComponent {
    public static final Codec<WeaponItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("damage").forGetter(WeaponItemComponent::damage)
    ).apply(instance, WeaponItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.WEAPON;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker.getWorld() instanceof ServerWorld serverWorld) {
            ActionContext context = MutableActionContext.stackUsage(serverWorld, stack, Hand.MAIN_HAND)
                .entityPosition(ActionContextParameter.THIS, attacker)
                .entityPosition(ActionContextParameter.TARGET, target);
            stack.invokeEvent(ItemEvents.USE_WEAPON, context);
        }
        stack.damage(this.damage, attacker);
        return true;
    }
}
