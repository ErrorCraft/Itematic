package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;

public record PreventUseWhenUsedOnTargetItemComponent(boolean block, boolean entity) implements ItemComponent<PreventUseWhenUsedOnTargetItemComponent> {
    public static final Codec<PreventUseWhenUsedOnTargetItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.optionalFieldOf("block", false).forGetter(PreventUseWhenUsedOnTargetItemComponent::block),
        Codec.BOOL.optionalFieldOf("entity", false).forGetter(PreventUseWhenUsedOnTargetItemComponent::entity)
    ).apply(instance, PreventUseWhenUsedOnTargetItemComponent::new));

    @Override
    public ItemComponentType<PreventUseWhenUsedOnTargetItemComponent> type() {
        return ItemComponentTypes.PREVENT_USE_WHEN_USED_ON_TARGET;
    }

    @Override
    public Codec<PreventUseWhenUsedOnTargetItemComponent> codec() {
        return CODEC;
    }

    public static PreventUseWhenUsedOnTargetItemComponent forBlock() {
        return new PreventUseWhenUsedOnTargetItemComponent(true, false);
    }

    public static PreventUseWhenUsedOnTargetItemComponent forEntity() {
        return new PreventUseWhenUsedOnTargetItemComponent(false, true);
    }
}
