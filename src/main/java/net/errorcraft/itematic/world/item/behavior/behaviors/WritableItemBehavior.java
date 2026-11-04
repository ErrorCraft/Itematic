package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.WritableBookContent;

public record WritableItemBehavior(Holder<Item> signResultItem) implements ItemBehavior<WritableItemBehavior> {
    public static final Codec<WritableItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Item.CODEC.fieldOf("sign_result_item").forGetter(WritableItemBehavior::signResultItem)
    ).apply(instance, WritableItemBehavior::new));

    public static WritableItemBehavior of(Holder<Item> transformsInto) {
        return new WritableItemBehavior(transformsInto);
    }

    @Override
    public ItemBehaviorType<WritableItemBehavior> type() {
        return ItemBehaviorType.WRITABLE;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.WRITABLE_BOOK_CONTENT, WritableBookContent.EMPTY);
        builder.set(ItematicDataComponents.SIGN_RESULT_ITEM, this.signResultItem);
    }
}
