package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackUtil;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record MarkBannerOnItemAction(PositionTarget position) implements Action<MarkBannerOnItemAction> {
    public static final MapCodec<MarkBannerOnItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(MarkBannerOnItemAction::position)
    ).apply(instance, MarkBannerOnItemAction::new));

    public static MarkBannerOnItemAction of(PositionTarget position) {
        return new MarkBannerOnItemAction(position);
    }

    @Override
    public ActionType<MarkBannerOnItemAction> type() {
        return ActionTypes.MARK_BANNER_ON_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.getBlockPos(this.position.parameter());
        if (pos == null) {
            return false;
        }

        World world = context.world();
        if (!world.getBlockState(pos).isIn(BlockTags.BANNERS)) {
            return false;
        }

        ItemStack stack = context.get(LootContextParameters.TOOL);
        if (ItemStackUtil.isNullOrEmpty(stack)) {
            return false;
        }

        MapState state = FilledMapItem.getMapState(stack, world);
        return state == null || state.addBanner(world, pos);
    }
}
