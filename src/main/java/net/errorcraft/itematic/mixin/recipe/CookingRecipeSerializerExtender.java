package net.errorcraft.itematic.mixin.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(CookingRecipeSerializer.class)
public class CookingRecipeSerializerExtender<T extends AbstractCookingRecipe> {
    @Redirect(
        method = "method_53766",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getCodec()Lcom/mojang/serialization/Codec;"
        )
    )
    private static Codec<RegistryEntry<Item>> getCodecUseRegistryFixedCodec(DefaultedRegistry<Item> instance) {
        return RegistryFixedCodec.of(RegistryKeys.ITEM);
    }

    @ModifyArg(
        method = "method_53766",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            remap = false
        ),
        index = 0
    )
    private static <A, S> Function<? super RegistryEntry<Item>, ? extends ItemStack> xmapToUseItemStackConstructor(Function<? super A, ? extends S> to) {
        return ItemStack::new;
    }

    @ModifyArg(
        method = "method_53766",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            remap = false
        ),
        index = 1
    )
    private static <A, S> Function<? super ItemStack, ? extends RegistryEntry<Item>> xmapFromUseItemStackGetRegistryEntry(Function<? super A, ? extends S> from) {
        return ItemStack::getRegistryEntry;
    }
}
