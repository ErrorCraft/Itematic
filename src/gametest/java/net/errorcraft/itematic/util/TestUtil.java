package net.errorcraft.itematic.util;

import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class TestUtil {
    private TestUtil() {}

    public static ItemStack createItemStackWithSlightDamage(ServerLevel world, ResourceKey<Item> item) {
        ItemStack stack = world.itematic$createStack(item);
        if (!stack.isDamageableItem()) {
            throw new AssertionError("Item " + item.identifier() + " is not damageable");
        }

        stack.setDamageValue(1);
        return stack;
    }

    public static ItemStack createItemStackWithEnchantment(ServerLevel world, ResourceKey<Item> item, ResourceKey<Enchantment> enchantment) {
        ItemStack stack = world.itematic$createStack(item);
        Holder<Enchantment> enchantmentEntry = world.registryAccess()
            .lookupOrThrow(Registries.ENCHANTMENT)
            .getOrThrow(enchantment);
        stack.enchant(enchantmentEntry, 1);
        return stack;
    }

    public static <T extends ItemBehavior<T>> T getItemBehavior(GameTestHelper helper, ItemStack stack, ItemBehaviorType<T> type) {
        return stack.itematic$getBehavior(type)
            .orElseThrow(() -> helper.assertionException(
                "test.error.item.expected_item_behavior",
                ItematicRegistries.ITEM_BEHAVIOR_TYPE.getId(type)
            ));
    }

    public static <T> T getDataComponent(GameTestHelper helper, ItemStack stack, DataComponentType<T> type) {
        T component = stack.get(type);
        if (component != null) {
            return component;
        }

        throw helper.assertionException(
            "test.error.item_stack.expected_data_component",
            "item stack",
            type
        );
    }

    public static <T extends BlockEntity> T getBlockEntity(GameTestHelper context, BlockPos pos, BlockEntityType<T> type) {
        return context.getLevel().getBlockEntity(context.absolutePos(pos), type)
            .orElseThrow(() -> context.assertionException(
                pos,
                "test.error.block_entity.expected_block_entity_type",
                BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(type)
            ));
    }

    public static <E extends Entity> E getSingleEntity(GameTestHelper context, EntityType<E> type) {
        List<E> entities = context.getEntities(type);
        if (entities.isEmpty()) {
            throw context.assertionException(
                "test.error.expected_entity",
                type.getDescription()
            );
        }

        if (entities.size() > 1) {
            throw context.assertionException(
                "test.error.entity.too_many_entities",
                type.toShortString(),
                entities.size()
            );
        }

        return entities.getFirst();
    }

    public static <E extends Entity> E getSingleEntityAt(GameTestHelper context, EntityType<E> type, BlockPos pos) {
        List<E> entities = getEntitiesAt(context, type, pos);
        if (entities.isEmpty()) {
            throw context.assertionException(
                "test.error.expected_entity_around",
                type.getDescription(),
                pos.getX(),
                pos.getY(),
                pos.getZ()
            );
        }

        if (entities.size() > 1) {
            throw context.assertionException(
                "test.error.too_many_entities",
                type.toShortString(),
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                entities.size()
            );
        }

        return entities.getFirst();
    }

    public static <E extends Entity> List<E> getEntitiesAt(GameTestHelper context, EntityType<E> type, BlockPos pos) {
        return context.getLevel().getEntities(type, new AABB(context.absolutePos(pos)), Entity::isAlive);
    }

    public static Player createMockPlayer(GameTestHelper context, GameType gameMode, BlockPos pos) {
        Player player = context.makeMockPlayer(gameMode);
        setEntityPos(context, player, pos);
        return player;
    }

    public static <T extends Entity> T createEntity(GameTestHelper context, EntityType<T> type, Consumer<T> initializer) {
        return createEntityAt(context, type, BlockPos.ZERO, initializer);
    }

    public static <T extends Entity> T createEntityAt(GameTestHelper context, EntityType<T> type, BlockPos pos, Consumer<T> initializer) {
        T entity = type.create(context.getLevel(), EntitySpawnReason.COMMAND);
        if (entity == null) {
            throw context.assertionException(
                "test.error.entity_type.cannot_create_entity",
                type.getDescription()
            );
        }

        setEntityPos(context, entity, pos);
        initializer.accept(entity);
        return entity;
    }

    public static <T extends Entity> void spawnEntity(GameTestHelper context, T entity, BlockPos pos) {
        spawnEntity(context, entity, Vec3.atBottomCenterOf(pos));
    }

    public static <T extends Entity> void spawnEntity(GameTestHelper context, T entity, Vec3 pos) {
        Vec3 absolutePos = context.absoluteVec(pos);
        entity.snapTo(absolutePos);
        context.getLevel().addFreshEntity(entity);
    }

    public static void setEntityPos(GameTestHelper context, Entity entity, BlockPos pos) {
        BlockPos absolutePos = context.absolutePos(pos);
        entity.setPos(Vec3.atBottomCenterOf(absolutePos));
    }

    public static Optional<ItemStack> useStackOnBlockInside(GameTestHelper context, Player player, ItemStack stack, BlockPos pos, Direction direction) {
        BlockPos absolutePos = context.absolutePos(pos);
        InteractionResult result = stack.useOn(
            new UseOnContext(
                player,
                InteractionHand.MAIN_HAND,
                new BlockHitResult(
                    Vec3.atCenterOf(absolutePos),
                    direction,
                    absolutePos,
                    false
                )
            )
        );
        if (result instanceof InteractionResult.Success success) {
            return Optional.ofNullable(success.heldItemTransformedTo());
        }

        return Optional.empty();
    }

    public static void useBlock(GameTestHelper context, BlockPos pos, Player player, Direction direction) {
        BlockPos absolutePos = context.absolutePos(pos);
        context.useBlock(
            pos,
            player,
            new BlockHitResult(
                Vec3.atCenterOf(absolutePos),
                direction,
                absolutePos,
                false
            )
        );
    }

    @SuppressWarnings("unchecked")
    public static <T extends AbstractContainerMenu> T getMenuFromBlock(GameTestHelper context, BlockPos pos, Player player, MenuType<T> type) {
        BlockPos absolutePos = context.absolutePos(pos);
        MenuProvider factory = context.getBlockState(pos).getMenuProvider(context.getLevel(), absolutePos);
        if (factory == null) {
            throw context.assertionException(pos, "test.error.menu.does_not_provide_menu");
        }

        AbstractContainerMenu menu = factory.createMenu(-1, player.getInventory(), player);
        if (menu == null) {
            throw context.assertionException(pos, "test.error.menu.does_not_create_menu");
        }

        try {
            MenuType<?> actualType = menu.getType();
            if (type == actualType) {
                return (T) menu;
            }

            throw context.assertionException(
                pos,
                "test.error.menu.has_incorrect_menu_type",
                BuiltInRegistries.MENU.getKey(actualType),
                BuiltInRegistries.MENU.getKey(type)
            );
        } catch (UnsupportedOperationException ignored) {
            throw context.assertionException(pos, "test.error.menu.does_not_create_menu_by_type");
        }
    }
}
