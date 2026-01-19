package net.errorcraft.itematic.gametest;

import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTestException;
import net.minecraft.test.PositionedException;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.function.Consumer;

public class TestUtil {
    private TestUtil() {}

    public static ItemStack createItemStackWithSlightDamage(ServerWorld world, RegistryKey<Item> item) {
        ItemStack stack = world.itematic$createStack(item);
        if (!stack.isDamageable()) {
            throw new AssertionError("Item " + item.getValue() + " is not damageable");
        }

        stack.setDamage(1);
        return stack;
    }

    public static ItemStack createItemStackWithEnchantment(ServerWorld world, RegistryKey<Item> item, Enchantment enchantment) {
        ItemStack stack = world.itematic$createStack(item);
        stack.addEnchantment(enchantment, 1);
        return stack;
    }

    public static <T extends ItemComponent<T>> T getItemComponent(ItemStack stack, ItemComponentType<T> type) {
        return stack.itematic$getComponent(type)
            .orElseThrow(() -> new GameTestException("Item " + stack.itematic$key() + " does not contain the " + ItematicRegistries.ITEM_COMPONENT_TYPE.getKey(type).orElseThrow() + " item component"));
    }

    public static <T> T getDataComponent(ItemStack stack, DataComponentType<T> type) {
        T component = stack.get(type);
        if (component == null) {
            throw new GameTestException("Item stack does not contain the " + type + " component");
        }
        return component;
    }

    public static <T extends BlockEntity> T getBlockEntity(TestContext context, BlockPos pos, BlockEntityType<T> type) {
        return context.getWorld().getBlockEntity(context.getAbsolutePos(pos), type)
            .orElseThrow(() -> new GameTestException("Block entity at position " + pos + " was not of type " + Registries.BLOCK_ENTITY_TYPE.getKey(type).orElseThrow()));
    }

    public static PlayerEntity createMockPlayer(TestContext context, GameMode gameMode, BlockPos pos) {
        PlayerEntity player = context.createMockPlayer(gameMode);
        setEntityPos(context, player, pos);
        return player;
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

    @SuppressWarnings("unchecked")
    public static <T extends ScreenHandler> T getMenuFromBlock(TestContext context, BlockPos pos, PlayerEntity player, ScreenHandlerType<T> type) {
        BlockPos absolutePos = context.getAbsolutePos(pos);
        NamedScreenHandlerFactory factory = context.getBlockState(pos).createScreenHandlerFactory(context.getWorld(), absolutePos);
        if (factory == null) {
            throw new PositionedException("Block does not provide a menu", absolutePos, pos, context.getTick());
        }

        ScreenHandler menu = factory.createMenu(-1, player.getInventory(), player);
        if (menu == null) {
            throw new PositionedException("Block does not create a menu", absolutePos, pos, context.getTick());
        }

        try {
            ScreenHandlerType<?> actualType = menu.getType();
            if (type == actualType) {
                return (T) menu;
            }

            throw new PositionedException(
                "Block has the incorrect menu type " + Registries.SCREEN_HANDLER.getId(actualType) + ", expected " + Registries.SCREEN_HANDLER.getId(type),
                absolutePos,
                pos,
                context.getTick()
            );
        } catch (UnsupportedOperationException ignored) {
            throw new PositionedException("Block does not create a menu by type", absolutePos, pos, context.getTick());
        }
    }
}
