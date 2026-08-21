package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
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

public record WritableItemBehavior(Holder<Item> transformsInto) implements ItemBehavior<WritableItemBehavior> {
    public static final Codec<WritableItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.create(Registries.ITEM).fieldOf("transforms_into").forGetter(WritableItemBehavior::transformsInto)
    ).apply(instance, WritableItemBehavior::new));

    public static WritableItemBehavior of(Holder<Item> transformsInto) {
        return new WritableItemBehavior(transformsInto);
    }

    @Override
    public ItemBehaviorType<WritableItemBehavior> type() {
        return ItemBehaviorType.WRITABLE;
    }

    @Override
    public ItemResult use(Level level, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        user.openItemGui(stack, hand);
        user.awardStat(Stats.ITEM_USED.itematic$get(stack.getItemHolder()));
        return ItemResult.SUCCEED;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.WRITABLE_BOOK_CONTENT, WritableBookContent.EMPTY);
    }
}
