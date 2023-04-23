package errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import errorcraft.itematic.item.component.ItemComponent;
import errorcraft.itematic.item.component.ItemComponentType;
import errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

import java.util.Optional;

public record ForgeableItemComponent(Optional<TagKey<Enchantment>> enchantments) implements ItemComponent {
    public static final Codec<ForgeableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TagKey.unprefixedCodec(RegistryKeys.ENCHANTMENT).optionalFieldOf("enchantments").forGetter(ForgeableItemComponent::enchantments)
    ).apply(instance, ForgeableItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.FORGEABLE;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    public static ForgeableItemComponent of(TagKey<Enchantment> enchantments) {
        return new ForgeableItemComponent(Optional.of(enchantments));
    }
}
