package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;

public record PotionItemComponent() implements ItemComponent {
    public static final Codec<PotionItemComponent> CODEC = Codec.unit(new PotionItemComponent());

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.POTION;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void finishUsing(World world, LivingEntity user, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (!world.isClient()) {
            this.applyEffects(user, stack);
        }
        user.emitGameEvent(GameEvent.DRINK);
    }

    private void applyEffects(LivingEntity target, ItemStack stack) {
        PlayerEntity player = target instanceof PlayerEntity playerEntity ? playerEntity : null;
        List<StatusEffectInstance> effectInstances = PotionUtil.getPotionEffects(stack);
        for (StatusEffectInstance effectInstance : effectInstances) {
            if (effectInstance.getEffectType().value().isInstant()) {
                effectInstance.getEffectType().value().applyInstantEffect(player, player, target, effectInstance.getAmplifier(), 1.0d);
                continue;
            }
            target.addStatusEffect(new StatusEffectInstance(effectInstance));
        }
    }
}
