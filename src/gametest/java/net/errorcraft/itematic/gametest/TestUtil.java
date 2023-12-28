package net.errorcraft.itematic.gametest;

import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
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

    public static void setEntityPos(TestContext context, Entity entity, BlockPos pos) {
        BlockPos absolutePos = context.getAbsolutePos(pos);
        entity.setPosition(Vec3d.ofBottomCenter(absolutePos));
    }
}
