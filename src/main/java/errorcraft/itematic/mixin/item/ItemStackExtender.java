package errorcraft.itematic.mixin.item;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import errorcraft.itematic.access.item.ItemStackAccess;
import errorcraft.itematic.item.ItemStackUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Function;

@Mixin(ItemStack.class)
public class ItemStackExtender implements ItemStackAccess {
    @Shadow
    @Nullable
    private NbtCompound nbt;

    private RegistryEntry<Item> entry;

    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"
        )
    )
    private static Codec<ItemStack> useCustomItemStackCodec(Function<RecordCodecBuilder.Instance<ItemStack>, ? extends App<RecordCodecBuilder.Mu<ItemStack>, ItemStack>> builder) {
        return ItemStackUtil.CODEC;
    }

    @Inject(
        method = "<init>(Lnet/minecraft/registry/entry/RegistryEntry;)V",
        at = @At("TAIL")
    )
    private void constructorSetRegistryEntry(RegistryEntry<Item> entry, CallbackInfo info) {
        this.entry = entry;
    }

    @Inject(
        method = "<init>(Lnet/minecraft/registry/entry/RegistryEntry;I)V",
        at = @At("TAIL")
    )
    private void constructorSetRegistryEntry(RegistryEntry<Item> entry, int count, CallbackInfo info) {
        this.entry = entry;
    }

    @Inject(
        method = "getRegistryEntry",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getRegistryEntryUseField(CallbackInfoReturnable<RegistryEntry<Item>> info) {
        info.setReturnValue(this.entry);
    }

    @Redirect(
        method = "writeNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getId(Ljava/lang/Object;)Lnet/minecraft/util/Identifier;"
        )
    )
    @NotNull
    private <T> Identifier getIdUseField(DefaultedRegistry<T> instance, T value) {
        return ItemStackUtil.getId(this.entry);
    }

    @Override
    public Optional<NbtCompound> getOptionalNbt() {
        return Optional.ofNullable(this.nbt);
    }
}
