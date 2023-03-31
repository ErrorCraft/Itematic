package errorcraft.itematic.mixin.recipe;

import errorcraft.itematic.access.registry.DynamicRegistryManagerAccess;
import errorcraft.itematic.recipe.RecipeSerializerUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RecipeMatcher.class)
public class RecipeMatcherExtender implements DynamicRegistryManagerAccess {
    private DynamicRegistryManager registryManager;
    private Registry<Item> itemRegistry;

    /**
     * @author ErrorCraft
     * @reason Uses a dynamic item registry for data-driven items.
     */
    @Overwrite
    public static int getItemId(ItemStack stack) {
        return RecipeSerializerUtil.getRawId(stack.getRegistryEntry());
    }

    @Redirect(
        method = "addInput(Lnet/minecraft/item/ItemStack;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/RecipeMatcher;getItemId(Lnet/minecraft/item/ItemStack;)I"
        )
    )
    private int addInputGetItemIdUseDynamicRegistry(ItemStack stack) {
        return this.itemRegistry.getRawId(stack.getItem());
    }

    @Override
    public DynamicRegistryManager getRegistryManager() {
        return this.registryManager;
    }

    @Override
    public void setRegistryManager(DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
        this.itemRegistry = registryManager.get(RegistryKeys.ITEM);
    }
}
