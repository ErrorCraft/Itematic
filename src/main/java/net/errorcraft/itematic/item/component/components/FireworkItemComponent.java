package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record FireworkItemComponent() implements ItemComponent<FireworkItemComponent> {
    public static final FireworkItemComponent INSTANCE = new FireworkItemComponent();
    public static final Codec<FireworkItemComponent> CODEC = Codec.unit(INSTANCE);

    @Override
    public ItemComponentType<FireworkItemComponent> type() {
        return ItemComponentTypes.FIREWORK;
    }

    @Override
    public Codec<FireworkItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (!user.isFallFlying()) {
            return ActionResult.PASS;
        }
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }
        FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(world, stack, user);
        world.spawnEntity(fireworkRocketEntity);
        stack.decrementUnlessCreative(1, user);
        user.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
        return ActionResult.CONSUME;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context, ItemStackConsumer resultStackConsumer) {
        World world = context.getWorld();
        ItemStack stack = context.getStack();
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }
        FireworkRocketEntity entity = createFireworkEntity(world, stack, context);
        world.spawnEntity(entity);
        stack.decrement(1);
        return ActionResult.CONSUME;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        FireworksComponent fireworks = stack.get(DataComponentTypes.FIREWORKS);
        if (fireworks != null) {
            fireworks.appendTooltip(tooltip::add, context);
        }
    }

    private static FireworkRocketEntity createFireworkEntity(World world, ItemStack stack, ItemUsageContext context) {
        Direction direction = context.getSide();
        Vec3d position = context.getHitPos().add(
            direction.getOffsetX() * FireworkRocketItem.OFFSET_POS_MULTIPLIER,
            direction.getOffsetY() * FireworkRocketItem.OFFSET_POS_MULTIPLIER,
            direction.getOffsetZ() * FireworkRocketItem.OFFSET_POS_MULTIPLIER
        );
        return new FireworkRocketEntity(world, context.getPlayer(), position.getX(), position.getY(), position.getZ(), stack);
    }
}
