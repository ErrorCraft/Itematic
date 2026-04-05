package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.JukeboxPlayableComponent;
import net.minecraft.registry.RegistryPair;
import net.minecraft.registry.entry.RegistryEntry;

public record PlayableSongItemComponent(RegistryEntry<JukeboxSong> song) implements ItemComponent<PlayableSongItemComponent> {
    public static final Codec<PlayableSongItemComponent> CODEC = JukeboxSong.ENTRY_CODEC.xmap(PlayableSongItemComponent::new, PlayableSongItemComponent::song);

    public static ItemComponent<?>[] of(RegistryEntry<JukeboxSong> song) {
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
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.JUKEBOX_PLAYABLE, new JukeboxPlayableComponent(new RegistryPair<>(this.song)));
    }
}
