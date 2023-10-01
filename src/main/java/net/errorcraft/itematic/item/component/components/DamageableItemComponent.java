package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.ItemStack;

public record DamageableItemComponent(int durability, boolean preserveItem) implements ItemComponent {
    public static Codec<DamageableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("durability").forGetter(DamageableItemComponent::durability),
        Codec.BOOL.optionalFieldOf("preserve_item", false).forGetter(DamageableItemComponent::preserveItem)
    ).apply(instance, DamageableItemComponent::new));

    public DamageableItemComponent(int durability) {
        this(durability, false);
    }

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.DAMAGEABLE;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    public int maximumDamage() {
        return this.durability - (this.preserveItem ? 1 : 0);
    }

    public boolean isUsable(ItemStack stack) {
        return stack.getDamage() < this.maximumDamage();
    }
}
