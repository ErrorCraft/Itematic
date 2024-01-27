package net.errorcraft.itematic.mixin.text;

import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Function;

public class HoverEventExtender {
    @Mixin(HoverEvent.ItemStackContent.class)
    public static class ItemStackContentExtender {
        @Unique
        private RegistryEntry<Item> entry;

        @Inject(
            method = "<init>(Lnet/minecraft/item/ItemStack;)V",
            at = @At("TAIL")
        )
        private void setRegistryEntry(ItemStack stack, CallbackInfo info) {
            this.entry = stack.getRegistryEntry();
        }

        @Redirect(
            method = "asStack",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/item/ItemStack;"
            )
        )
        private ItemStack newItemStackUseRegistryEntry(ItemConvertible item, int count) {
            if (this.entry == null) {
                return ItemStack.EMPTY;
            }
            return new ItemStack(this.entry, count);
        }

        @Redirect(
            method = { "<clinit>", "method_54202" },
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/registry/DefaultedRegistry;getCodec()Lcom/mojang/serialization/Codec;"
            )
        )
        private static Codec<RegistryEntry<Item>> getCodecUseRegistryFixedCodec(DefaultedRegistry<Item> instance) {
            return RegistryFixedCodec.of(RegistryKeys.ITEM);
        }

        @ModifyArg(
            method = "method_54202",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/serialization/MapCodec;forGetter(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;",
                ordinal = 0,
                remap = false
            ),
            slice = @Slice(
                from = @At(
                    value = "CONSTANT",
                    args = "stringValue=id"
                )
            )
        )
        private static <O, A> Function<HoverEvent.ItemStackContent, RegistryEntry<Item>> forGetterUseRegistryEntry(Function<O, A> getter) {
            return content -> ((ItemStackContentExtender)(Object) content).entry;
        }

        @ModifyArg(
            method = "method_54202",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/datafixers/Products$P3;apply(Lcom/mojang/datafixers/kinds/Applicative;Lcom/mojang/datafixers/util/Function3;)Lcom/mojang/datafixers/kinds/App;",
                remap = false
            )
        )
        private static Function3<RegistryEntry<Item>, Integer, Optional<NbtCompound>, HoverEvent.ItemStackContent> applyUseRegistryEntryItemStackConstructor(Function3<Item, Integer, Optional<NbtCompound>, HoverEvent.ItemStackContent> function) {
            return (entry, count, nbt) -> new HoverEvent.ItemStackContent(new ItemStack(entry, count, nbt));
        }

        @ModifyArg(
            method = "method_54201",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/datafixers/util/Either;map(Ljava/util/function/Function;Ljava/util/function/Function;)Ljava/lang/Object;",
                remap = false
            ),
            index = 0
        )
        private static <L, T> Function<RegistryEntry<Item>, HoverEvent.ItemStackContent> itemToCastUseNull(Function<? super L, ? extends T> original) {
            return entry -> new HoverEvent.ItemStackContent(new ItemStack(entry));
        }

        /**
         * @author ErrorCraft
         * @reason Legacy serializers do not have access to dynamic registries.
         */
        @Overwrite
        private static DataResult<HoverEvent.ItemStackContent> legacySerializer(Text text) {
            return DataResult.error(() -> "Legacy item stack hover event is not supported");
        }
    }
}
