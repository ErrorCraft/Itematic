package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackUtil;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

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
        BlockPos pos = context.get(this.position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return false;
        }

        Level world = context.world();
        if (!world.getBlockState(pos).is(BlockTags.BANNERS)) {
            return false;
        }

        ItemStack stack = context.get(LootContextParams.TOOL);
        if (ItemStackUtil.isNullOrEmpty(stack)) {
            return false;
        }

        MapItemSavedData state = MapItem.getSavedData(stack, world);
        return state == null || state.toggleBanner(world, pos);
    }
}
