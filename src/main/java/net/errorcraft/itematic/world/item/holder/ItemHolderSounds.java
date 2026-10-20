package net.errorcraft.itematic.world.item.holder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public record ItemHolderSounds(Holder<SoundEvent> insertItem, Holder<SoundEvent> insertItemFail, Holder<SoundEvent> removeItem, Holder<SoundEvent> empty) {
    public static final Codec<ItemHolderSounds> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        SoundEvent.CODEC.fieldOf("insert_item").forGetter(ItemHolderSounds::insertItem),
        SoundEvent.CODEC.fieldOf("insert_item_fail").forGetter(ItemHolderSounds::insertItemFail),
        SoundEvent.CODEC.fieldOf("remove_item").forGetter(ItemHolderSounds::removeItem),
        SoundEvent.CODEC.fieldOf("empty").forGetter(ItemHolderSounds::empty)
    ).apply(instance, ItemHolderSounds::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemHolderSounds> STREAM_CODEC = StreamCodec.composite(
        SoundEvent.STREAM_CODEC, ItemHolderSounds::insertItem,
        SoundEvent.STREAM_CODEC, ItemHolderSounds::insertItemFail,
        SoundEvent.STREAM_CODEC, ItemHolderSounds::removeItem,
        SoundEvent.STREAM_CODEC, ItemHolderSounds::empty,
        ItemHolderSounds::new
    );

    public static void playInsertItemSound(ItemStack stack, Entity user) {
        ItemHolderSounds sounds = stack.get(ItematicDataComponents.ITEM_HOLDER_SOUNDS);
        if (sounds == null) {
            return;
        }

        user.playSound(
            sounds.insertItem.value(),
            0.8f,
            0.8f + user.level().getRandom().nextFloat() * 0.4f
        );
    }

    public static void playInsertItemFailSound(ItemStack stack, Entity user) {
        ItemHolderSounds sounds = stack.get(ItematicDataComponents.ITEM_HOLDER_SOUNDS);
        if (sounds == null) {
            return;
        }

        user.playSound(
            sounds.insertItemFail.value(),
            1.0f,
            1.0f
        );
    }

    public static void playRemoveItemSound(ItemStack stack, Entity user) {
        ItemHolderSounds sounds = stack.get(ItematicDataComponents.ITEM_HOLDER_SOUNDS);
        if (sounds == null) {
            return;
        }

        user.playSound(
            sounds.removeItem.value(),
            0.8f,
            0.8f + user.level().getRandom().nextFloat() * 0.4f
        );
    }

    public static void playEmptySound(ItemStack stack, Entity user) {
        ItemHolderSounds sounds = stack.get(ItematicDataComponents.ITEM_HOLDER_SOUNDS);
        if (sounds == null) {
            return;
        }

        user.playSound(
            sounds.empty.value(),
            0.8f,
            0.8f + user.level().getRandom().nextFloat() * 0.4f
        );
    }
}
