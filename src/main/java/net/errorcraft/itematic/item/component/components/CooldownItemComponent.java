package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.component.type.UseCooldownDataComponent;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.SharedConstants;
import net.minecraft.component.ComponentMap;
import net.minecraft.util.dynamic.Codecs;

public record CooldownItemComponent(int ticks) implements ItemComponent<CooldownItemComponent> {
    public static final Codec<CooldownItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.POSITIVE_INT.fieldOf("ticks").forGetter(CooldownItemComponent::ticks)
    ).apply(instance, CooldownItemComponent::new));

    public static CooldownItemComponent of(int ticks) {
        return new CooldownItemComponent(ticks);
    }

    @Override
    public ItemComponentType<CooldownItemComponent> type() {
        return ItemComponentTypes.COOLDOWN;
    }

    @Override
    public Codec<CooldownItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(ItematicDataComponentTypes.USE_COOLDOWN, new UseCooldownDataComponent((float) this.ticks / SharedConstants.TICKS_PER_SECOND));
    }
}
