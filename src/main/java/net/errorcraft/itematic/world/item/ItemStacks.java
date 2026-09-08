package net.errorcraft.itematic.world.item;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.jspecify.annotations.Nullable;

public class ItemStacks {
    private static final MapCodec<ItemStack> FAILED_MAP_CODEC = MapCodec.recursive(
        "ItemStack", _ -> RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(Registries.ITEM).fieldOf(ItemInstance.FIELD_ID).forGetter(ItemStack::itematic$key),
            ExtraCodecs.intRange(1, 99).fieldOf(ItemInstance.FIELD_COUNT).orElse(1).forGetter(ItemStack::getCount),
            DataComponentPatch.CODEC.optionalFieldOf(ItemInstance.FIELD_COMPONENTS, DataComponentPatch.EMPTY).forGetter(ItemStack::getComponentsPatch)
        ).apply(instance, ItemStacks::createFailed)));
    public static final MapCodec<ItemStack> POSSIBLY_FAILED_MAP_CODEC = Codec.mapEither(
        ItemStack.MAP_CODEC,
        FAILED_MAP_CODEC
    ).xmap(
        Either::unwrap,
        stack -> stack.itematic$isSuccessfullyLoaded()
            ? Either.left(stack)
            : Either.right(stack)
    );
    public static final Codec<ItemStack> POSSIBLY_FAILED_CODEC = POSSIBLY_FAILED_MAP_CODEC.codec();

    @SuppressWarnings("DataFlowIssue")
    private static ItemStack createFailed(ResourceKey<Item> item, Integer count, DataComponentPatch components) {
        ItemStack stack = new ItemStack(null, count, components);
        stack.itematic$setFailedKey(item);
        return stack;
    }

    private ItemStacks() {}

    public static boolean isNullOrEmpty(@Nullable ItemStack stack) {
        return stack == null || stack.isEmpty();
    }

    public static ItemStack fromItemInstance(ItemInstance item) {
        return switch (item) {
            case ItemStack itemStack -> itemStack;
            case ItemStackTemplate itemStackTemplate -> itemStackTemplate.create();
            default -> ItemStack.EMPTY;
        };
    }
}
