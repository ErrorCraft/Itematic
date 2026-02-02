package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.WritableBookContentComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public record WritableItemComponent(RegistryEntry<Item> transformsInto) implements ItemComponent<WritableItemComponent> {
    public static final Codec<WritableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("transforms_into").forGetter(WritableItemComponent::transformsInto)
    ).apply(instance, WritableItemComponent::new));

    @Override
    public ItemComponentType<WritableItemComponent> type() {
        return ItemComponentTypes.WRITABLE;
    }

    @Override
    public Codec<WritableItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        user.useBook(stack, hand);
        user.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
        return ItemResult.SUCCEED;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.WRITABLE_BOOK_CONTENT, WritableBookContentComponent.DEFAULT);
    }

    public static WritableItemComponent of(RegistryEntry<Item> transformsInto) {
        return new WritableItemComponent(transformsInto);
    }
}
