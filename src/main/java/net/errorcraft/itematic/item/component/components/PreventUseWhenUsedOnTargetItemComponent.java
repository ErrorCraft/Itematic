package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public record PreventUseWhenUsedOnTargetItemComponent(boolean block, boolean entity) implements ItemComponent<PreventUseWhenUsedOnTargetItemComponent> {
    public static final Codec<PreventUseWhenUsedOnTargetItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.optionalFieldOf("block", false).forGetter(PreventUseWhenUsedOnTargetItemComponent::block),
        Codec.BOOL.optionalFieldOf("entity", false).forGetter(PreventUseWhenUsedOnTargetItemComponent::entity)
    ).apply(instance, PreventUseWhenUsedOnTargetItemComponent::new));

    public static PreventUseWhenUsedOnTargetItemComponent forBlock() {
        return new PreventUseWhenUsedOnTargetItemComponent(true, false);
    }

    @Override
    public ItemComponentType<PreventUseWhenUsedOnTargetItemComponent> type() {
        return ItemComponentTypes.PREVENT_USE_WHEN_USED_ON_TARGET;
    }

    @Override
    public Codec<PreventUseWhenUsedOnTargetItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ItemResult useOnBlock(UseOnContext context, ItemStackExchanger stackExchanger) {
        return this.block ? ItemResult.CONSUME : ItemResult.PASS;
    }

    @Override
    public ItemResult useOnEntity(Player user, LivingEntity target, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        return this.entity ? ItemResult.CONSUME : ItemResult.PASS;
    }
}
