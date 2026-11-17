package net.errorcraft.itematic.mixin.client.gui.screens.inventory;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.client.resources.item.smithing.SmithingTemplateDisplay;
import net.errorcraft.itematic.client.resources.item.smithing.SmithingTemplateDisplayManager;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenExtender extends ItemCombinerScreen<SmithingMenu> {
    public SmithingScreenExtender(SmithingMenu menu, Inventory inventory, Component title, Identifier menuResource) {
        super(menu, inventory, title, menuResource);
    }

    @Unique
    private SmithingTemplateDisplayManager smithingTemplateDisplays;

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void setSmithingTemplateDisplays(SmithingMenu menu, Inventory inventory, Component title, CallbackInfo info) {
        this.smithingTemplateDisplays = this.minecraft.itematic$smithingTemplateDisplays();
    }

    @Inject(
        method = "containerTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/SmithingScreen;getTemplateItem()Ljava/util/Optional;"
        )
    )
    private void storeSmithingTemplate(CallbackInfo info, @Share("smithingTemplate") LocalRef<@Nullable SmithingTemplateDisplay> smithingTemplateReference) {
        Identifier smithingTemplateId = this.menu.getSlot(0)
            .getItem()
            .get(ItematicDataComponents.SMITHING_TEMPLATE_DISPLAY);
        if (smithingTemplateId == null) {
            return;
        }

        SmithingTemplateDisplay smithingTemplateDisplay = this.smithingTemplateDisplays.get(smithingTemplateId);
        smithingTemplateReference.set(smithingTemplateDisplay);
    }

    @WrapOperation(
        method = "containerTick",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;map(Ljava/util/function/Function;)Ljava/util/Optional;",
            ordinal = 0
        )
    )
    private Optional<List<Identifier>> getBaseSlotEmptyTexturesUseDataComponent(Optional<SmithingTemplateItem> instance, Function<SmithingTemplateItem, List<Identifier>> mapper, Operation<Optional<List<Identifier>>> original, @Share("smithingTemplate") LocalRef<@Nullable SmithingTemplateDisplay> smithingTemplateReference) {
        return Optional.ofNullable(smithingTemplateReference.get())
            .map(SmithingTemplateDisplay::baseSlot)
            .map(SmithingTemplateDisplay.Slot::emptyTextures);
    }

    @WrapOperation(
        method = "containerTick",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;map(Ljava/util/function/Function;)Ljava/util/Optional;",
            ordinal = 1
        )
    )
    private Optional<List<Identifier>> getAdditionsSlotEmptyTexturesUseDataComponent(Optional<SmithingTemplateItem> instance, Function<SmithingTemplateItem, List<Identifier>> mapper, Operation<Optional<List<Identifier>>> original, @Share("smithingTemplate") LocalRef<@Nullable SmithingTemplateDisplay> smithingTemplateReference) {
        return Optional.ofNullable(smithingTemplateReference.get())
            .map(SmithingTemplateDisplay::additionsSlot)
            .map(SmithingTemplateDisplay.Slot::emptyTextures);
    }

    @ModifyConstant(
        method = "extractOnboardingTooltips",
        constant = @Constant(
            classValue = SmithingTemplateItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfSmithingTemplateItemUseDataComponent(Object reference, Class<SmithingTemplateItem> clazz, @Local(name = "template") ItemStack template, @Share("smithingTemplate") LocalRef<SmithingTemplateDisplay> smithingTemplateReference) {
        Identifier smithingTemplateId = template.get(ItematicDataComponents.SMITHING_TEMPLATE_DISPLAY);
        if (smithingTemplateId == null) {
            return false;
        }

        SmithingTemplateDisplay smithingTemplateDisplay = this.smithingTemplateDisplays.get(smithingTemplateId);
        if (smithingTemplateDisplay == null) {
            return false;
        }

        smithingTemplateReference.set(smithingTemplateDisplay);
        return true;
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

    @WrapOperation(
        method = "extractOnboardingTooltips",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/SmithingTemplateItem;getBaseSlotDescription()Lnet/minecraft/network/chat/Component;"
        )
    )
    private Component getBaseSlotDescriptionUseDataComponent(SmithingTemplateItem instance, Operation<Component> original, @Share("smithingTemplate") LocalRef<SmithingTemplateDisplay> smithingTemplateReference) {
        return smithingTemplateReference.get().baseSlot().description();
    }

    @WrapOperation(
        method = "extractOnboardingTooltips",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/SmithingTemplateItem;getAdditionSlotDescription()Lnet/minecraft/network/chat/Component;"
        )
    )
    private Component getAdditionsSlotDescriptionUseDataComponent(SmithingTemplateItem instance, Operation<Component> original, @Share("smithingTemplate") LocalRef<SmithingTemplateDisplay> smithingTemplateReference) {
        return smithingTemplateReference.get().additionsSlot().description();
    }
}
