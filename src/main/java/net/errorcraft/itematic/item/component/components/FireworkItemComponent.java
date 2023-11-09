package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.FireworkStarItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record FireworkItemComponent() implements ItemComponent {
    public static final Codec<FireworkItemComponent> CODEC = Codec.unit(new FireworkItemComponent());
    private static final String FLIGHT_TRANSLATION_KEY = "item.minecraft.firework_rocket.flight";
    private static final String EXPLOSION_INDENTATION = "  ";

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.FIREWORK;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
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
        if (!user.getAbilities().creativeMode) {
            stack.decrement(1);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
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
        NbtCompound fireworks = stack.getSubNbt(FireworkRocketItem.FIREWORKS_KEY);
        if (fireworks == null) {
            return;
        }
        if (fireworks.contains(FireworkRocketItem.FLIGHT_KEY, NbtElement.NUMBER_TYPE)) {
            tooltip.add(
                Text.translatable(FLIGHT_TRANSLATION_KEY)
                    .append(ScreenTexts.SPACE)
                    .append(String.valueOf(fireworks.getByte(FireworkRocketItem.FLIGHT_KEY)))
                    .formatted(Formatting.GRAY)
            );
        }
        NbtList explosions = fireworks.getList(FireworkRocketItem.EXPLOSIONS_KEY, NbtElement.COMPOUND_TYPE);
        appendExplosionsTooltip(explosions, tooltip);
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

    private static void appendExplosionsTooltip(NbtList explosions, List<Text> tooltip) {
        if (explosions.isEmpty()) {
            return;
        }
        for (int i = 0; i < explosions.size(); i++) {
            ArrayList<Text> explosionText = new ArrayList<>();
            FireworkStarItem.appendFireworkTooltip(explosions.getCompound(i), explosionText);
            if (explosionText.isEmpty()) {
                continue;
            }
            for (int j = 1; j < explosionText.size(); ++j) {
                tooltip.add(
                    Text.literal(EXPLOSION_INDENTATION)
                        .append(explosionText.get(j))
                        .formatted(Formatting.GRAY)
                );
            }
        }
    }
}
