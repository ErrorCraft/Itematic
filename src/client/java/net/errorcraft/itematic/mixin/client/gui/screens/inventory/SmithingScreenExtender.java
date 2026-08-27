package net.errorcraft.itematic.mixin.client.gui.screens.inventory;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.SmithingTemplateProviderItemBehavior;
import net.errorcraft.itematic.world.item.smithing.template.SmithingTemplate;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.equipment.Equippable;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenExtender extends ItemCombinerScreen<SmithingMenu> {
    public SmithingScreenExtender(SmithingMenu menu, Inventory inventory, Component title, Identifier menuResource) {
        super(menu, inventory, title, menuResource);
    }

    @Inject(
        method = "containerTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/SmithingScreen;getTemplateItem()Ljava/util/Optional;"
        )
    )
    private void storeSmithingTemplate(CallbackInfo info, @Share("smithingTemplate") LocalRef<Optional<SmithingTemplate>> smithingTemplate) {
        smithingTemplate.set(this.menu.getSlot(0)
            .getItem()
            .itematic$getBehavior(ItemBehaviorType.SMITHING_TEMPLATE_PROVIDER)
            .map(SmithingTemplateProviderItemBehavior::template)
        );
    }

    @Redirect(
        method = "containerTick",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;map(Ljava/util/function/Function;)Ljava/util/Optional;",
            ordinal = 0
        )
    )
    private Optional<List<Identifier>> getEmptyBaseSlotTexturesFromSmithingTemplate(Optional<SmithingTemplateItem> instance, Function<SmithingTemplateItem, List<Identifier>> mapper, @Share("smithingTemplate") LocalRef<Optional<SmithingTemplate>> smithingTemplate) {
        return smithingTemplate.get().map(SmithingTemplate::emptyBaseSlotTextures);
    }

    @Redirect(
        method = "containerTick",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;map(Ljava/util/function/Function;)Ljava/util/Optional;",
            ordinal = 1
        )
    )
    private Optional<List<Identifier>> getEmptyAdditionsSlotTexturesFromSmithingTemplate(Optional<SmithingTemplateItem> instance, Function<SmithingTemplateItem, List<Identifier>> mapper, @Share("smithingTemplate") LocalRef<Optional<SmithingTemplate>> smithingTemplate) {
        return smithingTemplate.get().map(SmithingTemplate::emptyAdditionsSlotTextures);
    }

    @WrapOperation(
        method = "updateArmorStandPreview",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
        )
    )
    @Nullable
    private Object alsoCheckEquipmentItemBehavior(ItemStack instance, DataComponentType<Equippable> type, Operation<Object> original) {
        if (!instance.itematic$hasBehavior(ItemBehaviorType.EQUIPMENT)) {
            return null;
        }

        return original.call(instance, type);
    }

    @ModifyConstant(
        method = "extractOnboardingTooltips",
        constant = @Constant(
            classValue = SmithingTemplateItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfSmithingTemplateItemUseItemBehavior(Object reference, Class<SmithingTemplateItem> clazz, @Local(name = "template") ItemStack template, @Share("smithingTemplate") LocalRef<SmithingTemplate> smithingTemplateReference) {
        Optional<SmithingTemplate> smithingTemplate = template.itematic$getBehavior(ItemBehaviorType.SMITHING_TEMPLATE_PROVIDER)
            .map(SmithingTemplateProviderItemBehavior::template);
        smithingTemplate.ifPresent(smithingTemplateReference::set);
        return smithingTemplate.isPresent();
    }

    @ModifyVariable(
        method = "extractOnboardingTooltips",
        at = @At("LOAD"),
        ordinal = 0
    )
    @Nullable
    private Item castToSmithingTemplateItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "extractOnboardingTooltips",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/SmithingTemplateItem;getBaseSlotDescription()Lnet/minecraft/network/chat/Component;"
        )
    )
    private Component getBaseSlotDescriptionUseSmithingTemplate(SmithingTemplateItem instance, @Share("smithingTemplate") LocalRef<SmithingTemplate> smithingTemplateReference) {
        return smithingTemplateReference.get().baseSlotDescription();
    }

    @Redirect(
        method = "extractOnboardingTooltips",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/SmithingTemplateItem;getAdditionSlotDescription()Lnet/minecraft/network/chat/Component;"
        )
    )
    private Component getAdditionsSlotDescriptionUseSmithingTemplate(SmithingTemplateItem instance, @Share("smithingTemplate") LocalRef<SmithingTemplate> smithingTemplateReference) {
        return smithingTemplateReference.get().additionsSlotDescription();
    }
}
