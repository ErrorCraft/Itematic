package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public record WritableItemComponent(RegistryEntry<Item> transformsInto) implements ItemComponent {
    public static final Codec<WritableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("transforms_into").forGetter(WritableItemComponent::transformsInto)
    ).apply(instance, WritableItemComponent::new));

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.WRITABLE;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        user.useBook(stack, hand);
        user.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
        return ActionResult.success(world.isClient());
    }

    public static WritableItemComponent of(RegistryEntry<Item> transformsInto) {
        return new WritableItemComponent(transformsInto);
    }
}
