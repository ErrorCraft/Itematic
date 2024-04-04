package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.map.MapState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public record MarkBannerOnItemAction(ActionContextParameter position) implements Action<MarkBannerOnItemAction> {
    public static final MapCodec<MarkBannerOnItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(MarkBannerOnItemAction::position)
    ).apply(instance, MarkBannerOnItemAction::new));

    public static MarkBannerOnItemAction of(ActionContextParameter position) {
        return new MarkBannerOnItemAction(position);
    }

    @Override
    public ActionType<MarkBannerOnItemAction> type() {
        return ActionTypes.MARK_BANNER_ON_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        ServerWorld world = context.world();
        BlockPos pos = context.blockPos(this.position);
        if (!world.getBlockState(pos).isIn(BlockTags.BANNERS)) {
            return false;
        }
        MapState state = FilledMapItem.getMapState(context.stack(), world);
        return state == null || state.addBanner(world, pos);
    }
}
