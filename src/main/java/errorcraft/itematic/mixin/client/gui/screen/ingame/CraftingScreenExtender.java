package errorcraft.itematic.mixin.client.gui.screen.ingame;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(CraftingScreen.class)
public class CraftingScreenExtender {
    @Shadow
    @Final
    private RecipeBookWidget recipeBook;

    @Inject(
        method = "<init>",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/screen/ingame/CraftingScreen;recipeBook:Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;",
            shift = At.Shift.AFTER
        )
    )
    private void initSetRecipeBookDynamicRegistryManager(CraftingScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo info) {
        this.recipeBook.setRegistryManager(inventory.player.world.getRegistryManager());
    }
}
