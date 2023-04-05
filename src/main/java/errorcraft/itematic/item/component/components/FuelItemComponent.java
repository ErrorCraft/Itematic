package errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import errorcraft.itematic.item.component.ItemComponent;
import errorcraft.itematic.item.component.ItemComponentType;
import errorcraft.itematic.item.component.ItemComponentTypes;

public record FuelItemComponent(int ticks) implements ItemComponent {
    public static final Codec<FuelItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("ticks").forGetter(FuelItemComponent::ticks)
    ).apply(instance, FuelItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.FUEL;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }
}
