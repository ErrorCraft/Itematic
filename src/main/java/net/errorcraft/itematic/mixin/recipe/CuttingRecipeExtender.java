package net.errorcraft.itematic.mixin.recipe;

import com.mojang.datafixers.util.Function4;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.access.recipe.CuttingRecipeAccess;
import net.errorcraft.itematic.access.recipe.CuttingRecipeSerializerAccess;
import net.errorcraft.itematic.recipe.CuttingRecipeFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CuttingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CuttingRecipe.class)
public class CuttingRecipeExtender implements CuttingRecipeAccess {
    @Shadow
    @Final
    @Mutable
    protected ItemStack result;

    @Override
    public void setResult(RegistryEntry<Item> result, int count) {
        this.result = new ItemStack(result, count);
    }

    @Mixin(CuttingRecipe.Serializer.class)
    public static class SerializerExtender<T extends CuttingRecipe> implements CuttingRecipeSerializerAccess<T> {
        @Unique
        private CuttingRecipeFactory<T> factory;

        @Unique
        private static Function4<String, Ingredient, RegistryEntry<Item>, Integer, CuttingRecipe> tempFactory;

        @Redirect(
            method = "method_53774",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/registry/DefaultedRegistry;getCodec()Lcom/mojang/serialization/Codec;"
            )
        )
        private static Codec<RegistryEntry<Item>> getCodecUseRegistryFixedCodec(DefaultedRegistry<Item> instance) {
            return RegistryFixedCodec.of(RegistryKeys.ITEM);
        }

        @Inject(
            method = "<init>",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"
            )
        )
        private void storeTempFactory(CuttingRecipe.Serializer.RecipeFactory<T> recipeFactory, CallbackInfo info) {
            tempFactory = this::create;
        }

        @ModifyArg(
            method = "method_53774",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/datafixers/Products$P4;apply(Lcom/mojang/datafixers/kinds/Applicative;Lcom/mojang/datafixers/util/Function4;)Lcom/mojang/datafixers/kinds/App;"
            )
        )
        private static <T1, T2, T3, T4, R> Function4<String, Ingredient, RegistryEntry<Item>, Integer, CuttingRecipe> useRegistryEntryFactory(final Function4<T1, T2, T3, T4, R> function) {
            return tempFactory;
        }

        @Inject(
            method = "<init>",
            at = @At(
                value = "INVOKE_ASSIGN",
                target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"
            )
        )
        private void resetTempFactory(CuttingRecipe.Serializer.RecipeFactory<T> recipeFactory, CallbackInfo info) {
            tempFactory = null;
        }

        @Override
        public void setFactory(CuttingRecipeFactory<T> factory) {
            this.factory = factory;
        }

        private T create(String group, Ingredient ingredient, RegistryEntry<Item> result, int count) {
            if (this.factory == null) {
                return null;
            }
            return this.factory.create(group, ingredient, result, count);
        }
    }
}
