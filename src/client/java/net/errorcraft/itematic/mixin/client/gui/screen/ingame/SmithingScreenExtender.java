package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.SmithingTemplateItemComponent;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplate;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenExtender extends ForgingScreen<SmithingScreenHandler> {
    public SmithingScreenExtender(SmithingScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @Inject(
        method = "handledScreenTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;getSmithingTemplate()Ljava/util/Optional;"
        )
    )
    private void storeSmithingTemplate(CallbackInfo info, @Share("smithingTemplate") LocalRef<Optional<SmithingTemplate>> smithingTemplate) {
        smithingTemplate.set(this.handler.getSlot(0)
            .getStack()
            .itematic$getComponent(ItemComponentTypes.SMITHING_TEMPLATE)
            .map(SmithingTemplateItemComponent::template)
            .map(RegistryEntry::value)
        );
    }

    @Redirect(
        method = "handledScreenTick",
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
        method = "handledScreenTick",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;map(Ljava/util/function/Function;)Ljava/util/Optional;",
            ordinal = 1
        )
    )
    private Optional<List<Identifier>> getEmptyAdditionsSlotTexturesFromSmithingTemplate(Optional<SmithingTemplateItem> instance, Function<SmithingTemplateItem, List<Identifier>> mapper, @Share("smithingTemplate") LocalRef<Optional<SmithingTemplate>> smithingTemplate) {
        return smithingTemplate.get().map(SmithingTemplate::emptyAdditionsSlotTextures);
    }

    @ModifyConstant(
        method = "renderSlotTooltip",
        constant = @Constant(
            classValue = SmithingTemplateItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfSmithingTemplateItemUseItemComponentCheck(Object reference, Class<SmithingTemplateItem> clazz, @Local(ordinal = 0) ItemStack itemStack, @Share("smithingTemplate") LocalRef<SmithingTemplate> smithingTemplate) {
        Optional<SmithingTemplate> optionalSmithingTemplate = itemStack.itematic$getComponent(ItemComponentTypes.SMITHING_TEMPLATE)
            .map(SmithingTemplateItemComponent::template)
            .map(RegistryEntry::value);
        optionalSmithingTemplate.ifPresent(smithingTemplate::set);
        return optionalSmithingTemplate.isPresent();
    }

    @ModifyVariable(
        method = "renderSlotTooltip",
        at = @At("LOAD"),
        ordinal = 0
    )
    private Item castToSmithingTemplateItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "renderSlotTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/SmithingTemplateItem;getBaseSlotDescription()Lnet/minecraft/text/Text;"
        )
    )
    private Text getBaseSlotDescriptionUseSmithingTemplate(SmithingTemplateItem instance, @Share("smithingTemplate") LocalRef<SmithingTemplate> smithingTemplate) {
        return smithingTemplate.get().baseSlotDescription();
    }

    @Redirect(
        method = "renderSlotTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/SmithingTemplateItem;getAdditionsSlotDescription()Lnet/minecraft/text/Text;"
        )
    )
    private Text getAdditionsSlotDescriptionUseSmithingTemplate(SmithingTemplateItem instance, @Share("smithingTemplate") LocalRef<SmithingTemplate> smithingTemplate) {
        return smithingTemplate.get().additionsSlotDescription();
    }
}
