package errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import errorcraft.itematic.item.component.ItemComponent;
import errorcraft.itematic.item.component.ItemComponentType;
import errorcraft.itematic.item.component.ItemComponentTypes;

public record DamageableItemComponent(int durability) implements ItemComponent {
    public static Codec<DamageableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("durability").forGetter(DamageableItemComponent::durability)
    ).apply(instance, DamageableItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.DAMAGEABLE;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }
}
