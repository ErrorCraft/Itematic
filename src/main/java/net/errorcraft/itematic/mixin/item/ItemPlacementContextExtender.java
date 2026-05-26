package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.access.item.ItemPlacementContextAccess;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemPlacementContext.class)
public class ItemPlacementContextExtender extends ItemUsageContext implements ItemPlacementContextAccess {
    public ItemPlacementContextExtender(PlayerEntity player, Hand hand, BlockHitResult hit) {
        super(player, hand, hit);
    }

    @Override
    public ItemPlacementContext itematic$offset(int x, int y, int z) {
        BlockHitResult hit = this.getHitResult();
        BlockHitResult newHit = hit.withBlockPos(hit.getBlockPos().add(x, y, z));
        return new ItemPlacementContext(this.getWorld(), this.getPlayer(), this.getHand(), this.getStack(), newHit);
    }

    @Override
    public ActionContext itematic$actionContext(ItemStackExchanger stackExchanger) {
        return ActionContext.builder(this.getWorld())
            .stackExchanger(stackExchanger)
            .addOptional(LootContextParameters.THIS_ENTITY, this.getPlayer())
            .addOptional(LootContextParameters.ORIGIN, this.getPlayer(), Entity::getPos)
            .add(ItematicContextParameters.INTERACTED_POSITION, this.getBlockPos().toCenterPos())
            .add(LootContextParameters.TOOL, this.getStack())
            .add(ItematicContextParameters.HAND, this.getHand())
            .add(ItematicContextParameters.SIDE, this.getSide())
            .build();
    }
}
