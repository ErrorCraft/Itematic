package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.JukeboxSong;

public record PlayableSongItemBehavior(Holder<JukeboxSong> song) implements ItemBehavior<PlayableSongItemBehavior> {
    public static final Codec<PlayableSongItemBehavior> CODEC = JukeboxSong.CODEC.xmap(PlayableSongItemBehavior::new, PlayableSongItemBehavior::song);

    public static ItemBehavior<?>[] of(Holder<JukeboxSong> song) {
        return new ItemBehavior<?>[] {
            StackableItemBehavior.of(1),
            new PlayableSongItemBehavior(song)
        };
    }

    @Override
    public ItemBehaviorType<PlayableSongItemBehavior> type() {
        return ItemBehaviorType.PLAYABLE_SONG;
    }

    @Override
    public Codec<PlayableSongItemBehavior> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.JUKEBOX_PLAYABLE, new JukeboxPlayable(new EitherHolder<>(this.song)));
    }
}
