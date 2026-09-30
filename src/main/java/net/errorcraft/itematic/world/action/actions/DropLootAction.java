package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record DropLootAction(PositionTarget position, ResourceKey<LootTable> lootTable, BlockTransformer.DropStrategy dropStrategy) implements Action<DropLootAction> {
    public static final MapCodec<DropLootAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(DropLootAction::position),
        LootTable.KEY_CODEC.fieldOf("loot_table").forGetter(DropLootAction::lootTable),
        BlockTransformer.DropStrategy.CODEC.optionalFieldOf("drop_strategy", BlockTransformer.DropStrategy.FROM_MIDDLE).forGetter(DropLootAction::dropStrategy)
    ).apply(instance, DropLootAction::new));

    public static DropLootAction of(PositionTarget position, ResourceKey<LootTable> lootTable, BlockTransformer.DropStrategy dropStrategy) {
        return new DropLootAction(position, lootTable, dropStrategy);
    }

    @Override
    public ActionType<DropLootAction> type() {
        return ActionType.DROP_LOOT;
    }

    @Override
    public boolean execute(ActionContext context) {
        if (!(context.level() instanceof ServerLevel level)) {
            return false;
        }

        BlockPos pos = context.get(this.position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return false;
        }

        Direction interactedSide = context.getOrDefault(ItematicContextKeys.SIDE, Direction.UP);
        return Block.dropFromBlockInteractLootTable(
            level,
            this.lootTable,
            pos,
            level.getBlockState(pos),
            context.get(LootContextParams.BLOCK_ENTITY),
            context.get(LootContextParams.TOOL),
            context.get(LootContextParams.THIS_ENTITY),
            (_, stack) -> this.dropStrategy.pop(level, pos, interactedSide, stack)
        );
    }
}
