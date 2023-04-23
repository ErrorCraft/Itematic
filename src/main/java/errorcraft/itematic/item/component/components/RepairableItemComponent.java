package errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import errorcraft.itematic.item.component.ItemComponent;
import errorcraft.itematic.item.component.ItemComponentType;
import errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public record RepairableItemComponent(TagKey<Item> items) implements ItemComponent {
    public static final Codec<RepairableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TagKey.unprefixedCodec(RegistryKeys.ITEM).fieldOf("items").forGetter(RepairableItemComponent::items)
    ).apply(instance, RepairableItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.REPAIRABLE;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }
}
