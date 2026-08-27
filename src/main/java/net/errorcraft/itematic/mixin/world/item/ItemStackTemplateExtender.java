package net.errorcraft.itematic.mixin.world.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.access.world.item.ItemInstanceAccess;
import net.errorcraft.itematic.util.ItematicUtil;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.ItemStackTemplates;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ItemStackTemplate.class)
public class ItemStackTemplateExtender implements ItemInstanceAccess {
    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    @Final
    private Holder<Item> item;

    @WrapOperation(
        method = "lambda$static$1",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/ItemStackTemplate;"
        )
    )
    private static ItemStackTemplate newItemStackTemplateUseHolder(Item item, Operation<ItemStackTemplate> original, Holder<Item> itemHolder) {
        return ItemStackTemplates.of(itemHolder);
    }

    @Inject(
        method = {
            "<init>(Lnet/minecraft/world/item/Item;)V",
            "<init>(Lnet/minecraft/world/item/Item;I)V",
            "<init>(Lnet/minecraft/world/item/Item;Lnet/minecraft/core/component/DataComponentPatch;)V"
        },
        at = @At("TAIL")
    )
    private static void logWarningForDirectItems(CallbackInfo info) {
        LOGGER.warn(ItematicUtil.stackTraceMessage("Tried to create an item stack template from an item value directly. This is no longer supported and should be modified to use a holder instead."));
    }

    @ModifyArg(
        method = "get",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/component/DataComponentPatch;get(Lnet/minecraft/core/component/DataComponentGetter;Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
        )
    )
    private DataComponentGetter useDataComponentsOnItemInstead(DataComponentGetter prototype) {
        return this.item.value().components();
    }

    @Override
    public <T extends ItemBehavior<T>> boolean itematic$hasBehavior(ItemBehaviorType<T> type) {
        return this.item.value().itematic$hasBehavior(type);
    }

    @Override
    public <T extends ItemBehavior<T>> Optional<T> itematic$getBehavior(ItemBehaviorType<T> type) {
        return this.item.value().itematic$getBehavior(type);
    }

    @Override
    public boolean itematic$invokeEvent(ItemEvent event, ActionContext context) {
        try {
            return this.item.value().itematic$invokeEvent(event, context);
        } catch (StackOverflowError e) {
            return false;
        }
    }

    @Override
    public boolean itematic$hasEventListener(ItemEvent event) {
        return this.item.value().itematic$hasEventListener(event);
    }
}
