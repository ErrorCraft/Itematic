package errorcraft.itematic.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ItemComponent {
    ItemComponentType<?> getType();
    Codec<? extends ItemComponent> getCodec();

    default TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        return TypedActionResult.pass(stack);
    }

    default TypedActionResult<ItemStack> useOnBlock(ItemUsageContext context) {
        return TypedActionResult.pass(context.getStack());
    }

    default boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        return false;
    }

    default ItemStack finishUsing(World world, LivingEntity user, ItemStack stack) {
        return stack;
    }
}
