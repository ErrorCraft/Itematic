package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.JukeboxSong;

public record PlayableSongItemComponent(Holder<JukeboxSong> song) implements ItemComponent<PlayableSongItemComponent> {
    public static final Codec<PlayableSongItemComponent> CODEC = JukeboxSong.CODEC.xmap(PlayableSongItemComponent::new, PlayableSongItemComponent::song);

    public static ItemComponent<?>[] of(Holder<JukeboxSong> song) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            new PlayableSongItemComponent(song)
        };
    }

    @Override
    public ItemComponentType<PlayableSongItemComponent> type() {
        return ItemComponentTypes.PLAYABLE_SONG;
    }

    @Override
    public Codec<PlayableSongItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.JUKEBOX_PLAYABLE, new JukeboxPlayable(new EitherHolder<>(this.song)));
    }
}
