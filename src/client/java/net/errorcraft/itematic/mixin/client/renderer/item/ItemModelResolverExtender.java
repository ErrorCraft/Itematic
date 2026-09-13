package net.errorcraft.itematic.mixin.client.renderer.item;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemModelResolver.class)
public class ItemModelResolverExtender {
    @Shadow
    @Final
    private ModelManager modelManager;

    @WrapMethod(
        method = "appendItemLayers"
    )
    private void checkSuccessfullyLoaded(ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, Level level, ItemOwner owner, int seed, Operation<Void> original) {
        if (item.itematic$isSuccessfullyLoaded()) {
            original.call(output, item, displayContext, level, owner, seed);
            return;
        }

        this.modelManager.itematic$failedToLoadItemModel()
            .update(
                output,
                item,
                (ItemModelResolver)(Object) this,
                displayContext,
                level instanceof ClientLevel clientLevel ? clientLevel : null,
                owner,
                seed
            );
    }
}
