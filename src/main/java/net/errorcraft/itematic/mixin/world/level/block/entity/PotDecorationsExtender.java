package net.errorcraft.itematic.mixin.world.level.block.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.access.world.level.block.entity.PotDecorationsAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.PotDecorations;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Mixin(PotDecorations.class)
public abstract class PotDecorationsExtender implements PotDecorationsAccess {
    @Shadow
    @Final
    public static PotDecorations EMPTY;

    @Shadow
    @Final
    public static Codec<PotDecorations> CODEC;

    @Shadow
    @Final
    private Optional<Holder<Item>> back;

    @Shadow
    @Final
    private Optional<Holder<Item>> left;

    @Shadow
    @Final
    private Optional<Holder<Item>> right;

    @Shadow
    @Final
    private Optional<Holder<Item>> front;

    @WrapOperation(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/DefaultedRegistry;byNameCodec()Lcom/mojang/serialization/Codec;"
        )
    )
    private static Codec<Optional<Holder<Item>>> doNotUseStaticRegistry(DefaultedRegistry<Item> instance, Operation<Codec<Item>> original) {
        return ExtraCodecs.optionalEmptyMap(Item.CODEC);
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"
        ),
        index = 0
    )
    private static Function<List<Optional<Holder<Item>>>, PotDecorations> xmapToPotDecorationsUseHolders(Function<List<Item>, PotDecorations> to) {
        return PotDecorationsAccessor::create;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"
        ),
        index = 1
    )
    private static Function<PotDecorations, List<Optional<Holder<Item>>>> xmapFromPotDecorationsUseHolders(Function<PotDecorations, List<Item>> from) {
        return PotDecorations::itematic$optionalEntries;
    }

    @WrapOperation(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/ByteBufCodecs;registry(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/network/codec/StreamCodec;"
        )
    )
    private static StreamCodec<RegistryFriendlyByteBuf, Optional<Holder<Item>>> doNotUseValueStreamCodec(ResourceKey<? extends Registry<Item>> registryKey, Operation<StreamCodec<RegistryFriendlyByteBuf, Item>> original) {
        return ByteBufCodecs.holderRegistry(registryKey).apply(ByteBufCodecs::optional);
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/StreamCodec;map(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/StreamCodec;"
        ),
        index = 0
    )
    private static Function<List<Optional<Holder<Item>>>, PotDecorations> mapToPotDecorationsUseHolders(Function<List<Item>, PotDecorations> to) {
        return PotDecorationsAccessor::create;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/StreamCodec;map(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/StreamCodec;"
        ),
        index = 1
    )
    private static Function<PotDecorations, List<Optional<Holder<Item>>>> mapFromPotDecorationsUseHolders(Function<PotDecorations, List<Item>> from) {
        return PotDecorations::itematic$optionalEntries;
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
    private Optional<Item> mapToItem(Optional<Holder<Item>> original) {
        return original.map(Holder::value);
    }

    @WrapOperation(
        method = "getItem",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;get(I)Ljava/lang/Object;"
        )
    )
    private static <E> @Nullable E getItemReturnNull(List<E> instance, int index, Operation<E> original) {
        return null;
    }

    @WrapOperation(
        method = "getItem",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;of(Ljava/lang/Object;)Ljava/util/Optional;"
        )
    )
    private static <T> Optional<Holder<Item>> optionalUseValueFromList(T value, Operation<Optional<T>> original, List<Optional<Holder<Item>>> sherds, int i) {
        return sherds.get(i);
    }

    @WrapOperation(
        method = "addToTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/PotDecorations;addSideDetailsToTooltip(Ljava/util/function/Consumer;Ljava/util/Optional;)V"
        )
    )
    private void appendSideDetailsToTooltipUseHolder(Consumer<Component> consumer, Optional<Holder<Item>> side, Operation<Void> original) {
        side.map(ItemStack::new)
            .map(ItemStack::getHoverName)
            .map(Component::plainCopy)
            .map(text -> text.withStyle(ChatFormatting.GRAY))
            .ifPresent(consumer);
    }

    @Override
    public List<Optional<Holder<Item>>> itematic$optionalEntries() {
        return List.of(this.back, this.left, this.right, this.front);
    }

    @Override
    public List<Holder<Item>> itematic$entries(HolderLookup.Provider lookup) {
        HolderLookup.RegistryLookup<Item> items = lookup.lookupOrThrow(Registries.ITEM);
        return Stream.of(this.back, this.left, this.right, this.front)
            .map(optionalEntry -> optionalEntry.orElseGet(() -> items.getOrThrow(ItemIds.BRICK)))
            .toList();
    }
}
