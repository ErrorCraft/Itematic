package net.errorcraft.itematic.item.component;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ItemComponent {
    ItemComponentType<?> type();
    Codec<? extends ItemComponent> codec();

    default ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        return ActionResult.PASS;
    }

    default ActionResult useOnBlock(ItemUsageContext context, ItemStackConsumer resultStackConsumer) {
        return ActionResult.PASS;
    }

    default ActionResult useOnEntity(PlayerEntity user, LivingEntity target, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        return ActionResult.PASS;
    }

    default boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker, ItemStackConsumer resultStackConsumer) {
        return false;
    }

    default boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, ItemStackConsumer resultStackConsumer) {
        return false;
    }

    default void using(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {}

    default void stopUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, ItemStackConsumer resultStackConsumer) {}

    default void finishUsing(World world, LivingEntity user, ItemStack stack, ItemStackConsumer resultStackConsumer) {}

    default void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {}
}
