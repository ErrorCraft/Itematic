package net.errorcraft.itematic.mixin.world.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;

@Mixin(DyeColor.class)
public class DyeColorExtender {
    @WrapOperation(
        method = "findColorMixInRecipes",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/component/DataComponentLookup;findAll(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/util/Collection;"
        )
    )
    private static <C> Collection<? extends Holder<Item>> useDataComponentOnItemInstead(DataComponentLookup<Item> instance, DataComponentType<C> type, C value, Operation<Collection<Holder<Item>>> original, ServerLevel level) {
        return level.registryAccess()
            .lookupOrThrow(Registries.ITEM)
            .listElements()
            .filter(item -> item.value().components().get(type) == value)
            .toList();
    }
}
