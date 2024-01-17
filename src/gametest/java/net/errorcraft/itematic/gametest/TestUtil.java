package net.errorcraft.itematic.gametest;

import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.function.Consumer;

public class TestUtil {
    private TestUtil() {}

    public static <T extends ItemComponent> T getItemComponent(ItemStack stack, ItemComponentType<T> type) {
        return stack.itematic$getComponent(type)
            .orElseThrow(() -> new GameTestException("Item " + stack.itematic$key() + " does not contain the " + ItematicRegistries.ITEM_COMPONENT_TYPE.getKey(type).orElseThrow() + " item component"));
    }

    public static <T extends BlockEntity> T getBlockEntity(TestContext context, BlockPos pos, BlockEntityType<T> type) {
        return context.getWorld().getBlockEntity(context.getAbsolutePos(pos), type)
            .orElseThrow(() -> new GameTestException("Block entity at position " + pos + " was not of type " + Registries.BLOCK_ENTITY_TYPE.getKey(type).orElseThrow()));
    }

    public static <T extends Entity> T createEntity(TestContext context, EntityType<T> type, Consumer<T> initializer) {
        T entity = type.create(context.getWorld());
        if (entity == null) {
            throw new GameTestException("Entity is null");
        }
        initializer.accept(entity);
        return entity;
    }

    public static <T extends Entity> void spawnEntity(TestContext context, T entity, BlockPos pos) {
        spawnEntity(context, entity, Vec3d.ofBottomCenter(pos));
    }

    public static <T extends Entity> void spawnEntity(TestContext context, T entity, Vec3d pos) {
        Vec3d absolutePos = context.getAbsolute(pos);
        entity.refreshPositionAfterTeleport(absolutePos);
        context.getWorld().spawnEntity(entity);
    }

    public static void setEntityPos(TestContext context, Entity entity, BlockPos pos) {
        BlockPos absolutePos = context.getAbsolutePos(pos);
        entity.setPosition(Vec3d.ofBottomCenter(absolutePos));
    }

    public static void useStackOnBlockInside(TestContext context, PlayerEntity player, ItemStack stack, BlockPos pos, Direction direction) {
        BlockPos absolutePos = context.getAbsolutePos(pos);
        BlockHitResult hitResult = new BlockHitResult(Vec3d.ofCenter(absolutePos), direction, absolutePos, true);
        stack.useOnBlock(new ItemUsageContext(player, Hand.MAIN_HAND, hitResult));
    }

    public static void useBlock(TestContext context, BlockPos pos, PlayerEntity player, Direction direction) {
        BlockPos absolutePos = context.getAbsolutePos(pos);
        context.useBlock(pos, player, new BlockHitResult(Vec3d.ofCenter(absolutePos), direction, absolutePos, true));
    }
}
