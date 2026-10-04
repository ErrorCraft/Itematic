package net.errorcraft.itematic.mixin.world.item;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.world.item.group.CreativeModeTabsCache;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.stream.Stream;

@Mixin(CreativeModeTabs.class)
public class CreativeModeTabsExtender {
    @WrapMethod(
        method = "streamAllTabs"
    )
    private static Stream<CreativeModeTab> streamCachedTabsFromDataDrivenRegistry(Operation<Stream<CreativeModeTab>> original) {
        return CreativeModeTabsCache.streamTabs();
    }

    @WrapMethod(
        method = "getDefaultTab"
    )
    private static CreativeModeTab useCachedTabFromDataDrivenRegistry(Operation<CreativeModeTab> original) {
        // TODO: Fix NPE when no tabs are present
        return CreativeModeTabsCache.firstTab();
    }

    @WrapMethod(
        method = "searchTab"
    )
    private static CreativeModeTab useCachedSearchTabFromDataDrivenRegistry(Operation<CreativeModeTab> original) {
        // TODO: Fix NPE when tab is not present
        return CreativeModeTabsCache.search();
    }

    @WrapOperation(
        method = "tryRebuildTabContents",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CreativeModeTabs;buildAllTabContents(Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;)V"
        )
    )
    private static void cacheCreativeModeTabsFromDataDrivenRegistryAndDoNotTriggerFabricApiValidation(CreativeModeTab.ItemDisplayParameters parameters, Operation<Void> original, @Local(name = "lookup", argsOnly = true) HolderLookup.Provider lookup) {
        CreativeModeTabsCache.refresh(lookup, parameters);
    }
}
