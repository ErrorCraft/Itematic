package net.errorcraft.itematic.mixin.loot.function;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.access.loot.function.SetPotionLootFunctionAccess;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.SetPotionLootFunction;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Mixin(SetPotionLootFunction.class)
public class SetPotionLootFunctionExtender implements SetPotionLootFunctionAccess {
    @Unique
    private RegistryEntryList<Potion> potions;

    @Redirect(
        method = "method_53394",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/Registry;getEntryCodec()Lcom/mojang/serialization/Codec;"
        )
    )
    private static Codec<RegistryEntryList<Potion>> getEntryCodecUseRegistryEntryListCodec(Registry<Potion> instance) {
        return RegistryCodecs.entryList(RegistryKeys.POTION);
    }

    @ModifyArg(
        method = "method_53394",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/MapCodec;forGetter(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;",
            remap = false
        )
    )
    private static <O, A> Function<SetPotionLootFunction, RegistryEntryList<Potion>> forGetterUseRegistryEntryList(Function<O, A> getter) {
        return itemModifier -> ((SetPotionLootFunctionExtender)(Object) itemModifier).potions;
    }

    @ModifyArg(
        method = "method_53394",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/datafixers/Products$P2;apply(Lcom/mojang/datafixers/kinds/Applicative;Ljava/util/function/BiFunction;)Lcom/mojang/datafixers/kinds/App;",
            remap = false
        )
    )
    private static BiFunction<List<LootCondition>, RegistryEntryList<Potion>, SetPotionLootFunction> applyUseRegistryEntryItemStackConstructor(BiFunction<List<LootCondition>, RegistryEntry<Potion>, SetPotionLootFunction> instance) {
        return (conditions, potions) -> {
            SetPotionLootFunction itemModifier = instance.apply(conditions, null);
            ((SetPotionLootFunctionExtender)(Object) itemModifier).potions = potions;
            return itemModifier;
        };
    }

    @WrapOperation(
        method = "process",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;apply(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;Ljava/lang/Object;Ljava/util/function/BiFunction;)Ljava/lang/Object;"
        )
    )
    private <T, U> Object applyPotionContentComponentCheckForRegistryEntryFromRegistryEntryList(ItemStack instance, ComponentType<T> type, T defaultValue, U change, BiFunction<T, U, T> applier, Operation<Object> original, @Local(argsOnly = true) LootContext context) {
        this.potions.getRandom(context.getRandom())
            .ifPresent(potion -> original.call(instance, type, defaultValue, potion, applier));
        return null;
    }

    @Override
    public void itematic$setPotions(RegistryEntryList<Potion> potions) {
        this.potions = potions;
    }
}
