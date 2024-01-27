package net.errorcraft.itematic.mixin.entity.player;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class HungerManagerExtender {
    @Shadow
    public abstract void add(int food, float saturationModifier);

    @Inject(
        method = "eat",
        at = @At("HEAD"),
        cancellable = true
    )
    public void eatUseItemComponent(Item item, ItemStack stack, CallbackInfo info) {
        info.cancel();
        if (item == null) {
            return;
        }

        item.itematic$getComponent(ItemComponentTypes.FOOD)
            .ifPresent(food -> this.add(food.nutrition(), food.saturationModifier()));
    }
}
