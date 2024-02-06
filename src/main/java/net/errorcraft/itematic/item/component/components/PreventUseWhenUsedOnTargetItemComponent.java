package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.dynamic.Codecs;

public record PreventUseWhenUsedOnTargetItemComponent(boolean block, boolean entity) implements ItemComponent<PreventUseWhenUsedOnTargetItemComponent> {
    public static final Codec<PreventUseWhenUsedOnTargetItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "block", false).forGetter(PreventUseWhenUsedOnTargetItemComponent::block),
        Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "entity", false).forGetter(PreventUseWhenUsedOnTargetItemComponent::entity)
    ).apply(instance, PreventUseWhenUsedOnTargetItemComponent::new));

    @Override
    public ItemComponentType<PreventUseWhenUsedOnTargetItemComponent> type() {
        return ItemComponentTypes.PREVENT_USE_WHEN_USED_ON_TARGET;
    }

    @Override
    public Codec<PreventUseWhenUsedOnTargetItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context, ItemStackConsumer resultStackConsumer) {
        return this.block ? ActionResult.CONSUME_PARTIAL : ActionResult.PASS;
    }

    @Override
    public ActionResult useOnEntity(PlayerEntity user, LivingEntity target, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        return this.entity ? ActionResult.CONSUME_PARTIAL : ActionResult.PASS;
    }

    public static PreventUseWhenUsedOnTargetItemComponent forBlock() {
        return new PreventUseWhenUsedOnTargetItemComponent(true, false);
    }

    public static PreventUseWhenUsedOnTargetItemComponent forEntity() {
        return new PreventUseWhenUsedOnTargetItemComponent(false, true);
    }
}
