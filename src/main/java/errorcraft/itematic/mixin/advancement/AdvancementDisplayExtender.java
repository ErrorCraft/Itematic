package errorcraft.itematic.mixin.advancement;

import com.google.gson.JsonObject;
import errorcraft.itematic.access.advancement.AdvancementDisplayAccess;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AdvancementDisplay.class)
public class AdvancementDisplayExtender implements AdvancementDisplayAccess {
    @Shadow
    @Mutable
    @Final
    private ItemStack icon;

    @Inject(
        method = "iconFromJson",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void useEmptyStack(JsonObject json, CallbackInfoReturnable<ItemStack> info) {
        info.setReturnValue(ItemStack.EMPTY);
    }

    @Override
    public void setIcon(ItemStack itemStack) {
        this.icon = itemStack;
    }
}
