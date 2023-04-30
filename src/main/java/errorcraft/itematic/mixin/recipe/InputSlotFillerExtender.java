package errorcraft.itematic.mixin.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.InputSlotFiller;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(InputSlotFiller.class)
public class InputSlotFillerExtender<C extends Inventory> {
    @Shadow
    @Final
    protected RecipeMatcher matcher;

    private Registry<Item> registry;

    @Inject(
        method = "fillInputSlots(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/recipe/Recipe;Z)V",
        at = @At("HEAD")
    )
    private void fillInputSlotsSetDynamicRegistryManager(ServerPlayerEntity entity, @Nullable Recipe<C> recipe, boolean craftAll, CallbackInfo ci) {
        if (this.registry == null) {
            DynamicRegistryManager registryManager = entity.getWorld().getRegistryManager();
            this.matcher.setRegistryManager(registryManager);
            this.registry = registryManager.get(RegistryKeys.ITEM);
        }
    }

    @Redirect(
        method = { "fillInputSlots(Lnet/minecraft/recipe/Recipe;Z)V", "acceptAlignedInput" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/RecipeMatcher;getStackFromId(I)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack fillInputSlotsGetStackFromIdUseDynamicRegistry(int itemId) {
        if (itemId == 0) {
            return ItemStack.EMPTY;
        }
        Optional<? extends RegistryEntry<Item>> optionalEntry = this.registry.getEntry(itemId);
        return optionalEntry.map(ItemStack::new).orElse(ItemStack.EMPTY);
    }
}
