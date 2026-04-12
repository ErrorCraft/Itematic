package net.errorcraft.itematic.mixin.block.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.access.block.entity.SherdsAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.entity.Sherds;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.dynamic.Codecs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Mixin(Sherds.class)
public abstract class SherdsExtender implements SherdsAccess {
    @Shadow
    @Final
    public static Sherds DEFAULT;

    @Shadow
    @Final
    public static Codec<Sherds> CODEC;

    @Shadow
    @Final
    private Optional<RegistryEntry<Item>> back;

    @Shadow
    @Final
    private Optional<RegistryEntry<Item>> left;

    @Shadow
    @Final
    private Optional<RegistryEntry<Item>> right;

    @Shadow
    @Final
    private Optional<RegistryEntry<Item>> front;

    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getCodec()Lcom/mojang/serialization/Codec;"
        )
    )
    private static Codec<Optional<RegistryEntry<Item>>> getCodecUseRegistryFixedCodec(DefaultedRegistry<Item> instance) {
        return Codecs.optional(RegistryFixedCodec.of(RegistryKeys.ITEM));
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            remap = false
        ),
        index = 0
    )
    private static Function<List<Optional<RegistryEntry<Item>>>, Sherds> xmapToSherdsUseRegistryEntry(Function<List<Item>, Sherds> to) {
        return SherdsAccessor::create;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            remap = false
        ),
        index = 1
    )
    private static Function<Sherds, List<Optional<RegistryEntry<Item>>>> xmapFromSherdsUseRegistryEntry(Function<Sherds, List<Item>> from) {
        return Sherds::itematic$optionalEntries;
    }

    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/PacketCodecs;registryValue(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/network/codec/PacketCodec;"
        )
    )
    private static PacketCodec<RegistryByteBuf, Optional<RegistryEntry<Item>>> createPacketCodecUseRegistryEntry(RegistryKey<Registry<Item>> registry) {
        return PacketCodecs.optional(PacketCodecs.registryEntry(registry));
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/PacketCodec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;"
        ),
        index = 0
    )
    private static Function<List<Optional<RegistryEntry<Item>>>, Sherds> xmapToSherdsForPacketCodecUseRegistryEntry(Function<List<Item>, Sherds> to) {
        return SherdsAccessor::create;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/PacketCodec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;"
        ),
        index = 1
    )
    private static Function<Sherds, List<Optional<RegistryEntry<Item>>>> xmapFromSherdsForPacketCodecUseRegistryEntry(Function<Sherds, List<Item>> from) {
        return Sherds::itematic$optionalEntries;
    }

    @ModifyReturnValue(
        method = {
            "back",
            "left",
            "right",
            "front"
        },
        at = @At("TAIL")
    )
    private Optional<Item> mapToItem(Optional<RegistryEntry<Item>> original) {
        return original.map(RegistryEntry::value);
    }

    @Redirect(
        method = "getSherd",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;get(I)Ljava/lang/Object;"
        )
    )
    private static <T> T getItemReturnNull(List<T> instance, int index) {
        return null;
    }

    @Redirect(
        method = "getSherd",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;of(Ljava/lang/Object;)Ljava/util/Optional;"
        )
    )
    private static <T> Optional<RegistryEntry<Item>> optionalUseValueFromList(T value, List<Optional<RegistryEntry<Item>>> sherds, int index) {
        return sherds.get(index);
    }

    @Redirect(
        method = "appendTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/Sherds;appendSherdTooltip(Ljava/util/function/Consumer;Ljava/util/Optional;)V"
        )
    )
    private void appendSherdTooltipUseRegistryEntry(Consumer<Text> textConsumer, Optional<RegistryEntry<Item>> sherd) {
        sherd.map(ItemStack::new)
            .map(ItemStack::getName)
            .map(Text::copyContentOnly)
            .map(text -> text.formatted(Formatting.GRAY))
            .ifPresent(textConsumer);
    }

    @Override
    public List<Optional<RegistryEntry<Item>>> itematic$optionalEntries() {
        return List.of(this.back, this.left, this.right, this.front);
    }

    @Override
    public List<RegistryEntry<Item>> itematic$entries(RegistryWrapper.WrapperLookup lookup) {
        RegistryWrapper.Impl<Item> items = lookup.getOrThrow(RegistryKeys.ITEM);
        return Stream.of(this.back, this.left, this.right, this.front)
            .map(optional -> optional.orElse(items.getOrThrow(ItemKeys.BRICK)))
            .toList();
    }
}
