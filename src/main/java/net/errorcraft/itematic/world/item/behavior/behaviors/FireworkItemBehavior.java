package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FireworkItemBehavior implements ItemBehavior<FireworkItemBehavior> {
    public static final FireworkItemBehavior INSTANCE = new FireworkItemBehavior();
    public static final Codec<FireworkItemBehavior> CODEC = MapCodec.unitCodec(INSTANCE);
    private static final Fireworks DEFAULT_DATA_COMPONENT = new Fireworks(1, List.of());

    private FireworkItemBehavior() {}

    @Override
    public ItemBehaviorType<FireworkItemBehavior> type() {
        return ItemBehaviorType.FIREWORK;
    }

    @Override
    public ItemResult use(Level level, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (!user.isFallFlying()) {
            return ItemResult.PASS;
        }

        if (level.isClientSide()) {
            return ItemResult.SUCCEED;
        }

        FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(level, stack, user);
        level.addFreshEntity(fireworkRocketEntity);
        stack.consume(1, user);
        user.awardStat(Stats.ITEM_USED.itematic$get(stack.typeHolder()));
        return ItemResult.CONSUME;
    }

    @Override
    public ItemResult useOnBlock(UseOnContext context, ItemStackExchanger stackExchanger) {
        Player player = context.getPlayer();
        if (player != null && player.isFallFlying()) {
            return ItemResult.PASS;
        }

        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();
        if (level.isClientSide()) {
            return ItemResult.SUCCEED;
        }

        FireworkRocketEntity entity = createFireworkEntity(level, stack, context);
        level.addFreshEntity(entity);
        stack.shrink(1);
        return ItemResult.CONSUME;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.FIREWORKS, DEFAULT_DATA_COMPONENT);
    }

    private static FireworkRocketEntity createFireworkEntity(Level level, ItemStack stack, UseOnContext context) {
        Direction direction = context.getClickedFace();
        Vec3 position = context.getClickLocation().add(
            direction.getStepX() * FireworkRocketItem.ROCKET_PLACEMENT_OFFSET,
            direction.getStepY() * FireworkRocketItem.ROCKET_PLACEMENT_OFFSET,
            direction.getStepZ() * FireworkRocketItem.ROCKET_PLACEMENT_OFFSET
        );
        return new FireworkRocketEntity(level, context.getPlayer(), position.x(), position.y(), position.z(), stack);
    }
}
