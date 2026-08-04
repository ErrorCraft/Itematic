package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.access.item.ItemPlacementContextAccess;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockPlaceContext.class)
public class ItemPlacementContextExtender extends UseOnContext implements ItemPlacementContextAccess {
    public ItemPlacementContextExtender(Player player, InteractionHand hand, BlockHitResult hit) {
        super(player, hand, hit);
    }

    @Override
    public BlockPlaceContext itematic$offset(int x, int y, int z) {
        BlockHitResult hit = this.getHitResult();
        BlockHitResult newHit = hit.withPosition(hit.getBlockPos().offset(x, y, z));
        return new BlockPlaceContext(this.getLevel(), this.getPlayer(), this.getHand(), this.getItemInHand(), newHit);
    }

    @Override
    public ActionContext itematic$actionContext(ItemStackExchanger stackExchanger) {
        return ActionContext.builder(this.getLevel())
            .stackExchanger(stackExchanger)
            .addOptional(LootContextParams.THIS_ENTITY, this.getPlayer())
            .addOptional(LootContextParams.ORIGIN, this.getPlayer(), Entity::position)
            .add(ItematicContextParameters.INTERACTED_POSITION, this.getClickedPos().getCenter())
            .add(LootContextParams.TOOL, this.getItemInHand())
            .add(ItematicContextParameters.HAND, this.getHand())
            .add(ItematicContextParameters.SIDE, this.getClickedFace())
            .build();
    }
}
