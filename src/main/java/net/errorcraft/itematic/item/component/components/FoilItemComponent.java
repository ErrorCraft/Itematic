package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;

public record FoilItemComponent(boolean foil) implements ItemComponent {
    public static final Codec<FoilItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.fieldOf("foil").forGetter(FoilItemComponent::foil)
    ).apply(instance, FoilItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.FOIL;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }
}
