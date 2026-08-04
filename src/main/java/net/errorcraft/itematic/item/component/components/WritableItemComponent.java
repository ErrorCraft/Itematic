package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.level.Level;

public record WritableItemComponent(Holder<Item> transformsInto) implements ItemComponent<WritableItemComponent> {
    public static final Codec<WritableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.create(Registries.ITEM).fieldOf("transforms_into").forGetter(WritableItemComponent::transformsInto)
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
    public ItemResult use(Level world, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        user.openItemGui(stack, hand);
        user.awardStat(Stats.ITEM_USED.itematic$getOrCreateStat(stack.getItemHolder()));
        return ItemResult.SUCCEED;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.WRITABLE_BOOK_CONTENT, WritableBookContent.EMPTY);
    }

    public static WritableItemComponent of(Holder<Item> transformsInto) {
        return new WritableItemComponent(transformsInto);
    }
}
