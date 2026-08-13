package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public record PreventUseWhenUsedOnTargetItemBehavior(boolean block, boolean entity) implements ItemBehavior<PreventUseWhenUsedOnTargetItemBehavior> {
    public static final Codec<PreventUseWhenUsedOnTargetItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.optionalFieldOf("block", false).forGetter(PreventUseWhenUsedOnTargetItemBehavior::block),
        Codec.BOOL.optionalFieldOf("entity", false).forGetter(PreventUseWhenUsedOnTargetItemBehavior::entity)
    ).apply(instance, PreventUseWhenUsedOnTargetItemBehavior::new));

    public static PreventUseWhenUsedOnTargetItemBehavior forBlock() {
        return new PreventUseWhenUsedOnTargetItemBehavior(true, false);
    }

    @Override
    public ItemBehaviorType<PreventUseWhenUsedOnTargetItemBehavior> type() {
        return ItemBehaviorType.PREVENT_USE_WHEN_USED_ON_TARGET;
    }

    @Override
    public Codec<PreventUseWhenUsedOnTargetItemBehavior> codec() {
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
