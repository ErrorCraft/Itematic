package net.errorcraft.itematic.mixin.recipe;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.recipe.RecipeUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.BiFunction;
import java.util.function.Function;

@Mixin(RecipeCodecs.class)
public class RecipeCodecsExtender {
    @Redirect(
        method = "method_53718",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/recipe/RecipeCodecs;CRAFTING_RESULT_ITEM:Lcom/mojang/serialization/Codec;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private static Codec<RegistryEntry<Item>> craftingResultItemUseRegistryEntryCodec() {
        return RecipeUtil.CRAFTING_RESULT_CODEC;
    }

    @ModifyArg(
        method = "method_53718",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/datafixers/Products$P2;apply(Lcom/mojang/datafixers/kinds/Applicative;Ljava/util/function/BiFunction;)Lcom/mojang/datafixers/kinds/App;",
            remap = false
        )
    )
    private static <T1, T2, R> BiFunction<RegistryEntry<Item>, Integer, ItemStack> applyUseItemStackConstructor(BiFunction<T1, T2, R> function) {
        return ItemStack::new;
    }

    @ModifyArg(
        method = "method_53718",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/MapCodec;forGetter(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;",
            ordinal = 0,
            remap = false
        )
    )
    private static <O, A> Function<ItemStack, RegistryEntry<Item>> craftingResultItemUseRegistryEntryCodec(Function<O, A> getter) {
        return ItemStack::getRegistryEntry;
    }
}
