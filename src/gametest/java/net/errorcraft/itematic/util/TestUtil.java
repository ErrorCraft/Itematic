package net.errorcraft.itematic.util;

import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.Optional;
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

    public static ItemStack createItemStackWithEnchantment(ServerWorld world, RegistryKey<Item> item, RegistryKey<Enchantment> enchantment) {
        ItemStack stack = world.itematic$createStack(item);
        RegistryEntry<Enchantment> enchantmentEntry = world.getRegistryManager()
            .getOrThrow(RegistryKeys.ENCHANTMENT)
            .getOrThrow(enchantment);
        stack.addEnchantment(enchantmentEntry, 1);
        return stack;
    }

    public static <T extends ItemComponent<T>> T getItemBehavior(TestContext helper, ItemStack stack, ItemComponentType<T> type) {
        return stack.itematic$getBehavior(type)
            .orElseThrow(() -> helper.createError(
                "test.error.item.expected_item_behavior",
                ItematicRegistries.ITEM_COMPONENT_TYPE.getId(type)
            ));
    }

    public static <T> T getDataComponent(TestContext helper, ItemStack stack, ComponentType<T> type) {
        T component = stack.get(type);
        if (component != null) {
            return component;
        }

        throw helper.createError(
            "test.error.item_stack.expected_data_component",
            "item stack",
            type
        );
    }

    public static <T extends BlockEntity> T getBlockEntity(TestContext context, BlockPos pos, BlockEntityType<T> type) {
        return context.getWorld().getBlockEntity(context.getAbsolutePos(pos), type)
            .orElseThrow(() -> context.createError(
                pos,
                "test.error.block_entity.expected_block_entity_type",
                Registries.BLOCK_ENTITY_TYPE.getId(type)
            ));
    }

    public static PlayerEntity createMockPlayer(TestContext context, GameMode gameMode, BlockPos pos) {
        PlayerEntity player = context.createMockPlayer(gameMode);
        setEntityPos(context, player, pos);
        return player;
    }

    public static <T extends Entity> T createEntity(TestContext context, EntityType<T> type, Consumer<T> initializer) {
        return createEntityAt(context, type, BlockPos.ORIGIN, initializer);
    }

    public static <T extends Entity> T createEntityAt(TestContext context, EntityType<T> type, BlockPos pos, Consumer<T> initializer) {
        T entity = type.create(context.getWorld(), SpawnReason.COMMAND);
        if (entity == null) {
            throw context.createError(
                "test.error.entity_type.cannot_create_entity",
                type.getName()
            );
        }

        setEntityPos(context, entity, pos);
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

    public static Optional<ItemStack> useStackOnBlockInside(TestContext context, PlayerEntity player, ItemStack stack, BlockPos pos, Direction direction) {
        BlockPos absolutePos = context.getAbsolutePos(pos);
        ActionResult result = stack.useOnBlock(
            new ItemUsageContext(
                player,
                Hand.MAIN_HAND,
                new BlockHitResult(
                    Vec3d.ofCenter(absolutePos),
                    direction,
                    absolutePos,
                    false
                )
            )
        );
        if (result instanceof ActionResult.Success success) {
            return Optional.ofNullable(success.getNewHandStack());
        }

        return Optional.empty();
    }

    public static void useBlock(TestContext context, BlockPos pos, PlayerEntity player, Direction direction) {
        BlockPos absolutePos = context.getAbsolutePos(pos);
        context.useBlock(
            pos,
            player,
            new BlockHitResult(
                Vec3d.ofCenter(absolutePos),
                direction,
                absolutePos,
                false
            )
        );
    }

    @SuppressWarnings("unchecked")
    public static <T extends ScreenHandler> T getMenuFromBlock(TestContext context, BlockPos pos, PlayerEntity player, ScreenHandlerType<T> type) {
        BlockPos absolutePos = context.getAbsolutePos(pos);
        NamedScreenHandlerFactory factory = context.getBlockState(pos).createScreenHandlerFactory(context.getWorld(), absolutePos);
        if (factory == null) {
            throw context.createError(pos, "test.error.menu.does_not_provide_menu");
        }

        ScreenHandler menu = factory.createMenu(-1, player.getInventory(), player);
        if (menu == null) {
            throw context.createError(pos, "test.error.menu.does_not_create_menu");
        }

        try {
            ScreenHandlerType<?> actualType = menu.getType();
            if (type == actualType) {
                return (T) menu;
            }

            throw context.createError(
                pos,
                "test.error.menu.has_incorrect_menu_type",
                Registries.SCREEN_HANDLER.getId(actualType),
                Registries.SCREEN_HANDLER.getId(type)
            );
        } catch (UnsupportedOperationException ignored) {
            throw context.createError(pos, "test.error.menu.does_not_create_menu_by_type");
        }
    }
}
