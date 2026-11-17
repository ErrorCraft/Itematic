package net.errorcraft.itematic.mixin.world.food;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.component.UseableListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FoodProperties.class)
public abstract class FoodPropertiesExtender implements UseableListener.Finished {
    @Shadow
    public abstract void onConsume(Level level, LivingEntity user, ItemStack stack, Consumable consumable);

    @WrapOperation(
        method = "onConsume",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
            ordinal = 0
        )
    )
    private void doNotPlaySound(Level instance, Entity except, double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch, Operation<Void> original) {}

    @Override
    public void itematic$onFinishedUsing(ItemStack stack, ItemStackExchanger stackExchanger, Level level, LivingEntity user) {
        this.onConsume(level, user, stack, Consumables.DEFAULT_FOOD);
        ActionContext context = ActionContext.builder(level)
            .stackExchanger(stackExchanger)
            .add(LootContextParams.THIS_ENTITY, user)
            .add(LootContextParams.ORIGIN, user.position())
            .add(LootContextParams.TOOL, stack)
            .add(ItematicContextKeys.HAND, user.getUsedItemHand())
            .build();
        stack.itematic$invokeEvent(ItemEvent.EAT_ITEM, context);
    }
}
